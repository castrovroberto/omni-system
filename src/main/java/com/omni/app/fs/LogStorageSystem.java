package com.omni.app.fs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Log storage system that supports retrieving logs within a time range.
 *
 * <p><b>Interview Question:</b> "Design Log Storage System" (LeetCode #635) - commonly asked for
 * Java roles to test understanding of TreeMap and range queries.
 *
 * <p><b>Design:</b> Uses TreeMap for O(log n) insertion and efficient range retrieval via subMap().
 *
 * <p><b>Interview Talking Points:</b>
 *
 * <ul>
 *   <li>Why TreeMap over HashMap? Red-Black tree enables O(log n) range queries
 *   <li>subMap() returns a view, not a copy - efficient for large ranges
 *   <li>Granularity handling converts timestamps to bucket keys
 * </ul>
 */
public class LogStorageSystem {

  /** Substring lengths for each granularity level. */
  private static final Map<String, Integer> GRANULARITY_LENGTHS = new HashMap<>();

  static {
    GRANULARITY_LENGTHS.put("Year", 4);
    GRANULARITY_LENGTHS.put("Month", 7);
    GRANULARITY_LENGTHS.put("Day", 10);
    GRANULARITY_LENGTHS.put("Hour", 13);
    GRANULARITY_LENGTHS.put("Minute", 16);
    GRANULARITY_LENGTHS.put("Second", 19);
  }

  /** Max suffix to append for end bound (exclusive range becomes inclusive). */
  @SuppressWarnings("PMD.AvoidUsingHardCodedIP") // Not an IP - timestamp suffix MM:DD:HH:MM:SS
  private static final String MAX_SUFFIX = ":12:31:23:59:59";

  /** Min suffix for padding timestamps. */
  @SuppressWarnings("PMD.AvoidUsingHardCodedIP") // Not an IP - timestamp suffix MM:DD:HH:MM:SS
  private static final String MIN_SUFFIX = ":01:01:00:00:00";

  /**
   * Stores logs by timestamp. TreeMap provides O(log n) insertion and O(log n + k) range queries.
   */
  private final TreeMap<String, List<Integer>> logs;

  /** Creates an empty log storage system. */
  public LogStorageSystem() {
    this.logs = new TreeMap<>();
  }

  /**
   * Stores a log with the given ID at the specified timestamp.
   *
   * <p><b>Complexity:</b> O(log n) where n is the number of unique timestamps.
   *
   * @param id the log identifier
   * @param timestamp the timestamp in format "YYYY:MM:DD:HH:MM:SS"
   */
  public void put(int id, String timestamp) {
    logs.computeIfAbsent(timestamp, k -> new ArrayList<>()).add(id);
  }

  /**
   * Retrieves all log IDs within the time range at the specified granularity.
   *
   * <p><b>Complexity:</b> O(log n + k) where n is the number of timestamps and k is the result
   * size.
   *
   * <p><b>Example:</b> With granularity "Day", timestamps "2017:01:01:00:00:00" and
   * "2017:01:01:23:59:59" are treated as equivalent since we only compare up to the day.
   *
   * @param start the start timestamp (inclusive)
   * @param end the end timestamp (inclusive)
   * @param granularity one of: "Year", "Month", "Day", "Hour", "Minute", "Second"
   * @return list of log IDs within the range
   */
  public List<Integer> retrieve(String start, String end, String granularity) {
    int len = GRANULARITY_LENGTHS.get(granularity);

    // Truncate to granularity and pad with min/max values
    String startKey = start.substring(0, len) + MIN_SUFFIX.substring(len - 4);
    String endKey = end.substring(0, len) + MAX_SUFFIX.substring(len - 4);

    List<Integer> result = new ArrayList<>();

    // subMap returns a VIEW of the portion of the map, very efficient
    for (List<Integer> ids : logs.subMap(startKey, true, endKey, true).values()) {
      result.addAll(ids);
    }

    return result;
  }

  /**
   * Returns the number of unique timestamps stored.
   *
   * @return the count of distinct timestamps
   */
  public int size() {
    return logs.size();
  }

  /**
   * Returns the total number of log entries.
   *
   * @return the total log count
   */
  public int totalLogs() {
    return logs.values().stream().mapToInt(List::size).sum();
  }
}
