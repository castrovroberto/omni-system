package com.omni.app.network;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ServiceNode Tests")
class ServiceNodeTest {

  @Test
  @DisplayName("Subscribe and notify observers")
  void subscribe_notify_observersReceiveEvents() {
    ServiceNode node = new ServiceNode("web-server");
    MyList<String> events = new MyArrayList<>();
    ServiceObserver observer = (name, event) -> events.add(name + ":" + event);

    node.subscribe(observer);
    node.notifyObservers("STARTED");

    assertEquals(1, events.size());
    assertEquals("web-server:STARTED", events.get(0));
  }

  @Test
  @DisplayName("Multiple observers receive notifications")
  void multipleObservers_allNotified() {
    ServiceNode node = new ServiceNode("db");
    MyList<String> log1 = new MyArrayList<>();
    MyList<String> log2 = new MyArrayList<>();

    node.subscribe((name, event) -> log1.add(event));
    node.subscribe((name, event) -> log2.add(event));
    node.notifyObservers("CRASH");

    assertEquals(1, log1.size());
    assertEquals(1, log2.size());
  }

  @Test
  @DisplayName("Unsubscribe stops notifications")
  void unsubscribe_noMoreEvents() {
    ServiceNode node = new ServiceNode("api");
    MyList<String> events = new MyArrayList<>();
    ServiceObserver observer = (name, event) -> events.add(event);

    node.subscribe(observer);
    node.notifyObservers("event1");
    node.unsubscribe(observer);
    node.notifyObservers("event2");

    assertEquals(1, events.size());
  }

  @Test
  @DisplayName("Observer count tracks subscriptions")
  void observerCount_tracksSubscriptions() {
    ServiceNode node = new ServiceNode("cache");
    assertEquals(0, node.getObserverCount());
    ServiceObserver obs = (name, event) -> {};
    node.subscribe(obs);
    assertEquals(1, node.getObserverCount());
    node.unsubscribe(obs);
    assertEquals(0, node.getObserverCount());
  }

  @Test
  @DisplayName("getName returns service name")
  void getName_returnsName() {
    ServiceNode node = new ServiceNode("my-service");
    assertEquals("my-service", node.getName());
  }
}
