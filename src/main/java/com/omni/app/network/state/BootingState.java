package com.omni.app.network.state;

/** Server state representing the booting phase. Can transition to Running or Crashed. */
public class BootingState implements ServerState {

  @Override
  public void boot(ServerContext context) {
    // Already booting - no-op
  }

  @Override
  public void run(ServerContext context) {
    context.setState(new RunningState());
  }

  @Override
  public void crash(ServerContext context) {
    context.setState(new CrashedState());
  }

  @Override
  public void stop(ServerContext context) {
    context.setState(new StoppingState());
  }

  @Override
  public String getStateName() {
    return "BOOTING";
  }
}
