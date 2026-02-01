package com.omni.app.network.state;

/** Server state representing the running phase. Can transition to Crashed or Stopping. */
public class RunningState implements ServerState {

  @Override
  public void boot(ServerContext context) {
    // Already running - no-op
  }

  @Override
  public void run(ServerContext context) {
    // Already running - no-op
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
    return "RUNNING";
  }
}
