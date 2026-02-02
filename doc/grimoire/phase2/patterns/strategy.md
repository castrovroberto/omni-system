# Strategy Pattern

> Define a family of algorithms, encapsulate each one, and make them interchangeable.

## Problem
`MyHashMap` needs different hash functions for different key types. Hard-coding a single algorithm limits flexibility.

## Solution

```
<<interface>>
HashStrategy<K>
  + hash(key: K, capacity: int): int
        △
        │
   ┌────┴────────┐
   │             │
DefaultHash   DJB2Hash
(Object.hashCode)  (String-optimized)
```

## Implementation

```java
public interface HashStrategy<K> {
    int hash(K key, int capacity);
}

public class DefaultHashStrategy<K> implements HashStrategy<K> {
    @Override
    public int hash(K key, int capacity) {
        return Math.abs(key.hashCode()) % capacity;
    }
}

public class DJB2HashStrategy implements HashStrategy<String> {
    @Override
    public int hash(String key, int capacity) {
        long hash = 5381;
        for (char c : key.toCharArray()) {
            hash = ((hash << 5) + hash) + c;
        }
        return (int) (Math.abs(hash) % capacity);
    }
}
```

## Usage

```java
// Default strategy
MyHashMap<String, User> users = new MyHashMap<>();

// Swap strategy at runtime
users.setHashStrategy(new DJB2HashStrategy());

// Test with predictable strategy
users.setHashStrategy(key -> 0);  // All bucket 0 (worst case)
```

## Benefits
- **Interchangeable algorithms**: Swap at runtime
- **Testability**: Mock strategies for unit tests
- **Open for extension**: Add new strategies without changing HashMap

## Used In
- [HashStrategy](../../../../src/main/java/com/omni/core/map/hash/HashStrategy.java)
- [DefaultHashStrategy](../../../../src/main/java/com/omni/core/map/hash/DefaultHashStrategy.java)
- [DJB2HashStrategy](../../../../src/main/java/com/omni/core/map/hash/DJB2HashStrategy.java)
