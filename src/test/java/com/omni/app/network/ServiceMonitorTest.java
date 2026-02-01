package com.omni.app.network;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ServiceMonitor Tests")
class ServiceMonitorTest {

  private NetworkTopology topology;
  private ServiceMonitor monitor;

  @BeforeEach
  void setUp() {
    topology = new NetworkTopology();
    topology.addServer("web");
    topology.addServer("api");
    topology.addServer("db");
    topology.addServer("cache");
    topology.connect("web", "api");
    topology.connect("api", "db");
    topology.connect("api", "cache");
    monitor = new ServiceMonitor(topology);
  }

  @Test
  @DisplayName("Find cascading failures from central node")
  void findCascadingFailures_centralNode() {
    MyList<String> affected = monitor.findCascadingFailures("api");
    // api connects to web, db, cache - all should be affected
    assertEquals(3, affected.size());
    assertFalse(affected.contains("api"));
    assertTrue(affected.contains("web"));
    assertTrue(affected.contains("db"));
    assertTrue(affected.contains("cache"));
  }

  @Test
  @DisplayName("Find cascading failures from leaf node")
  void findCascadingFailures_leafNode() {
    MyList<String> affected = monitor.findCascadingFailures("db");
    // db connects to api, which connects to web and cache
    assertTrue(affected.size() >= 1);
    assertFalse(affected.contains("db"));
  }

  @Test
  @DisplayName("Find cascading failures from non-existent server")
  void findCascadingFailures_nonExistent() {
    MyList<String> affected = monitor.findCascadingFailures("unknown");
    assertTrue(affected.isEmpty());
  }

  @Test
  @DisplayName("Observer receives state change events")
  void onStateChanged_logsEvent() {
    monitor.onStateChanged("web", "CRASHED");
    MyList<String> log = monitor.getEventLog();
    assertEquals(1, log.size());
    assertEquals("web: CRASHED", log.get(0));
  }

  @Test
  @DisplayName("Clear log empties event log")
  void clearLog_emptiesLog() {
    monitor.onStateChanged("web", "CRASHED");
    monitor.clearLog();
    assertTrue(monitor.getEventLog().isEmpty());
  }

  @Test
  @DisplayName("Isolated server has no cascading failures")
  void findCascadingFailures_isolatedServer() {
    topology.addServer("isolated");
    MyList<String> affected = monitor.findCascadingFailures("isolated");
    assertTrue(affected.isEmpty());
  }
}
