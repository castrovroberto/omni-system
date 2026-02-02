package com.omni.app.worker;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class ThreadPoolTest {

  @Test
  void submit_executesTask() throws InterruptedException {
    ThreadPool pool = new ThreadPool(2);
    AtomicInteger counter = new AtomicInteger(0);
    CountDownLatch latch = new CountDownLatch(1);

    pool.submit(
        () -> {
          counter.incrementAndGet();
          latch.countDown();
        });

    latch.await();
    assertEquals(1, counter.get());
    pool.shutdown();
  }

  @Test
  void submit_multipleTasks_allExecuted() throws InterruptedException {
    ThreadPool pool = new ThreadPool(4);
    int taskCount = 100;
    AtomicInteger counter = new AtomicInteger(0);
    CountDownLatch latch = new CountDownLatch(taskCount);

    for (int i = 0; i < taskCount; i++) {
      pool.submit(
          () -> {
            counter.incrementAndGet();
            latch.countDown();
          });
    }

    latch.await();
    assertEquals(taskCount, counter.get());
    pool.shutdown();
  }

  @Test
  void submitTask_returnsResult() throws Exception {
    ThreadPool pool = new ThreadPool(2);

    TaskFuture<Integer> future = pool.submit(() -> 42);
    Integer result = future.get(1000);

    assertEquals(42, result);
    pool.shutdown();
  }

  @Test
  void submitTask_propagatesException() throws InterruptedException {
    ThreadPool pool = new ThreadPool(2);

    TaskFuture<Integer> future =
        pool.submit(
            () -> {
              throw new RuntimeException("Test exception");
            });

    assertThrows(RuntimeException.class, () -> future.get(1000));
    pool.shutdown();
  }

  @Test
  void shutdown_preventsNewTasks() throws InterruptedException {
    ThreadPool pool = new ThreadPool(2);
    pool.shutdown();

    assertThrows(IllegalStateException.class, () -> pool.submit(() -> {}));
  }

  @Test
  void getWorkerCount_returnsCorrectValue() throws InterruptedException {
    ThreadPool pool = new ThreadPool(4);

    assertEquals(4, pool.getWorkerCount());
    pool.shutdown();
  }

  @Test
  void isShutdown_reflectsState() throws InterruptedException {
    ThreadPool pool = new ThreadPool(2);

    assertFalse(pool.isShutdown());

    pool.shutdown();

    assertTrue(pool.isShutdown());
  }
}
