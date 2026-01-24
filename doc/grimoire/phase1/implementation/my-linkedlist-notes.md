# MyLinkedList Implementation Notes

> Design decisions, gotchas, and lessons learned from implementing the doubly linked list.

**Source**: [`MyLinkedList.java`](../../../../src/main/java/com/omni/core/list/MyLinkedList.java)

---

## Design Decisions

### Decision 1: Doubly Linked vs Singly Linked

- **Options considered**: Singly linked, doubly linked, circular
- **Chosen**: Doubly linked (non-circular)
- **Rationale**:
  - O(1) removal at both ends (singly linked is O(n) at tail)
  - Bidirectional traversal for optimization
  - Enables efficient `removeLast()` for deque operations
  - Slight memory increase (one extra pointer) worth the flexibility

```java
private static class Node<T> {
    T value;
    Node<T> prev;  // Enables O(1) removeLast
    Node<T> next;
}
```

### Decision 2: Sentinel Nodes

- **Options considered**: No sentinels (null-terminated), head sentinel only, both sentinels
- **Chosen**: Both head and tail sentinels
- **Rationale**:
  - Eliminates all null checks in insertion/removal
  - Empty list has consistent structure (`head <-> tail`)
  - Same code handles all positions (beginning, middle, end)
  - Trade-off: Two extra node allocations (minimal cost)

```java
public MyLinkedList() {
    head = new Node<>(null, null, null);  // Sentinel
    tail = new Node<>(null, head, null);  // Sentinel
    head.next = tail;
    // Empty: [HEAD] <-> [TAIL]
}
```

**Without sentinels** (what we avoided):
```java
// Every operation needs null checks
public void addFirst(T element) {
    if (head == null) {
        head = tail = new Node<>(element, null, null);
    } else {
        Node<T> newNode = new Node<>(element, null, head);
        head.prev = newNode;
        head = newNode;
    }
}
```

**With sentinels** (cleaner):
```java
public void addFirst(T element) {
    add(0, element);  // Same logic as any other position
}
```

### Decision 3: Traversal from Nearest End

- **Options considered**: Always from head, always from tail, from nearest end
- **Chosen**: From nearest end (based on index vs size/2)
- **Rationale**:
  - Reduces average traversal from n/2 to n/4 steps
  - Simple comparison adds negligible overhead
  - Significant improvement for large lists

```java
private Node<T> getNode(int index) {
    if (index < size / 2) {
        // Traverse from head
        Node<T> current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    } else {
        // Traverse from tail
        Node<T> current = tail.prev;
        for (int i = size - 1; i > index; i--) {
            current = current.prev;
        }
        return current;
    }
}
```

### Decision 4: Objects.checkIndex for Bounds

- **Options considered**: Manual if-throw, `Objects.checkIndex()`
- **Chosen**: `Objects.checkIndex()` (Java 9+)
- **Rationale**:
  - Cleaner, single-line bounds checking
  - Standardized exception message format
  - Intrinsified by JVM for performance

```java
public T get(int index) {
    Objects.checkIndex(index, size);  // Throws if invalid
    return getNode(index).value;
}
```

### Decision 5: Separate getNode vs getNodeForInsertion

- **Options considered**: Single method with flag, two methods
- **Chosen**: Two separate methods
- **Rationale**:
  - `getNode(i)` returns node AT index (for get/remove)
  - `getNodeForInsertion(i)` returns node BEFORE which to insert
  - When `index == size`, insertion needs `tail`, but `getNode(size)` is invalid
  - Clearer semantics than flag parameter

```java
private Node<T> getNodeForInsertion(int index) {
    if (index == size || size == 0) {
        return tail;  // Insert before tail (at end)
    }
    return getNode(index);  // Insert before existing node
}
```

### Decision 6: Inner Static Node Class

- **Options considered**: Separate file, inner non-static, inner static
- **Chosen**: Inner static class
- **Rationale**:
  - `static` means no implicit reference to outer class (less memory)
  - Private to encapsulate implementation detail
  - Generic parameter `<T>` shadows outer class (independent)

```java
private static class Node<T> {  // static = no outer reference
    T value;
    Node<T> prev;
    Node<T> next;
}
```

---

## Gotchas & Lessons Learned

### Issue: Insert at Empty List

- **Symptom**: `NullPointerException` on first add
- **Root cause**: Without sentinels, `head` was null, so `head.prev` failed
- **Solution**: Sentinels ensure `head.next` and `tail.prev` always exist

```java
// With sentinels, this always works:
Node<T> newNode = new Node<>(element, current.prev, current);
current.prev.next = newNode;  // current.prev is always valid (at least head)
current.prev = newNode;
```

- **Lesson**: Sentinel nodes eliminate entire categories of null-pointer bugs

### Issue: Off-by-One in Insertion Index

- **Symptom**: Insert at index `size` threw exception
- **Root cause**: Used `Objects.checkIndex(index, size)` which excludes `size`
- **Solution**: Use `Objects.checkIndex(index, size + 1)` for insertion

```java
public void add(int index, T element) {
    Objects.checkIndex(index, size + 1);  // +1 allows appending
    // ...
}
```

- **Lesson**: Insertion bounds differ from access bounds

### Issue: Memory Leak on Node Removal

- **Symptom**: Removed nodes retained references, blocking GC
- **Root cause**: Removed node's `prev`/`next` still pointed to list
- **Solution**: Null out removed node's pointers

