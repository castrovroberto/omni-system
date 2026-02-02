package com.omni.core.concurrent;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class BlockingQueueTest {

  @Test
  void putAndTake_singleThread_works() throws InterruptedException {
    BlockingQueue<String> queue = new BlockingQueue<>(10);

    queue.put("a");
    queue.put("b");
    queue.put("c");

    assertEquals("a", queue.take());
    assertEquals("b", queue.take());
    assertEquals("c", queue.take());
  }

  @Test
  void take_blocksWhenEmpty() throws InterruptedException {
    BlockingQueue<String> queue = new BlockingQueue<>(10);
    AtomicInteger taken = new AtomicInteger(0);

    Thread consumer =
        new Thread(
            () -> {
              try {
                queue.take();
                taken.incrementAndGet();
              } catch (InterruptedException e) {
                // Expected
              }
            });

    consumer.start();
    Thread.sleep(50); // Let consumer block

    assertEquals(0, taken.get()); // Should still be waiting

    queue.put("unblock");
    consumer.join(100);

    assertEquals(1, taken.get()); // Now it got the item
  }

  @Test
  void put_blocksWhenAtCapacity() throws InterruptedException {
    BlockingQueue<String> queue = new BlockingQueue<>(2);
    AtomicInteger puts = new AtomicInteger(0);

    Thread producer =
        new Thread(
            () -> {
              try {
                queue.put("1");
                puts.incrementAndGet();
                queue.put("2");
                puts.incrementAndGet();
                queue.put("3"); // Should block
                puts.incrementAndGet();
              } catch (InterruptedException e) {
                // Expected
              }
            });

    producer.start();
    Thread.sleep(50);

    assertEquals(2, puts.get()); // Third put should be blocking

    queue.take(); // Free up space
    producer.join(100);

    assertEquals(3, puts.get()); // Now all puts completed
  }

  @Test
  void producerConsumer_allItemsTransferred() throws InterruptedException {
    BlockingQueue<Integer> queue = new BlockingQueue<>(5);
    int itemCount = 100;
    AtomicInteger produced = new AtomicInteger(0);
    AtomicInteger consumed = new AtomicInteger(0);
    CountDownLatch done = new CountDownLatch(2);

    Thread producer =
        new Thread(
            () -> {
              try {
                for (int i = 0; i < itemCount; i++) {
                  queue.put(i);
                  produced.incrementAndGet();
                }
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              }
              done.countDown();
            });

    Thread consumer =
        new Thread(
            () -> {
              try {
                for (int i = 0; i < itemCount; i++) {
                  queue.take();
                  consumed.incrementAndGet();
                }
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              }
              done.countDown();
            });

    producer.start();
    consumer.start();
    done.await();

    assertEquals(itemCount, produced.get());
    assertEquals(itemCount, consumed.get());
  }

  @Test
  void offer_returnsFalseWhenFull() {
    BlockingQueue<String> queue = new BlockingQueue<>(2);

    assertTrue(queue.offer("a"));
    assertTrue(queue.offer("b"));
    assertFalse(queue.offer("c")); // At capacity
  }

  @Test
  void poll_returnsNullWhenEmpty() {
    BlockingQueue<String> queue = new BlockingQueue<>(10);

    assertNull(queue.poll());
  }

  @Test
  void constructor_rejectsZeroCapacity() {
    assertThrows(IllegalArgumentException.class, () -> new BlockingQueue<>(0));
  }

  @Test
  void constructor_rejectsNegativeCapacity() {
    assertThrows(IllegalArgumentException.class, () -> new BlockingQueue<>(-1));
  }

  @Test
  void size_returnsCorrectCount() {
    BlockingQueue<String> queue = new BlockingQueue<>(10);

    assertEquals(0, queue.size());
    assertTrue(queue.isEmpty());

    queue.offer("a");
    queue.offer("b");

    assertEquals(2, queue.size());
    assertFalse(queue.isEmpty());
  }

  @Test
  void getCapacity_returnsConfiguredCapacity() {
    BlockingQueue<String> queue = new BlockingQueue<>(42);
    assertEquals(42, queue.getCapacity());
  }

  @Test
  void poll_returnsItemWhenAvailable() {
    BlockingQueue<String> queue = new BlockingQueue<>(10);
    queue.offer("item");

    assertEquals("item", queue.poll());
    assertTrue(queue.isEmpty());
  }

  @Test
  void offer_afterPoll_hasSpace() {
    BlockingQueue<String> queue = new BlockingQueue<>(1);
    assertTrue(queue.offer("first"));
    assertFalse(queue.offer("second"));

    queue.poll();

    assertTrue(queue.offer("third"));
  }
}
