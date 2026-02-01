package com.omni.core.graph.patterns;

import com.omni.core.graph.MyGraph;
import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import com.omni.core.map.MyHashMap;

/**
 * Graph-backed implementation of {@link NetworkMediator} (Mediator pattern).
 *
 * <p>Uses a graph to determine valid communication paths. Messages are only delivered between
 * connected nodes. Messages to offline or unconnected nodes are buffered.
 *
 * @param <V> the vertex/node type
 */
public class ServiceMediator<V> implements NetworkMediator<V> {

  private final MyGraph<V> topology;
  private final MyHashMap<V, Boolean> registeredNodes;
  private final MyHashMap<V, MyList<Message<V>>> messageQueues;

  /**
   * Creates a mediator backed by the given graph topology.
   *
   * @param topology the graph defining valid communication paths
   */
  public ServiceMediator(MyGraph<V> topology) {
    if (topology == null) {
      throw new IllegalArgumentException("Topology graph cannot be null");
    }
    this.topology = topology;
    this.registeredNodes = new MyHashMap<>();
    this.messageQueues = new MyHashMap<>();
  }

  @Override
  public void register(V node) {
    if (node == null) {
      throw new IllegalArgumentException("Node cannot be null");
    }
    if (!registeredNodes.containsKey(node)) {
      registeredNodes.put(node, true);
      messageQueues.put(node, new MyArrayList<>());
    }
  }

  @Override
  public void unregister(V node) {
    registeredNodes.remove(node);
    messageQueues.remove(node);
  }

  @Override
  public boolean isRegistered(V node) {
    return registeredNodes.containsKey(node);
  }

  @Override
  public boolean sendMessage(V sender, V receiver, String message) {
    if (!isRegistered(sender)) {
      return false;
    }
    if (!isRegistered(receiver)) {
      return false;
    }
    if (!canCommunicate(sender, receiver)) {
      return false;
    }

    MyList<Message<V>> queue = messageQueues.get(receiver).orElse(new MyArrayList<>());
    queue.add(new Message<>(sender, message));
    return true;
  }

  @Override
  public int broadcast(V sender, String message) {
    if (!isRegistered(sender)) {
      return 0;
    }

    int count = 0;
    MyList<V> neighbors = getReachableNodes(sender);
    for (int i = 0; i < neighbors.size(); i++) {
      V receiver = neighbors.get(i);
      if (!receiver.equals(sender) && isRegistered(receiver)) {
        MyList<Message<V>> queue = messageQueues.get(receiver).orElse(new MyArrayList<>());
        queue.add(new Message<>(sender, message));
        count++;
      }
    }
    return count;
  }

  @Override
  public MyList<Message<V>> getMessagesFor(V node) {
    return messageQueues.get(node).orElse(new MyArrayList<>());
  }

  @Override
  public void clearMessagesFor(V node) {
    if (messageQueues.containsKey(node)) {
      messageQueues.put(node, new MyArrayList<>());
    }
  }

  /**
   * Checks if sender can communicate with receiver based on graph topology.
   *
   * @param sender the sender node
   * @param receiver the receiver node
   * @return true if communication path exists
   */
  private boolean canCommunicate(V sender, V receiver) {
    // Direct edge check
    if (topology.hasVertex(sender) && topology.hasVertex(receiver)) {
      return topology.hasEdge(sender, receiver)
          || (!topology.isDirected() && topology.hasEdge(receiver, sender));
    }
    return false;
  }

  /**
   * Returns all nodes reachable from the given node in one hop.
   *
   * @param node the starting node
   * @return list of reachable nodes
   */
  private MyList<V> getReachableNodes(V node) {
    if (topology.hasVertex(node)) {
      return topology.getNeighbors(node);
    }
    return new MyArrayList<>();
  }

  /**
   * Returns the number of registered nodes.
   *
   * @return registered node count
   */
  public int registeredCount() {
    return registeredNodes.size();
  }
}
