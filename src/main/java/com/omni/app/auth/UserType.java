package com.omni.app.auth;

/** The types of users that can be created in the system. */
public enum UserType {
  /** Administrator with full system access. */
  ADMIN,

  /** Developer with read/write access to resources. */
  DEVELOPER,

  /** Viewer with read-only access. */
  VIEWER
}
