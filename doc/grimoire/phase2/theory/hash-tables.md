# Hash Tables (MyHashMap)

## Overview

A hash table is a data structure that provides average O(1) time complexity for insert, lookup, and delete operations. It achieves this by using a hash function to compute an index into an array of buckets, from which the desired value can be found.

In the Omni-System, `MyHashMap<K,V>` implements a hash table using **separate chaining** for collision resolution.

## How It Works

### The Hashing Process

```
Key: "alice"
     │
     ▼
┌─────────────┐
│ hashCode()  │  → 92668753
└─────────────┘
     │
     ▼
┌─────────────┐
│   % capacity │  → 92668753 % 16 = 1
└─────────────┘
     │
     ▼
┌─────────────┐
│  Bucket[1]  │  → Store entry here
└─────────────┘
```

### Memory Layout (Separate Chaining)

```
Buckets Array:
Index:  0       1           2       3       ...     15
      ┌───┐   ┌───┐       ┌───┐   ┌───┐           ┌───┐
      │ ● │   │ ● │       │nil│   │ ● │    ...    │nil│
      └─┬─┘   └─┬─┘       └───┘   └─┬─┘           └───┘
        │       │                   │
        ▼       ▼                   ▼
      ┌───┐   ┌───┐               ┌───┐
      │A=1│   │B=2│               │D=4│
      └─┬─┘   └─┬─┘               └───┘
        │       │
        ▼       ▼
      ┌───┐   ┌───┐
      │E=5│   │C=3│  ← Collision chain
      └───┘   └───┘
```

Each bucket contains a linked list of entries that hash to the same index.

### Collision Resolution: Separate Chaining

When two keys hash to the same bucket (collision), we add them to a linked list:

```java
// Collision example
hash("alice") % 16 = 5
hash("bob")   % 16 = 5  // Same bucket!

Bucket[5]: alice=100 → bob=200
```

**Advantages of chaining:**
- Simple implementation
- Never runs out of space
- Deletions are easy

**Disadvantages:**
- Extra memory for linked list nodes
- Poor cache locality (nodes scattered in memory)

### Load Factor and Resizing

The **load factor** is the ratio of entries to buckets:

```
load_factor = size / capacity
```

When load factor exceeds a threshold (default 0.75), we **resize**:

1. Create new array with double capacity
2. Rehash all entries (their bucket index changes)
3. Replace old array

```
Before resize (load = 12/16 = 0.75):
┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐
│ 2 │ 1 │ 0 │ 1 │ 2 │ 0 │ 1 │ 0 │ 2 │ 1 │ 0 │ 0 │ 1 │ 0 │ 1 │ 0 │
└───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘
                     (chain lengths)

After resize (load = 12/32 = 0.375):
┌───┬───┬───┬───┬───┬───┬───┬───┬...┬───┬───┬───┬───┬───┬───┬───┐
│ 1 │ 0 │ 1 │ 0 │ 1 │ 0 │ 1 │ 1 │...│ 0 │ 1 │ 0 │ 1 │ 1 │ 0 │ 1 │
└───┴───┴───┴───┴───┴───┴───┴───┴...┴───┴───┴───┴───┴───┴───┴───┘
                     (shorter chains after rehash)
```

## Complexity Analysis

| Operation | Average Case | Worst Case | Notes |
|-----------|--------------|------------|-------|
| `put(k, v)` | O(1) | O(n) | O(n) when all keys collide |
| `get(k)` | O(1) | O(n) | O(n) when all keys collide |
| `remove(k)` | O(1) | O(n) | O(n) when all keys collide |
| `containsKey(k)` | O(1) | O(n) | Same as get |
| `containsValue(v)` | O(n) | O(n) | Must scan all entries |
| Resize | O(n) | O(n) | Rehash all entries |

### Why O(1) Average?

With a good hash function and load factor ≤ 0.75:
- Each bucket has ~1 entry on average
- Lookup is: compute hash (O(1)) + access bucket (O(1)) + scan short list (O(1))

