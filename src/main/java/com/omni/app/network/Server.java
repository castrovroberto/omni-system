package com.omni.app.network;

import com.omni.app.network.state.ServerContext;

/**
 * A server that combines the Observer pattern ({@link ServiceNode}) with the State pattern ({@link
 * ServerContext}).
 *
 * <p>State transitions automatically notify subscribed observers.
 */
public class Server {

  private final ServiceNode serviceNode;
  private final ServerContext context;

  /**
   * Constructs a server with the given name.
   *
   * @param name the server name
   */
  public Server(String name) {
    this.serviceNode = new ServiceNode(name);
    this.context = new ServerContext();
  }

  /** Boots the server. */
  public void boot() {
    context.boot();
    serviceNode.notifyObservers("STATE_CHANGED:" + context.getStateName());
  }

  /** Runs the server. */
  public void run() {
    context.run();
    serviceNode.notifyObservers("STATE_CHANGED:" + context.getStateName());
  }

  /** Crashes the server. */
  public void crash() {
    context.crash();
    serviceNode.notifyObservers("STATE_CHANGED:" + context.getStateName());
  }

  /** Stops the server. */
  public void stop() {
    context.stop();
    serviceNode.notifyObservers("STATE_CHANGED:" + context.getStateName());
  }

  /**
   * Returns the current state name.
   *
   * @return the state name
   */
  public String getStateName() {
    return context.getStateName();
  }

  /**
   * Returns the service node for observer management.
   *
   * @return the service node
   */
  public ServiceNode getServiceNode() {
    return serviceNode;
  }

  /**
   * Returns the server name.
   *
   * @return the name
   */
  public String getName() {
    return serviceNode.getName();
  }
}
