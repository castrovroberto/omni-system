# ConcurrentHashMap Implementation Notes

> Thread-safe hash map using full synchronization.

## Design

`ConcurrentHashMap<K,V>` wraps `MyHashMap<K,V>` with synchronized access to all operations.

```java
public class ConcurrentHashMap<K, V> implements MyMap<K, V> {
    private final MyMap<K, V> delegate;
    private final Object lock = new Object();
    
    @Override
    public Optional<V> put(K key, V value) {
        synchronized (lock) {
            return delegate.put(key, value);
        }
    }
}
```

---

## Why Full Synchronization?

### Striped Locking Considered

Initially we explored striped locking (multiple locks for different hash ranges):

```java
// Striped approach (NOT USED)
private final Object[] locks = new Object[16];

private Object getLockFor(K key) {
    return locks[key.hashCode() % 16];
}
```

**Problem:** The underlying `MyHashMap` isn't thread-safe internally. When `put()` triggers a resize, all buckets are accessed — creating race conditions across stripes.

### Full Synchronization Chosen

For an educational project, correctness trumps performance:

```java
synchronized (lock) {
    return delegate.put(key, value);
}
```

---

## Atomic Compound Operations

Added atomic methods that can't be safely composed from basic operations:

### putIfAbsent

```java
public Optional<V> putIfAbsent(K key, V value) {
    synchronized (lock) {
        Optional<V> existing = delegate.get(key);
        if (existing.isEmpty()) {
            delegate.put(key, value);
        }
        return existing;
    }
}
```

### remove(key, expectedValue)

```java
public boolean remove(K key, V value) {
    synchronized (lock) {
        Optional<V> existing = delegate.get(key);
        if (existing.isPresent() && existing.get().equals(value)) {
            delegate.remove(key);
            return true;
        }
        return false;
    }
}
```

---

## Snapshot Iterators

The `keys()`, `values()`, and `entries()` methods return **snapshot copies**:

```java
@Override
public Iterable<K> keys() {
    synchronized (lock) {
        MyList<K> result = new MyArrayList<>();
        for (K key : delegate.keys()) {
            result.add(key);
        }
        return result;
    }
}
```

**Trade-off:**
- ✅ Safe to iterate without holding lock
- ❌ Memory overhead for copy
- ❌ May not reflect concurrent updates

---

## Trade-offs Summary

| Aspect | Our Choice | Alternative |
|--------|------------|-------------|
| Locking | Single lock | Striped locking |
| Iteration | Snapshot copy | Fail-fast iterator |
| Complexity | Simple | Higher concurrency |

For production use, consider `java.util.concurrent.ConcurrentHashMap` which uses sophisticated segment-based locking.
