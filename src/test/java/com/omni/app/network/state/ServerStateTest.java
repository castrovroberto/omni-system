package com.omni.app.network.state;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Server State Tests")
class ServerStateTest {

  @Test
  @DisplayName("Initial state is STOPPED")
  void initialState_stopped() {
    ServerContext ctx = new ServerContext();
    assertEquals("STOPPED", ctx.getStateName());
  }

  @Test
  @DisplayName("STOPPED -> boot -> BOOTING")
  void stopped_boot_booting() {
    ServerContext ctx = new ServerContext();
    ctx.boot();
    assertEquals("BOOTING", ctx.getStateName());
  }

  @Test
  @DisplayName("BOOTING -> run -> RUNNING")
  void booting_run_running() {
    ServerContext ctx = new ServerContext();
    ctx.boot();
    ctx.run();
    assertEquals("RUNNING", ctx.getStateName());
  }

  @Test
  @DisplayName("RUNNING -> crash -> CRASHED")
  void running_crash_crashed() {
    ServerContext ctx = new ServerContext();
    ctx.boot();
    ctx.run();
    ctx.crash();
    assertEquals("CRASHED", ctx.getStateName());
  }

  @Test
  @DisplayName("CRASHED -> boot -> BOOTING (restart)")
  void crashed_boot_booting() {
    ServerContext ctx = new ServerContext();
    ctx.boot();
    ctx.run();
    ctx.crash();
    ctx.boot();
    assertEquals("BOOTING", ctx.getStateName());
  }

  @Test
  @DisplayName("RUNNING -> stop -> STOPPED")
  void running_stop_stopped() {
    ServerContext ctx = new ServerContext();
    ctx.boot();
    ctx.run();
    ctx.stop();
    assertEquals("STOPPED", ctx.getStateName());
  }

  @Test
  @DisplayName("No-op transitions stay in same state")
  void noOp_staysSame() {
    ServerContext ctx = new ServerContext();
    ctx.run(); // stopped -> run is no-op
    assertEquals("STOPPED", ctx.getStateName());

    ctx.boot();
    ctx.boot(); // booting -> boot is no-op
    assertEquals("BOOTING", ctx.getStateName());

    ctx.run();
    ctx.run(); // running -> run is no-op
    assertEquals("RUNNING", ctx.getStateName());
  }

  @Test
  @DisplayName("Full lifecycle: STOPPED -> BOOTING -> RUNNING -> STOPPED")
  void fullLifecycle() {
    ServerContext ctx = new ServerContext();
    ctx.boot();
    assertEquals("BOOTING", ctx.getStateName());
    ctx.run();
    assertEquals("RUNNING", ctx.getStateName());
    ctx.stop();
    assertEquals("STOPPED", ctx.getStateName());
  }
}
