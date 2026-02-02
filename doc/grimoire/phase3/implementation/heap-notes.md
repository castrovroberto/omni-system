# MyHeap Implementation Notes

## Design Decisions

### 1. MyArrayList Backing
```java
private final MyArrayList<T> data;
```
Uses our Phase 1 array list. O(1) amortized append for insert.

### 2. Comparator-Based Ordering
```java
private final Comparator<T> comparator;
```
Enables min-heap (default), max-heap, or custom ordering.

### 3. Static Factory for Natural Ordering
```java
public static <T extends Comparable<T>> MyHeap<T> natural() {
    return new MyHeap<>((a, b) -> a.compareTo(b));
}
```
Convenience for min-heap with Comparable types.

---

## Array Index Formulas

```
For node at index i:
├── Parent:      (i - 1) / 2
├── Left child:  2 * i + 1
└── Right child: 2 * i + 2

Example (min-heap):
Index:  0   1   2   3   4   5
Array: [1 | 3 | 2 | 7 | 5 | 4]

Tree:       1(0)
           /    \
        3(1)    2(2)
        / \     /
      7(3) 5(4) 4(5)
```

---

## Sift Operations

### Sift Up (after insert)
```java
private void siftUp(int index) {
    while (index > 0) {
        int parent = (index - 1) / 2;
        if (comparator.compare(data.get(index), data.get(parent)) < 0) {
            swap(index, parent);
            index = parent;
        } else {
            break;
        }
    }
}
```

### Sift Down (after extract)
```java
private void siftDown(int index) {
    while (true) {
        int smallest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        
        if (left < size && compare(left) < compare(smallest)) {
            smallest = left;
        }
        if (right < size && compare(right) < compare(smallest)) {
            smallest = right;
        }
        if (smallest == index) break;
        
        swap(index, smallest);
        index = smallest;
    }
}
```

---

## Gotchas

### 1. Extract Must Handle Last Element
```java
public T extractRoot() {
    T root = data.get(0);
    data.set(0, data.get(data.size() - 1));
    data.remove(data.size() - 1);
    if (!data.isEmpty()) {
        siftDown(0);  // Only if heap still has elements!
    }
    return root;
}
```

### 2. Heapify Index Range
Only non-leaf nodes need sift-down:
```java
for (int i = (size / 2) - 1; i >= 0; i--) {
    siftDown(i);
}
```
Leaves are already valid heaps (no children).

### 3. Size-1 Edge Case
Single element heap: extract returns it, no sift needed.

---

## Complexity

| Operation | Time |
|-----------|------|
| peek | O(1) |
| insert | O(log n) |
| extractRoot | O(log n) |
| heapify | O(n) |
| contains/search | O(n) |

Heapify is O(n) because most nodes are near leaves and require few swaps.
