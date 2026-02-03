package com.omni.core.concurrent;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Test;

class AtomicHitCounterTest {

  @Test
  void hit_singleHit_countsCorrectly() {
    AtomicHitCounter counter = new AtomicHitCounter(300);

    counter.hit(100);

    assertEquals(1, counter.getHits(100));
  }

  @Test
  void hit_multipleHitsSameSecond_accumulates() {
    AtomicHitCounter counter = new AtomicHitCounter(300);

    counter.hit(100);
    counter.hit(100);
    counter.hit(100);

    assertEquals(3, counter.getHits(100));
  }

  @Test
  void getHits_outsideWindow_excluded() {
    AtomicHitCounter counter = new AtomicHitCounter(60);

    counter.hit(10);
    counter.hit(50);

    // At timestamp 75, hit at 10 is outside the 60-second window
    assertEquals(1, counter.getHits(75));
  }

  @Test
  void getHits_allInsideWindow_allCounted() {
    AtomicHitCounter counter = new AtomicHitCounter(60);

    counter.hit(10);
    counter.hit(20);
    counter.hit(30);

    assertEquals(3, counter.getHits(50));
  }

  @Test
  void hit_sameBucketDifferentTime_resets() {
    AtomicHitCounter counter = new AtomicHitCounter(60);

    counter.hit(10);
    counter.hit(70); // Same bucket as 10, different timestamp

    assertEquals(1, counter.getHits(70));
  }

  @Test
  void concurrentHits_allCounted() throws InterruptedException {
    AtomicHitCounter counter = new AtomicHitCounter(300);
    int threadCount = 10;
    int hitsPerThread = 100;
    CountDownLatch latch = new CountDownLatch(threadCount);
    long timestamp = 100;

    for (int t = 0; t < threadCount; t++) {
      new Thread(
              () -> {
                for (int i = 0; i < hitsPerThread; i++) {
                  counter.hit(timestamp);
                }
                latch.countDown();
              })
          .start();
    }

    latch.await();

    assertEquals(threadCount * hitsPerThread, counter.getHits(timestamp));
  }

  @Test
  void concurrentHits_differentTimestamps_allCounted() throws InterruptedException {
    AtomicHitCounter counter = new AtomicHitCounter(300);
    int threadCount = 10;
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int t = 0; t < threadCount; t++) {
      final int threadId = t;
      new Thread(
              () -> {
                counter.hit(100 + threadId); // Different timestamps
                latch.countDown();
              })
          .start();
    }

    latch.await();

    assertEquals(threadCount, counter.getHits(200));
  }

  @Test
  void getWindowSeconds_returnsConfiguredValue() {
    AtomicHitCounter counter = new AtomicHitCounter(120);
    assertEquals(120, counter.getWindowSeconds());
  }

  @Test
  void constructor_negativeWindow_throws() {
    assertThrows(IllegalArgumentException.class, () -> new AtomicHitCounter(-1));
  }

  @Test
  void constructor_zeroWindow_throws() {
    assertThrows(IllegalArgumentException.class, () -> new AtomicHitCounter(0));
  }
}
