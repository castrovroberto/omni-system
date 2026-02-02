package com.omni.app.worker;

import com.omni.core.concurrent.BlockingQueue;
import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;

/**
 * A simple fixed-size thread pool using a {@link BlockingQueue}.
 *
 * <p>Tasks are submitted to a shared queue and executed by worker threads. This demonstrates the
 * producer-consumer pattern with our custom concurrent data structures.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * ThreadPool pool = new ThreadPool(4);
 *
 * for (int i = 0; i < 100; i++) {
 *     final int taskId = i;
 *     pool.submit(() -> {
 *         System.out.println("Task " + taskId + " on " + Thread.currentThread().getName());
 *     });
 * }
 *
 * pool.shutdown();
 * }</pre>
 */
public class ThreadPool {

  private final MyList<WorkerThread> workers;
  private final BlockingQueue<Runnable> taskQueue;
  private volatile boolean isShutdown = false;

  /**
   * Creates a thread pool with the specified number of worker threads.
   *
   * @param workerCount the number of workers
   * @throws IllegalArgumentException if workerCount is less than 1
   */
  public ThreadPool(int workerCount) {
    this(workerCount, 100);
  }

  /**
   * Creates a thread pool with the specified number of workers and queue capacity.
   *
   * @param workerCount the number of workers
   * @param queueCapacity the maximum number of pending tasks
   */
  public ThreadPool(int workerCount, int queueCapacity) {
    if (workerCount < 1) {
      throw new IllegalArgumentException("Worker count must be at least 1");
    }

    this.taskQueue = new BlockingQueue<>(queueCapacity);
    this.workers = new MyArrayList<>();

    for (int i = 0; i < workerCount; i++) {
      WorkerThread worker = new WorkerThread("Worker-" + i, taskQueue);
      workers.add(worker);
      worker.start();
    }
  }

  /**
   * Submits a task for execution.
   *
   * @param task the task to execute
   * @throws IllegalStateException if the pool has been shut down
   */
  public void submit(Runnable task) {
    if (isShutdown) {
      throw new IllegalStateException("ThreadPool has been shut down");
    }
    try {
      taskQueue.put(task);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Interrupted while submitting task", e);
    }
  }

  /**
   * Submits a task with a result.
   *
   * @param task the task
   * @param <T> the result type
   * @return a future representing the result
   */
  public <T> TaskFuture<T> submit(Task<T> task) {
    TaskFuture<T> future = new TaskFuture<>();
    submit(
        () -> {
          try {
            T result = task.execute();
            future.complete(result);
          } catch (Exception e) {
            future.completeExceptionally(e);
          }
        });
    return future;
  }

  /**
   * Shuts down the pool. Running tasks will complete, but no new tasks will be accepted.
   *
   * @throws InterruptedException if interrupted while waiting for workers to stop
   */
  public void shutdown() throws InterruptedException {
    isShutdown = true;

    // Shutdown all workers
    for (int i = 0; i < workers.size(); i++) {
      workers.get(i).shutdown();
    }

    // Wait for workers to finish
    for (int i = 0; i < workers.size(); i++) {
      workers.get(i).join(1000);
    }
  }

  /**
   * Returns the number of pending tasks in the queue.
   *
   * @return the pending task count
   */
  public int getPendingTaskCount() {
    return taskQueue.size();
  }

  /**
   * Returns the number of workers in this pool.
   *
   * @return the worker count
   */
  public int getWorkerCount() {
    return workers.size();
  }

  /**
   * Returns true if this pool has been shut down.
   *
   * @return true if shut down
   */
  public boolean isShutdown() {
    return isShutdown;
  }
}
