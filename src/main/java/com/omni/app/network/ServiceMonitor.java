package com.omni.app.network;

import com.omni.core.graph.GraphAlgorithms;
import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;

/**
 * Monitors services and detects cascading failures using graph traversal.
 *
 * <p>Implements {@link ServiceObserver} to receive failure notifications. Uses BFS on the network
 * topology to find all servers reachable from a failed server (potential cascade).
 */
public class ServiceMonitor implements ServiceObserver {

  private final NetworkTopology topology;
  private final MyList<String> eventLog;

  /**
   * Constructs a monitor for the given topology.
   *
   * @param topology the network topology
   */
  public ServiceMonitor(NetworkTopology topology) {
    this.topology = topology;
    this.eventLog = new MyArrayList<>();
  }

  @Override
  public void onStateChanged(String serviceName, String event) {
    eventLog.add(serviceName + ": " + event);
  }

  /**
   * Finds all servers that could be affected by a failure at the given server, using BFS to
   * discover reachable nodes.
   *
   * @param failedServer the server that failed
   * @return list of potentially affected servers (excluding the failed one)
   */
  public MyList<String> findCascadingFailures(String failedServer) {
    if (!topology.hasServer(failedServer)) {
      return new MyArrayList<>();
    }
    MyList<String> reachable = GraphAlgorithms.bfs(topology.getGraph(), failedServer);
    MyList<String> affected = new MyArrayList<>();
    for (int i = 0; i < reachable.size(); i++) {
      if (!reachable.get(i).equals(failedServer)) {
        affected.add(reachable.get(i));
      }
    }
    return affected;
  }

  /**
   * Returns the event log.
   *
   * @return list of logged events
   */
  public MyList<String> getEventLog() {
    return eventLog;
  }

  /** Clears the event log. */
  public void clearLog() {
    eventLog.clear();
  }
}
