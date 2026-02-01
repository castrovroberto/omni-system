package com.omni.core.graph.patterns;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.graph.AdjacencyListGraph;
import com.omni.core.list.MyList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Mediator Pattern Tests")
class ServiceMediatorTest {

  private AdjacencyListGraph<String> topology;
  private ServiceMediator<String> mediator;

  @BeforeEach
  void setUp() {
    // Create a simple network topology:
    // Database <-> API <-> Frontend
    // |
    // Cache
    topology = new AdjacencyListGraph<>(false);
    topology.addVertex("Database");
    topology.addVertex("API");
    topology.addVertex("Frontend");
    topology.addVertex("Cache");
    topology.addEdge("Database", "API");
    topology.addEdge("API", "Frontend");
    topology.addEdge("API", "Cache");

    mediator = new ServiceMediator<>(topology);
    mediator.register("Database");
    mediator.register("API");
    mediator.register("Frontend");
    mediator.register("Cache");
  }

  // ==================== Registration ====================

  @Nested
  @DisplayName("Registration")
  class Registration {

    @Test
    @DisplayName("Register and check node")
    void registerNode() {
      ServiceMediator<String> m = new ServiceMediator<>(topology);
      assertFalse(m.isRegistered("API"));
      m.register("API");
      assertTrue(m.isRegistered("API"));
    }

    @Test
    @DisplayName("Unregister removes node")
    void unregisterNode() {
      mediator.unregister("Cache");
      assertFalse(mediator.isRegistered("Cache"));
      assertEquals(3, mediator.registeredCount());
    }

    @Test
    @DisplayName("Null node throws on register")
    void nullNode_throws() {
      assertThrows(IllegalArgumentException.class, () -> mediator.register(null));
    }

    @Test
    @DisplayName("Duplicate registration is idempotent")
    void duplicateRegister() {
      mediator.register("API");
      assertEquals(4, mediator.registeredCount());
    }
  }

  // ==================== Message Delivery ====================

  @Nested
  @DisplayName("Message Delivery")
  class Messaging {

    @Test
    @DisplayName("Message delivered to connected node")
    void messageToConnectedNode() {
      boolean sent = mediator.sendMessage("Database", "API", "SELECT * FROM users");
      assertTrue(sent);

      MyList<NetworkMediator.Message<String>> messages = mediator.getMessagesFor("API");
      assertEquals(1, messages.size());
      assertEquals("Database", messages.get(0).sender());
      assertEquals("SELECT * FROM users", messages.get(0).content());
    }

    @Test
    @DisplayName("Message not delivered to unconnected node")
    void messageToUnconnectedNode() {
      // Database is not directly connected to Frontend
      boolean sent = mediator.sendMessage("Database", "Frontend", "Hello");
      assertFalse(sent);
      assertEquals(0, mediator.getMessagesFor("Frontend").size());
    }

    @Test
    @DisplayName("Message not delivered from unregistered sender")
    void messageFromUnregistered() {
      mediator.unregister("Database");
      boolean sent = mediator.sendMessage("Database", "API", "Hello");
      assertFalse(sent);
    }

    @Test
    @DisplayName("Message not delivered to unregistered receiver")
    void messageToUnregistered() {
      mediator.unregister("API");
      boolean sent = mediator.sendMessage("Database", "API", "Hello");
      assertFalse(sent);
    }

    @Test
    @DisplayName("Clear messages removes all pending")
    void clearMessages() {
      mediator.sendMessage("Database", "API", "Message 1");
      mediator.sendMessage("Cache", "API", "Message 2");
      assertEquals(2, mediator.getMessagesFor("API").size());

      mediator.clearMessagesFor("API");
      assertEquals(0, mediator.getMessagesFor("API").size());
    }
  }

  // ==================== Broadcast ====================

  @Nested
  @DisplayName("Broadcast")
  class Broadcast {

    @Test
    @DisplayName("Broadcast reaches all connected registered nodes")
    void broadcastToConnected() {
      // API is connected to Database, Frontend, Cache
      int count = mediator.broadcast("API", "System maintenance in 5 minutes");
      assertEquals(3, count);

      assertEquals(1, mediator.getMessagesFor("Database").size());
      assertEquals(1, mediator.getMessagesFor("Frontend").size());
      assertEquals(1, mediator.getMessagesFor("Cache").size());
    }

    @Test
    @DisplayName("Broadcast does not include sender")
    void broadcastExcludesSender() {
      mediator.broadcast("API", "Hello");
      assertEquals(0, mediator.getMessagesFor("API").size());
    }

    @Test
    @DisplayName("Broadcast from unregistered returns 0")
    void broadcastFromUnregistered() {
      mediator.unregister("API");
      int count = mediator.broadcast("API", "Hello");
      assertEquals(0, count);
    }

    @Test
    @DisplayName("Broadcast skips unregistered receivers")
    void broadcastSkipsUnregistered() {
      mediator.unregister("Cache");
      int count = mediator.broadcast("API", "Hello");
      assertEquals(2, count); // Database and Frontend only
    }
  }

  // ==================== Directed Graph ====================

  @Nested
  @DisplayName("Directed Graph")
  class DirectedTopology {

    @Test
    @DisplayName("Directed graph respects edge direction")
    void directedEdges() {
      AdjacencyListGraph<String> directedGraph = new AdjacencyListGraph<>(true);
      directedGraph.addVertex("Producer");
      directedGraph.addVertex("Consumer");
      directedGraph.addEdge("Producer", "Consumer"); // One-way

      ServiceMediator<String> m = new ServiceMediator<>(directedGraph);
      m.register("Producer");
      m.register("Consumer");

      assertTrue(m.sendMessage("Producer", "Consumer", "Data"));
      assertFalse(m.sendMessage("Consumer", "Producer", "ACK")); // No edge this way
    }
  }

  // ==================== Edge Cases ====================

  @Nested
  @DisplayName("Edge Cases")
  class EdgeCases {

    @Test
    @DisplayName("Null topology throws")
    void nullTopology_throws() {
      assertThrows(IllegalArgumentException.class, () -> new ServiceMediator<>(null));
    }

    @Test
    @DisplayName("Messages for unregistered node returns empty list")
    void messagesForUnregistered() {
      MyList<NetworkMediator.Message<String>> messages = mediator.getMessagesFor("Unknown");
      assertNotNull(messages);
      assertTrue(messages.isEmpty());
    }

    @Test
    @DisplayName("Node not in topology cannot communicate")
    void nodeNotInTopology() {
      mediator.register("Orphan");
      boolean sent = mediator.sendMessage("Orphan", "API", "Hello");
      assertFalse(sent);
    }
  }
}
