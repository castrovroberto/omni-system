package com.omni.app.auth;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/**
 * Abstract base class for users in the system.
 *
 * <p>This class demonstrates the <b>Factory Method</b> design pattern. The static {@link
 * #create(UserType, String)} method creates the appropriate User subclass based on the requested
 * type.
 *
 * <p>Subclasses define their own permission sets:
 *
 * <ul>
 *   <li>{@link AdminUser}: Full administrative access
 *   <li>{@link DeveloperUser}: Read/write access
 *   <li>{@link ViewerUser}: Read-only access
 * </ul>
 */
public abstract class User {

  private final String username;
  private final Instant createdAt;
  private Instant lastLoginAt;

  /**
   * Creates a new user with the given username.
   *
   * @param username the unique username
   */
  protected User(String username) {
    this.username = Objects.requireNonNull(username, "Username cannot be null");
    this.createdAt = Instant.now();
    this.lastLoginAt = null;
  }

  /**
   * Factory method to create a user of the specified type.
   *
   * <p>This is the Factory Method pattern - the creation logic is encapsulated here, and callers
   * don't need to know about the specific subclasses.
   *
   * @param type the type of user to create
   * @param username the unique username
   * @return a new User instance of the appropriate type
   * @throws IllegalArgumentException if type is null
   */
  public static User create(UserType type, String username) {
    Objects.requireNonNull(type, "UserType cannot be null");
    return switch (type) {
      case ADMIN -> new AdminUser(username);
      case DEVELOPER -> new DeveloperUser(username);
      case VIEWER -> new ViewerUser(username);
    };
  }

  /**
   * Returns the permissions granted to this user.
   *
   * @return an unmodifiable set of permissions
   */
  public abstract Set<Permission> getPermissions();

  /**
   * Returns the type of this user.
   *
   * @return the user type
   */
  public abstract UserType getType();

  /**
   * Checks if this user has a specific permission.
   *
   * @param permission the permission to check
   * @return true if the user has the permission
   */
  public boolean hasPermission(Permission permission) {
    return getPermissions().contains(permission);
  }

  /**
   * Returns the username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Returns the time when this user was created.
   *
   * @return the creation timestamp
   */
  public Instant getCreatedAt() {
    return createdAt;
  }

  /**
   * Returns the time of the last login, or null if never logged in.
   *
   * @return the last login timestamp, or null
   */
  public Instant getLastLoginAt() {
    return lastLoginAt;
  }

  /**
   * Records a login for this user.
   *
   * @return the login timestamp
   */
  public Instant recordLogin() {
    this.lastLoginAt = Instant.now();
    return this.lastLoginAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(username, user.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }

  @Override
  public String toString() {
    return String.format("%s[username=%s, type=%s]", getClass().getSimpleName(), username, getType());
  }
}
