package com.omni.app.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("User Factory Tests")
class UserTest {

  @Nested
  @DisplayName("Factory Method Tests")
  class FactoryMethodTests {

    @Test
    @DisplayName("Create returns AdminUser for ADMIN type")
    void create_adminType_returnsAdminUser() {
      User user = User.create(UserType.ADMIN, "admin");

      assertInstanceOf(AdminUser.class, user);
      assertEquals("admin", user.getUsername());
      assertEquals(UserType.ADMIN, user.getType());
    }

    @Test
    @DisplayName("Create returns DeveloperUser for DEVELOPER type")
    void create_developerType_returnsDeveloperUser() {
      User user = User.create(UserType.DEVELOPER, "dev");

      assertInstanceOf(DeveloperUser.class, user);
      assertEquals("dev", user.getUsername());
      assertEquals(UserType.DEVELOPER, user.getType());
    }

    @Test
    @DisplayName("Create returns ViewerUser for VIEWER type")
    void create_viewerType_returnsViewerUser() {
      User user = User.create(UserType.VIEWER, "viewer");

      assertInstanceOf(ViewerUser.class, user);
      assertEquals("viewer", user.getUsername());
      assertEquals(UserType.VIEWER, user.getType());
    }

    @Test
    @DisplayName("Create with null type throws exception")
    void create_nullType_throwsException() {
      assertThrows(NullPointerException.class, () -> User.create(null, "user"));
    }

    @Test
    @DisplayName("Create with null username throws exception")
    void create_nullUsername_throwsException() {
      assertThrows(NullPointerException.class, () -> User.create(UserType.ADMIN, null));
    }
  }

  @Nested
  @DisplayName("Permission Tests")
  class PermissionTests {

    @Test
    @DisplayName("Admin has all permissions")
    void adminUser_hasAllPermissions() {
      User admin = User.create(UserType.ADMIN, "admin");

      assertTrue(admin.hasPermission(Permission.READ));
      assertTrue(admin.hasPermission(Permission.WRITE));
      assertTrue(admin.hasPermission(Permission.DELETE));
      assertTrue(admin.hasPermission(Permission.MANAGE_USERS));
      assertTrue(admin.hasPermission(Permission.ADMIN));
    }

    @Test
    @DisplayName("Developer has read and write permissions only")
    void developerUser_hasReadWriteOnly() {
      User dev = User.create(UserType.DEVELOPER, "dev");

      assertTrue(dev.hasPermission(Permission.READ));
      assertTrue(dev.hasPermission(Permission.WRITE));
      assertFalse(dev.hasPermission(Permission.DELETE));
      assertFalse(dev.hasPermission(Permission.MANAGE_USERS));
      assertFalse(dev.hasPermission(Permission.ADMIN));
    }

    @Test
    @DisplayName("Viewer has read permission only")
    void viewerUser_hasReadOnly() {
      User viewer = User.create(UserType.VIEWER, "viewer");

      assertTrue(viewer.hasPermission(Permission.READ));
      assertFalse(viewer.hasPermission(Permission.WRITE));
      assertFalse(viewer.hasPermission(Permission.DELETE));
      assertFalse(viewer.hasPermission(Permission.MANAGE_USERS));
      assertFalse(viewer.hasPermission(Permission.ADMIN));
    }

    @Test
    @DisplayName("getPermissions returns correct set for each type")
    void getPermissions_returnsCorrectSet() {
      User admin = User.create(UserType.ADMIN, "admin");
      User dev = User.create(UserType.DEVELOPER, "dev");
      User viewer = User.create(UserType.VIEWER, "viewer");

      assertEquals(5, admin.getPermissions().size());
      assertEquals(2, dev.getPermissions().size());
      assertEquals(1, viewer.getPermissions().size());
    }
  }

  @Nested
  @DisplayName("User Properties Tests")
  class UserPropertiesTests {

    @Test
    @DisplayName("CreatedAt is set on construction")
    void createdAt_setOnConstruction() {
      User user = User.create(UserType.ADMIN, "admin");

      assertNotNull(user.getCreatedAt());
    }

    @Test
    @DisplayName("LastLoginAt is null initially")
    void lastLoginAt_nullInitially() {
      User user = User.create(UserType.ADMIN, "admin");

      assertNull(user.getLastLoginAt());
    }

    @Test
    @DisplayName("RecordLogin updates lastLoginAt")
    void recordLogin_updatesLastLoginAt() {
      User user = User.create(UserType.ADMIN, "admin");

      user.recordLogin();

      assertNotNull(user.getLastLoginAt());
    }

    @Test
    @DisplayName("Equals based on username")
    void equals_basedOnUsername() {
      User user1 = User.create(UserType.ADMIN, "admin");
      User user2 = User.create(UserType.ADMIN, "admin");
      User user3 = User.create(UserType.ADMIN, "other");

      assertEquals(user1, user2);
      assertNotEquals(user1, user3);
    }

    @Test
    @DisplayName("HashCode based on username")
    void hashCode_basedOnUsername() {
      User user1 = User.create(UserType.ADMIN, "admin");
      User user2 = User.create(UserType.ADMIN, "admin");

      assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("ToString contains username and type")
    void toString_containsUsernameAndType() {
      User user = User.create(UserType.DEVELOPER, "devuser");

      String str = user.toString();
      assertTrue(str.contains("devuser"));
      assertTrue(str.contains("DEVELOPER"));
    }
  }
}
