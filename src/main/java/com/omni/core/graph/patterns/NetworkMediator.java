package com.omni.core.graph.patterns;

import com.omni.core.list.MyList;

/**
 * Mediator interface for centralized node communication (Mediator pattern).
 *
 * <p>Nodes communicate through the mediator rather than directly referencing each other. This
 * decouples nodes and allows the mediator to implement routing, filtering, or buffering logic.
 *
 * @param <V> the vertex/node type
 */
public interface NetworkMediator<V> {

  /**
   * Registers a node with the mediator.
   *
   * @param node the node to register
   */
  void register(V node);

  /**
   * Unregisters a node from the mediator.
   *
   * @param node the node to unregister
   */
  void unregister(V node);

  /**
   * Checks if a node is registered.
   *
   * @param node the node to check
   * @return true if registered
   */
  boolean isRegistered(V node);

  /**
   * Sends a message from sender to receiver.
   *
   * @param sender the sending node
   * @param receiver the receiving node
   * @param message the message content
   * @return true if the message was delivered (or queued for delivery)
   */
  boolean sendMessage(V sender, V receiver, String message);

  /**
   * Broadcasts a message from sender to all connected nodes.
   *
   * @param sender the sending node
   * @param message the message content
   * @return number of nodes that received (or will receive) the message
   */
  int broadcast(V sender, String message);

  /**
   * Returns pending messages for a node.
   *
   * @param node the node to get messages for
   * @return list of pending messages
   */
  MyList<Message<V>> getMessagesFor(V node);

  /**
   * Clears all pending messages for a node.
   *
   * @param node the node to clear messages for
   */
  void clearMessagesFor(V node);

  /**
   * Message record containing sender and content.
   *
   * @param <V> the vertex type
   */
  record Message<V>(V sender, String content) {}
}
