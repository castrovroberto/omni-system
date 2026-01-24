# Sorting Algorithms (MergeSort & QuickSort)

## Overview

Sorting is one of the most fundamental operations in computer science. The Omni-System implements two classic O(n log n) comparison-based sorting algorithms:

- **Merge Sort**: Stable, guaranteed O(n log n), uses O(n) extra space
- **Quick Sort**: Unstable, O(n log n) average, in-place with O(log n) stack space

Both use the **divide-and-conquer** paradigm.

## Merge Sort

### How It Works

1. **Divide**: Split the list into two halves
2. **Conquer**: Recursively sort each half
3. **Combine**: Merge the sorted halves

```
Original:  [38, 27, 43, 3, 9, 82, 10]
                    │
         ┌──────────┴──────────┐
         ▼                     ▼
    [38, 27, 43, 3]      [9, 82, 10]
         │                     │
    ┌────┴────┐           ┌────┴────┐
    ▼         ▼           ▼         ▼
[38, 27]  [43, 3]     [9, 82]    [10]
    │         │           │         │
  ┌─┴─┐     ┌─┴─┐       ┌─┴─┐       │
  ▼   ▼     ▼   ▼       ▼   ▼       ▼
[38][27]  [43][3]     [9][82]    [10]
  │   │     │   │       │   │       │
  └─┬─┘     └─┬─┘       └─┬─┘       │
    ▼         ▼           ▼         │
[27, 38]  [3, 43]     [9, 82]    [10]
    │         │           │         │
    └────┬────┘           └────┬────┘
         ▼                     ▼
   [3, 27, 38, 43]      [9, 10, 82]
         │                     │
         └──────────┬──────────┘
                    ▼
        [3, 9, 10, 27, 38, 43, 82]
```

### The Merge Operation

```
Left:  [3, 27, 38, 43]    Right: [9, 10, 82]
        ↑                         ↑
        i                         j

Compare left[i] with right[j], take smaller:

Result: [3]               (took from left, i++)
Result: [3, 9]            (took from right, j++)
Result: [3, 9, 10]        (took from right, j++)
Result: [3, 9, 10, 27]    (took from left, i++)
Result: [3, 9, 10, 27, 38](took from left, i++)
Result: [3, 9, 10, 27, 38, 43]    (took from left, i++)
Result: [3, 9, 10, 27, 38, 43, 82](append remaining from right)
```

### Complexity

| Metric | Value | Notes |
|--------|-------|-------|
| Time (best) | O(n log n) | Always divides in half |
| Time (average) | O(n log n) | Always divides in half |
| Time (worst) | O(n log n) | Always divides in half |
| Space | O(n) | Needs auxiliary array for merging |
| Stable | Yes | Equal elements maintain order |

### Why O(n log n)?

- **log n levels** of recursion (halving each time)
- **O(n) work** at each level (merging)
- Total: O(n) × O(log n) = O(n log n)

```
Level 0:  n elements to merge         → O(n) work
Level 1:  n/2 + n/2 elements to merge → O(n) work
Level 2:  n/4 × 4 elements to merge   → O(n) work
...
Level log(n): n × 1 elements          → O(n) work
                                      ─────────────
                                      O(n log n) total
```

---

## Quick Sort

### How It Works

1. **Choose pivot**: Select an element (median-of-three)
2. **Partition**: Rearrange so elements < pivot are left, > pivot are right
3. **Recurse**: Sort left and right partitions

```
Original:  [3, 7, 8, 5, 2, 1, 9, 5, 4]
                      ↓
           Choose pivot (median of 3, 8, 4) = 4
                      ↓
           Partition around 4:
           [3, 2, 1] [4] [7, 8, 5, 9, 5]
              ↓              ↓
           Recurse        Recurse
              ↓              ↓
           [1, 2, 3]     [5, 5, 7, 8, 9]
                      ↓
           [1, 2, 3, 4, 5, 5, 7, 8, 9]
```

### Partition Algorithm (Lomuto Scheme)

```
Array: [3, 7, 8, 5, 2, 1, 9, 5, 4]
                               ↑
                             pivot

i = -1 (boundary of "less than pivot" region)

j=0: 3 < 4 → swap, i=0  → [3, 7, 8, 5, 2, 1, 9, 5, 4]
j=1: 7 > 4 → skip       → [3, 7, 8, 5, 2, 1, 9, 5, 4]
j=2: 8 > 4 → skip       → [3, 7, 8, 5, 2, 1, 9, 5, 4]
j=3: 5 > 4 → skip       → [3, 7, 8, 5, 2, 1, 9, 5, 4]
j=4: 2 < 4 → swap, i=1  → [3, 2, 8, 5, 7, 1, 9, 5, 4]
j=5: 1 < 4 → swap, i=2  → [3, 2, 1, 5, 7, 8, 9, 5, 4]
j=6: 9 > 4 → skip       → [3, 2, 1, 5, 7, 8, 9, 5, 4]
j=7: 5 > 4 → skip       → [3, 2, 1, 5, 7, 8, 9, 5, 4]

Final: swap pivot with i+1:
       [3, 2, 1, 4, 7, 8, 9, 5, 5]
                ↑
              pivot in final position
```

