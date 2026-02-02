# Producer-Consumer Pattern

> Coordinating work between threads that produce and consume data.

## The Problem

We have:
- **Producer threads** that generate work items
- **Consumer threads** that process work items
- A shared **queue** between them

Without proper coordination:
- Producers may overrun the queue (memory exhaustion)
- Consumers may poll frantically when empty (CPU waste)

---

## The Solution: BlockingQueue

A BlockingQueue provides:
- `put(item)` — blocks if queue is full
- `take()` — blocks if queue is empty

```java
BlockingQueue<Task> queue = new BlockingQueue<>(10);

// Producer
queue.put(new Task("work"));  // Blocks if queue has 10 items

// Consumer
Task task = queue.take();  // Blocks if queue is empty
task.execute();
```

---

## How It Works

### Put (Producer)

```java
public synchronized void put(T item) throws InterruptedException {
    while (queue.size() >= capacity) {
        wait();  // Wait for space
    }
    queue.addLast(item);
    notifyAll();  // Wake up consumers
}
```

### Take (Consumer)

```java
public synchronized T take() throws InterruptedException {
    while (queue.isEmpty()) {
        wait();  // Wait for items
    }
    T item = queue.removeFirst();
    notifyAll();  // Wake up producers
    return item;
}
```

---

## Sequence Diagram

```
Producer                Queue                 Consumer
   │                      │                      │
   │──put(item)──────────▶│                      │
   │   (blocks if full)   │                      │
   │                      │◀────take()───────────│
   │                      │   (blocks if empty)  │
   │◀──notifyAll()────────│                      │
   │                      │───────item──────────▶│
   │                      │                      │
```

---

## Thread Pool Pattern

A common use of producer-consumer is a thread pool:

```java
ThreadPool pool = new ThreadPool(4);  // 4 worker threads

// Main thread submits tasks (producer)
for (int i = 0; i < 100; i++) {
    pool.submit(() -> processTask(i));
}

// Worker threads consume from internal BlockingQueue
pool.shutdown();
```

### Benefits

| Benefit | Description |
|---------|-------------|
| **Thread Reuse** | Avoid thread creation overhead |
| **Bounded Queue** | Prevent memory exhaustion |
| **Backpressure** | Automatically slow producers |

---

## Non-Blocking Alternatives

For cases where blocking is undesirable:

```java
// Offer - doesn't block
boolean added = queue.offer(item);
if (!added) {
    // Queue full, handle gracefully
}

// Poll - doesn't block
T item = queue.poll();
if (item == null) {
    // Queue empty, try later
}
```

---

## Key Takeaways

1. **BlockingQueue decouples producers and consumers** — neither needs to know about the other
2. **Bounded capacity provides backpressure** — producers slow down when consumers can't keep up
3. **Always use while loops** around wait conditions
4. **Use notifyAll()** to avoid lost wakeups
5. **Thread pools** are a practical application of this pattern
