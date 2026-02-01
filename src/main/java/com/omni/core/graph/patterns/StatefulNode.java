package com.omni.core.graph.patterns;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;

/**
 * Context class that wraps a vertex with state management (State pattern).
 *
 * <p>Tracks state transitions and notifies listeners when state changes occur.
 *
 * @param <V> the vertex type
 */
public class StatefulNode<V> {

  /** Listener for state change events. */
  @FunctionalInterface
  public interface StateChangeListener<V> {
    void onStateChange(StatefulNode<V> node, NodeState<V> oldState, NodeState<V> newState);
  }

  private final V vertex;
  private NodeState<V> currentState;
  private final MyList<StateChangeListener<V>> listeners;

  /**
   * Creates a new stateful node starting in the Booting state.
   *
   * @param vertex the wrapped vertex
   */
  public StatefulNode(V vertex) {
    this(vertex, NodeStates.booting());
  }

  /**
   * Creates a new stateful node with a specific initial state.
   *
   * @param vertex the wrapped vertex
   * @param initialState the initial state
   */
  public StatefulNode(V vertex, NodeState<V> initialState) {
    if (vertex == null) {
      throw new IllegalArgumentException("Vertex cannot be null");
    }
    if (initialState == null) {
      throw new IllegalArgumentException("Initial state cannot be null");
    }
    this.vertex = vertex;
    this.currentState = initialState;
    this.listeners = new MyArrayList<>();
  }

  /**
   * Returns the wrapped vertex.
   *
   * @return the vertex
   */
  public V getVertex() {
    return vertex;
  }

  /**
   * Returns the current state.
   *
   * @return current state
   */
  public NodeState<V> getState() {
    return currentState;
  }

  /**
   * Returns the name of the current state.
   *
   * @return state name
   */
  public String getStateName() {
    return currentState.name();
  }

  /**
   * Returns true if this node can accept incoming connections.
   *
   * @return true if connections are accepted
   */
  public boolean canAcceptConnections() {
    return currentState.canAcceptConnections();
  }

  /**
   * Returns true if this node can send outgoing requests.
   *
   * @return true if requests can be sent
   */
  public boolean canSendRequests() {
    return currentState.canSendRequests();
  }

  /**
   * Handles an event, potentially transitioning to a new state.
   *
   * @param event the event to handle
   */
  public void handleEvent(NodeEvent event) {
    if (event == null) {
      throw new IllegalArgumentException("Event cannot be null");
    }
    NodeState<V> oldState = currentState;
    NodeState<V> newState = currentState.next(event);
    if (newState != oldState) {
      currentState = newState;
      notifyListeners(oldState, newState);
    }
  }

  /**
   * Adds a state change listener.
   *
   * @param listener the listener to add
   */
  public void addListener(StateChangeListener<V> listener) {
    if (listener != null) {
      listeners.add(listener);
    }
  }

  /**
   * Removes a state change listener.
   *
   * @param listener the listener to remove
   */
  public void removeListener(StateChangeListener<V> listener) {
    for (int i = 0; i < listeners.size(); i++) {
      if (listeners.get(i) == listener) {
        listeners.remove(i);
        return;
      }
    }
  }

  private void notifyListeners(NodeState<V> oldState, NodeState<V> newState) {
    for (int i = 0; i < listeners.size(); i++) {
      listeners.get(i).onStateChange(this, oldState, newState);
    }
  }

  @Override
  public String toString() {
    return "StatefulNode{" + "vertex=" + vertex + ", state=" + currentState.name() + '}';
  }
}
