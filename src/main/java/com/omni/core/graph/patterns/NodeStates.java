package com.omni.core.graph.patterns;

/**
 * Concrete state implementations for server lifecycle (State pattern).
 *
 * <p>State transitions:
 *
 * <pre>
 *   [INITIAL] --START--> Booting --READY--> Running
 *                                             |
 *                                           CRASH
 *                                             v
 *   Shutdown <--SHUTDOWN-- Running <--READY-- Crashing --RESTART--> Rebooting
 *                                                                      |
 *                                                                    START
 *                                                                      v
 *                                                                   Booting
 * </pre>
 */
public final class NodeStates {

  private NodeStates() {
    // Utility class
  }

  /** Returns the initial Booting state. */
  @SuppressWarnings("unchecked")
  public static <V> NodeState<V> booting() {
    return (NodeState<V>) Booting.INSTANCE;
  }

  /** Returns the Running state. */
  @SuppressWarnings("unchecked")
  public static <V> NodeState<V> running() {
    return (NodeState<V>) Running.INSTANCE;
  }

  /** Returns the Crashing state. */
  @SuppressWarnings("unchecked")
  public static <V> NodeState<V> crashing() {
    return (NodeState<V>) Crashing.INSTANCE;
  }

  /** Returns the Rebooting state. */
  @SuppressWarnings("unchecked")
  public static <V> NodeState<V> rebooting() {
    return (NodeState<V>) Rebooting.INSTANCE;
  }

  /** Returns the Shutdown state. */
  @SuppressWarnings("unchecked")
  public static <V> NodeState<V> shutdown() {
    return (NodeState<V>) Shutdown.INSTANCE;
  }

  // ==================== State Implementations ====================

  /** Server is starting up and initializing. */
  private static final class Booting implements NodeState<Object> {
    static final Booting INSTANCE = new Booting();

    @Override
    public String name() {
      return "Booting";
    }

    @Override
    public NodeState<Object> next(NodeEvent event) {
      return switch (event) {
        case READY -> Running.INSTANCE;
        case CRASH -> Crashing.INSTANCE;
        case SHUTDOWN -> Shutdown.INSTANCE;
        default -> this;
      };
    }

    @Override
    public boolean canAcceptConnections() {
      return false;
    }

    @Override
    public boolean canSendRequests() {
      return false;
    }
  }

  /** Server is fully operational. */
  private static final class Running implements NodeState<Object> {
    static final Running INSTANCE = new Running();

    @Override
    public String name() {
      return "Running";
    }

    @Override
    public NodeState<Object> next(NodeEvent event) {
      return switch (event) {
        case CRASH -> Crashing.INSTANCE;
        case SHUTDOWN -> Shutdown.INSTANCE;
        default -> this;
      };
    }

    @Override
    public boolean canAcceptConnections() {
      return true;
    }

    @Override
    public boolean canSendRequests() {
      return true;
    }
  }

  /** Server has crashed and is not operational. */
  private static final class Crashing implements NodeState<Object> {
    static final Crashing INSTANCE = new Crashing();

    @Override
    public String name() {
      return "Crashing";
    }

    @Override
    public NodeState<Object> next(NodeEvent event) {
      return switch (event) {
        case RESTART -> Rebooting.INSTANCE;
        case SHUTDOWN -> Shutdown.INSTANCE;
        default -> this;
      };
    }

    @Override
    public boolean canAcceptConnections() {
      return false;
    }

    @Override
    public boolean canSendRequests() {
      return false;
    }
  }

  /** Server is rebooting after a crash. */
  private static final class Rebooting implements NodeState<Object> {
    static final Rebooting INSTANCE = new Rebooting();

    @Override
    public String name() {
      return "Rebooting";
    }

    @Override
    public NodeState<Object> next(NodeEvent event) {
      return switch (event) {
        case START -> Booting.INSTANCE;
        case READY -> Running.INSTANCE;
        case SHUTDOWN -> Shutdown.INSTANCE;
        default -> this;
      };
    }

    @Override
    public boolean canAcceptConnections() {
      return false;
    }

    @Override
    public boolean canSendRequests() {
      return false;
    }
  }

  /** Server has been shut down. */
  private static final class Shutdown implements NodeState<Object> {
    static final Shutdown INSTANCE = new Shutdown();

    @Override
    public String name() {
      return "Shutdown";
    }

    @Override
    public NodeState<Object> next(NodeEvent event) {
      return switch (event) {
        case START -> Booting.INSTANCE;
        default -> this;
      };
    }

    @Override
    public boolean canAcceptConnections() {
      return false;
    }

    @Override
    public boolean canSendRequests() {
      return false;
    }
  }
}
