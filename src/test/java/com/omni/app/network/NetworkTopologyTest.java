package com.omni.app.network;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NetworkTopology Tests")
class NetworkTopologyTest {

  private NetworkTopology topology;

  @BeforeEach
  void setUp() {
    topology = new NetworkTopology();
    topology.addServer("web");
    topology.addServer("api");
    topology.addServer("db");
  }

  @Test
  @DisplayName("Add servers and check existence")
  void addServer_hasServer() {
    assertTrue(topology.hasServer("web"));
    assertTrue(topology.hasServer("api"));
    assertFalse(topology.hasServer("cache"));
  }

  @Test
  @DisplayName("Connect servers and check connection")
  void connect_areConnected() {
    topology.connect("web", "api", 5.0);
    assertTrue(topology.areConnected("web", "api"));
    assertTrue(topology.areConnected("api", "web")); // undirected
  }

  @Test
  @DisplayName("Get all servers")
  void getServers_returnsAll() {
    assertEquals(3, topology.getServers().size());
  }

  @Test
  @DisplayName("Get graph returns the underlying graph")
  void getGraph_returnsGraph() {
    assertNotNull(topology.getGraph());
    assertFalse(topology.getGraph().isDirected());
  }
}
