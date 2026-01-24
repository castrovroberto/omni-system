# Doubly Linked Lists (MyLinkedList)

## Overview

A doubly linked list is a linear data structure where each element (node) contains a value and pointers to both the previous and next nodes. Unlike arrays, elements are not stored contiguously in memory—instead, they're connected through references, allowing O(1) insertions and deletions at known positions.

In the Omni-System, `MyLinkedList<T>` implements a doubly linked list with **sentinel nodes** for simplified boundary handling.

## How It Works

### Node Structure

Each node contains three fields:

```java
class Node<T> {
    T value;      // The actual data
    Node<T> prev; // Pointer to previous node
    Node<T> next; // Pointer to next node
}
```

### Memory Layout (Conceptual)

Unlike arrays, nodes are scattered in memory and connected by references:

```
Memory:   [Node@A5F2]    [Node@B3C1]    [Node@D7E4]
              |              |              |
              v              v              v
          +-------+      +-------+      +-------+
          | "foo" |      | "bar" |      | "baz" |
          |  prev |--->  |  prev |--->  |  prev |---> null
   null<--|  next |  <---|  next |  <---|  next |
          +-------+      +-------+      +-------+
```

### Sentinel Nodes Pattern

The Omni-System implementation uses **sentinel nodes** (dummy head and tail) to eliminate null checks:

```
     +------+       +--------+       +--------+       +------+
     | HEAD | <---> | Node 0 | <---> | Node 1 | <---> | TAIL |
     +------+       +--------+       +--------+       +------+
    (Sentinel)                                       (Sentinel)
```

**Benefits of sentinels:**
- No special cases for empty list
- No null checks when inserting at boundaries
- Uniform insertion/deletion logic

**Without sentinels** (traditional approach):
```java
// Adding to front without sentinels - messy null handling
if (head == null) {
    head = newNode;
    tail = newNode;
} else {
    newNode.next = head;
    head.prev = newNode;
    head = newNode;
}
```

**With sentinels** (cleaner):
```java
// Adding anywhere - same logic works for all positions
Node<T> newNode = new Node<>(element, current.prev, current);
current.prev.next = newNode;
current.prev = newNode;
```

## Complexity Analysis

| Operation | Average Case | Worst Case | Notes |
|-----------|--------------|------------|-------|
| `get(i)` | O(n) | O(n) | Must traverse to index |
| `add(element)` | O(1) | O(1) | Append to tail |
| `add(i, element)` | O(n) | O(n) | O(n) to find position, O(1) to insert |
| `addFirst(element)` | O(1) | O(1) | Direct pointer manipulation |
| `addLast(element)` | O(1) | O(1) | Direct pointer manipulation |
| `remove(i)` | O(n) | O(n) | O(n) to find, O(1) to remove |
| `removeFirst()` | O(1) | O(1) | Direct pointer manipulation |
| `removeLast()` | O(1) | O(1) | Direct pointer manipulation |
| `contains(element)` | O(n) | O(n) | Linear search |
| `size()` | O(1) | O(1) | Tracked separately |

### Traversal Optimization

The implementation optimizes `get(index)` by traversing from the nearest end:

```
Index: 0 ... size/2 ... size-1
       |        |        |
     Start    Split    Start
   from HEAD          from TAIL
```

```java
if (index < size / 2) {
    // Traverse from head
} else {
    // Traverse from tail
}
```

This reduces average traversal distance from n/2 to n/4.

## Space Complexity

- **Per-element overhead**: ~24 bytes (on 64-bit JVM)
  - 8 bytes: object header
  - 8 bytes: `prev` reference
  - 8 bytes: `next` reference
  - (value reference stored separately)

- **Total**: O(n) with significant constant factor

- **No wasted space**: Unlike arrays, only allocates what's needed

## Trade-offs

### When to Use MyLinkedList

- **Frequent insertions/deletions at both ends** (queue, deque)
- **Iterator-based removal** while traversing
- **Unknown size** that grows/shrinks frequently
- **No random access needed**

### When to Avoid MyLinkedList

- **Frequent random access** → Use ArrayList (O(1) vs O(n))
- **Memory-constrained** → ArrayList has less overhead per element
- **Cache performance matters** → ArrayList has better locality

