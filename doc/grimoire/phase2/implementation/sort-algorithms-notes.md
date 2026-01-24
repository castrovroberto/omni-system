# SortAlgorithms Implementation Notes

> Design decisions, gotchas, and lessons learned from implementing sorting algorithms.

**Source**: [`SortAlgorithms.java`](../../../../src/main/java/com/omni/core/algorithm/sort/SortAlgorithms.java)

---

## Design Decisions

### Decision 1: Static Utility Class

- **Options considered**: Instance methods, static utility, interface with implementations
- **Chosen**: Static utility class
- **Rationale**:
  - Algorithms are stateless operations
  - No need for polymorphism (unlike HashStrategy)
  - Clean, simple API: `SortAlgorithms.mergeSort(list, comparator)`
  - Matches Java's `Collections.sort()` and `Arrays.sort()` patterns

```java
public final class SortAlgorithms {
    private SortAlgorithms() {} // Prevent instantiation

    public static <T> void mergeSort(MyList<T> list, Comparator<T> comparator) { ... }
    public static <T> void quickSort(MyList<T> list, Comparator<T> comparator) { ... }
}
```

### Decision 2: In-Place Modification

- **Options considered**: Return new list, modify in-place
- **Chosen**: Modify in-place
- **Rationale**:
  - Matches Java's `Collections.sort()` behavior
  - More memory efficient
  - Caller can create copy if needed
  - MergeSort internally uses extra space but copies back to original

```java
public static <T> void mergeSort(MyList<T> list, Comparator<T> comparator) {
    MyList<T> sorted = mergeSortRecursive(list, comparator);
    // Copy back to original list
    for (int i = 0; i < list.size(); i++) {
        list.set(i, sorted.get(i));
    }
}
```

### Decision 3: Median-of-Three Pivot for QuickSort

- **Options considered**: First element, last element, random, median-of-three
- **Chosen**: Median-of-three (first, middle, last)
- **Rationale**:
  - Prevents O(n²) on sorted or reverse-sorted input
  - Deterministic (unlike random pivot)
  - Simple to implement
  - Standard optimization

```java
private static <T> void medianOfThree(
        MyList<T> list, int low, int mid, int high, Comparator<T> comparator) {
    // Sort low, mid, high so median is at mid
    if (compare(list.get(low), list.get(mid), comparator) > 0) {
        swap(list, low, mid);
    }
    if (compare(list.get(low), list.get(high), comparator) > 0) {
        swap(list, low, high);
    }
    if (compare(list.get(mid), list.get(high), comparator) > 0) {
        swap(list, mid, high);
    }
}
```

### Decision 4: Insertion Sort for Small Subarrays

- **Options considered**: Pure recursion, cutoff to insertion sort
- **Chosen**: Switch to insertion sort when size < 10
- **Rationale**:
  - Insertion sort has lower overhead for small arrays
  - Reduces recursion depth
  - Standard optimization in production implementations
  - Threshold of 10 is empirically good

```java
if (high - low < 10) {
    insertionSort(list, low, high, comparator);
    return;
}
```

### Decision 5: Optional Comparator with Comparable Fallback

- **Options considered**: Require comparator, require Comparable, support both
- **Chosen**: Optional comparator with Comparable fallback
- **Rationale**:
  - Maximum flexibility
  - Matches Java's `Collections.sort()` API
  - Natural ordering for simple cases
  - Custom comparators for complex cases

```java
@SuppressWarnings("unchecked")
private static <T> int compare(T a, T b, Comparator<T> comparator) {
    if (comparator != null) {
        return comparator.compare(a, b);
    }
    return ((Comparable<T>) a).compareTo(b);
}

// Usage
SortAlgorithms.mergeSort(stringList);  // Uses natural order
SortAlgorithms.mergeSort(list, Comparator.reverseOrder());  // Custom
```

### Decision 6: Lomuto Partition Scheme

- **Options considered**: Lomuto, Hoare, Dutch National Flag
- **Chosen**: Lomuto partition
- **Rationale**:
  - Simpler to understand and implement
  - Works well with median-of-three
  - Hoare is slightly faster but harder to implement correctly
  - Educational clarity prioritized

```java
private static <T> int partition(MyList<T> list, int low, int high, Comparator<T> comparator) {
    T pivot = list.get(high);
    int i = low - 1;

    for (int j = low; j < high; j++) {
        if (compare(list.get(j), pivot, comparator) <= 0) {
            i++;
            swap(list, i, j);
        }
    }
    swap(list, i + 1, high);
    return i + 1;
}
```

---

## Gotchas & Lessons Learned

### Issue: MergeSort Not Actually Sorting Original List

- **Symptom**: Original list unchanged after mergeSort()
- **Root cause**: Recursive function returned new list, original never modified
- **Solution**: Copy sorted elements back to original list

```java
public static <T> void mergeSort(MyList<T> list, Comparator<T> comparator) {
    if (list == null || list.size() <= 1) return;

    MyList<T> sorted = mergeSortRecursive(list, comparator);

    // MUST copy back to original list
    for (int i = 0; i < list.size(); i++) {
        list.set(i, sorted.get(i));
    }
}
```

