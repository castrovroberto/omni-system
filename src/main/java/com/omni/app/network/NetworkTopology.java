package com.omni.app.network;

import com.omni.core.graph.AdjacencyListGraph;
import com.omni.core.graph.MyGraph;
import com.omni.core.list.MyList;

/**
 * Represents a network topology as an undirected weighted graph of servers.
 *
 * <p>Wraps an {@link AdjacencyListGraph} to model server connections with latency weights.
 */
public class NetworkTopology {

  private final AdjacencyListGraph<String> graph;

  /** Constructs an empty network topology. */
  public NetworkTopology() {
    this.graph = new AdjacencyListGraph<>(false);
  }

  /**
   * Adds a server to the network.
   *
   * @param server the server name
   */
  public void addServer(String server) {
    graph.addVertex(server);
  }

  /**
   * Connects two servers with the given latency.
   *
   * @param server1 the first server
   * @param server2 the second server
   * @param latency the connection latency (weight)
   */
  public void connect(String server1, String server2, double latency) {
    graph.addEdge(server1, server2, latency);
  }

  /**
   * Connects two servers with default latency (1.0).
   *
   * @param server1 the first server
   * @param server2 the second server
   */
  public void connect(String server1, String server2) {
    graph.addEdge(server1, server2);
  }

  /**
   * Returns the underlying graph.
   *
   * @return the graph
   */
  public MyGraph<String> getGraph() {
    return graph;
  }

  /**
   * Returns all servers in the topology.
   *
   * @return list of server names
   */
  public MyList<String> getServers() {
    return graph.getVertices();
  }

  /**
   * Returns true if the server exists in the topology.
   *
   * @param server the server name
   * @return true if present
   */
  public boolean hasServer(String server) {
    return graph.hasVertex(server);
  }

  /**
   * Returns true if two servers are directly connected.
   *
   * @param server1 the first server
   * @param server2 the second server
   * @return true if connected
   */
  public boolean areConnected(String server1, String server2) {
    return graph.hasEdge(server1, server2);
  }
}
