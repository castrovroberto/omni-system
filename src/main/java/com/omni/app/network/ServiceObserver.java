package com.omni.app.network;

/**
 * Observer interface for monitoring service state changes.
 *
 * @see ServiceNode
 */
public interface ServiceObserver {

  /**
   * Called when a service's state changes.
   *
   * @param serviceName the name of the service that changed
   * @param event a description of the event
   */
  void onStateChanged(String serviceName, String event);
}
