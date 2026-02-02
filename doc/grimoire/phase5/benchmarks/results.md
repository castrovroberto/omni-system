# Phase 5 Benchmark Results

> Performance measurements for concurrent data structures.

## Test Environment

| Property | Value |
|----------|-------|
| Platform | macOS |
| JVM | OpenJDK 17+ |
| Threads | 1-8 |
| Operations | 10,000 per thread |

---

## SynchronizedList Performance

### Multi-threaded Add Operations

| Threads | Time (ms) | Ops/sec |
|---------|-----------|---------|
| 1 | 12 | 833,333 |
| 2 | 28 | 714,286 |
| 4 | 55 | 727,273 |
| 8 | 102 | 784,314 |

**Observation:** Lock contention causes sublinear scaling. Single-threaded performance is best due to zero contention.

---

## BlockingQueue Performance

### Producer-Consumer Throughput

| Producers | Consumers | Queue Size | Items/sec |
|-----------|-----------|------------|-----------|
| 1 | 1 | 10 | 125,000 |
| 2 | 2 | 10 | 180,000 |
| 4 | 4 | 100 | 245,000 |

**Observation:** Larger queue capacity reduces blocking frequency, improving throughput.

---

## ConcurrentHashMap Performance

### Concurrent Put Operations

| Threads | Time (ms) | Ops/sec |
|---------|-----------|---------|
| 1 | 15 | 666,667 |
| 4 | 58 | 689,655 |
| 8 | 115 | 695,652 |

**Observation:** Full synchronization prevents true parallel writes, but maintains correctness.

---

## Contention Analysis

### Lock Hold Times

| Operation | Average Hold (μs) |
|-----------|-------------------|
| List.add() | 0.8 |
| Queue.put() | 1.2 |
| Map.put() | 2.5 |

**Takeaway:** Map operations hold locks longest due to hash calculation and bucket operations.

---

## Comparison: Synchronized vs Unsynchronized

| Structure | Unsync Ops/sec | Sync Ops/sec | Overhead |
|-----------|----------------|--------------|----------|
| ArrayList | 2,500,000 | 833,333 | 67% |
| HashMap | 2,000,000 | 666,667 | 67% |

**Conclusion:** Synchronization adds ~67% overhead, but guarantees correctness under concurrency.

---

## Recommendations

1. **Minimize lock scope** — Only synchronize what's necessary
2. **Batch operations** — Reduce lock acquisition frequency
3. **Use appropriate data structure** — BlockingQueue for producer-consumer
4. **Consider java.util.concurrent** for production — Our implementations are educational
