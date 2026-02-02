# Observer Pattern

> Notify multiple objects about changes without tight coupling.

## Problem
`ServiceMonitor` needs to react when a `Server` changes state, but Server shouldn't depend on ServiceMonitor directly.

## Solution

```
Server (Subject)
    │
    ├─ observers: List<ServiceObserver>
    │
    └─ notifyObservers()
           │
           ▼
     ServiceObserver (interface)
           △
           │
    ┌──────┼──────┐
    │             │
ServiceMonitor  AlertService
```

## Implementation

```java
public interface ServiceObserver {
    void onServiceStateChanged(Server server, ServerState newState);
}

public class Server {
    private final MyList<ServiceObserver> observers = new MyArrayList<>();
    private ServerState state;
    
    public void addObserver(ServiceObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(ServiceObserver observer) {
        // Remove from list
    }
    
    public void setState(ServerState newState) {
        this.state = newState;
        notifyObservers();
    }
    
    private void notifyObservers() {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).onServiceStateChanged(this, state);
        }
    }
}
```

## Usage

```java
Server server = new Server("web-1");
ServiceMonitor monitor = new ServiceMonitor();
AlertService alerts = new AlertService();

server.addObserver(monitor);
server.addObserver(alerts);

server.crash();
// → monitor.onServiceStateChanged(server, CrashedState)
// → alerts.onServiceStateChanged(server, CrashedState)
```

## Benefits
- **Loose coupling**: Server doesn't know concrete observer types
- **Dynamic**: Add/remove observers at runtime
- **Broadcast**: One event, many listeners

## Used In
- [Server](../../../../src/main/java/com/omni/app/network/Server.java)
- [ServiceObserver](../../../../src/main/java/com/omni/app/network/ServiceObserver.java)
- [ServiceMonitor](../../../../src/main/java/com/omni/app/network/ServiceMonitor.java)
