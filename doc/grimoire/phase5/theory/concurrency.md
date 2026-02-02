# Concurrency Basics

> Understanding thread safety and synchronization fundamentals.

## What is Concurrency?

Concurrency means multiple threads executing code that accesses shared resources. Without proper coordination, this leads to **race conditions** — bugs that depend on unpredictable thread timing.

---

## Race Conditions

A race condition occurs when:
1. Two or more threads access shared data
2. At least one thread modifies the data
3. No synchronization controls access order

### Example: Counter Without Synchronization

```java
public class UnsafeCounter {
    private int count = 0;
    
    public void increment() {
        count++;  // NOT atomic: read → modify → write
    }
}
```

With 10 threads each calling `increment()` 1000 times, the final count is often less than 10,000 because increments get lost.

**Timeline showing lost update:**
```
Thread A: reads count (0)
Thread B: reads count (0)
Thread A: writes count (1)
Thread B: writes count (1)  ← Thread A's increment is lost!
```

---

## Synchronization with `synchronized`

The `synchronized` keyword ensures only one thread can execute a critical section at a time.

### Synchronized Method

```java
public class SafeCounter {
    private int count = 0;
    
    public synchronized void increment() {
        count++;  // Only one thread at a time
    }
}
```

### Synchronized Block

```java
public class SafeCounter {
    private int count = 0;
    private final Object lock = new Object();
    
    public void increment() {
        synchronized (lock) {
            count++;
        }
    }
}
```

---

## Lock Object Considerations

| Approach | Pros | Cons |
|----------|------|------|
| `synchronized(this)` | Simple | External code can deadlock you |
| Private lock object | Full control | More boilerplate |
| `synchronized` method | Clean syntax | Same as `synchronized(this)` |

**Best Practice:** Use a private final lock object for library code.

---

## Wait/Notify

When a thread needs to wait for a condition:

```java
synchronized (lock) {
    while (!condition) {
        lock.wait();  // Releases lock, waits for notify
    }
    // Condition is now true
}
```

To signal waiting threads:

```java
synchronized (lock) {
    condition = true;
    lock.notifyAll();  // Wake up all waiting threads
}
```

### Why `while` Instead of `if`?

**Spurious wakeups** — threads can wake without `notify()` being called. Always re-check the condition in a loop.

---

## Memory Visibility

Without synchronization, changes made by one thread may not be visible to others due to CPU caches and compiler optimizations.

```java
// Thread 1
running = false;  // Might not be visible to Thread 2

// Thread 2
while (running) {
    // May loop forever!
}
```

**Solutions:**
- `synchronized` blocks (force memory flush)
- `volatile` keyword (for simple flags)

---

## Key Takeaways

1. **Always protect shared mutable state** with synchronization
2. **Use private lock objects** to prevent external interference
3. **Check conditions in loops** after `wait()` returns
4. **Prefer `notifyAll()`** over `notify()` to avoid missed signals
5. **Document thread-safety** in your Javadoc
