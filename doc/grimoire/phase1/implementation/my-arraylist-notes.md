# MyArrayList Implementation Notes

> Design decisions, gotchas, and lessons learned from implementing the dynamic array.

**Source**: [`MyArrayList.java`](../../../../src/main/java/com/omni/core/list/MyArrayList.java)

---

## Design Decisions

### Decision 1: Default Initial Capacity

- **Options considered**: 0, 1, 10, 16
- **Chosen**: 10
- **Rationale**:
  - Matches `java.util.ArrayList` convention
  - Zero would require immediate resize on first add
  - Too large wastes memory for small lists
  - 10 is a reasonable middle ground for typical use cases

### Decision 2: Resize Factor

- **Options considered**: Add fixed amount (+10), multiply by 1.5, multiply by 2
- **Chosen**: Multiply by 2 (double)
- **Rationale**:
  - Guarantees amortized O(1) insertions
  - Simple bit-shift multiplication (`<< 1`)
  - 1.5x would save ~25% memory but requires more resizes
  - Fixed increment would degrade to O(n) per insertion

```java
int newCapacity = elements.length == 0 ? DEFAULT_CAPACITY : elements.length * 2;
```

### Decision 3: Generic Array Creation

- **Options considered**: `new T[]`, `Array.newInstance()`, `new Object[]` with cast
- **Chosen**: `new Object[]` with `@SuppressWarnings("unchecked")`
- **Rationale**:
  - Java doesn't allow `new T[]` due to type erasure
  - `Array.newInstance()` requires Class object and is slower
  - Cast from `Object[]` is safe since we control all access
  - Standard pattern used by Java Collections

```java
@SuppressWarnings("unchecked")
public MyArrayList(int initialCapacity) {
    this.elements = (T[]) new Object[initialCapacity];
}
```

### Decision 4: Fail-Fast Iterator via modCount

- **Options considered**: No detection, snapshot iterator, modCount tracking
- **Chosen**: modCount tracking (fail-fast)
- **Rationale**:
  - Matches Java Collections behavior
  - Catches bugs early (concurrent modification during iteration)
  - Minimal overhead (single int comparison)
  - Snapshot would require copying entire array

```java
private int modCount; // Incremented on structural changes

private class MyArrayListIterator implements Iterator<T> {
    private int expectedModCount = modCount;

    public T next() {
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        // ...
    }
}
```

### Decision 5: Null Element Handling

- **Options considered**: Reject nulls, allow nulls
- **Chosen**: Allow nulls
- **Rationale**:
  - Matches `java.util.ArrayList` behavior
  - More flexible for callers
  - Requires null-safe equality check in `indexOf()`

```java
public int indexOf(T element) {
    for (int i = 0; i < size; i++) {
        if (element == null ? elements[i] == null : element.equals(elements[i])) {
            return i;
        }
    }
    return -1;
}
```

### Decision 6: System.arraycopy vs Manual Loop

- **Options considered**: Manual for-loop, `System.arraycopy()`, `Arrays.copyOf()`
- **Chosen**: `System.arraycopy()`
- **Rationale**:
  - Native implementation, highly optimized
  - Handles overlapping regions correctly
  - Clearer intent than manual loop
  - `Arrays.copyOf()` creates new array; we need in-place shifting

```java
// Shift elements right for insertion
System.arraycopy(elements, index, elements, index + 1, size - index);

// Shift elements left for removal
System.arraycopy(elements, index + 1, elements, index, size - index - 1);
```

---

## Gotchas & Lessons Learned

### Issue: Memory Leak on Remove

- **Symptom**: Objects not garbage collected after removal
- **Root cause**: Removed index still held reference to object
- **Solution**: Explicitly null out the vacated slot

```java
public T remove(int index) {
    // ... shift elements left ...
    elements[--size] = null; // Help GC - critical!
    return removed;
}
```

- **Lesson**: Always null out references when removing from object arrays

### Issue: Off-by-One in Bounds Checking

- **Symptom**: `add(size, element)` threw exception
- **Root cause**: Used `index >= size` check (same as `get`)
- **Solution**: `add` allows `index == size` (append), `get` doesn't

```java
// add() - allows index == size
if (index < 0 || index > size) { throw... }

// get() - doesn't allow index == size
if (index < 0 || index >= size) { throw... }
```

- **Lesson**: Bounds checking varies by operation semantics

### Issue: Zero Capacity Edge Case

- **Symptom**: First add failed when constructed with capacity 0
- **Root cause**: `0 * 2 = 0`, resize didn't help
- **Solution**: Fall back to DEFAULT_CAPACITY when current capacity is 0

```java
int newCapacity = elements.length == 0 ? DEFAULT_CAPACITY : elements.length * 2;
```

- **Lesson**: Always handle zero/empty edge cases explicitly

### Issue: Iterator Remove State

- **Symptom**: `remove()` could be called twice, corrupting list
- **Root cause**: `lastReturnedIndex` not reset after remove
- **Solution**: Set `lastReturnedIndex = -1` after remove, check before allowing

```java
public void remove() {
    if (lastReturnedIndex < 0) {
        throw new IllegalStateException("remove() called without next()");
    }
    MyArrayList.this.remove(lastReturnedIndex);
    currentIndex = lastReturnedIndex; // Adjust for shifted elements
    lastReturnedIndex = -1; // Prevent double-remove
    expectedModCount = modCount; // Sync after our own modification
}
```

- **Lesson**: State machines in iterators need careful state management

### Issue: Iterator Index After Remove

- **Symptom**: Element skipped after `iterator.remove()`
- **Root cause**: `currentIndex` not adjusted for left-shift
- **Solution**: Set `currentIndex = lastReturnedIndex` after remove

- **Lesson**: Removal shifts elements; iterator position must compensate

---

## Code Patterns Used

### Pattern: Defensive Index Checking

```java
if (index < 0 || index >= size) {
    throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
}
```

Include both index and size in message for debugging.

### Pattern: Capacity vs Size Distinction

```java
private T[] elements;  // Length = capacity (allocated space)
private int size;      // Count of actual elements

// Capacity can exceed size
// Size can never exceed capacity
```

### Pattern: In-Place Modification with Return

```java
public T remove(int index) {
    final T removed = elements[index];  // Capture before modification
    // ... modify array ...
    return removed;  // Return captured value
}
```

---

## Performance Characteristics

### Measured Behavior

| Operation | 1K elements | 100K elements | 1M elements |
|-----------|-------------|---------------|-------------|
| `add(end)` | ~50 ns | ~50 ns | ~50 ns (amortized) |
| `add(0, x)` | ~1 µs | ~50 µs | ~500 µs |
| `get(i)` | ~5 ns | ~5 ns | ~5 ns |
| `remove(0)` | ~1 µs | ~50 µs | ~500 µs |

*Approximate values; actual performance depends on JVM and hardware*

### Memory Profile

- Object header: ~16 bytes
- `elements` reference: 8 bytes
- `size` int: 4 bytes
- `modCount` int: 4 bytes
- Backing array: 16 + (capacity × 8) bytes

Total overhead: ~48 bytes + unused capacity slots

---

## Future Improvements

*Out of scope for educational purposes, but worth noting:*

1. **Shrinking**: Reduce capacity when size drops below 25% (saves memory)
2. **Bulk operations**: `addAll()`, `removeAll()` could be more efficient
3. **Parallel iteration**: `Spliterator` for stream support
4. **Serialization**: `writeObject`/`readObject` for efficient serialization

---

*Part of the Omni-System Grimoire - Phase 1: Foundation*
