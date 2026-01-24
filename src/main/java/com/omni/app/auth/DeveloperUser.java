package com.omni.app.auth;

import java.util.Set;

/**
 * A developer user with read/write access to resources.
 *
 * <p>Developers can create and modify resources but cannot delete them or manage users.
 */
public class DeveloperUser extends User {

  private static final Set<Permission> PERMISSIONS = Set.of(Permission.READ, Permission.WRITE);

  /**
   * Creates a new developer user.
   *
   * @param username the unique username
   */
  DeveloperUser(String username) {
    super(username);
  }

  @Override
  public Set<Permission> getPermissions() {
    return PERMISSIONS;
  }

  @Override
  public UserType getType() {
    return UserType.DEVELOPER;
  }
}
