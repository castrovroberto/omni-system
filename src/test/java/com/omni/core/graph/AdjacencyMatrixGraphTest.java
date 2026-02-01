package com.omni.core.graph;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AdjacencyMatrixGraph Tests")
class AdjacencyMatrixGraphTest {

  // ==================== Vertex Operations ====================

  @Nested
  @DisplayName("Vertex Operations")
  class VertexOps {

    private AdjacencyMatrixGraph<String> graph;

    @BeforeEach
    void setUp() {
      graph = new AdjacencyMatrixGraph<>(true);
    }

    @Test
    @DisplayName("Add and check vertex")
    void addVertex_hasVertex_returnsTrue() {
      graph.addVertex("A");
      assertTrue(graph.hasVertex("A"));
      assertFalse(graph.hasVertex("B"));
    }

    @Test
    @DisplayName("Add duplicate vertex is idempotent")
    void addVertex_duplicate_noError() {
      graph.addVertex("A");
      graph.addVertex("A");
      assertEquals(1, graph.vertexCount());
    }

    @Test
    @DisplayName("Remove vertex")
    void removeVertex_removesVertexAndEdges() {
      graph.addVertex("A");
      graph.addVertex("B");
      graph.addEdge("A", "B");
      assertTrue(graph.removeVertex("A"));
      assertFalse(graph.hasVertex("A"));
      assertEquals(1, graph.vertexCount());
    }

    @Test
    @DisplayName("Remove non-existent vertex returns false")
    void removeVertex_nonExistent_returnsFalse() {
      assertFalse(graph.removeVertex("X"));
    }

    @Test
    @DisplayName("Null vertex throws")
    void addVertex_null_throws() {
      assertThrows(IllegalArgumentException.class, () -> graph.addVertex(null));
    }

    @Test
    @DisplayName("Get vertices returns all vertices")
    void getVertices_returnsAll() {
      graph.addVertex("A");
      graph.addVertex("B");
      graph.addVertex("C");
      assertEquals(3, graph.getVertices().size());
    }
  }

  // ==================== Edge Operations ====================

  @Nested
  @DisplayName("Edge Operations")
  class EdgeOps {

    private AdjacencyMatrixGraph<String> graph;

    @BeforeEach
    void setUp() {
      graph = new AdjacencyMatrixGraph<>(true);
      graph.addVertex("A");
      graph.addVertex("B");
      graph.addVertex("C");
    }

    @Test
    @DisplayName("Add and check edge")
    void addEdge_hasEdge_returnsTrue() {
      graph.addEdge("A", "B", 5.0);
      assertTrue(graph.hasEdge("A", "B"));
      assertEquals(5.0, graph.getEdgeWeight("A", "B"));
    }

    @Test
    @DisplayName("Remove edge")
    void removeEdge_removesEdge() {
      graph.addEdge("A", "B");
      assertTrue(graph.removeEdge("A", "B"));
      assertFalse(graph.hasEdge("A", "B"));
    }

    @Test
    @DisplayName("Remove non-existent edge returns false")
    void removeEdge_nonExistent_returnsFalse() {
      assertFalse(graph.removeEdge("A", "B"));
    }

    @Test
    @DisplayName("Edge to non-existent vertex throws")
    void addEdge_nonExistentVertex_throws() {
      assertThrows(IllegalArgumentException.class, () -> graph.addEdge("A", "Z"));
    }

    @Test
    @DisplayName("Get edge weight for non-existent edge throws")
    void getEdgeWeight_nonExistent_throws() {
      assertThrows(IllegalArgumentException.class, () -> graph.getEdgeWeight("A", "B"));
    }

    @Test
    @DisplayName("Edge count tracks additions and removals")
    void edgeCount_tracksOperations() {
      assertEquals(0, graph.edgeCount());
      graph.addEdge("A", "B");
      assertEquals(1, graph.edgeCount());
      graph.addEdge("B", "C");
      assertEquals(2, graph.edgeCount());
      graph.removeEdge("A", "B");
      assertEquals(1, graph.edgeCount());
    }

    @Test
    @DisplayName("Get neighbors returns adjacent vertices")
    void getNeighbors_returnsAdjacent() {
      graph.addEdge("A", "B");
      graph.addEdge("A", "C");
      MyList<String> neighbors = graph.getNeighbors("A");
      assertEquals(2, neighbors.size());
    }

    @Test
    @DisplayName("Get edges returns edge objects")
    void getEdges_returnsEdges() {
      graph.addEdge("A", "B", 3.0);
      graph.addEdge("A", "C", 7.0);
      MyList<Edge<String>> edges = graph.getEdges("A");
      assertEquals(2, edges.size());
    }

    @Test
    @DisplayName("Replacing edge updates weight")
    void addEdge_replace_updatesWeight() {
      graph.addEdge("A", "B", 5.0);
      graph.addEdge("A", "B", 10.0);
      assertEquals(10.0, graph.getEdgeWeight("A", "B"));
      assertEquals(1, graph.edgeCount());
    }
  }

  // ==================== Directed Graph ====================

  @Nested
  @DisplayName("Directed Graph")
  class Directed {

    @Test
    @DisplayName("Directed edges are one-way")
    void directedEdge_oneWay() {
      AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>(true);
      graph.addVertex("A");
      graph.addVertex("B");
      graph.addEdge("A", "B");
      assertTrue(graph.hasEdge("A", "B"));
      assertFalse(graph.hasEdge("B", "A"));
      assertTrue(graph.isDirected());
    }
  }

  // ==================== Undirected Graph ====================

  @Nested
  @DisplayName("Undirected Graph")
  class Undirected {

    @Test
    @DisplayName("Undirected edges are two-way")
    void undirectedEdge_twoWay() {
      AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>(false);
      graph.addVertex("A");
      graph.addVertex("B");
      graph.addEdge("A", "B", 3.0);
      assertTrue(graph.hasEdge("A", "B"));
      assertTrue(graph.hasEdge("B", "A"));
      assertEquals(3.0, graph.getEdgeWeight("B", "A"));
      assertFalse(graph.isDirected());
      assertEquals(1, graph.edgeCount());
    }

    @Test
    @DisplayName("Remove undirected edge removes both directions")
    void removeEdge_undirected_removesBoth() {
      AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>(false);
      graph.addVertex("A");
      graph.addVertex("B");
      graph.addEdge("A", "B");
      graph.removeEdge("A", "B");
      assertFalse(graph.hasEdge("A", "B"));
      assertFalse(graph.hasEdge("B", "A"));
    }
  }

  // ==================== Queries ====================

  @Nested
  @DisplayName("Queries")
  class Queries {

    @Test
    @DisplayName("Empty graph has zero counts")
    void emptyGraph_zeroCounts() {
      AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>(true);
      assertEquals(0, graph.vertexCount());
      assertEquals(0, graph.edgeCount());
    }

    @Test
    @DisplayName("Resizes when capacity exceeded")
    void resize_worksCorrectly() {
      AdjacencyMatrixGraph<Integer> graph = new AdjacencyMatrixGraph<>(true);
      for (int i = 0; i < 20; i++) {
        graph.addVertex(i);
      }
      assertEquals(20, graph.vertexCount());
      graph.addEdge(0, 19, 5.0);
      assertTrue(graph.hasEdge(0, 19));
      assertEquals(5.0, graph.getEdgeWeight(0, 19));
    }
  }
}
