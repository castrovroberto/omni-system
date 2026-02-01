package com.omni.core.graph.patterns;

/**
 * Events that trigger state transitions in {@link NodeState}.
 *
 * <p>Used with the State pattern to model server lifecycle events.
 */
public enum NodeEvent {
  /** Server is starting up. */
  START,

  /** Server has completed initialization and is ready to serve. */
  READY,

  /** Server has encountered a fatal error. */
  CRASH,

  /** Server is being restarted after a crash. */
  RESTART,

  /** Server is being gracefully shut down. */
  SHUTDOWN
}
