package com.omni.app.worker;

/**
 * A simple future representing the result of an asynchronous task.
 *
 * @param <T> the result type
 */
public class TaskFuture<T> {

  private T result;
  private Exception exception;
  private volatile boolean done = false;

  /**
   * Waits for the result and returns it.
   *
   * @return the result
   * @throws Exception if the task threw an exception
   * @throws InterruptedException if interrupted while waiting
   */
  public synchronized T get() throws Exception, InterruptedException {
    while (!done) {
      wait();
    }
    if (exception != null) {
      throw exception;
    }
    return result;
  }

  /**
   * Waits for the result with a timeout.
   *
   * @param timeoutMs the maximum time to wait in milliseconds
   * @return the result, or null if timed out
   * @throws Exception if the task threw an exception
   * @throws InterruptedException if interrupted while waiting
   */
  public synchronized T get(long timeoutMs) throws Exception, InterruptedException {
    if (!done) {
      wait(timeoutMs);
    }
    if (!done) {
      return null; // Timed out
    }
    if (exception != null) {
      throw exception;
    }
    return result;
  }

  /**
   * Returns true if the task has completed.
   *
   * @return true if done
   */
  public boolean isDone() {
    return done;
  }

  /**
   * Completes this future with a result.
   *
   * @param result the result
   */
  synchronized void complete(T result) {
    this.result = result;
    this.done = true;
    notifyAll();
  }

  /**
   * Completes this future exceptionally.
   *
   * @param exception the exception
   */
  synchronized void completeExceptionally(Exception exception) {
    this.exception = exception;
    this.done = true;
    notifyAll();
  }
}
