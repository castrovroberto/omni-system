package com.omni.app.log;

/**
 * Central logging facility for the Cloud Orchestrator.
 *
 * <p>Uses the Singleton pattern to ensure exactly one log sink exists in the system. This is
 * thread-safe using double-checked locking (preparing for Phase 5).
 */
public class LogManager {

  private static volatile LogManager instance;
  private final EventLog eventLog;
  private final AlertQueue alertQueue;

  /**
   * Private constructor to prevent direct instantiation. Use getInstance() to obtain the singleton
   * instance.
   */
  private LogManager() {
    this.eventLog = new EventLog();
    this.alertQueue = new AlertQueue();
  }

  /**
   * Returns the singleton instance of LogManager.
   *
   * <p>Uses double-checked locking pattern for thread safety.
   *
   * @return the singleton LogManager instance
   */
  public static LogManager getInstance() {
    if (instance == null) {
      synchronized (LogManager.class) {
        if (instance == null) {
          instance = new LogManager();
        }
      }
    }
    return instance;
  }

  /**
   * Logs an event to the event log. High-priority events (ERROR, CRITICAL) are also added to the
   * alert queue.
   *
   * @param event the system event to log
   * @throws NullPointerException if event is null
   */
  public void log(SystemEvent event) {
    if (event == null) {
      throw new NullPointerException("Event cannot be null");
    }

    // Always add to event log
    eventLog.add(event);

    // Add high-priority events to alert queue
    if (event.getSeverity() == SystemEvent.Severity.ERROR
        || event.getSeverity() == SystemEvent.Severity.CRITICAL) {
      alertQueue.addAlert(event);
    }
  }

  /**
   * Returns the event log containing all logged events.
   *
   * @return the event log (read-only view)
   */
  public EventLog getEventLog() {
    return eventLog;
  }

  /**
   * Returns the alert queue containing high-priority alerts.
   *
   * @return the alert queue (read-only view)
   */
  public AlertQueue getAlertQueue() {
    return alertQueue;
  }

  /**
   * Clears all logs and alerts.
   *
   * <p>Useful for testing or resetting the system state.
   */
  public void clear() {
    eventLog.clear();
    alertQueue.clear();
  }

  /**
   * Returns the total number of events in the log.
   *
   * @return the event count
   */
  public int getEventCount() {
    return eventLog.size();
  }

  /**
   * Returns the number of alerts in the queue.
   *
   * @return the alert count
   */
  public int getAlertCount() {
    return alertQueue.size();
  }
}
