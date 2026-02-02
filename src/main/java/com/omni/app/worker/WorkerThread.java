package com.omni.app.worker;

import com.omni.core.concurrent.BlockingQueue;

/**
 * A worker thread that pulls tasks from a {@link BlockingQueue} and executes them.
 *
 * <p>Workers run until interrupted or the pool is shut down.
 */
public class WorkerThread extends Thread {

  private final BlockingQueue<Runnable> taskQueue;
  private volatile boolean running = true;

  /**
   * Creates a worker thread.
   *
   * @param name the thread name
   * @param taskQueue the queue to pull tasks from
   */
  public WorkerThread(String name, BlockingQueue<Runnable> taskQueue) {
    super(name);
    this.taskQueue = taskQueue;
  }

  @Override
  public void run() {
    while (running) {
      try {
        Runnable task = taskQueue.take();
        task.run();
      } catch (InterruptedException e) {
        // Interrupted - check if still running
        currentThread().interrupt();
        break;
      } catch (Exception e) {
        // Log and continue - don't let one bad task kill the worker
        System.err.println("Task failed in " + getName() + ": " + e.getMessage());
      }
    }
  }

  /** Signals this worker to stop after completing the current task. */
  public void shutdown() {
    running = false;
    this.interrupt();
  }
}
