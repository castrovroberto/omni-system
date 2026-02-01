package com.omni.core.graph.patterns;

import com.omni.core.graph.Edge;
import com.omni.core.graph.MyGraph;
import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;

/**
 * Decorator that adds observer capabilities to any graph implementation.
 *
 * <p>Wraps an existing {@link MyGraph} and notifies registered observers when the graph structure
 * changes.
 *
 * @param <V> the vertex type
 */
public class ObservableGraph<V> implements MyGraph<V> {

  private final MyGraph<V> delegate;
  private final MyList<GraphObserver<V>> observers;

  /**
   * Creates an observable wrapper around the given graph.
   *
   * @param delegate the graph to wrap
   */
  public ObservableGraph(MyGraph<V> delegate) {
    if (delegate == null) {
      throw new IllegalArgumentException("Delegate graph cannot be null");
    }
    this.delegate = delegate;
    this.observers = new MyArrayList<>();
  }

  /**
   * Registers an observer to receive graph change notifications.
   *
   * @param observer the observer to register
   */
  public void addObserver(GraphObserver<V> observer) {
    if (observer != null && !containsObserver(observer)) {
      observers.add(observer);
    }
  }

  /**
   * Unregisters an observer.
   *
   * @param observer the observer to unregister
   */
  public void removeObserver(GraphObserver<V> observer) {
    for (int i = 0; i < observers.size(); i++) {
      if (observers.get(i) == observer) {
        observers.remove(i);
        return;
      }
    }
  }

  /**
   * Returns the number of registered observers.
   *
   * @return observer count
   */
  public int observerCount() {
    return observers.size();
  }

  private boolean containsObserver(GraphObserver<V> observer) {
    for (int i = 0; i < observers.size(); i++) {
      if (observers.get(i) == observer) {
        return true;
      }
    }
    return false;
  }

  // ==================== Delegate with Notifications ====================

  @Override
  public void addVertex(V vertex) {
    boolean hadVertex = hasVertex(vertex);
    delegate.addVertex(vertex);
    if (!hadVertex) {
      for (int i = 0; i < observers.size(); i++) {
        observers.get(i).onVertexAdded(vertex);
      }
    }
  }

  @Override
  public boolean removeVertex(V vertex) {
    boolean removed = delegate.removeVertex(vertex);
    if (removed) {
      for (int i = 0; i < observers.size(); i++) {
        observers.get(i).onVertexRemoved(vertex);
      }
    }
    return removed;
  }

  @Override
  public void addEdge(V source, V target, double weight) {
    delegate.addEdge(source, target, weight);
    for (int i = 0; i < observers.size(); i++) {
      observers.get(i).onEdgeAdded(source, target, weight);
    }
  }

  @Override
  public boolean removeEdge(V source, V target) {
    boolean removed = delegate.removeEdge(source, target);
    if (removed) {
      for (int i = 0; i < observers.size(); i++) {
        observers.get(i).onEdgeRemoved(source, target);
      }
    }
    return removed;
  }

  /**
   * Notifies all observers of a vertex state change.
   *
   * @param vertex the vertex whose state changed
   * @param oldState the previous state
   * @param newState the new state
   */
  public void notifyStateChange(V vertex, NodeState<V> oldState, NodeState<V> newState) {
    for (int i = 0; i < observers.size(); i++) {
      observers.get(i).onVertexStateChanged(vertex, oldState, newState);
    }
  }

  // ==================== Pure Delegates ====================

  @Override
  public boolean hasVertex(V vertex) {
    return delegate.hasVertex(vertex);
  }

  @Override
  public boolean hasEdge(V source, V target) {
    return delegate.hasEdge(source, target);
  }

  @Override
  public double getEdgeWeight(V source, V target) {
    return delegate.getEdgeWeight(source, target);
  }

  @Override
  public MyList<V> getNeighbors(V vertex) {
    return delegate.getNeighbors(vertex);
  }

  @Override
  public MyList<Edge<V>> getEdges(V vertex) {
    return delegate.getEdges(vertex);
  }

  @Override
  public MyList<V> getVertices() {
    return delegate.getVertices();
  }

  @Override
  public int vertexCount() {
    return delegate.vertexCount();
  }

  @Override
  public int edgeCount() {
    return delegate.edgeCount();
  }

  @Override
  public boolean isDirected() {
    return delegate.isDirected();
  }
}
