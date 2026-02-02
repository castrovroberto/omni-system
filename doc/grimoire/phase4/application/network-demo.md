# Network Topology & Dependency Resolver Demo

> Phase 4 demo application showcasing graph structures and algorithms.

---

## Components

### NetworkTopology

Models server network as a weighted graph:

```java
NetworkTopology network = new NetworkTopology();

// Add servers
network.addServer("web-1");
network.addServer("web-2");
network.addServer("db-primary");
network.addServer("cache");

// Add connections with latency weights (ms)
network.connect("web-1", "cache", 5);
network.connect("web-2", "cache", 8);
network.connect("cache", "db-primary", 10);
network.connect("web-1", "db-primary", 50);
```

### LatencyRouter (Uses Dijkstra)

Find lowest-latency path:

```java
LatencyRouter router = new LatencyRouter(network);

MyList<String> path = router.findFastestPath("web-1", "db-primary");
// Returns: [web-1, cache, db-primary] (15ms total)
// Not:     [web-1, db-primary] (50ms direct)

double latency = router.getLatency("web-1", "db-primary");
// Returns: 15.0
```

### DependencyResolver (Uses Topological Sort)

Resolve service startup order:

```java
DependencyResolver resolver = new DependencyResolver();

// Service dependencies
resolver.addDependency("api", "auth");      // api needs auth
resolver.addDependency("api", "database");  // api needs database
resolver.addDependency("auth", "database"); // auth needs database
resolver.addDependency("database", "redis"); // database needs redis

MyList<String> startupOrder = resolver.resolve();
// Returns: [redis, database, auth, api]
```

Cycle detection:
```java
resolver.addDependency("redis", "api");  // Creates cycle!
resolver.resolve();  // Throws CycleDetectedException
```

---

## Server Lifecycle (State Pattern)

```java
Server server = new Server("web-1");
server.getState();  // BootingState

server.start();  // Transitions to RunningState
server.getState();  // RunningState

server.crash();  // Transitions to CrashedState
server.start();  // Transitions to BootingState (reboot)
```

State machine:
```
BootingState ──start()──▶ RunningState
      │                        │
      │                        │ crash()
      ▼ crash()                ▼
CrashedState ◀───crash()── StoppingState
      │                        ▲
      └───start()───▶ BootingState
                               │
                     RunningState ──stop()──┘
```

### ServiceMonitor (Observer Pattern)

```java
ServiceMonitor monitor = new ServiceMonitor();
server.addObserver(monitor);

server.crash();
// monitor.onServiceStateChanged(server, CrashedState)
// → Triggers alert, logs incident
```

---

## Pattern Usage

### Graph as Backend

```
NetworkTopology
    │
    └─ AdjacencyListGraph<String>
           │
           ├─ servers as vertices
           └─ connections as weighted edges
```

### Observer + State Collaboration

```
Server                    ServiceMonitor
   │                           │
   ├─ state: ServerState       │
   │                           │
   └─ observers: List<ServiceObserver>
           │
           └──notify()──────▶ onStateChanged()
```

---

## Usage Scenarios

### Scenario 1: Network Routing
```java
// Find optimal route through network
MyList<String> route = router.findFastestPath("client", "database");
for (String hop : route) {
    forwardPacket(hop);
}
```

### Scenario 2: Safe Deployment
```java
// Get safe deployment order (dependencies first)
MyList<String> order = resolver.resolve();
for (String service : order) {
    deploy(service);
    waitForHealthy(service);
}
```

### Scenario 3: Failure Propagation
```java
server.addObserver(new FailureHandler());

// When server crashes, handler triggered
server.crash();
// → FailureHandler.onStateChanged()
// → Alerts team, starts failover
```

---

*Part of the Omni-System Grimoire - Phase 4*
