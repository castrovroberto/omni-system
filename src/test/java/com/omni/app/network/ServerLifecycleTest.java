package com.omni.app.network;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ServerLifecycle Tests")
class ServerLifecycleTest {

  private ServerLifecycle lifecycle;

  @BeforeEach
  void setUp() {
    lifecycle = new ServerLifecycle();
    lifecycle.addServer("web");
    lifecycle.addServer("api");
    lifecycle.addServer("db");
  }

  @Test
  @DisplayName("Add and get server")
  void addServer_getServer() {
    assertTrue(lifecycle.getServer("web").isPresent());
    assertEquals("web", lifecycle.getServer("web").get().getName());
    assertTrue(lifecycle.getServer("unknown").isEmpty());
  }

  @Test
  @DisplayName("Boot all servers")
  void bootAll_allBooting() {
    lifecycle.bootAll();
    assertEquals("BOOTING", lifecycle.getServer("web").get().getStateName());
    assertEquals("BOOTING", lifecycle.getServer("api").get().getStateName());
    assertEquals("BOOTING", lifecycle.getServer("db").get().getStateName());
  }

  @Test
  @DisplayName("Run all servers")
  void runAll_afterBoot_allRunning() {
    lifecycle.bootAll();
    lifecycle.runAll();
    assertEquals("RUNNING", lifecycle.getServer("web").get().getStateName());
  }

  @Test
  @DisplayName("Stop all servers")
  void stopAll_allStopped() {
    lifecycle.bootAll();
    lifecycle.runAll();
    lifecycle.stopAll();
    assertEquals("STOPPED", lifecycle.getServer("web").get().getStateName());
  }

  @Test
  @DisplayName("Size and server names")
  void sizeAndNames() {
    assertEquals(3, lifecycle.size());
    assertEquals(3, lifecycle.getServerNames().size());
  }
}
