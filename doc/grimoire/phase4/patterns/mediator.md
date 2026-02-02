# Mediator Pattern

> Centralize complex communication between objects.

## Problem
Multiple `ServiceNode`s need to communicate. Direct connections create O(n²) coupling.

## Solution

```
Without Mediator:         With Mediator:
    A ─── B                   A
    │ ╲ ╱ │                   │
    │  ╳  │                   ▼
    │ ╱ ╲ │              Mediator
    C ─── D               ╱  │  ╲
                         ▼   ▼   ▼
  O(n²) connections     B    C    D
                       O(n) connections
```

## Implementation

```java
public interface NetworkMediator {
    void notify(ServiceNode sender, String event);
}

public class ServiceMediator implements NetworkMediator {
    private final MyList<ServiceNode> services = new MyArrayList<>();
    
    public void register(ServiceNode service) {
        services.add(service);
        service.setMediator(this);
    }
    
    @Override
    public void notify(ServiceNode sender, String event) {
        // Coordinate based on event
        for (int i = 0; i < services.size(); i++) {
            ServiceNode service = services.get(i);
            if (service != sender) {
                service.receive(event);
            }
        }
    }
}

public class ServiceNode {
    private NetworkMediator mediator;
    private String name;
    
    public void setMediator(NetworkMediator mediator) {
        this.mediator = mediator;
    }
    
    public void send(String event) {
        mediator.notify(this, event);
    }
    
    public void receive(String event) {
        // Handle incoming event
    }
}
```

## Usage

```java
ServiceMediator mediator = new ServiceMediator();

ServiceNode auth = new ServiceNode("auth");
ServiceNode api = new ServiceNode("api");
ServiceNode database = new ServiceNode("database");

mediator.register(auth);
mediator.register(api);
mediator.register(database);

auth.send("USER_LOGGED_IN");
// → api.receive("USER_LOGGED_IN")
// → database.receive("USER_LOGGED_IN")
```

## Benefits
- **Reduced coupling**: Services don't reference each other
- **Centralized logic**: Coordination rules in one place
- **Easier testing**: Mock the mediator for unit tests

## Trade-offs
- Mediator can become a "god object" if too complex
- Consider splitting into multiple specialized mediators

## Used In
- [NetworkMediator](../../../../src/main/java/com/omni/core/graph/patterns/NetworkMediator.java)
- [ServiceMediator](../../../../src/main/java/com/omni/core/graph/patterns/ServiceMediator.java)
- [ServiceNode](../../../../src/main/java/com/omni/app/network/ServiceNode.java)
