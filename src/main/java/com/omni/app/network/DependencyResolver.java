package com.omni.app.network;

import com.omni.core.graph.AdjacencyListGraph;
import com.omni.core.graph.CycleDetectedException;
import com.omni.core.graph.GraphAlgorithms;
import com.omni.core.list.MyList;

/**
 * Resolves service startup order using topological sort on a dependency graph.
 *
 * <p>Wraps a directed {@link AdjacencyListGraph} where an edge from A to B means "A depends on B"
 * (B must start before A).
 */
public class DependencyResolver {

  private final AdjacencyListGraph<String> graph;

  /** Constructs an empty dependency resolver. */
  public DependencyResolver() {
    this.graph = new AdjacencyListGraph<>(true);
  }

  /**
   * Registers a service.
   *
   * @param service the service name
   */
  public void addService(String service) {
    graph.addVertex(service);
  }

  /**
   * Declares that {@code service} depends on {@code dependsOn}. The dependency must start first.
   *
   * @param service the dependent service
   * @param dependsOn the dependency
   */
  public void addDependency(String service, String dependsOn) {
    if (!graph.hasVertex(service)) {
      graph.addVertex(service);
    }
    if (!graph.hasVertex(dependsOn)) {
      graph.addVertex(dependsOn);
    }
    graph.addEdge(service, dependsOn);
  }

  /**
   * Returns the startup order (dependencies first).
   *
   * @return list of services in startup order
   * @throws CycleDetectedException if there is a circular dependency
   */
  public MyList<String> resolveStartupOrder() throws CycleDetectedException {
    // Topological sort returns dependencies-last order for our edge direction
    // (service -> dependsOn), so the result is already reversed: dependsOn comes first
    // because topoSort puts nodes with no outgoing unvisited edges first (post-order + prepend)
    MyList<String> sorted = GraphAlgorithms.topologicalSort(graph);
    // The topo sort with addFirst gives us: if A->B (A depends on B), B appears before A
    // That's correct since topo sort output has B before A when edge is A->B
    // Wait - our edge is A->B meaning "A depends on B", and topoSort with DFS post-order
    // prepends, giving us B before A. But standard topo sort for A->B means A before B.
    // We need to reverse: dependencies first means B before A.
    // Our topoSort does addFirst in post-order, which gives reverse post-order = topo order
    // where A comes before B (for edge A->B). We need B before A. So reverse.
    return reverse(sorted);
  }

  private MyList<String> reverse(MyList<String> list) {
    com.omni.core.list.MyArrayList<String> reversed = new com.omni.core.list.MyArrayList<>();
    for (int i = list.size() - 1; i >= 0; i--) {
      reversed.add(list.get(i));
    }
    return reversed;
  }
}
