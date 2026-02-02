package com.omni.app.worker;

/**
 * A runnable task that can be submitted to a {@link ThreadPool}.
 *
 * @param <T> the result type
 */
@FunctionalInterface
public interface Task<T> {

  /**
   * Executes the task and returns a result.
   *
   * @return the result
   * @throws Exception if the task fails
   */
  T execute() throws Exception;
}
