# SynchronizedList Implementation Notes

> Thread-safe wrapper using synchronized delegation.

## Design

`SynchronizedList<T>` wraps any `MyList<T>` and synchronizes all method calls.

```java
public class SynchronizedList<T> implements MyList<T> {
    private final MyList<T> delegate;
    private final Object lock;
    
    @Override
    public void add(T element) {
        synchronized (lock) {
            delegate.add(element);
        }
    }
}
```

---

## Key Decisions

### 1. Private Lock Object

Using `private final Object lock` instead of `synchronized(this)`:
- Prevents external code from interfering with our synchronization
- Follows defensive programming principles

### 2. Exposing the Lock

The `getLock()` method allows compound operations:

```java
synchronized (list.getLock()) {
    if (!list.isEmpty()) {
        return list.get(0);  // Safe compound operation
    }
}
```

Without this, check-then-act would be unsafe:

```java
if (!list.isEmpty()) {
    // Another thread could remove the element here!
    return list.get(0);  // May throw IndexOutOfBoundsException
}
```

### 3. Non-Thread-Safe Iterator

The iterator delegates directly to the underlying list:

```java
@Override
public Iterator<T> iterator() {
    return delegate.iterator();  // NOT thread-safe!
}
```

**Why?** Creating a snapshot copy on every iteration would be expensive. Instead, we document that iteration requires external synchronization:

```java
synchronized (list.getLock()) {
    for (T item : list) {
        // Safe iteration
    }
}
```

---

## Trade-offs

| Aspect | Choice | Rationale |
|--------|--------|-----------|
| Granularity | Method-level | Simple, correct |
| Lock visibility | Exposed via getter | Enable compound operations |
| Iterator safety | Caller responsibility | Avoid snapshot overhead |

---

## Alternatives Considered

| Alternative | Why Not Used |
|-------------|--------------|
| Copy-on-write | High memory overhead |
| Read-write lock | Overkill for educational purposes |
| Lock-free algorithms | Very complex, subtle bugs |