### Median-of-Three Pivot Selection

Naive pivot selection (first or last element) degrades to O(n²) on sorted input.

**Median-of-three** picks the median of first, middle, and last elements:

```
Array: [1, 2, 3, 4, 5, 6, 7, 8, 9]  (already sorted)
        ↑        ↑              ↑
      first    middle         last
       (1)      (5)            (9)

Median of (1, 5, 9) = 5  ← Good pivot!

Without median-of-three:
- First element (1) → all elements go right → O(n²)
- Last element (9) → all elements go left → O(n²)

With median-of-three:
- Pivot (5) → balanced partitions → O(n log n)
```

### Complexity

| Metric | Value | Notes |
|--------|-------|-------|
| Time (best) | O(n log n) | Balanced partitions |
| Time (average) | O(n log n) | Random-ish partitions |
| Time (worst) | O(n²) | All elements in one partition |
| Space | O(log n) | Recursion stack |
| Stable | No | Partition swaps distant elements |

### Insertion Sort for Small Subarrays

For small arrays (< 10 elements), insertion sort is faster due to:
- Lower constant factors
- Better cache behavior
- No recursion overhead

```java
if (high - low < 10) {
    insertionSort(list, low, high, comparator);
    return;
}
```

---

## Comparison: MergeSort vs QuickSort

| Criterion | MergeSort | QuickSort |
|-----------|-----------|-----------|
| Time (worst) | O(n log n) | O(n²) |
| Time (average) | O(n log n) | O(n log n) |
| Space | O(n) | O(log n) |
| Stable | Yes | No |
| Cache performance | Poor | Good |
| In-place | No | Yes |
| Parallelizable | Yes | Harder |

### When to Use MergeSort

- **Stability required**: Preserves order of equal elements
- **Guaranteed performance**: No O(n²) worst case
- **External sorting**: Works well with disk I/O
- **Linked lists**: No random access needed

### When to Use QuickSort

- **Memory-constrained**: O(log n) vs O(n) space
- **Cache performance**: Sequential access patterns
- **Average case**: Usually faster in practice
- **Random data**: Low probability of worst case

---

## Stability Explained

**Stable sort**: Equal elements maintain their relative order.

```
Original: [(Bob, 25), (Alice, 30), (Charlie, 25)]
                 ↑                        ↑
              First 25                Second 25

Sort by age:

Stable (MergeSort):
[(Bob, 25), (Charlie, 25), (Alice, 30)]
     ↑            ↑
  Still first  Still second

Unstable (QuickSort):
[(Charlie, 25), (Bob, 25), (Alice, 30)]
      ↑            ↑
   Now first    Now second (order changed!)
```

Stability matters for:
- Multi-key sorting (sort by last name, then first name)
- Preserving original order for equal keys
- Consistent, reproducible results

---

## Visual: Divide-and-Conquer

```
                    ┌─────────────────────────────────┐
                    │         Original Array          │
                    │    [38, 27, 43, 3, 9, 82, 10]   │
                    └─────────────────────────────────┘
                                    │
                                 DIVIDE
                                    │
                    ┌───────────────┴───────────────┐
                    ▼                               ▼
          ┌─────────────────┐             ┌─────────────────┐
          │  [38, 27, 43, 3] │             │   [9, 82, 10]   │
          └─────────────────┘             └─────────────────┘
                    │                               │
                    ▼                               ▼
            ... (recurse) ...               ... (recurse) ...
                    │                               │
                    ▼                               ▼
          ┌─────────────────┐             ┌─────────────────┐
          │  [3, 27, 38, 43] │             │   [9, 10, 82]   │
          └─────────────────┘             └─────────────────┘
                    │                               │
                    └───────────────┬───────────────┘
                                    │
                                 COMBINE
                                    │
                                    ▼
                    ┌─────────────────────────────────┐
                    │         Sorted Array            │
                    │  [3, 9, 10, 27, 38, 43, 82]     │
                    └─────────────────────────────────┘
```

---

## Real-World Applications

| Use Case | Algorithm | Why? |
|----------|-----------|------|
| **Arrays.sort (primitives)** | Dual-pivot QuickSort | Speed, in-place |
| **Arrays.sort (objects)** | TimSort (MergeSort variant) | Stability |
| **Database ORDER BY** | External MergeSort | Handles disk I/O |
| **Parallel sorting** | Parallel MergeSort | Independent subproblems |
| **Nearly sorted data** | TimSort/InsertionSort | Exploits existing order |

---

## Common Pitfalls

1. **QuickSort on sorted data**: Use median-of-three or random pivot
2. **Stack overflow**: Deep recursion on large arrays (use iterative version)
3. **Forgetting stability**: Use MergeSort when order matters
4. **Overhead for small arrays**: Switch to InsertionSort

---

## Further Reading

- **CLRS Chapters 2, 7**: Sorting algorithms
- **TimSort**: Python/Java's hybrid algorithm
- **Radix Sort**: O(n) for integers (non-comparison)
- **Parallel MergeSort**: Divide work across threads

---

*Part of the Omni-System Grimoire - Phase 2: Organization & Speed*
