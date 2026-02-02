# State Pattern

> Allow an object to alter its behavior when its internal state changes.

## Problem
`Server` has complex transitions (booting, running, stopping, crashed). Using if/else or switch leads to unmaintainable code.

## Solution

```
Server (Context)
    │
    └─ state: ServerState
              │
              ▼
       ServerState (interface)
           △
      ┌────┼────┬────┐
      │    │    │    │
  Booting Running Stopping Crashed
```

## Implementation

```java
public interface ServerState {
    void start(Server server);
    void stop(Server server);
    void crash(Server server);
}

public class BootingState implements ServerState {
    @Override
    public void start(Server server) {
        // Boot sequence complete
        server.setState(new RunningState());
    }
    
    @Override
    public void stop(Server server) {
        throw new IllegalStateException("Cannot stop while booting");
    }
    
    @Override
    public void crash(Server server) {
        server.setState(new CrashedState());
    }
}

public class RunningState implements ServerState {
    @Override
    public void start(Server server) {
        // Already running - no-op
    }
    
    @Override
    public void stop(Server server) {
        server.setState(new StoppingState());
    }
    
    @Override
    public void crash(Server server) {
        server.setState(new CrashedState());
    }
}
```

### Context (Server)

```java
public class Server {
    private ServerState state = new BootingState();
    
    public void setState(ServerState state) {
        this.state = state;
        notifyObservers();
    }
    
    public void start() { state.start(this); }
    public void stop()  { state.stop(this); }
    public void crash() { state.crash(this); }
}
```

## State Transitions

```
         ┌────────────────────────────┐
         │                            ▼
    BootingState ──start()──▶ RunningState
         │                        │   │
         │ crash()                │   │ stop()
         ▼                        │   ▼
    CrashedState ◀───crash()──────┘ StoppingState
         │                              │
         └───start()───▶ BootingState ◀─┘
```

## Benefits
- **No conditionals**: State logic in dedicated classes
- **Open/Closed**: Add states without changing Server
- **Single Responsibility**: Each state handles its transitions

## Used In
- [ServerState](../../../../src/main/java/com/omni/app/network/state/ServerState.java)
- [BootingState](../../../../src/main/java/com/omni/app/network/state/BootingState.java)
- [RunningState](../../../../src/main/java/com/omni/app/network/state/RunningState.java)
- [StoppingState](../../../../src/main/java/com/omni/app/network/state/StoppingState.java)
- [CrashedState](../../../../src/main/java/com/omni/app/network/state/CrashedState.java)
