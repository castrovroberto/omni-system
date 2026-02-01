package com.omni.app.fs;

/**
 * Command pattern interface for file system operations.
 *
 * <p>Jobs can be executed and undone, and have a priority for scheduling.
 */
public interface Job {

  /** Executes this job. */
  void execute();

  /** Undoes this job, reverting its effects. */
  void undo();

  /**
   * Returns the priority of this job. Lower values indicate higher priority.
   *
   * @return the priority
   */
  int getPriority();
}
