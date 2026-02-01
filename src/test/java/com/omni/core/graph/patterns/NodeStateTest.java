package com.omni.core.graph.patterns;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("State Pattern Tests")
class NodeStateTest {

  // ==================== State Transitions ====================

  @Nested
  @DisplayName("State Transitions")
  class StateTransitions {

    @Test
    @DisplayName("Booting -> Running on READY")
    void booting_ready_running() {
      NodeState<String> state = NodeStates.booting();
      assertEquals("Booting", state.name());
      NodeState<String> next = state.next(NodeEvent.READY);
      assertEquals("Running", next.name());
    }

    @Test
    @DisplayName("Booting -> Crashing on CRASH")
    void booting_crash_crashing() {
      NodeState<String> state = NodeStates.booting();
      NodeState<String> next = state.next(NodeEvent.CRASH);
      assertEquals("Crashing", next.name());
    }

    @Test
    @DisplayName("Running -> Crashing on CRASH")
    void running_crash_crashing() {
      NodeState<String> state = NodeStates.running();
      assertEquals("Running", state.name());
      NodeState<String> next = state.next(NodeEvent.CRASH);
      assertEquals("Crashing", next.name());
    }

    @Test
    @DisplayName("Running -> Shutdown on SHUTDOWN")
    void running_shutdown() {
      NodeState<String> state = NodeStates.running();
      NodeState<String> next = state.next(NodeEvent.SHUTDOWN);
      assertEquals("Shutdown", next.name());
    }

    @Test
    @DisplayName("Crashing -> Rebooting on RESTART")
    void crashing_restart_rebooting() {
      NodeState<String> state = NodeStates.crashing();
      assertEquals("Crashing", state.name());
      NodeState<String> next = state.next(NodeEvent.RESTART);
      assertEquals("Rebooting", next.name());
    }

    @Test
    @DisplayName("Rebooting -> Booting on START")
    void rebooting_start_booting() {
      NodeState<String> state = NodeStates.rebooting();
      assertEquals("Rebooting", state.name());
      NodeState<String> next = state.next(NodeEvent.START);
      assertEquals("Booting", next.name());
    }

    @Test
    @DisplayName("Rebooting -> Running on READY (fast boot)")
    void rebooting_ready_running() {
      NodeState<String> state = NodeStates.rebooting();
      NodeState<String> next = state.next(NodeEvent.READY);
      assertEquals("Running", next.name());
    }

    @Test
    @DisplayName("Shutdown -> Booting on START")
    void shutdown_start_booting() {
      NodeState<String> state = NodeStates.shutdown();
      assertEquals("Shutdown", state.name());
      NodeState<String> next = state.next(NodeEvent.START);
      assertEquals("Booting", next.name());
    }

    @Test
    @DisplayName("Invalid transitions return same state")
    void invalidTransition_sameState() {
      NodeState<String> running = NodeStates.running();
      assertSame(running, running.next(NodeEvent.START));
      assertSame(running, running.next(NodeEvent.RESTART));

      NodeState<String> crashing = NodeStates.crashing();
      assertSame(crashing, crashing.next(NodeEvent.START));
      assertSame(crashing, crashing.next(NodeEvent.READY));
    }
  }

  // ==================== Capability Flags ====================

  @Nested
  @DisplayName("Capability Flags")
  class Capabilities {

    @Test
    @DisplayName("Running state can accept and send")
    void running_canAcceptAndSend() {
      NodeState<String> state = NodeStates.running();
      assertTrue(state.canAcceptConnections());
      assertTrue(state.canSendRequests());
    }

    @Test
    @DisplayName("Booting state cannot accept or send")
    void booting_cannotAcceptOrSend() {
      NodeState<String> state = NodeStates.booting();
      assertFalse(state.canAcceptConnections());
      assertFalse(state.canSendRequests());
    }

    @Test
    @DisplayName("Crashing state cannot accept or send")
    void crashing_cannotAcceptOrSend() {
      NodeState<String> state = NodeStates.crashing();
      assertFalse(state.canAcceptConnections());
      assertFalse(state.canSendRequests());
    }

    @Test
    @DisplayName("Rebooting state cannot accept or send")
    void rebooting_cannotAcceptOrSend() {
      NodeState<String> state = NodeStates.rebooting();
      assertFalse(state.canAcceptConnections());
      assertFalse(state.canSendRequests());
    }