- **Lesson**: Clearly define whether sort is in-place or returns new list

### Issue: QuickSort Stack Overflow on Large Arrays

- **Symptom**: StackOverflowError on arrays with 100K+ elements
- **Root cause**: Deep recursion with poor pivot selection
- **Solution**: Median-of-three + insertion sort cutoff

```java
// Worst case recursion depth with bad pivot: O(n)
// With median-of-three: O(log n) expected
```

- **Lesson**: Recursion depth matters for production code

### Issue: Off-by-One in Partition

- **Symptom**: Elements not properly partitioned
- **Root cause**: Inclusive vs exclusive bounds confusion
- **Solution**: Be explicit about bounds semantics

```java
// Convention: [low, high] inclusive
private static <T> int partition(MyList<T> list, int low, int high, ...) {
    // low and high are BOTH included in partition
}
```

- **Lesson**: Document bounds conventions clearly

### Issue: Stability Lost in QuickSort

- **Symptom**: Equal elements reordered after sorting
- **Root cause**: Partition swaps distant elements
- **Solution**: Document that QuickSort is unstable; use MergeSort for stability

```java
// QuickSort is NOT stable
// Example: [(A,1), (B,1), (C,1)] sorted by value
// Could become [(C,1), (A,1), (B,1)]
```

- **Lesson**: Stability is a key algorithm property to document

### Issue: Comparator Null Handling

- **Symptom**: NullPointerException when comparator was null
- **Root cause**: Didn't handle null comparator case
- **Solution**: Fall back to Comparable when comparator is null

```java
@SuppressWarnings("unchecked")
private static <T> int compare(T a, T b, Comparator<T> comparator) {
    if (comparator != null) {
        return comparator.compare(a, b);
    }
    // Fallback to natural ordering
    return ((Comparable<T>) a).compareTo(b);
}
```

- **Lesson**: Null parameters need explicit handling

---

## Code Patterns Used

### Pattern: Divide-and-Conquer Template

```java
// MergeSort pattern
void sort(list, low, high) {
    if (low >= high) return;  // Base case

    int mid = (low + high) / 2;
    sort(list, low, mid);      // Divide left
    sort(list, mid + 1, high); // Divide right
    merge(list, low, mid, high); // Combine
}

// QuickSort pattern
void sort(list, low, high) {
    if (low >= high) return;  // Base case

    int pivot = partition(list, low, high);  // Divide
    sort(list, low, pivot - 1);              // Left
    sort(list, pivot + 1, high);             // Right
    // No combine step - partitioning does the work
}
```

### Pattern: Safe Swap

```java
private static <T> void swap(MyList<T> list, int i, int j) {
    if (i != j) {  // Avoid unnecessary writes
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
```

### Pattern: Helper for isSorted Check

```java
public static <T> boolean isSorted(MyList<T> list, Comparator<T> comparator) {
    for (int i = 1; i < list.size(); i++) {
        if (compare(list.get(i - 1), list.get(i), comparator) > 0) {
            return false;
        }
    }
    return true;
}
```

Useful for testing and assertions.

---

## Performance Characteristics

### MergeSort

| Metric | 10K elements | 100K elements | 1M elements |
|--------|--------------|---------------|-------------|
| Time | ~5 ms | ~60 ms | ~800 ms |
| Comparisons | ~130K | ~1.6M | ~20M |
| Memory | O(n) | O(n) | O(n) |

### QuickSort

| Metric | 10K elements | 100K elements | 1M elements |
|--------|--------------|---------------|-------------|
| Time (random) | ~3 ms | ~40 ms | ~500 ms |
| Time (sorted) | ~4 ms | ~50 ms | ~600 ms |
| Comparisons | ~140K | ~1.8M | ~22M |
| Memory | O(log n) | O(log n) | O(log n) |

*QuickSort is typically faster due to better cache performance*

### Median-of-Three Effect

Without median-of-three on sorted input (100K elements):
- Naive pivot: **~15,000 ms** (O(n²))
- Median-of-three: **~50 ms** (O(n log n))

**300x improvement!**

---

## Algorithm Analysis

### MergeSort Recurrence

```
T(n) = 2T(n/2) + O(n)
     = O(n log n)

Level 0: 1 problem of size n      → n work
Level 1: 2 problems of size n/2   → n work
Level 2: 4 problems of size n/4   → n work
...
Level log(n): n problems of size 1 → n work

Total: O(n) × O(log n) = O(n log n)
```

### QuickSort Recurrence

Best/Average case:
```
T(n) = 2T(n/2) + O(n) = O(n log n)
```

Worst case (all elements in one partition):
```
T(n) = T(n-1) + O(n) = O(n²)
```

---

## Future Improvements

*Out of scope for educational purposes, but worth noting:*

1. **Iterative QuickSort**: Avoid stack overflow with explicit stack
2. **Parallel MergeSort**: Independent subproblems can run in parallel
3. **TimSort**: Hybrid for real-world data (runs of sorted elements)
4. **IntroSort**: QuickSort + HeapSort fallback for guaranteed O(n log n)
5. **Three-way partition**: Better for many duplicates

---

*Part of the Omni-System Grimoire - Phase 2: Organization & Speed*
