package com.omni.app.auth;

import com.omni.core.algorithm.sort.SortAlgorithms;
import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import com.omni.core.map.MyHashMap;
import com.omni.core.map.MyMap;
import java.util.Comparator;
import java.util.Optional;

/**
 * Registry for managing users in the system.
 *
 * <p>This class provides:
 *
 * <ul>
 *   <li>User registration and lookup by username (O(1) via MyHashMap)
 *   <li>Sorted views of users (by name, last login, etc.)
 *   <li>User statistics and queries
 * </ul>
 *
 * <p>Demonstrates the use of {@link MyHashMap} for fast lookup and {@link SortAlgorithms} for
 * sorted views.
 */
public class UserRegistry {

  private final MyMap<String, User> users;

  /** Creates a new empty user registry. */
  public UserRegistry() {
    this.users = new MyHashMap<>();
  }

  /**
   * Registers a new user in the system.
   *
   * @param type the type of user to create
   * @param username the unique username
   * @return the created user
   * @throws IllegalArgumentException if the username is already taken
   */
  public User registerUser(UserType type, String username) {
    if (users.containsKey(username)) {
      throw new IllegalArgumentException("Username already exists: " + username);
    }

    User user = User.create(type, username);
    users.put(username, user);
    return user;
  }

  /**
   * Looks up a user by username.
   *
   * @param username the username to find
   * @return the user if found
   */
  public Optional<User> findUser(String username) {
    return users.get(username);
  }

  /**
   * Checks if a username is already registered.
   *
   * @param username the username to check
   * @return true if the username exists
   */
  public boolean userExists(String username) {
    return users.containsKey(username);
  }

  /**
   * Removes a user from the registry.
   *
   * @param username the username to remove
   * @return the removed user, if found
   */
  public Optional<User> removeUser(String username) {
    return users.remove(username);
  }

  /**
   * Returns all users in the registry.
   *
   * @return an iterable over all users
   */
  public Iterable<User> getAllUsers() {
    return users.values();
  }

  /**
   * Returns the number of registered users.
   *
   * @return the user count
   */
  public int getUserCount() {
    return users.size();
  }

  /**
   * Returns all users sorted by username.
   *
   * @return a sorted list of users
   */
  public MyList<User> getUsersSortedByUsername() {
    MyList<User> userList = collectUsers();
    SortAlgorithms.mergeSort(userList, Comparator.comparing(User::getUsername));
    return userList;
  }

  /**
   * Returns all users sorted by last login time (most recent first).
   *
   * <p>Users who have never logged in are placed at the end.
   *
   * @return a sorted list of users
   */
  public MyList<User> getUsersSortedByLastLogin() {
    MyList<User> userList = collectUsers();
    SortAlgorithms.mergeSort(
        userList,
        (u1, u2) -> {
          if (u1.getLastLoginAt() == null && u2.getLastLoginAt() == null) {
            return 0;
          }
          if (u1.getLastLoginAt() == null) {
            return 1;
          }
          if (u2.getLastLoginAt() == null) {
            return -1;
          }
          // Most recent first (descending)
          return u2.getLastLoginAt().compareTo(u1.getLastLoginAt());
        });
    return userList;
  }

  /**
   * Returns all users sorted by creation time (newest first).
   *
   * @return a sorted list of users
   */
  public MyList<User> getUsersSortedByCreationTime() {
    MyList<User> userList = collectUsers();
    SortAlgorithms.mergeSort(
        userList, (u1, u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt())); // Newest first
    return userList;
  }

  /**
   * Returns all users of a specific type.
   *
   * @param type the user type to filter by
   * @return a list of matching users
   */
  public MyList<User> getUsersByType(UserType type) {
    MyList<User> result = new MyArrayList<>();
    for (User user : users.values()) {
      if (user.getType() == type) {
        result.add(user);
      }
    }
    return result;
  }

  /**
   * Returns the count of users by type.
   *
   * @param type the user type to count
   * @return the number of users of that type
   */
  public int countUsersByType(UserType type) {
    int count = 0;
    for (User user : users.values()) {
      if (user.getType() == type) {
        count++;
      }
    }
    return count;
  }

  /**
   * Returns all users who have never logged in.
   *
   * @return a list of users with no login history
   */
  public MyList<User> getInactiveUsers() {
    MyList<User> result = new MyArrayList<>();
    for (User user : users.values()) {
      if (user.getLastLoginAt() == null) {
        result.add(user);
      }
    }
    return result;
  }

  /** Clears all users from the registry. */
  public void clear() {
    users.clear();
  }

  private MyList<User> collectUsers() {
    MyList<User> userList = new MyArrayList<>();
    for (User user : users.values()) {
      userList.add(user);
    }
    return userList;
  }
}
