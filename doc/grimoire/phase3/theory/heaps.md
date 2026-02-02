# Heaps

> Complete binary tree with heap property, ideal for priority queues.

## The Heap Property

- **Min-Heap:** parent ≤ children (smallest at root)
- **Max-Heap:** parent ≥ children (largest at root)

```
Min-Heap:           Max-Heap:
      1                   9
     / \                 / \
    3   2               7   8
   / \ /               / \ /
  7  5 4              3  5 4
```

## Array Representation

A heap is stored as an array using level-order indexing:

```
Index:  0   1   2   3   4   5
Array: [1 | 3 | 2 | 7 | 5 | 4]

Tree:       1(0)
           /    \
        3(1)    2(2)
        / \     /
      7(3) 5(4) 4(5)
```

**Navigation formulas:**
- Parent of i: `(i - 1) / 2`
- Left child of i: `2i + 1`
- Right child of i: `2i + 2`

## Core Operations

### Insert (Sift Up)
1. Add element at end of array
2. Compare with parent, swap if violates heap property
3. Repeat until valid or at root

```java
void insert(T value) {
    data.add(value);
    siftUp(data.size() - 1);
}

void siftUp(int index) {
    while (index > 0) {
        int parent = (index - 1) / 2;
        if (comparator.compare(data.get(index), data.get(parent)) < 0) {
            swap(index, parent);
            index = parent;
        } else break;
    }
}
```

### Extract Root (Sift Down)
1. Save root value
2. Move last element to root
3. Compare with smaller child, swap if needed
4. Repeat until valid or at leaf

**Complexity:** O(log n)

### Heapify (Floyd's Algorithm)
Build heap from unordered array in O(n):

```java
for (int i = (size / 2) - 1; i >= 0; i--) {
    siftDown(i);
}
```

*Why O(n)?* Most nodes are near leaves and require few swaps.

## Complexity

| Operation | Time |
|-----------|------|
| peek | O(1) |
| insert | O(log n) |
| extractRoot | O(log n) |
| heapify | O(n) |
| search | O(n) |

## Use Cases

| Application | Heap Type |
|-------------|-----------|
| Task scheduler | Min-Heap (lowest priority first) |
| Dijkstra's algorithm | Min-Heap (shortest distance) |
| Find k largest | Min-Heap of size k |
| Find k smallest | Max-Heap of size k |
| Median stream | Two heaps (min + max) |

## Heap Sort

```java
// 1. Build max-heap from array
// 2. Repeatedly extract max and place at end
for (int i = n - 1; i > 0; i--) {
    swap(0, i);
    heapify(0, i);  // heapify reduced range
}
```

**Complexity:** O(n log n), in-place, not stable.

## Mastery Check

1. Insert 10 random values → verify min/max at root
2. Extract all elements → verify sorted order
3. Compare heapify O(n) vs n insertions O(n log n)
