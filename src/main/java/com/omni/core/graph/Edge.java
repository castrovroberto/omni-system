package com.omni.core.graph;

/**
 * Represents a weighted edge in a graph.
 *
 * <pre>
 *   source ---weight---> target
 * </pre>
 *
 * @param source the source vertex
 * @param target the target vertex
 * @param weight the edge weight
 * @param <V> the vertex type
 */
public record Edge<V>(V source, V target, double weight) implements Comparable<Edge<V>> {

  /**
   * Creates an unweighted edge (weight = 1.0).
   *
   * @param source the source vertex
   * @param target the target vertex
   */
  public Edge(V source, V target) {
    this(source, target, 1.0);
  }

  @Override
  public int compareTo(Edge<V> other) {
    return Double.compare(this.weight, other.weight);
  }
}
