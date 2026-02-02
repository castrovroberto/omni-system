# BlockingQueue Implementation Notes

> Producer-consumer queue using wait/notify.

## Design

`BlockingQueue<T>` provides blocking `put()` and `take()` operations backed by a `MyLinkedList<T>`.

```java
public class BlockingQueue<T> {
    private final MyLinkedList<T> queue;
    private final int capacity;
    
    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() >= capacity) {
            wait();
        }
        queue.addLast(item);
        notifyAll();
    }
}
```

---

## Key Decisions

### 1. LinkedList Backing

Using `MyLinkedList` for O(1) head removal:

```java
queue.addLast(item);   // O(1) enqueue
queue.removeFirst();   // O(1) dequeue
```

ArrayList would require O(n) shifts on remove.

### 2. While Loop for Condition

**Wrong:**
```java
if (queue.isEmpty()) {
    wait();
}
// Might fail - spurious wakeup or race
```

**Correct:**
```java
while (queue.isEmpty()) {
    wait();
}
// Guaranteed: queue has at least one item
```

### 3. notifyAll() vs notify()

Using `notifyAll()` because:
- Multiple producers may be waiting for space
- Multiple consumers may be waiting for items
- `notify()` could miss relevant waiters

### 4. Non-Blocking Alternatives

Added `offer()` and `poll()` for situations where blocking is undesirable:

```java
public synchronized boolean offer(T item) {
    if (queue.size() >= capacity) {
        return false;  // Don't block
    }
    queue.addLast(item);
    notifyAll();
    return true;
}
```

---

## Thread Safety Analysis

| Method | Behavior | Thread Safety |
|--------|----------|---------------|
| `put()` | Blocks until space | ✅ Synchronized |
| `take()` | Blocks until item | ✅ Synchronized |
| `offer()` | Returns immediately | ✅ Synchronized |
| `poll()` | Returns immediately | ✅ Synchronized |
| `size()` | Snapshot | ✅ Synchronized |

---

## Potential Improvements

| Improvement | Complexity | Benefit |
|-------------|------------|---------|
| Fairness (FIFO wake order) | High | Prevent starvation |
| Timeout support | Medium | Avoid indefinite blocking |
| Separate locks for put/take | High | Better concurrency |
