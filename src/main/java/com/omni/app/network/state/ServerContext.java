package com.omni.app.network.state;

/**
 * Context class for the server State pattern. Holds the current state and delegates lifecycle
 * calls.
 */
public class ServerContext {

  private ServerState currentState;

  /** Constructs a server context in the stopped state. */
  public ServerContext() {
    this.currentState = new StoppingState();
  }

  /**
   * Sets the current server state.
   *
   * @param state the new state
   */
  public void setState(ServerState state) {
    this.currentState = state;
  }

  /**
   * Returns the current state.
   *
   * @return the current state
   */
  public ServerState getState() {
    return currentState;
  }

  /**
   * Returns the name of the current state.
   *
   * @return the state name
   */
  public String getStateName() {
    return currentState.getStateName();
  }

  /** Delegates boot to current state. */
  public void boot() {
    currentState.boot(this);
  }

  /** Delegates run to current state. */
  public void run() {
    currentState.run(this);
  }

  /** Delegates crash to current state. */
  public void crash() {
    currentState.crash(this);
  }

  /** Delegates stop to current state. */
  public void stop() {
    currentState.stop(this);
  }
}
