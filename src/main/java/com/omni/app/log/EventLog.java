package com.omni.app.log;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;

/**
 * Stores all system events chronologically using MyArrayList.
 *
 * <p>Provides fast O(1) appending and O(1) index-based access.
 */
public class EventLog {

  private final MyList<SystemEvent> events;

  /** Constructs an empty event log. */
  public EventLog() {
    this.events = new MyArrayList<>();
  }

  /**
   * Adds an event to the log. Events are stored in chronological order (order added).
   *
   * @param event the system event to add
   * @throws NullPointerException if event is null
   */
  public void add(SystemEvent event) {
    if (event == null) {
      throw new NullPointerException("Event cannot be null");
    }
    events.add(event);
  }

  /**
   * Gets an event by index.
   *
   * @param index the index of the event (0-based)
   * @return the event at that index
   * @throws IndexOutOfBoundsException if index is invalid
   */
  public SystemEvent get(int index) {
    return events.get(index);
  }

  /**
   * Returns the number of events in the log.
   *
   * @return the event count
   */
  public int size() {
    return events.size();
  }

  /**
   * Returns true if the log is empty.
   *
   * @return true if no events are stored
   */
  public boolean isEmpty() {
    return events.isEmpty();
  }

  /** Removes all events from the log. */
  public void clear() {
    events.clear();
  }

  /**
   * Returns an iterable view of all events. This allows foreach iteration: for (SystemEvent event :
   * eventLog.getAll()) { ... }
   *
   * @return an iterable over all events
   */
  public Iterable<SystemEvent> getAll() {
    return events;
  }
}
