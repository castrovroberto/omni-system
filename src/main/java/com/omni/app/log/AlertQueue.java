package com.omni.app.log;

import com.omni.core.list.MyLinkedList;
import java.util.NoSuchElementException;

/**
 * High-priority alert queue using MyLinkedList for fast insertion.
 *
 * <p>Alerts are added to the beginning of the queue (addFirst) for O(1) insertion. This makes it
 * ideal for high-priority alerts that need immediate attention.
 */
public class AlertQueue {

  private final MyLinkedList<SystemEvent> alerts;

  /** Constructs an empty alert queue. */
  public AlertQueue() {
    this.alerts = new MyLinkedList<>();
  }

  /**
   * Adds an alert to the queue. Alerts are added to the front (highest priority) for immediate
   * processing.
   *
   * @param alert the system event alert to add
   * @throws NullPointerException if alert is null
   */
  public void addAlert(SystemEvent alert) {
    if (alert == null) {
      throw new NullPointerException("Alert cannot be null");
    }
    alerts.addFirst(alert); // O(1) insertion at the front
  }

  /**
   * Removes and returns the next alert from the queue. Returns alerts in FIFO order (first in,
   * first out).
   *
   * @return the next alert in the queue
   * @throws NoSuchElementException if the queue is empty
   */
  public SystemEvent getNextAlert() {
    if (alerts.isEmpty()) {
      throw new NoSuchElementException("Alert queue is empty");
    }
    return alerts.removeFirst(); // O(1) removal from the front
  }

  /**
   * Peeks at the next alert without removing it.
   *
   * @return the next alert in the queue
   * @throws NoSuchElementException if the queue is empty
   */
  public SystemEvent peekNextAlert() {
    if (alerts.isEmpty()) {
      throw new NoSuchElementException("Alert queue is empty");
    }
    return alerts.get(0);
  }

  /**
   * Returns the number of alerts in the queue.
   *
   * @return the alert count
   */
  public int size() {
    return alerts.size();
  }

  /**
   * Returns true if the queue is empty.
   *
   * @return true if no alerts are queued
   */
  public boolean isEmpty() {
    return alerts.isEmpty();
  }

  /** Removes all alerts from the queue. */
  public void clear() {
    alerts.clear();
  }

  /**
   * Returns an iterable view of all alerts. This allows foreach iteration: for (SystemEvent alert :
   * alertQueue.getAll()) { ... }
   *
   * @return an iterable over all alerts
   */
  public Iterable<SystemEvent> getAll() {
    return alerts;
  }
}
