package com.omni.app.auth;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UserRegistry Tests")
class UserRegistryTest {

  private UserRegistry registry;

  @BeforeEach
  void setUp() {
    registry = new UserRegistry();
  }

  @Nested
  @DisplayName("Registration Tests")
  class RegistrationTests {

    @Test
    @DisplayName("RegisterUser creates and stores user")
    void registerUser_createsAndStores() {
      User user = registry.registerUser(UserType.ADMIN, "admin");

      assertNotNull(user);
      assertEquals("admin", user.getUsername());
      assertEquals(UserType.ADMIN, user.getType());
      assertEquals(1, registry.getUserCount());
    }

    @Test
    @DisplayName("RegisterUser throws for duplicate username")
    void registerUser_duplicate_throws() {
      registry.registerUser(UserType.ADMIN, "admin");

      assertThrows(
          IllegalArgumentException.class, () -> registry.registerUser(UserType.VIEWER, "admin"));
    }

    @Test
    @DisplayName("RegisterUser allows different usernames")
    void registerUser_differentNames_allowed() {
      registry.registerUser(UserType.ADMIN, "admin");
      registry.registerUser(UserType.DEVELOPER, "dev");
      registry.registerUser(UserType.VIEWER, "viewer");

      assertEquals(3, registry.getUserCount());
    }
  }

  @Nested
  @DisplayName("Lookup Tests")
  class LookupTests {

    @Test
    @DisplayName("FindUser returns user for existing username")
    void findUser_existing_returnsUser() {
      registry.registerUser(UserType.ADMIN, "admin");

      var found = registry.findUser("admin");

      assertTrue(found.isPresent());
      assertEquals("admin", found.get().getUsername());
    }

    @Test
    @DisplayName("FindUser returns empty for nonexistent username")
    void findUser_nonexistent_returnsEmpty() {
      var found = registry.findUser("nonexistent");

      assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("UserExists returns true for existing user")
    void userExists_existing_returnsTrue() {
      registry.registerUser(UserType.ADMIN, "admin");

      assertTrue(registry.userExists("admin"));
    }

    @Test
    @DisplayName("UserExists returns false for nonexistent user")
    void userExists_nonexistent_returnsFalse() {
      assertFalse(registry.userExists("nonexistent"));
    }
  }

  @Nested
  @DisplayName("Removal Tests")
  class RemovalTests {

    @Test
    @DisplayName("RemoveUser removes and returns user")
    void removeUser_existing_removes() {
      registry.registerUser(UserType.ADMIN, "admin");
      assertEquals(1, registry.getUserCount());

      var removed = registry.removeUser("admin");

      assertTrue(removed.isPresent());
      assertEquals(0, registry.getUserCount());
    }

    @Test
    @DisplayName("RemoveUser returns empty for nonexistent")
    void removeUser_nonexistent_returnsEmpty() {
      var removed = registry.removeUser("nonexistent");

      assertTrue(removed.isEmpty());
    }

    @Test
    @DisplayName("Clear removes all users")
    void clear_removesAll() {
      registry.registerUser(UserType.ADMIN, "admin");
      registry.registerUser(UserType.DEVELOPER, "dev");
      assertEquals(2, registry.getUserCount());

      registry.clear();

      assertEquals(0, registry.getUserCount());
    }
  }

  @Nested
  @DisplayName("Sorted View Tests")
  class SortedViewTests {

    @BeforeEach
    void setUpUsers() {
      registry.registerUser(UserType.ADMIN, "charlie");
      registry.registerUser(UserType.DEVELOPER, "alice");
      registry.registerUser(UserType.VIEWER, "bob");
    }

    @Test
    @DisplayName("GetUsersSortedByUsername returns alphabetically sorted")
    void getUsersSortedByUsername_sorted() {
      MyList<User> sorted = registry.getUsersSortedByUsername();

      assertEquals(3, sorted.size());
      assertEquals("alice", sorted.get(0).getUsername());
      assertEquals("bob", sorted.get(1).getUsername());
      assertEquals("charlie", sorted.get(2).getUsername());
    }

    @Test
    @DisplayName("GetUsersSortedByLastLogin handles null logins")
    void getUsersSortedByLastLogin_handlesNull() {
      // No logins yet - all have null lastLoginAt
      MyList<User> sorted = registry.getUsersSortedByLastLogin();

      assertEquals(3, sorted.size());
      // Order is undefined for all-null case, just verify all present
    }

    @Test
    @DisplayName("GetUsersSortedByLastLogin orders most recent first")
    void getUsersSortedByLastLogin_mostRecentFirst() throws InterruptedException {
      // Record logins in order
      registry.findUser("charlie").get().recordLogin();
      Thread.sleep(10);
      registry.findUser("alice").get().recordLogin();
      Thread.sleep(10);
      registry.findUser("bob").get().recordLogin();

      MyList<User> sorted = registry.getUsersSortedByLastLogin();

      // Most recent (bob) should be first
      assertEquals("bob", sorted.get(0).getUsername());
      assertEquals("alice", sorted.get(1).getUsername());
      assertEquals("charlie", sorted.get(2).getUsername());
    }

    @Test
    @DisplayName("GetUsersSortedByCreationTime returns newest first")
    void getUsersSortedByCreationTime_newestFirst() {
      MyList<User> sorted = registry.getUsersSortedByCreationTime();

      assertEquals(3, sorted.size());
      // Last registered (bob) should be first
      assertEquals("bob", sorted.get(0).getUsername());
    }
  }

  @Nested
  @DisplayName("Filter Tests")
  class FilterTests {

    @BeforeEach
    void setUpUsers() {
      registry.registerUser(UserType.ADMIN, "admin1");
      registry.registerUser(UserType.ADMIN, "admin2");
      registry.registerUser(UserType.DEVELOPER, "dev1");
      registry.registerUser(UserType.DEVELOPER, "dev2");
      registry.registerUser(UserType.DEVELOPER, "dev3");
      registry.registerUser(UserType.VIEWER, "viewer1");
    }

    @Test
    @DisplayName("GetUsersByType returns only matching users")
    void getUsersByType_returnsMatching() {
      MyList<User> admins = registry.getUsersByType(UserType.ADMIN);
      MyList<User> devs = registry.getUsersByType(UserType.DEVELOPER);
      MyList<User> viewers = registry.getUsersByType(UserType.VIEWER);

      assertEquals(2, admins.size());
      assertEquals(3, devs.size());
      assertEquals(1, viewers.size());
    }

    @Test
    @DisplayName("CountUsersByType returns correct count")
    void countUsersByType_returnsCorrect() {
      assertEquals(2, registry.countUsersByType(UserType.ADMIN));
      assertEquals(3, registry.countUsersByType(UserType.DEVELOPER));
      assertEquals(1, registry.countUsersByType(UserType.VIEWER));
    }

    @Test
    @DisplayName("GetInactiveUsers returns users with no login")
    void getInactiveUsers_returnsNoLogin() {
      // Record login for some users
      registry.findUser("admin1").get().recordLogin();
      registry.findUser("dev1").get().recordLogin();

      MyList<User> inactive = registry.getInactiveUsers();

      assertEquals(4, inactive.size());
    }
  }
}