    @Test
    @DisplayName("Shutdown state cannot accept or send")
    void shutdown_cannotAcceptOrSend() {
      NodeState<String> state = NodeStates.shutdown();
      assertFalse(state.canAcceptConnections());
      assertFalse(state.canSendRequests());
    }
  }

  // ==================== StatefulNode ====================

  @Nested
  @DisplayName("StatefulNode")
  class StatefulNodeTests {

    @Test
    @DisplayName("New node starts in Booting state")
    void newNode_booting() {
      StatefulNode<String> node = new StatefulNode<>("Server1");
      assertEquals("Server1", node.getVertex());
      assertEquals("Booting", node.getStateName());
    }

    @Test
    @DisplayName("Handle event transitions state")
    void handleEvent_transitions() {
      StatefulNode<String> node = new StatefulNode<>("Server1");
      node.handleEvent(NodeEvent.READY);
      assertEquals("Running", node.getStateName());
      assertTrue(node.canAcceptConnections());
    }

    @Test
    @DisplayName("Full lifecycle simulation")
    void fullLifecycle() {
      StatefulNode<String> node = new StatefulNode<>("Server1");
      assertEquals("Booting", node.getStateName());

      node.handleEvent(NodeEvent.READY);
      assertEquals("Running", node.getStateName());

      node.handleEvent(NodeEvent.CRASH);
      assertEquals("Crashing", node.getStateName());

      node.handleEvent(NodeEvent.RESTART);
      assertEquals("Rebooting", node.getStateName());

      node.handleEvent(NodeEvent.READY);
      assertEquals("Running", node.getStateName());

      node.handleEvent(NodeEvent.SHUTDOWN);
      assertEquals("Shutdown", node.getStateName());
    }

    @Test
    @DisplayName("Listener notified on state change")
    void listener_notifiedOnChange() {
      StatefulNode<String> node = new StatefulNode<>("Server1");
      int[] callCount = {0};
      String[] states = new String[2];

      node.addListener(
          (n, oldState, newState) -> {
            callCount[0]++;
            states[0] = oldState.name();
            states[1] = newState.name();
          });

      node.handleEvent(NodeEvent.READY);
      assertEquals(1, callCount[0]);
      assertEquals("Booting", states[0]);
      assertEquals("Running", states[1]);
    }

    @Test
    @DisplayName("Listener not notified when no transition")
    void listener_notCalledWhenNoTransition() {
      StatefulNode<String> node = new StatefulNode<>("Server1");
      int[] callCount = {0};
      node.addListener((n, oldState, newState) -> callCount[0]++);

      // START on Booting does nothing
      node.handleEvent(NodeEvent.START);
      assertEquals(0, callCount[0]);
    }

    @Test
    @DisplayName("Remove listener stops notifications")
    void removeListener_stopsNotifications() {
      StatefulNode<String> node = new StatefulNode<>("Server1");
      int[] callCount = {0};
      StatefulNode.StateChangeListener<String> listener = (n, o, ne) -> callCount[0]++;

      node.addListener(listener);
      node.handleEvent(NodeEvent.READY);
      assertEquals(1, callCount[0]);

      node.removeListener(listener);
      node.handleEvent(NodeEvent.CRASH);
      assertEquals(1, callCount[0]); // Still 1, not 2
    }

    @Test
    @DisplayName("Null vertex throws")
    void nullVertex_throws() {
      assertThrows(IllegalArgumentException.class, () -> new StatefulNode<>(null));
    }

    @Test
    @DisplayName("Null event throws")
    void nullEvent_throws() {
      StatefulNode<String> node = new StatefulNode<>("Server1");
      assertThrows(IllegalArgumentException.class, () -> node.handleEvent(null));
    }

    @Test
    @DisplayName("Custom initial state")
    void customInitialState() {
      StatefulNode<String> node = new StatefulNode<>("Server1", NodeStates.running());
      assertEquals("Running", node.getStateName());
    }

    @Test
    @DisplayName("toString includes vertex and state")
    void toStringFormat() {
      StatefulNode<String> node = new StatefulNode<>("Server1");
      String str = node.toString();
      assertTrue(str.contains("Server1"));
      assertTrue(str.contains("Booting"));
    }
  }
}
