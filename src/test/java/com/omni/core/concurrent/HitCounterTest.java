package com.omni.core.concurrent;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class HitCounterTest {

  @Test
  void hit_singleHit_countsCorrectly() {
    HitCounter counter = new HitCounter(300);

    counter.hit(100);

    assertEquals(1, counter.getHits(100));
  }

  @Test
  void hit_multipleHitsSameSecond_accumulates() {
    HitCounter counter = new HitCounter(300);

    counter.hit(100);
    counter.hit(100);
    counter.hit(100);

    assertEquals(3, counter.getHits(100));
  }

  @Test
  void getHits_outsideWindow_excluded() {
    HitCounter counter = new HitCounter(60); // 1 minute window

    counter.hit(10);
    counter.hit(50);

    // At timestamp 75, hit at 10 is outside the 60-second window (75-60=15 > 10)
    // but hit at 50 is inside (75-60=15 < 50)
    assertEquals(1, counter.getHits(75));
  }

  @Test
  void getHits_allInsideWindow_allCounted() {
    HitCounter counter = new HitCounter(60);

    counter.hit(10);
    counter.hit(20);
    counter.hit(30);

    assertEquals(3, counter.getHits(50));
  }

  @Test
  void hit_sameBucketDifferentTime_resets() {
    HitCounter counter = new HitCounter(60);

    counter.hit(10);
    // 70 will hash to same bucket as 10, but is a different timestamp
    counter.hit(70);

    assertEquals(1, counter.getHits(70));
  }

  @Test
  void concurrentHits_allCounted() throws InterruptedException {
    HitCounter counter = new HitCounter(300);
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
  void concurrentReads_noExceptions() throws InterruptedException {
    HitCounter counter = new HitCounter(300);
    counter.hit(100);

    int threadCount = 20;
    CountDownLatch latch = new CountDownLatch(threadCount);
    AtomicInteger readSum = new AtomicInteger(0);

    for (int t = 0; t < threadCount; t++) {
      new Thread(
              () -> {
                for (int i = 0; i < 100; i++) {
                  readSum.addAndGet(counter.getHits(100));
                }
                latch.countDown();
              })
          .start();
    }

    latch.await();

    // All reads should return 1, so sum should be 20 * 100 = 2000
    assertEquals(2000, readSum.get());
  }

  @Test
  void getWindowSeconds_returnsConfiguredValue() {
    HitCounter counter = new HitCounter(120);
    assertEquals(120, counter.getWindowSeconds());
  }

  @Test
  void constructor_negativeWindow_throws() {
    assertThrows(IllegalArgumentException.class, () -> new HitCounter(-1));
  }

  @Test
  void constructor_zeroWindow_throws() {
    assertThrows(IllegalArgumentException.class, () -> new HitCounter(0));
  }
}
