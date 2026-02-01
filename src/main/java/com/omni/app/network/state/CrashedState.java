package com.omni.app.network.state;

/** Server state representing a crashed server. Can only transition to Booting (restart). */
public class CrashedState implements ServerState {

  @Override
  public void boot(ServerContext context) {
    context.setState(new BootingState());
  }

  @Override
  public void run(ServerContext context) {
    // Cannot run from crashed - no-op
  }

  @Override
  public void crash(ServerContext context) {
    // Already crashed - no-op
  }

  @Override
  public void stop(ServerContext context) {
    context.setState(new StoppingState());
  }

  @Override
  public String getStateName() {
    return "CRASHED";
  }
}
