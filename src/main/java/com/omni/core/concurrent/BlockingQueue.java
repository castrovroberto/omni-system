package com.omni.core.concurrent;

import com.omni.core.list.MyLinkedList;

/**
 * A thread-safe blocking queue for producer-consumer patterns.
 *
 * <p>This queue blocks on {@link #put(Object)} when at capacity and blocks on {@link #take()} when
 * empty. Uses {@code wait()} and {@code notifyAll()} for signaling between threads.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * BlockingQueue<Task> queue = new BlockingQueue<>(10);
 *
 * // Producer thread
 * queue.put(new Task("work"));
 *
 * // Consumer thread
 * Task task = queue.take();
 * task.execute();
 * }</pre>
 *
 * @param <T> the element type
 */
public class BlockingQueue<T> {

  private final MyLinkedList<T> queue;
  private final int capacity;

  /**
   * Creates a blocking queue with the specified capacity.
   *
   * @param capacity the maximum number of elements
   * @throws IllegalArgumentException if capacity is less than 1
   */
  public BlockingQueue(int capacity) {
    if (capacity < 1) {
      throw new IllegalArgumentException("Capacity must be at least 1");
    }
    this.queue = new MyLinkedList<>();
    this.capacity = capacity;
  }

  /**
   * Adds an item to the queue, blocking if at capacity.
   *
   * @param item the item to add
   * @throws InterruptedException if the thread is interrupted while waiting
   */
  public synchronized void put(T item) throws InterruptedException {
    while (queue.size() >= capacity) {
      wait();
    }
    queue.addLast(item);
    notifyAll();
  }

  /**
   * Removes and returns an item from the queue, blocking if empty.
   *
   * @return the item
   * @throws InterruptedException if the thread is interrupted while waiting
   */
  public synchronized T take() throws InterruptedException {
    while (queue.isEmpty()) {
      wait();
    }
    T item = queue.removeFirst();
    notifyAll();
    return item;
  }

  /**
   * Attempts to add an item without blocking.
   *
   * @param item the item to add
   * @return true if added, false if at capacity
   */
  public synchronized boolean offer(T item) {
    if (queue.size() >= capacity) {
      return false;
    }
    queue.addLast(item);
    notifyAll();
    return true;
  }

  /**
   * Attempts to remove an item without blocking.
   *
   * @return the item, or null if empty
   */
  public synchronized T poll() {
    if (queue.isEmpty()) {
      return null;
    }
    T item = queue.removeFirst();
    notifyAll();
    return item;
  }

  /**
   * Returns the current number of items in the queue.
   *
   * @return the size
   */
  public synchronized int size() {
    return queue.size();
  }

  /**
   * Returns true if the queue is empty.
   *
   * @return true if empty
   */
  public synchronized boolean isEmpty() {
    return queue.isEmpty();
  }

  /**
   * Returns the capacity of this queue.
   *
   * @return the capacity
   */
  public int getCapacity() {
    return capacity;
  }
}
