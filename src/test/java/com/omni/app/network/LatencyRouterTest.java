package com.omni.app.network;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyList;
import com.omni.core.map.MyHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LatencyRouter Tests")
class LatencyRouterTest {

  private LatencyRouter router;

  @BeforeEach
  void setUp() {
    NetworkTopology topology = new NetworkTopology();
    topology.addServer("A");
    topology.addServer("B");
    topology.addServer("C");
    topology.addServer("D");
    topology.connect("A", "B", 4.0);
    topology.connect("A", "C", 2.0);
    topology.connect("C", "B", 1.0);
    topology.connect("B", "D", 5.0);
    router = new LatencyRouter(topology);
  }

  @Test
  @DisplayName("Find shortest path")
  void findShortestPath_returnsOptimalPath() {
    MyList<String> path = router.findShortestPath("A", "D");
    assertFalse(path.isEmpty());
    assertEquals("A", path.get(0));
    assertEquals("D", path.get(path.size() - 1));
  }

  @Test
  @DisplayName("Find shortest distance")
  void findShortestDistance_returnsOptimalDistance() {
    double dist = router.findShortestDistance("A", "D");
    assertEquals(8.0, dist); // A->C(2) + C->B(1) + B->D(5) = 8
  }

  @Test
  @DisplayName("Find all distances from source")
  void findAllDistances_returnsAllDistances() {
    MyHashMap<String, Double> distances = router.findAllDistances("A");
    assertEquals(0.0, distances.get("A").orElse(-1.0));
    assertEquals(3.0, distances.get("B").orElse(-1.0));
    assertEquals(2.0, distances.get("C").orElse(-1.0));
    assertEquals(8.0, distances.get("D").orElse(-1.0));
  }

  @Test
  @DisplayName("Unreachable server returns infinity")
  void findShortestDistance_unreachable_infinity() {
    NetworkTopology t = new NetworkTopology();
    t.addServer("X");
    t.addServer("Y");
    LatencyRouter r = new LatencyRouter(t);
    assertEquals(Double.POSITIVE_INFINITY, r.findShortestDistance("X", "Y"));
  }
}