### Comparison with MyArrayList

| Criteria | MyArrayList | MyLinkedList |
|----------|-------------|--------------|
| `get(i)` | O(1) | O(n) |
| `add(end)` | O(1)* | O(1) |
| `add(0, x)` | O(n) | O(1) |
| `remove(0)` | O(n) | O(1) |
| Memory/element | ~8 bytes | ~24 bytes |
| Cache locality | Excellent | Poor |
| Iterator remove | O(n) | O(1) |

*amortized

## Visual Representation

### Insertion

```
Before:
    [HEAD] <---> [A] <---> [B] <---> [TAIL]
                           ^
                    Insert 'X' before 'B'

Step 1 - Create node with pointers:
    [X]
     |---> points to [B]
     |---> points to [A]

Step 2 - Update neighbors:
    [HEAD] <---> [A] <---> [X] <---> [B] <---> [TAIL]
```

### Removal

```
Before:
    [HEAD] <---> [A] <---> [B] <---> [C] <---> [TAIL]
                           ^
                    Remove 'B'

Step 1 - Bypass node:
    [A].next = [C]
    [C].prev = [A]

    [HEAD] <---> [A] <---------------> [C] <---> [TAIL]
                       [B] (orphaned)

Step 2 - Clear references (help GC):
    [B].prev = null
    [B].next = null
```

## Implementation Highlights

### Key Design Decisions in MyLinkedList

1. **Sentinel nodes**: Eliminate null checks and special cases
2. **Doubly linked**: O(1) removal at both ends, bidirectional traversal
3. **Traverse from nearest end**: Cuts average lookup time in half
4. **Fail-fast iterator**: Detects concurrent modification

### The Sentinel Pattern in Detail

```java
public MyLinkedList() {
    head = new Node<>(null, null, null);  // Sentinel
    tail = new Node<>(null, head, null);  // Sentinel
    head.next = tail;
    // Empty list: HEAD <-> TAIL (no real nodes)
}
```

When adding the first element:
```
Before: [HEAD] <---> [TAIL]

After:  [HEAD] <---> [Node] <---> [TAIL]
```

The same insertion code works regardless of list size.

### Deque Operations

The implementation provides deque (double-ended queue) operations:

```java
addFirst(element)   // O(1) - insert at head
addLast(element)    // O(1) - insert at tail
removeFirst()       // O(1) - remove from head
removeLast()        // O(1) - remove from tail
getFirst()          // O(1) - peek at head
getLast()           // O(1) - peek at tail
```

This makes `MyLinkedList` suitable for implementing:
- Queues (FIFO)
- Stacks (LIFO)
- Deques (both ends)

## Real-World Applications

| Use Case | Why Linked List? |
|----------|------------------|
| **LRU Cache** | O(1) move-to-front on access |
| **Undo/Redo** | Easy insertion of history states |
| **Music playlist** | Insert/remove songs anywhere |
| **Memory allocators** | Track free memory blocks |
| **Polynomial arithmetic** | Terms can be easily inserted/merged |

## Common Pitfalls

1. **Using for random access**: `list.get(i)` in a loop is O(n²)
   ```java
   // BAD - O(n²)
   for (int i = 0; i < list.size(); i++) {
       process(list.get(i));
   }

   // GOOD - O(n)
   for (T element : list) {
       process(element);
   }
   ```

2. **Forgetting it's not cache-friendly**: Sequential access still scatters memory reads

3. **Memory overhead**: For small values (primitives), node overhead dominates

4. **Null in singly-linked**: Without tail pointer, `addLast` is O(n)

## Singly vs Doubly Linked

| Aspect | Singly Linked | Doubly Linked |
|--------|---------------|---------------|
| Memory per node | 2 refs | 3 refs |
| Remove at tail | O(n) | O(1) |
| Reverse traversal | Impossible | O(n) |
| Implementation | Simpler | More complex |

The Omni-System uses doubly-linked for full O(1) end operations.

## Further Reading

- **CLRS Chapter 10.2**: Linked Lists
- **Java Collections**: `java.util.LinkedList` source
- **Skip Lists**: Probabilistic alternative with O(log n) search

---

*Part of the Omni-System Grimoire - Phase 1: Foundation*
