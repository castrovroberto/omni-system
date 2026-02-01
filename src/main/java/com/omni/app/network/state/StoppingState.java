package com.omni.app.network.state;

/** Server state representing a stopped server. Can only transition to Booting. */
public class StoppingState implements ServerState {

  @Override
  public void boot(ServerContext context) {
    context.setState(new BootingState());
  }

  @Override
  public void run(ServerContext context) {
    // Cannot run from stopped - no-op
  }

  @Override
  public void crash(ServerContext context) {
    // Cannot crash from stopped - no-op
  }

  @Override
  public void stop(ServerContext context) {
    // Already stopped - no-op
  }

  @Override
  public String getStateName() {
    return "STOPPED";
  }
}
