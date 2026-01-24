package com.omni.app.auth;

import java.util.Set;

/**
 * An administrator user with full system access.
 *
 * <p>Admin users have all permissions including user management and system administration.
 */
public class AdminUser extends User {

  private static final Set<Permission> PERMISSIONS =
      Set.of(
          Permission.READ,
          Permission.WRITE,
          Permission.DELETE,
          Permission.MANAGE_USERS,
          Permission.ADMIN);

  /**
   * Creates a new admin user.
   *
   * @param username the unique username
   */
  AdminUser(String username) {
    super(username);
  }

  @Override
  public Set<Permission> getPermissions() {
    return PERMISSIONS;
  }

  @Override
  public UserType getType() {
    return UserType.ADMIN;
  }
}
