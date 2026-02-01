package com.omni.app.network;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.graph.CycleDetectedException;
import com.omni.core.list.MyList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DependencyResolver Tests")
class DependencyResolverTest {

  @Test
  @DisplayName("Resolves linear dependency chain")
  void resolve_linearChain() throws CycleDetectedException {
    DependencyResolver resolver = new DependencyResolver();
    resolver.addDependency("app", "api");
    resolver.addDependency("api", "db");

    MyList<String> order = resolver.resolveStartupOrder();
    assertEquals(3, order.size());

    int dbIdx = order.indexOf("db");
    int apiIdx = order.indexOf("api");
    int appIdx = order.indexOf("app");
    assertTrue(dbIdx < apiIdx, "db should start before api");
    assertTrue(apiIdx < appIdx, "api should start before app");
  }

  @Test
  @DisplayName("Resolves diamond dependency")
  void resolve_diamondDependency() throws CycleDetectedException {
    DependencyResolver resolver = new DependencyResolver();
    resolver.addDependency("app", "auth");
    resolver.addDependency("app", "cache");
    resolver.addDependency("auth", "db");
    resolver.addDependency("cache", "db");

    MyList<String> order = resolver.resolveStartupOrder();
    assertEquals(4, order.size());

    int dbIdx = order.indexOf("db");
    int appIdx = order.indexOf("app");
    assertTrue(dbIdx < appIdx, "db should start before app");
  }

  @Test
  @DisplayName("Detects circular dependency")
  void resolve_circularDependency_throws() {
    DependencyResolver resolver = new DependencyResolver();
    resolver.addDependency("A", "B");
    resolver.addDependency("B", "C");
    resolver.addDependency("C", "A");

    assertThrows(CycleDetectedException.class, resolver::resolveStartupOrder);
  }

  @Test
  @DisplayName("Single service with no dependencies")
  void resolve_singleService() throws CycleDetectedException {
    DependencyResolver resolver = new DependencyResolver();
    resolver.addService("standalone");

    MyList<String> order = resolver.resolveStartupOrder();
    assertEquals(1, order.size());
    assertEquals("standalone", order.get(0));
  }
}
