package com.omni.app.network;

import com.omni.core.graph.GraphAlgorithms;
import com.omni.core.list.MyList;
import com.omni.core.map.MyHashMap;

/**
 * Routes traffic through a {@link NetworkTopology} using Dijkstra's algorithm to find lowest
 * latency paths.
 */
public class LatencyRouter {

  private final NetworkTopology topology;

  /**
   * Constructs a router for the given topology.
   *
   * @param topology the network topology
   */
  public LatencyRouter(NetworkTopology topology) {
    this.topology = topology;
  }

  /**
   * Finds the shortest (lowest latency) path between two servers.
   *
   * @param source the source server
   * @param target the target server
   * @return list of servers forming the path, or empty list if unreachable
   */
  public MyList<String> findShortestPath(String source, String target) {
    return GraphAlgorithms.dijkstraPath(topology.getGraph(), source, target);
  }

  /**
   * Finds the shortest distance (total latency) between two servers.
   *
   * @param source the source server
   * @param target the target server
   * @return the total latency, or {@link Double#POSITIVE_INFINITY} if unreachable
   */
  public double findShortestDistance(String source, String target) {
    MyHashMap<String, Double> distances = GraphAlgorithms.dijkstra(topology.getGraph(), source);
    return distances.get(target).orElse(Double.POSITIVE_INFINITY);
  }

  /**
   * Finds all distances from a source server to every other server.
   *
   * @param source the source server
   * @return map from server name to distance
   */
  public MyHashMap<String, Double> findAllDistances(String source) {
    return GraphAlgorithms.dijkstra(topology.getGraph(), source);
  }
}
