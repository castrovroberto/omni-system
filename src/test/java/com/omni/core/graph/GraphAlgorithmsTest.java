package com.omni.core.graph;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyList;
import com.omni.core.map.MyHashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("GraphAlgorithms Tests")
class GraphAlgorithmsTest {

  private AdjacencyListGraph<String> buildSimpleDirectedGraph() {
    AdjacencyListGraph<String> g = new AdjacencyListGraph<>(true);
    g.addVertex("A");
    g.addVertex("B");
    g.addVertex("C");
    g.addVertex("D");
    g.addEdge("A", "B");
    g.addEdge("A", "C");
    g.addEdge("B", "D");
    g.addEdge("C", "D");
    return g;
  }

  private AdjacencyListGraph<String> buildWeightedGraph() {
    AdjacencyListGraph<String> g = new AdjacencyListGraph<>(false);
    g.addVertex("A");
    g.addVertex("B");
    g.addVertex("C");
    g.addVertex("D");
    g.addVertex("E");
    g.addEdge("A", "B", 4.0);
    g.addEdge("A", "C", 2.0);
    g.addEdge("C", "B", 1.0);
    g.addEdge("B", "D", 5.0);
    g.addEdge("C", "D", 8.0);
    g.addEdge("D", "E", 3.0);
    return g;
  }

  // ==================== DFS Tests ====================

  @Nested
  @DisplayName("DFS Tests")
  class DFSTests {

    @Test
    @DisplayName("DFS recursive visits all reachable vertices")
    void dfsRecursive_visitsAllReachable() {
      AdjacencyListGraph<String> g = buildSimpleDirectedGraph();
      MyList<String> result = GraphAlgorithms.dfsRecursive(g, "A");
      assertEquals(4, result.size());
      assertEquals("A", result.get(0));
      assertTrue(result.contains("B"));
      assertTrue(result.contains("C"));
      assertTrue(result.contains("D"));
    }

    @Test
    @DisplayName("DFS iterative visits all reachable vertices")
    void dfsIterative_visitsAllReachable() {
      AdjacencyListGraph<String> g = buildSimpleDirectedGraph();
      MyList<String> result = GraphAlgorithms.dfsIterative(g, "A");
      assertEquals(4, result.size());
      assertEquals("A", result.get(0));
      assertTrue(result.contains("B"));
      assertTrue(result.contains("C"));
      assertTrue(result.contains("D"));
    }

    @Test
    @DisplayName("DFS from disconnected vertex returns only that vertex")
    void dfs_disconnectedVertex_returnsSingle() {
      AdjacencyListGraph<String> g = buildSimpleDirectedGraph();
      g.addVertex("Z");
      MyList<String> result = GraphAlgorithms.dfsRecursive(g, "Z");
      assertEquals(1, result.size());
      assertEquals("Z", result.get(0));
    }
  }

  // ==================== BFS Tests ====================

  @Nested
  @DisplayName("BFS Tests")
  class BFSTests {

    @Test
    @DisplayName("BFS visits vertices in level order")
    void bfs_levelOrder() {
      AdjacencyListGraph<String> g = buildSimpleDirectedGraph();
      MyList<String> result = GraphAlgorithms.bfs(g, "A");
      assertEquals(4, result.size());
      assertEquals("A", result.get(0));
      // B and C should appear before D (level 1 before level 2)
      int bIdx = result.indexOf("B");
      int cIdx = result.indexOf("C");
      int dIdx = result.indexOf("D");
      assertTrue(bIdx < dIdx);
      assertTrue(cIdx < dIdx);
    }

    @Test
    @DisplayName("BFS from disconnected vertex returns only that vertex")
    void bfs_disconnectedVertex_returnsSingle() {
      AdjacencyListGraph<String> g = buildSimpleDirectedGraph();
      g.addVertex("Z");
      MyList<String> result = GraphAlgorithms.bfs(g, "Z");
      assertEquals(1, result.size());
    }
  }

  // ==================== Dijkstra Tests ====================

  @Nested
  @DisplayName("Dijkstra Tests")
  class DijkstraTests {

