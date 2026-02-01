package com.omni.core.graph.traversal;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.graph.AdjacencyListGraph;
import com.omni.core.list.MyList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("GraphTraversal Tests")
class GraphTraversalTest {

  private AdjacencyListGraph<String> graph;

  @BeforeEach
  void setUp() {
    graph = new AdjacencyListGraph<>(true);
    graph.addVertex("A");
    graph.addVertex("B");
    graph.addVertex("C");
    graph.addVertex("D");
    graph.addEdge("A", "B");
    graph.addEdge("A", "C");
    graph.addEdge("B", "D");
    graph.addEdge("C", "D");
  }

  @Nested
  @DisplayName("DFS Traversal")
  class DFSTests {

    @Test
    @DisplayName("DFS visits all reachable vertices")
    void dfs_visitsAll() {
      DFSTraversal<String> dfs = new DFSTraversal<>(graph, "A");
      MyList<String> result = dfs.traverse();
      assertEquals(4, result.size());
      assertEquals("A", result.get(0));
      assertTrue(result.contains("B"));
      assertTrue(result.contains("C"));
      assertTrue(result.contains("D"));
    }

    @Test
    @DisplayName("DFS from leaf visits only that vertex")
    void dfs_leaf_singleVertex() {
      DFSTraversal<String> dfs = new DFSTraversal<>(graph, "D");
      MyList<String> result = dfs.traverse();
      assertEquals(1, result.size());
      assertEquals("D", result.get(0));
    }
  }

  @Nested
  @DisplayName("BFS Traversal")
  class BFSTests {

    @Test
    @DisplayName("BFS visits vertices in level order")
    void bfs_levelOrder() {
      BFSTraversal<String> bfs = new BFSTraversal<>(graph, "A");
      MyList<String> result = bfs.traverse();
      assertEquals(4, result.size());
      assertEquals("A", result.get(0));
      int dIdx = result.indexOf("D");
      int bIdx = result.indexOf("B");
      int cIdx = result.indexOf("C");
      assertTrue(bIdx < dIdx);
      assertTrue(cIdx < dIdx);
    }

    @Test
    @DisplayName("BFS from leaf visits only that vertex")
    void bfs_leaf_singleVertex() {
      BFSTraversal<String> bfs = new BFSTraversal<>(graph, "D");
      MyList<String> result = bfs.traverse();
      assertEquals(1, result.size());
      assertEquals("D", result.get(0));
    }
  }
}
