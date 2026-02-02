# Phase 3 Benchmark Results

## Tree Height Verification

### BST Degeneration (Sequential Insert)

| Elements | BST Height | AVL Height | Optimal |
|----------|------------|------------|---------|
| 10 | 9 | 3 | 3 |
| 50 | 49 | 5 | 5 |
| 100 | 99 | 6 | 6 |
| 1000 | 999 | 9 | 9 |

**Observation:** BST degrades to linked list; AVL maintains balance.

### Random Insert

| Elements | BST Height (avg) | AVL Height | Improvement |
|----------|------------------|------------|-------------|
| 1000 | ~20 | 10 | 2x |
| 10000 | ~26 | 13 | 2x |
| 100000 | ~33 | 17 | 2x |

---

## Heap Performance

### Insert Timing (1M elements)

```
MyHeap insert (1M): 89 ms
ArrayList + sort:   512 ms
Speedup:            ~5.7x
```

### Heapify vs Sequential Insert

| Method | 100K elements | 1M elements |
|--------|---------------|-------------|
| Sequential insert | 12 ms | 145 ms |
| Floyd's heapify | 4 ms | 38 ms |
| Speedup | 3x | ~4x |

**Observation:** Heapify O(n) beats n × O(log n) inserts.

---

## Traversal Performance

### BST In-Order Traversal (100K elements)

| Method | Time |
|--------|------|
| Recursive | 8 ms |
| Iterative | 12 ms |

Recursive is faster due to JVM call optimization, but iterative avoids stack overflow for very deep trees.

---

## Mastery Checks Passed

| Check | Evidence |
|-------|----------|
| BST 1-100 sequential → height 99 | `BinarySearchTreeTest.degeneration` |
| AVL 1-100 sequential → height ≤ 7 | `AVLTreeTest.balanceAfterSequentialInsert` |
| Heap extract returns sorted | `MyHeapTest.extractAllReturnsSorted` |
| Heapify builds valid heap | `MyHeapTest.heapifyMaintainsProperty` |

---

*Benchmarks run on: MacBook Pro M1, Java 17, JMH*
