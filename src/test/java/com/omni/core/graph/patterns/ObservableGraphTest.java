package com.omni.core.graph.patterns;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.graph.AdjacencyListGraph;
import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Observer Pattern Tests")
class ObservableGraphTest {

  private AdjacencyListGraph<String> baseGraph;
  private ObservableGraph<String> graph;
  private TestObserver observer;

  @BeforeEach
  void setUp() {
    baseGraph = new AdjacencyListGraph<>(true);
    graph = new ObservableGraph<>(baseGraph);
    observer = new TestObserver();
    graph.addObserver(observer);
  }

  // ==================== Vertex Notifications ====================

  @Nested
  @DisplayName("Vertex Notifications")
  class VertexNotifications {

    @Test
    @DisplayName("Observer notified on vertex add")
    void onVertexAdded() {
      graph.addVertex("A");
      assertEquals(1, observer.verticesAdded.size());
      assertEquals("A", observer.verticesAdded.get(0));
    }

    @Test
    @DisplayName("Observer not notified on duplicate vertex add")
    void onDuplicateVertexAdd_noNotification() {
      graph.addVertex("A");
      graph.addVertex("A");
      assertEquals(1, observer.verticesAdded.size());
    }

    @Test
    @DisplayName("Observer notified on vertex remove")
    void onVertexRemoved() {
      graph.addVertex("A");
      graph.removeVertex("A");
      assertEquals(1, observer.verticesRemoved.size());
      assertEquals("A", observer.verticesRemoved.get(0));
    }

    @Test
    @DisplayName("Observer not notified when removing non-existent vertex")
    void onRemoveNonExistent_noNotification() {
      graph.removeVertex("X");
      assertEquals(0, observer.verticesRemoved.size());
    }
  }

  // ==================== Edge Notifications ====================

  @Nested
  @DisplayName("Edge Notifications")
  class EdgeNotifications {

    @BeforeEach
    void addVertices() {
      graph.addVertex("A");
      graph.addVertex("B");
      observer.clear();
    }

    @Test
    @DisplayName("Observer notified on edge add")
    void onEdgeAdded() {
      graph.addEdge("A", "B", 5.0);
      assertEquals(1, observer.edgesAdded.size());
      EdgeEvent e = observer.edgesAdded.get(0);
      assertEquals("A", e.source);
      assertEquals("B", e.target);
      assertEquals(5.0, e.weight);
    }

    @Test
    @DisplayName("Observer notified on edge remove")
    void onEdgeRemoved() {
      graph.addEdge("A", "B");
      observer.clear();
      graph.removeEdge("A", "B");
      assertEquals(1, observer.edgesRemoved.size());
      assertEquals("A", observer.edgesRemoved.get(0).source);
    }

    @Test
    @DisplayName("Observer not notified when removing non-existent edge")
    void onRemoveNonExistent_noNotification() {
      graph.removeEdge("A", "B");
      assertEquals(0, observer.edgesRemoved.size());
    }
  }

  // ==================== Multiple Observers ====================

  @Nested
  @DisplayName("Multiple Observers")
  class MultipleObservers {

    @Test
    @DisplayName("All observers receive notifications")
    void allObserversNotified() {
      TestObserver observer2 = new TestObserver();
      graph.addObserver(observer2);

      graph.addVertex("A");
      assertEquals(1, observer.verticesAdded.size());
      assertEquals(1, observer2.verticesAdded.size());
    }

    @Test
    @DisplayName("Same observer not added twice")
    void sameObserverNotDuplicated() {
      graph.addObserver(observer);
      graph.addObserver(observer);
      assertEquals(1, graph.observerCount());
    }

    @Test
    @DisplayName("Remove observer stops notifications")
    void removeObserver() {
      graph.removeObserver(observer);
      graph.addVertex("A");
      assertEquals(0, observer.verticesAdded.size());
    }
  }

  // ==================== State Change Notifications ====================

  @Nested
  @DisplayName("State Change Notifications")
  class StateChanges {

    @Test
    @DisplayName("Notify state change propagates to observers")
    void notifyStateChange() {
      graph.addVertex("Server1");
      NodeState<String> oldState = NodeStates.booting();
      NodeState<String> newState = NodeStates.running();

      graph.notifyStateChange("Server1", oldState, newState);

      assertEquals(1, observer.stateChanges.size());
      StateChangeEvent e = observer.stateChanges.get(0);
      assertEquals("Server1", e.vertex);
      assertEquals("Booting", e.oldState.name());
      assertEquals("Running", e.newState.name());
    }
  }

  // ==================== Graph Operations ====================

  @Nested
  @DisplayName("Graph Operations")
  class GraphOperations {

    @Test
    @DisplayName("Delegate operations work correctly")
    void delegateOperations() {
      graph.addVertex("A");
      graph.addVertex("B");
      graph.addEdge("A", "B", 3.0);

      assertTrue(graph.hasVertex("A"));
      assertTrue(graph.hasEdge("A", "B"));
      assertEquals(3.0, graph.getEdgeWeight("A", "B"));
      assertEquals(2, graph.vertexCount());
      assertEquals(1, graph.edgeCount());
      assertTrue(graph.isDirected());
    }

    @Test
    @DisplayName("Null delegate throws")
    void nullDelegate_throws() {
      assertThrows(IllegalArgumentException.class, () -> new ObservableGraph<>(null));
    }
  }

  // ==================== Test Helper Classes ====================

  private static class TestObserver implements GraphObserver<String> {
    MyList<String> verticesAdded = new MyArrayList<>();
    MyList<String> verticesRemoved = new MyArrayList<>();
    MyList<EdgeEvent> edgesAdded = new MyArrayList<>();
    MyList<EdgeEvent> edgesRemoved = new MyArrayList<>();
    MyList<StateChangeEvent> stateChanges = new MyArrayList<>();

    @Override
    public void onVertexAdded(String vertex) {
      verticesAdded.add(vertex);
    }

    @Override
    public void onVertexRemoved(String vertex) {
      verticesRemoved.add(vertex);
    }

    @Override
    public void onEdgeAdded(String source, String target, double weight) {
      edgesAdded.add(new EdgeEvent(source, target, weight));
    }

    @Override
    public void onEdgeRemoved(String source, String target) {
      edgesRemoved.add(new EdgeEvent(source, target, 0));
    }

    @Override
    public void onVertexStateChanged(
        String vertex, NodeState<String> oldState, NodeState<String> newState) {
      stateChanges.add(new StateChangeEvent(vertex, oldState, newState));
    }

    void clear() {
      verticesAdded = new MyArrayList<>();
      verticesRemoved = new MyArrayList<>();
      edgesAdded = new MyArrayList<>();
      edgesRemoved = new MyArrayList<>();
      stateChanges = new MyArrayList<>();
    }
  }

  private record EdgeEvent(String source, String target, double weight) {}

  private record StateChangeEvent(
      String vertex, NodeState<String> oldState, NodeState<String> newState) {}
}
