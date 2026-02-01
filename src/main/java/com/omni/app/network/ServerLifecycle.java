package com.omni.app.network;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import com.omni.core.map.MyHashMap;
import java.util.Optional;

/**
 * Manages a fleet of {@link Server} instances via a {@link MyHashMap}.
 *
 * <p>Provides lifecycle operations across the fleet.
 */
public class ServerLifecycle {

  private final MyHashMap<String, Server> servers;

  /** Constructs an empty server lifecycle manager. */
  public ServerLifecycle() {
    this.servers = new MyHashMap<>();
  }

  /**
   * Registers a new server.
   *
   * @param name the server name
   * @return the created server
   */
  public Server addServer(String name) {
    Server server = new Server(name);
    servers.put(name, server);
    return server;
  }

  /**
   * Returns a server by name.
   *
   * @param name the server name
   * @return the server, or empty if not found
   */
  public Optional<Server> getServer(String name) {
    return servers.get(name);
  }

  /** Boots all servers. */
  public void bootAll() {
    for (String name : servers.keys()) {
      servers.get(name).ifPresent(Server::boot);
    }
  }

  /** Runs all servers. */
  public void runAll() {
    for (String name : servers.keys()) {
      servers.get(name).ifPresent(Server::run);
    }
  }

  /** Stops all servers. */
  public void stopAll() {
    for (String name : servers.keys()) {
      servers.get(name).ifPresent(Server::stop);
    }
  }

  /**
   * Returns all server names.
   *
   * @return list of server names
   */
  public MyList<String> getServerNames() {
    MyList<String> names = new MyArrayList<>();
    for (String name : servers.keys()) {
      names.add(name);
    }
    return names;
  }

  /**
   * Returns the number of servers.
   *
   * @return server count
   */
  public int size() {
    return servers.size();
  }
}
