package com.omni.app.fs;

import com.omni.core.tree.MyHeap;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A priority-based job scheduler backed by a {@link MyHeap}.
 *
 * <p>Jobs are executed in priority order (lower priority value = executed first).
 */
public class JobScheduler {

  private final MyHeap<Job> jobQueue;

  /** Constructs a new job scheduler. */
  public JobScheduler() {
    this.jobQueue = new MyHeap<>(Comparator.comparingInt(Job::getPriority));
  }

  /**
   * Schedules a job for execution.
   *
   * @param job the job to schedule
   */
  public void schedule(Job job) {
    jobQueue.insert(job);
  }

  /**
   * Executes the highest-priority job.
   *
   * @return the executed job
   * @throws NoSuchElementException if no jobs are scheduled
   */
  public Job executeNext() {
    Job job = jobQueue.extractRoot();
    job.execute();
    return job;
  }

  /**
   * Executes all scheduled jobs in priority order.
   *
   * @return the number of jobs executed
   */
  public int executeAll() {
    int count = 0;
    while (!jobQueue.isEmpty()) {
      executeNext();
      count++;
    }
    return count;
  }

  /**
   * Returns the number of pending jobs.
   *
   * @return the number of pending jobs
   */
  public int pendingJobs() {
    return jobQueue.size();
  }
}
