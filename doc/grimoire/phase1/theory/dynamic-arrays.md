# Dynamic Arrays (MyArrayList)

## Overview

A dynamic array is a resizable array data structure that provides O(1) random access to elements by index. Unlike static arrays, it automatically grows when capacity is exceeded, making it one of the most versatile and commonly used data structures in programming.

In the Omni-System, `MyArrayList<T>` implements this concept, serving as the foundation for many higher-level structures.

## How It Works

### Memory Layout

```
Index:   0   1   2   3   4   ...   Capacity-1
        +---+---+---+---+---+-----+---+
Array:  | A | B | C |   |   | ... |   |
        +---+---+---+---+---+-----+---+
                  ^                   ^
                  |                   |
                Size=3             Capacity
```

- **Capacity**: Total allocated space in the backing array
- **Size**: Number of actual elements stored
- Elements occupy contiguous memory locations

### The Resizing Strategy

When the array is full and a new element is added:

1. Allocate a new array with **double the capacity**
2. Copy all existing elements to the new array
3. Replace the old array reference

```
Before (full):  [A] [B] [C]  capacity=3, size=3

Add 'D':        [A] [B] [C] [D] [ ] [ ]  capacity=6, size=4
                     (new array)
```

**Why double?** This gives us **amortized O(1)** insertion. If we only added one slot at a time, every insertion would require copying all elements (O(n) each time).

### Amortized Analysis

Consider inserting n elements starting from capacity 1:

| Insertions | Copies Made | Running Total |
|------------|-------------|---------------|
| 1          | 0           | 0             |
| 2          | 1           | 1             |
| 3          | 2           | 3             |
| 5          | 4           | 7             |
| 9          | 8           | 15            |
| n          | ~n          | ~2n           |

Total copies ≈ 2n for n insertions → **amortized O(1)** per insertion.

## Complexity Analysis

| Operation | Average Case | Worst Case | Notes |
|-----------|--------------|------------|-------|
| `get(i)` | O(1) | O(1) | Direct array indexing |
| `add(element)` | O(1)* | O(n) | *Amortized; worst case is resize |
| `add(i, element)` | O(n) | O(n) | Shift elements right |
| `remove(i)` | O(n) | O(n) | Shift elements left |
| `contains(element)` | O(n) | O(n) | Linear search |
| `indexOf(element)` | O(n) | O(n) | Linear search |
| `set(i, element)` | O(1) | O(1) | Direct array assignment |
| `size()` | O(1) | O(1) | Tracked separately |

## Space Complexity

- **Overhead**: O(n) where n is capacity (not size)
- **Wasted space**: At most 50% after a resize (size = capacity/2 + 1)
- **Minimum waste**: 0% when size = capacity

## Trade-offs

### When to Use MyArrayList

- **Frequent random access** by index (O(1) lookup)
- **Mostly appending** to the end (amortized O(1))
- **Memory efficiency matters** (contiguous storage, good cache locality)
- **Known approximate size** (can pre-allocate to avoid resizing)

### When to Avoid MyArrayList

- **Frequent insertions/deletions at the beginning** → Use LinkedList (O(1) vs O(n))
- **Frequent insertions/deletions in the middle** → Consider LinkedList
- **Memory is very tight** and size fluctuates wildly → Wasted capacity might be an issue

### Comparison with MyLinkedList

| Criteria | MyArrayList | MyLinkedList |
|----------|-------------|--------------|
| Random access | O(1) | O(n) |
| Add to end | O(1) amortized | O(1) |
| Add to beginning | O(n) | O(1) |
| Memory per element | ~8 bytes (ref) | ~24 bytes (value + 2 refs) |
| Cache performance | Excellent | Poor |

## Visual Representation

### Insertion at Index

```
Before: [A] [B] [C] [D] [ ]
         0   1   2   3   4
             ^ Insert 'X' at index 1

Step 1 - Shift right:
        [A] [ ] [B] [C] [D]
         0   1   2   3   4

Step 2 - Insert:
        [A] [X] [B] [C] [D]
         0   1   2   3   4
```

### Removal at Index

```
Before: [A] [B] [C] [D]
         0   1   2   3
             ^ Remove index 1 ('B')

Step 1 - Shift left:
        [A] [C] [D] [D]
         0   1   2   3

Step 2 - Null last:
        [A] [C] [D] [null]
         0   1   2   3
        size = 3
```

## Implementation Highlights

### Key Design Decisions in MyArrayList

1. **Default capacity of 10**: Balances memory usage with resize frequency
2. **Doubling strategy**: Guarantees amortized O(1) appends
3. **Fail-fast iterator**: Detects concurrent modification via `modCount`
4. **Null-out removed elements**: Helps garbage collection

### The modCount Pattern

```java
private int modCount; // Incremented on structural changes

// In iterator:
private int expectedModCount = modCount;

public T next() {
    if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
    }
    // ...
}
```

This pattern detects when the list is modified while iterating, preventing subtle bugs.

## Real-World Applications

| Use Case | Why Dynamic Array? |
|----------|-------------------|
| **ArrayList in Java** | The standard List implementation |
| **Vector in C++** | STL's dynamic array |
| **Log buffers** | Append-only with occasional reads |
| **UI element lists** | Random access for rendering |
| **String builders** | Character arrays that grow |

## Common Pitfalls

1. **Forgetting resize cost**: While amortized O(1), a single add can be O(n)
2. **Excessive inserts at index 0**: Each is O(n) - use LinkedList instead
3. **Not pre-sizing**: If you know you'll add 10,000 elements, start with that capacity
4. **Memory leaks**: Always null out removed elements (Java GC won't collect referenced objects)

## Further Reading

- **CLRS Chapter 17**: Amortized Analysis
- **Java Collections Framework**: `java.util.ArrayList` source code
- **Cache-Oblivious Algorithms**: Why contiguous memory matters

---

*Part of the Omni-System Grimoire - Phase 1: Foundation*
