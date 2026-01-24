package com.omni.app.auth;

import java.util.Set;

/**
 * A viewer user with read-only access to resources.
 *
 * <p>Viewers can only view resources; they cannot create, modify, or delete anything.
 */
public class ViewerUser extends User {

  private static final Set<Permission> PERMISSIONS = Set.of(Permission.READ);

  /**
   * Creates a new viewer user.
   *
   * @param username the unique username
   */
  ViewerUser(String username) {
    super(username);
  }

  @Override
  public Set<Permission> getPermissions() {
    return PERMISSIONS;
  }

  @Override
  public UserType getType() {
    return UserType.VIEWER;
  }
}
