package com.omni.core.graph.patterns;

/**
 * Observer interface for graph change notifications (Observer pattern).
 *
 * <p>Implementers receive callbacks when the observed graph's structure changes.
 *
 * @param <V> the vertex type
 */
public interface GraphObserver<V> {

  /**
   * Called when a vertex is added to the graph.
   *
   * @param vertex the added vertex
   */
  void onVertexAdded(V vertex);

  /**
   * Called when a vertex is removed from the graph.
   *
   * @param vertex the removed vertex
   */
  void onVertexRemoved(V vertex);

  /**
   * Called when an edge is added to the graph.
   *
   * @param source the source vertex
   * @param target the target vertex
   * @param weight the edge weight
   */
  void onEdgeAdded(V source, V target, double weight);

  /**
   * Called when an edge is removed from the graph.
   *
   * @param source the source vertex
   * @param target the target vertex
   */
  void onEdgeRemoved(V source, V target);

  /**
   * Called when a vertex's state changes (if using StatefulNode).
   *
   * @param vertex the vertex whose state changed
   * @param oldState the previous state
   * @param newState the new state
   */
  default void onVertexStateChanged(V vertex, NodeState<V> oldState, NodeState<V> newState) {
    // Default implementation does nothing
  }
}
