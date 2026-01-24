package com.omni.app.auth;

/**
 * Permissions that can be granted to users in the system.
 *
 * <p>Permissions follow a hierarchical model where higher privilege levels include all lower-level
 * permissions.
 */
public enum Permission {
  /** View resources but not modify them. */
  READ,

  /** Create and modify own resources. */
  WRITE,

  /** Delete resources. */
  DELETE,

  /** Manage other users' permissions. */
  MANAGE_USERS,

  /** Full system administration access. */
  ADMIN
}
