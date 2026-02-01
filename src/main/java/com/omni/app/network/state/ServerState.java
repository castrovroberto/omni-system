package com.omni.app.network.state;

/**
 * Interface representing a server state in the State pattern.
 *
 * <p>Each state defines how the server responds to lifecycle transitions.
 */
public interface ServerState {

  /**
   * Handle boot transition.
   *
   * @param context the server context
   */
  void boot(ServerContext context);

  /**
   * Handle run transition.
   *
   * @param context the server context
   */
  void run(ServerContext context);

  /**
   * Handle crash transition.
   *
   * @param context the server context
   */
  void crash(ServerContext context);

  /**
   * Handle stop transition.
   *
   * @param context the server context
   */
  void stop(ServerContext context);

  /**
   * Returns the name of this state.
   *
   * @return the state name
   */
  String getStateName();
}
