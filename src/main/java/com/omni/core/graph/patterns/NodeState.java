package com.omni.core.graph.patterns;

/**
 * State interface for the State pattern representing server lifecycle states.
 *
 * <p>Each state defines:
 *
 * <ul>
 *   <li>Its name for logging/debugging
 *   <li>Transition logic based on events
 *   <li>Capability flags (can accept connections, can send requests)
 * </ul>
 *
 * <p>Implementations should be immutable singletons accessed via {@link NodeStates}.
 *
 * @param <V> the vertex type (for future extensions)
 */
public interface NodeState<V> {

  /**
   * Returns the name of this state.
   *
   * @return state name (e.g., "Booting", "Running")
   */
  String name();

  /**
   * Computes the next state based on the given event.
   *
   * @param event the event that occurred
   * @return the next state, or this state if no transition is defined
   */
  NodeState<V> next(NodeEvent event);

  /**
   * Returns true if this state can accept incoming connections.
   *
   * @return true if connections are accepted
   */
  boolean canAcceptConnections();

  /**
   * Returns true if this state can send outgoing requests.
   *
   * @return true if requests can be sent
   */
  boolean canSendRequests();
}