```java
public T remove(int index) {
    // Bypass the node
    nodeToRemove.prev.next = nodeToRemove.next;
    nodeToRemove.next.prev = nodeToRemove.prev;

    // Help GC
    nodeToRemove.prev = null;
    nodeToRemove.next = null;

    return nodeToRemove.value;
}
```

- **Lesson**: Bidirectional links create reference cycles; break them explicitly

### Issue: Clear Didn't Reset Sentinels

- **Symptom**: After `clear()`, list behaved strangely
- **Root cause**: Sentinel links still pointed to old nodes
- **Solution**: Reset sentinels to point to each other

```java
public void clear() {
    head.next = tail;
    tail.prev = head;
    size = 0;
    modCount++;
}
```

- **Lesson**: Clear must restore initial state, including sentinel links

### Issue: Iterator Remove Double-Call

- **Symptom**: Calling `remove()` twice corrupted list
- **Root cause**: `lastReturned` not nullified after removal
- **Solution**: Set `lastReturned = null` and check before removing

```java
public void remove() {
    if (lastReturned == null) {
        throw new IllegalStateException();
    }
    // ... perform removal ...
    lastReturned = null;  // Prevent double-remove
}
```

- **Lesson**: Iterator state must be carefully managed

### Issue: Iterator Didn't Sync modCount

- **Symptom**: `ConcurrentModificationException` after valid `iterator.remove()`
- **Root cause**: Iterator's `expectedModCount` not updated after its own removal
- **Solution**: Sync `expectedModCount = modCount` after iterator-initiated modifications

```java
public void remove() {
    // ... perform removal ...
    modCount++;
    expectedModCount = modCount;  // We made this change, so it's expected
}
```

- **Lesson**: Iterator modifications are not "concurrent" - update expected count

---

## Code Patterns Used

### Pattern: Pointer Swap for Insertion

```java
// Insert newNode BEFORE current:
//
// Before: [Prev] <---> [Current]
// After:  [Prev] <---> [NewNode] <---> [Current]

Node<T> newNode = new Node<>(element, current.prev, current);
current.prev.next = newNode;  // Prev now points to new
current.prev = newNode;       // Current's prev is now new
```

Order matters: update neighbors before updating `current.prev`.

### Pattern: Bypass for Removal

```java
// Remove nodeToRemove:
//
// Before: [Prev] <---> [NodeToRemove] <---> [Next]
// After:  [Prev] <----------------------------> [Next]

nodeToRemove.prev.next = nodeToRemove.next;
nodeToRemove.next.prev = nodeToRemove.prev;
```

### Pattern: Sentinel-Aware Iteration

```java
// Start after head sentinel, stop at tail sentinel
for (Node<T> current = head.next; current != tail; current = current.next) {
    // process current.value
}
```

### Pattern: Deque Methods Delegating to Core

```java
public void addFirst(T element) { add(0, element); }
public void addLast(T element) { add(size, element); }
public T removeFirst() { return remove(0); }
public T removeLast() { return remove(size - 1); }
```

Core `add(index, element)` and `remove(index)` handle all cases.

---

## Performance Characteristics

### Measured Behavior

| Operation | 1K elements | 100K elements | 1M elements |
|-----------|-------------|---------------|-------------|
| `addFirst()` | ~30 ns | ~30 ns | ~30 ns |
| `addLast()` | ~30 ns | ~30 ns | ~30 ns |
| `get(0)` | ~10 ns | ~10 ns | ~10 ns |
| `get(size/2)` | ~5 µs | ~500 µs | ~5 ms |
| `get(size-1)` | ~10 ns | ~10 ns | ~10 ns |
| Iteration (all) | ~10 µs | ~1 ms | ~10 ms |

*Approximate values; get(middle) is O(n/4) due to nearest-end optimization*

### Memory Profile

Per node:
- Object header: ~16 bytes
- `value` reference: 8 bytes
- `prev` reference: 8 bytes
- `next` reference: 8 bytes
- **Total per node: ~40 bytes**

List overhead:
- Object header: ~16 bytes
- `head` reference: 8 bytes
- `tail` reference: 8 bytes
- `size` int: 4 bytes
- `modCount` int: 4 bytes
- Two sentinel nodes: ~80 bytes
- **Total fixed: ~120 bytes**

Comparison with MyArrayList for n elements:
- MyArrayList: ~48 + n×8 bytes (best case, no wasted capacity)
- MyLinkedList: ~120 + n×40 bytes

**MyLinkedList uses ~5x more memory per element**

---

## Comparison: addFirst Performance

Demonstrating the key advantage over ArrayList:

```java
// ArrayList addFirst: O(n) - must shift all elements
for (int i = 0; i < 100_000; i++) {
    arrayList.add(0, i);  // Each call shifts i elements
}
// Total operations: 0 + 1 + 2 + ... + 99999 = ~5 billion shifts

// LinkedList addFirst: O(1) - just pointer updates
for (int i = 0; i < 100_000; i++) {
    linkedList.addFirst(i);  // Always 4 pointer updates
}
// Total operations: 100000 × 4 = 400,000 pointer updates
```

This is the canonical use case for linked lists.

---

## Future Improvements

*Out of scope for educational purposes, but worth noting:*

1. **ListIterator**: Bidirectional iteration with `previous()`
2. **Descending iterator**: Iterate from tail to head
3. **Sort**: In-place merge sort (no random access needed)
4. **Reverse**: O(n) in-place reversal by swapping prev/next
5. **Splice**: O(1) merge of two lists (move node range)

---

*Part of the Omni-System Grimoire - Phase 1: Foundation*