    @Test
    @DisplayName("Dijkstra computes shortest distances")
    void dijkstra_shortestDistances() {
      AdjacencyListGraph<String> g = buildWeightedGraph();
      MyHashMap<String, Double> dist = GraphAlgorithms.dijkstra(g, "A");
      assertEquals(0.0, dist.get("A").orElse(-1.0));
      assertEquals(3.0, dist.get("B").orElse(-1.0)); // A->C(2) + C->B(1) = 3
      assertEquals(2.0, dist.get("C").orElse(-1.0)); // A->C(2)
      assertEquals(8.0, dist.get("D").orElse(-1.0)); // A->C->B->D = 2+1+5 = 8
      assertEquals(11.0, dist.get("E").orElse(-1.0)); // 8 + 3 = 11
    }

    @Test
    @DisplayName("Dijkstra path reconstruction")
    void dijkstraPath_reconstructsPath() {
      AdjacencyListGraph<String> g = buildWeightedGraph();
      MyList<String> path = GraphAlgorithms.dijkstraPath(g, "A", "D");
      assertEquals(4, path.size());
      assertEquals("A", path.get(0));
      assertEquals("C", path.get(1));
      assertEquals("B", path.get(2));
      assertEquals("D", path.get(3));
    }

    @Test
    @DisplayName("Dijkstra unreachable vertex returns empty path")
    void dijkstraPath_unreachable_emptyPath() {
      AdjacencyListGraph<String> g = new AdjacencyListGraph<>(true);
      g.addVertex("A");
      g.addVertex("B");
      MyList<String> path = GraphAlgorithms.dijkstraPath(g, "A", "B");
      assertTrue(path.isEmpty());
    }

    @Test
    @DisplayName("Dijkstra source to self returns zero distance")
    void dijkstra_sourceToSelf_zeroDistance() {
      AdjacencyListGraph<String> g = buildWeightedGraph();
      MyHashMap<String, Double> dist = GraphAlgorithms.dijkstra(g, "A");
      assertEquals(0.0, dist.get("A").orElse(-1.0));
    }
  }

  // ==================== Topological Sort Tests ====================

  @Nested
  @DisplayName("Topological Sort Tests")
  class TopologicalSortTests {

    @Test
    @DisplayName("Topological sort returns valid ordering")
    void topologicalSort_validOrdering() throws CycleDetectedException {
      AdjacencyListGraph<String> g = new AdjacencyListGraph<>(true);
      g.addVertex("A");
      g.addVertex("B");
      g.addVertex("C");
      g.addVertex("D");
      g.addEdge("A", "B");
      g.addEdge("A", "C");
      g.addEdge("B", "D");
      g.addEdge("C", "D");

      MyList<String> order = GraphAlgorithms.topologicalSort(g);
      assertEquals(4, order.size());

      // A must come before B and C; B and C must come before D
      int aIdx = order.indexOf("A");
      int bIdx = order.indexOf("B");
      int cIdx = order.indexOf("C");
      int dIdx = order.indexOf("D");
      assertTrue(aIdx < bIdx);
      assertTrue(aIdx < cIdx);
      assertTrue(bIdx < dIdx);
      assertTrue(cIdx < dIdx);
    }

    @Test
    @DisplayName("Topological sort detects cycle")
    void topologicalSort_cycle_throws() {
      AdjacencyListGraph<String> g = new AdjacencyListGraph<>(true);
      g.addVertex("A");
      g.addVertex("B");
      g.addVertex("C");
      g.addEdge("A", "B");
      g.addEdge("B", "C");
      g.addEdge("C", "A");

      assertThrows(CycleDetectedException.class, () -> GraphAlgorithms.topologicalSort(g));
    }

    @Test
    @DisplayName("Topological sort on undirected graph throws")
    void topologicalSort_undirected_throws() {
      AdjacencyListGraph<String> g = new AdjacencyListGraph<>(false);
      g.addVertex("A");
      assertThrows(IllegalArgumentException.class, () -> GraphAlgorithms.topologicalSort(g));
    }

    @Test
    @DisplayName("Topological sort of single vertex")
    void topologicalSort_singleVertex() throws CycleDetectedException {
      AdjacencyListGraph<String> g = new AdjacencyListGraph<>(true);
      g.addVertex("A");
      MyList<String> order = GraphAlgorithms.topologicalSort(g);
      assertEquals(1, order.size());
      assertEquals("A", order.get(0));
    }
  }
}