### When O(n) Happens

If all keys hash to the same bucket:
```
Bucket[0]: key1 → key2 → key3 → ... → keyN
           ↑
           Must traverse entire chain
```

This is why hash function quality matters!

## Space Complexity

- **Buckets array**: O(capacity)
- **Entries**: O(n) where n = number of entries
- **Per entry overhead**: ~24 bytes (key ref + value ref + next pointer)
- **Wasted space**: At most 25% after resize (new load = old_load / 2)

## The Strategy Pattern for Hashing

The implementation uses the Strategy pattern to allow swappable hash functions:

```java
public interface HashStrategy<K> {
    int hash(K key, int capacity);
}

// Usage
MyHashMap<String, Integer> map = new MyHashMap<>();
map.setHashStrategy(new DJB2HashStrategy()); // Swap algorithm
```

### DJB2 Hash Algorithm

A simple and effective hash function for strings:

```
hash = 5381
for each character c:
    hash = hash * 33 + c
```

Why it works:
- 5381 is a prime with good bit distribution
- 33 = 32 + 1 = 2^5 + 1 (fast to compute with shift)
- Produces well-distributed values for ASCII text

## Trade-offs

### When to Use MyHashMap

- **Fast key lookup** (O(1) average)
- **No ordering needed** (entries are unordered)
- **Key equality well-defined** (proper hashCode/equals)
- **Memory is available** (overhead per entry)

### When to Avoid MyHashMap

- **Need sorted order** → Use TreeMap/AVL Tree
- **Keys aren't hashable** → Use other structures
- **Memory-constrained** → Open addressing may be better
- **Iteration order matters** → Use LinkedHashMap

### Comparison with Trees

| Criteria | HashMap | TreeMap |
|----------|---------|---------|
| Get/Put | O(1) avg | O(log n) |
| Ordering | None | Sorted |
| Range queries | Not efficient | O(log n + k) |
| Memory | More (chains) | More (tree nodes) |
| hashCode needed | Yes | No |
| Comparable needed | No | Yes |

## Implementation Highlights

### Power-of-2 Capacity

Capacity is always a power of 2 (16, 32, 64, ...) because:

```java
// Fast modulo for power of 2:
index = hash & (capacity - 1)  // Bitwise AND

// Instead of:
index = hash % capacity        // Division (slower)
```

### Bit Spreading

Poor hashCode implementations may not use all bits:

```java
// Original hash might cluster in low bits
int h = key.hashCode();

// Spread high bits into low bits
h ^= (h >>> 16);

// Now distribution is better
```

### Null Key Handling

Null keys hash to bucket 0:

```java
private int getBucketIndex(K key) {
    if (key == null) return 0;
    return hashStrategy.hash(key, capacity);
}
```

## Real-World Applications

| Use Case | Why Hash Table? |
|----------|-----------------|
| **Caches** | O(1) lookup of cached values |
| **Symbol tables** | Compiler variable lookup |
| **Databases** | Index for primary keys |
| **Counting** | Word frequency, vote tallying |
| **Deduplication** | Seen set for unique items |
| **Routing** | URL to handler mapping |

## Common Pitfalls

1. **Mutable keys**: If key changes after insertion, it's lost forever
   ```java
   List<Integer> key = new ArrayList<>();
   map.put(key, "value");
   key.add(1);  // Hash changed! Can't find "value" anymore
   ```

2. **Inconsistent hashCode/equals**: Must follow the contract
   ```java
   // If a.equals(b), then a.hashCode() == b.hashCode()
   ```

3. **Poor hash distribution**: All keys in one bucket = O(n)

4. **Forgetting load factor**: Too high = slow; too low = memory waste

## Further Reading

- **CLRS Chapter 11**: Hash Tables
- **Java HashMap source**: Reference implementation
- **Cuckoo Hashing**: Alternative collision resolution
- **Robin Hood Hashing**: Cache-friendly open addressing

---

*Part of the Omni-System Grimoire - Phase 2: Organization & Speed*
