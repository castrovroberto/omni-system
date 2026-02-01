package com.omni.core.graph;

import com.omni.core.list.MyList;

/**
 * Interface for graph data structures in the Omni-System.
 *
 * <p>Supports both directed and undirected graphs with weighted edges.
 *
 * @param <V> the vertex type
 */
public interface MyGraph<V> {

  /**
   * Adds a vertex to the graph.
   *
   * @param vertex the vertex to add
   * @throws IllegalArgumentException if vertex is null
   */
  void addVertex(V vertex);

  /**
   * Removes a vertex and all its incident edges.
   *
   * @param vertex the vertex to remove
   * @return true if the vertex was present and removed
   */
  boolean removeVertex(V vertex);

  /**
   * Adds a weighted edge between source and target.
   *
   * @param source the source vertex
   * @param target the target vertex
   * @param weight the edge weight
   * @throws IllegalArgumentException if either vertex is not in the graph
   */
  void addEdge(V source, V target, double weight);

  /**
   * Adds an unweighted edge (weight = 1.0) between source and target.
   *
   * @param source the source vertex
   * @param target the target vertex
   * @throws IllegalArgumentException if either vertex is not in the graph
   */
  default void addEdge(V source, V target) {
    addEdge(source, target, 1.0);
  }

  /**
   * Removes the edge from source to target.
   *
   * @param source the source vertex
   * @param target the target vertex
   * @return true if the edge was present and removed
   */
  boolean removeEdge(V source, V target);

  /**
   * Returns true if the graph contains the given vertex.
   *
   * @param vertex the vertex to check
   * @return true if present
   */
  boolean hasVertex(V vertex);

  /**
   * Returns true if the graph contains an edge from source to target.
   *
   * @param source the source vertex
   * @param target the target vertex
   * @return true if the edge exists
   */
  boolean hasEdge(V source, V target);

  /**
   * Returns the weight of the edge from source to target.
   *
   * @param source the source vertex
   * @param target the target vertex
   * @return the edge weight
   * @throws IllegalArgumentException if the edge does not exist
   */
  double getEdgeWeight(V source, V target);

  /**
   * Returns the neighbors of the given vertex.
   *
   * @param vertex the vertex
   * @return list of neighboring vertices
   * @throws IllegalArgumentException if vertex is not in the graph
   */
  MyList<V> getNeighbors(V vertex);

  /**
   * Returns all edges from the given vertex.
   *
   * @param vertex the vertex
   * @return list of edges from this vertex
   * @throws IllegalArgumentException if vertex is not in the graph
   */
  MyList<Edge<V>> getEdges(V vertex);

  /**
   * Returns all vertices in the graph.
   *
   * @return list of all vertices
   */
  MyList<V> getVertices();

  /**
   * Returns the number of vertices.
   *
   * @return vertex count
   */
  int vertexCount();

  /**
   * Returns the number of edges.
   *
   * @return edge count
   */
  int edgeCount();

  /**
   * Returns true if this is a directed graph.
   *
   * @return true if directed
   */
  boolean isDirected();
}
