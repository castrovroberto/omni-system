package com.omni.app.log;

import com.omni.core.algorithm.search.SearchAlgorithms;
import com.omni.core.list.MyList;
import java.time.Instant;
import java.util.Comparator;

/**
 * Utility class for searching through event logs using various search algorithms.
 *
 * <p>Demonstrates the use of Linear Search and Binary Search algorithms on MyList implementations.
 */
public final class LogSearcher {

  private LogSearcher() {
    // Utility class - prevent instantiation
  }

  /**
   * Performs linear search to find an event by timestamp.
   *
   * <p>Time complexity: O(n) where n is the log size. Works on unsorted logs.
   *
   * @param log the event log to search
   * @param timestamp the timestamp to search for
   * @return the index of the first matching event, or -1 if not found
   */
  public static int searchByTimestamp(EventLog log, Instant timestamp) {
    if (log == null || timestamp == null) {
      return -1;
    }

    MyList<SystemEvent> events = new com.omni.core.list.MyArrayList<>();
    for (SystemEvent event : log.getAll()) {
      events.add(event);
    }

    return SearchAlgorithms.linearSearch(
        events,
        createEventWithTimestamp(timestamp),
        Comparator.comparing(SystemEvent::getTimestamp));
  }

  /**
   * Performs binary search to find an event by timestamp in a sorted log.
   *
   * <p>Time complexity: O(log n) where n is the log size. Requires the log to be sorted by
   * timestamp.
   *
   * @param sortedLog the sorted event log to search
   * @param timestamp the timestamp to search for
   * @return the index if found, or -(insertion_point + 1) if not found
   */
  public static int binarySearchByTimestamp(EventLog sortedLog, Instant timestamp) {
    if (sortedLog == null || timestamp == null) {
      throw new IllegalArgumentException("Log and timestamp cannot be null");
    }

    MyList<SystemEvent> events = new com.omni.core.list.MyArrayList<>();
    for (SystemEvent event : sortedLog.getAll()) {
      events.add(event);
    }

    return SearchAlgorithms.binarySearch(
        events,
        createEventWithTimestamp(timestamp),
        Comparator.comparing(SystemEvent::getTimestamp));
  }

  /**
   * Performs linear search to find events by severity.
   *
   * <p>Returns the index of the first event with the specified severity.
   *
   * @param log the event log to search
   * @param severity the severity level to search for
   * @return the index of the first matching event, or -1 if not found
   */
  public static int searchBySeverity(EventLog log, SystemEvent.Severity severity) {
    if (log == null || severity == null) {
      return -1;
    }

    MyList<SystemEvent> events = new com.omni.core.list.MyArrayList<>();
    for (SystemEvent event : log.getAll()) {
      events.add(event);
    }

    return SearchAlgorithms.linearSearch(
        events, createEventWithSeverity(severity), Comparator.comparing(SystemEvent::getSeverity));
  }

  /**
   * Performs linear search to find an event by source name.
   *
   * @param log the event log to search
   * @param source the source name to search for
   * @return the index of the first matching event, or -1 if not found
   */
  public static int searchBySource(EventLog log, String source) {
    if (log == null || source == null) {
      return -1;
    }

    MyList<SystemEvent> events = new com.omni.core.list.MyArrayList<>();
    for (SystemEvent event : log.getAll()) {
      events.add(event);
    }

    return SearchAlgorithms.linearSearch(
        events, createEventWithSource(source), Comparator.comparing(SystemEvent::getSource));
  }

  // Helper methods to create event-like objects for searching
  // These are dummy events used only for comparison

  private static SystemEvent createEventWithTimestamp(Instant timestamp) {
    return SystemEvent.builder()
        .timestamp(timestamp)
        .severity(SystemEvent.Severity.INFO)
        .source("dummy")
        .message("dummy")
        .build();
  }

  private static SystemEvent createEventWithSeverity(SystemEvent.Severity severity) {
    return SystemEvent.builder()
        .timestamp(Instant.now())
        .severity(severity)
        .source("dummy")
        .message("dummy")
        .build();
  }

  private static SystemEvent createEventWithSource(String source) {
    return SystemEvent.builder()
        .timestamp(Instant.now())
        .severity(SystemEvent.Severity.INFO)
        .source(source)
        .message("dummy")
        .build();
  }
}
