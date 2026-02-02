# Decorator Pattern

> Attach additional responsibilities to an object dynamically.

## Problem
`SessionStore` needs caching, but we don't want to modify the core `MyHashMap`. Future needs might include logging, metrics, etc.

## Solution

```
<<interface>>
MyMap<K,V>
    △
    │
┌───┴───────────┐
│               │
MyHashMap    CachingHashMap
(concrete)   (decorator)
                 │
                 └─ wraps MyMap<K,V>
```

## Implementation

```java
public class CachingHashMap<K, V> implements MyMap<K, V> {
    private final MyMap<K, V> delegate;  // Wrapped map
    private final MyMap<K, V> cache;     // LRU cache
    private final int maxCacheSize;
    
    public CachingHashMap(MyMap<K, V> delegate, int cacheSize) {
        this.delegate = delegate;
        this.cache = new MyHashMap<>();
        this.maxCacheSize = cacheSize;
    }
    
    @Override
    public Optional<V> get(K key) {
        // Check cache first
        Optional<V> cached = cache.get(key);
        if (cached.isPresent()) {
            cacheHits++;
            return cached;
        }
        
        // Cache miss - fetch from delegate
        cacheMisses++;
        Optional<V> value = delegate.get(key);
        value.ifPresent(v -> addToCache(key, v));
        return value;
    }
    
    @Override
    public void put(K key, V value) {
        delegate.put(key, value);
        addToCache(key, value);
    }
    
    private void addToCache(K key, V value) {
        if (cache.size() >= maxCacheSize) {
            evictLRU();
        }
        cache.put(key, value);
    }
}
```

## Usage

```java
// Base implementation
MyMap<String, Session> store = new MyHashMap<>();

// Wrap with caching (transparent to clients)
MyMap<String, Session> cached = new CachingHashMap<>(store, 100);

// Same interface, automatic caching
cached.put("token123", session);
cached.get("token123");  // Cache hit!

// Stack decorators
MyMap<String, Session> logged = new LoggingHashMap<>(cached);
```

## Benefits
- **Composition over inheritance**: Add behavior without subclassing
- **Single Responsibility**: Each decorator does one thing
- **Stackable**: Combine multiple decorators
- **Transparent**: Same interface as wrapped object

## Used In
- [CachingHashMap](../../../../src/main/java/com/omni/core/map/CachingHashMap.java)
