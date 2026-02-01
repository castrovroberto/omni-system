package com.omni.app.network;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;

/**
 * A service node that supports the Observer pattern for state change notifications.
 *
 * <p>Observers can subscribe to receive notifications when the service state changes.
 */
public class ServiceNode {

  private final String name;
  private final MyList<ServiceObserver> observers;

  /**
   * Constructs a service node with the given name.
   *
   * @param name the service name
   */
  public ServiceNode(String name) {
    this.name = name;
    this.observers = new MyArrayList<>();
  }

  /**
   * Subscribes an observer to state change notifications.
   *
   * @param observer the observer to subscribe
   */
  public void subscribe(ServiceObserver observer) {
    observers.add(observer);
  }

  /**
   * Unsubscribes an observer from notifications.
   *
   * @param observer the observer to unsubscribe
   */
  public void unsubscribe(ServiceObserver observer) {
    int idx = observers.indexOf(observer);
    if (idx >= 0) {
      observers.remove(idx);
    }
  }

  /**
   * Notifies all observers of an event.
   *
   * @param event the event description
   */
  public void notifyObservers(String event) {
    for (int i = 0; i < observers.size(); i++) {
      observers.get(i).onStateChanged(name, event);
    }
  }

  /**
   * Returns the service name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the number of subscribed observers.
   *
   * @return observer count
   */
  public int getObserverCount() {
    return observers.size();
  }
}
