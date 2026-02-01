package com.omni.core.graph;

/**
 * Thrown when a cycle is detected in a graph where one is not expected, such as during topological
 * sort.
 */
public class CycleDetectedException extends Exception {

  /**
   * Constructs a new exception with the specified message.
   *
   * @param message the detail message
   */
  public CycleDetectedException(String message) {
    super(message);
  }
}
