# Phase 2 Demo Application: Auth Service

> How Phase 2 components power the Cloud Orchestrator's authentication system

---

## Overview

The Auth Service demonstrates practical usage of Phase 2 components in a realistic backend scenario. It provides user management and session handling for the Mock Cloud Orchestrator.

```
┌─────────────────────────────────────────────────────────────────┐
│                        Auth Service                              │
│                                                                  │
│  ┌─────────────────────┐    ┌─────────────────────────────────┐ │
│  │    UserRegistry     │    │         SessionStore            │ │
│  │    (MyHashMap)      │    │         (MyHashMap)             │ │
│  │                     │    │                                 │ │
│  │  username → User    │    │  token → UserSession            │ │
│  │  "alice" → Admin    │    │  "abc123" → Session(alice)      │ │
│  │  "bob" → Developer  │    │  "def456" → Session(bob)        │ │
│  │  "carol" → Viewer   │    │                                 │ │
│  └─────────────────────┘    └─────────────────────────────────┘ │
│           │                              │                       │
│           │ User.create()                │ createSession()       │
│           ▼                              ▼                       │
│  ┌─────────────────────┐    ┌─────────────────────────────────┐ │
│  │   Factory Method    │    │      CachingHashMap             │ │
│  │   Pattern           │    │      (Decorator Pattern)        │ │
│  └─────────────────────┘    └─────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

---

## Components

### User (Factory Method Pattern)

Abstract base class with a factory method for creating user subtypes.

**Why Factory Method?**
- Encapsulates creation logic
- Caller doesn't need to know about subclasses
- Easy to add new user types
- Centralizes permission assignment

```java
// Factory method - caller doesn't know about AdminUser, DeveloperUser, etc.
User admin = User.create(UserType.ADMIN, "alice");
User dev = User.create(UserType.DEVELOPER, "bob");
User viewer = User.create(UserType.VIEWER, "carol");

// Each type has different permissions
admin.hasPermission(Permission.ADMIN);      // true
dev.hasPermission(Permission.ADMIN);        // false
viewer.hasPermission(Permission.WRITE);     // false
```

**User Type Hierarchy**:

```
User (abstract)
├── AdminUser
│   └── Permissions: READ, WRITE, DELETE, MANAGE_USERS, ADMIN
├── DeveloperUser
│   └── Permissions: READ, WRITE
└── ViewerUser
    └── Permissions: READ
```

---

### SessionStore (Uses MyHashMap)

Manages authenticated user sessions with O(1) token lookup.

**Why MyHashMap?**
- Session tokens are unique keys
- O(1) lookup by token (most common operation)
- O(1) creation and invalidation
- No ordering needed

```java
public class SessionStore {
    private final MyMap<String, UserSession> sessions = new MyHashMap<>();

    public UserSession createSession(User user) {
        UserSession session = new UserSession(user);
        sessions.put(session.getToken(), session);  // O(1)
        return session;
    }

    public Optional<UserSession> getSession(String token) {
        return sessions.get(token);  // O(1)
    }

    public boolean invalidateSession(String token) {
        return sessions.remove(token).isPresent();  // O(1)
    }
}
```

**Session Lifecycle**:

```
    User logs in
         │
         ▼
┌─────────────────┐
│ createSession() │──→ Generate UUID token
└─────────────────┘     Store in MyHashMap
         │
         ▼
┌─────────────────┐
│   getSession()  │──→ O(1) lookup by token
└─────────────────┘     Check expiration
         │              Touch (update lastAccessed)
         ▼
┌─────────────────┐
│invalidateSession│──→ Remove from MyHashMap
└─────────────────┘     Logout complete
```

---

### UserRegistry (Uses MyHashMap + SortAlgorithms)

User management with sorted views for reporting.

**Why MyHashMap + Sorting?**
- Fast user lookup by username (O(1))
- Sorted views for admin dashboards
- Filter by user type
- Find inactive users

```java
public class UserRegistry {
    private final MyMap<String, User> users = new MyHashMap<>();

    public User registerUser(UserType type, String username) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username exists");
        }
        User user = User.create(type, username);  // Factory Method
        users.put(username, user);
        return user;
    }

    public MyList<User> getUsersSortedByUsername() {
        MyList<User> userList = collectUsers();
        SortAlgorithms.mergeSort(userList, Comparator.comparing(User::getUsername));
        return userList;
    }

    public MyList<User> getUsersSortedByLastLogin() {
        MyList<User> userList = collectUsers();
        SortAlgorithms.mergeSort(userList, (u1, u2) -> {
            // Most recent first, nulls last
            if (u1.getLastLoginAt() == null) return 1;
            if (u2.getLastLoginAt() == null) return -1;
            return u2.getLastLoginAt().compareTo(u1.getLastLoginAt());
        });
        return userList;
    }
}
```

**Sorted Views**:

| View | Sort Key | Use Case |
|------|----------|----------|
| By username | Alphabetical | User directory |
| By last login | Most recent first | Activity monitoring |
| By creation time | Newest first | Recent signups |

---

### CachingHashMap (Decorator Pattern)

LRU cache wrapper for frequently accessed sessions.

**Why Decorator?**
- Adds caching without modifying SessionStore
- Transparent to callers
- Can wrap any MyMap implementation
- Demonstrates composition over inheritance

```java
// Without caching
MyMap<String, UserSession> store = new MyHashMap<>();

// With LRU caching (decorator)
MyMap<String, UserSession> cachedStore = new CachingHashMap<>(store, 100);

// Same interface, automatic caching
cachedStore.put(token, session);
cachedStore.get(token);  // First call: cache miss, fetch from backing
cachedStore.get(token);  // Second call: cache hit!
```

**LRU Eviction**:

```
Cache (max size 3):
Access order: [token1, token2, token3]
                ↑                  ↑
               LRU               MRU

Add token4:
1. Evict LRU (token1)
2. Add token4 as MRU

New state: [token2, token3, token4]
```

**Cache Statistics**:

```java
CachingHashMap<String, UserSession> cache = new CachingHashMap<>(backing, 100);

// After many operations
cache.getCacheHits();     // 8500
cache.getCacheMisses();   // 1500
cache.getHitRatio();      // 0.85 (85% hit rate)
cache.getEvictions();     // 9400
```

---

## Data Flow

### User Registration

```
Client                  UserRegistry              MyHashMap
   │                         │                        │
   │  registerUser(ADMIN,    │                        │
   │    "alice")             │                        │
   │────────────────────────>│                        │
   │                         │  containsKey("alice")  │
   │                         │───────────────────────>│
   │                         │        false           │
   │                         │<───────────────────────│
   │                         │                        │
   │                         │  User.create(ADMIN,    │
   │                         │    "alice")            │
   │                         │  ──────┐               │
   │                         │        │ Factory       │
   │                         │  <─────┘ Method        │
   │                         │                        │
   │                         │  put("alice", user)    │
   │                         │───────────────────────>│
   │                         │                        │
   │      AdminUser          │                        │
   │<────────────────────────│                        │
```

### Authentication Flow

```
Client        SessionStore       MyHashMap      CachingHashMap
   │               │                 │                │
   │ login(user)   │                 │                │
   │──────────────>│                 │                │
   │               │ createSession() │                │
   │               │ ───────┐        │                │
   │               │        │        │                │
   │               │ <──────┘        │                │
   │               │                 │                │
   │               │ put(token, session)              │
   │               │───────────────────────────────-->│
   │               │                 │   put(token)   │
   │               │                 │<───────────────│
   │               │                 │                │
   │   session     │                 │                │
   │<──────────────│                 │                │
   │               │                 │                │
   │ getSession(token)               │                │
   │──────────────>│                 │                │
   │               │ get(token)      │                │
   │               │───────────────────────────────-->│
   │               │                 │  cache hit!    │
   │               │                 │<───────────────│
   │               │                 │                │
   │   session     │                 │                │
   │<──────────────│                 │                │
```

---

## Design Patterns in Action

### Factory Method (User.create)

```java
public abstract class User {
    public static User create(UserType type, String username) {
        return switch (type) {
            case ADMIN -> new AdminUser(username);
            case DEVELOPER -> new DeveloperUser(username);
            case VIEWER -> new ViewerUser(username);
        };
    }
}
```

**Benefits**:
- Clients don't instantiate subclasses directly
- Easy to add new user types
- Centralized permission logic
- Type-safe creation

### Strategy Pattern (HashStrategy)

```java
// Default strategy
MyHashMap<String, User> users = new MyHashMap<>();

// Custom strategy for testing
users.setHashStrategy(new DJB2HashStrategy());
```

**Benefits**:
- Swappable algorithms at runtime
- Easy testing with mock strategies
- Separation of concerns

### Decorator Pattern (CachingHashMap)

```java
// Base implementation
MyMap<String, Session> store = new MyHashMap<>();

// Add caching behavior
MyMap<String, Session> cached = new CachingHashMap<>(store, 100);

// Stack multiple decorators
MyMap<String, Session> logged = new LoggingHashMap<>(cached);
```

**Benefits**:
- Add behavior without modification
- Compose features dynamically
- Single Responsibility Principle

---

## Example: Full Authentication Scenario

```java
// Setup
UserRegistry registry = new UserRegistry();
SessionStore sessions = new SessionStore();

// 1. Register users
User admin = registry.registerUser(UserType.ADMIN, "alice");
User dev = registry.registerUser(UserType.DEVELOPER, "bob");
User viewer = registry.registerUser(UserType.VIEWER, "carol");

// 2. User logs in
UserSession session = sessions.createSession(admin);
String token = session.getToken();  // "550e8400-e29b-..."

// 3. Validate token on API request
Optional<UserSession> validated = sessions.getSession(token);
if (validated.isPresent()) {
    User user = validated.get().getUser();

    // Check permissions
    if (user.hasPermission(Permission.ADMIN)) {
        // Allow admin operation
    }
}

// 4. Admin views user list
MyList<User> activeUsers = registry.getUsersSortedByLastLogin();
for (User u : activeUsers) {
    System.out.printf("%s (%s) - Last login: %s%n",
        u.getUsername(),
        u.getType(),
        u.getLastLoginAt());
}

// 5. User logs out
sessions.invalidateSession(token);

// 6. Cleanup expired sessions
int removed = sessions.cleanupExpiredSessions();
```

---

## Integration with Phase 1

The Auth Service uses Phase 1 components:

```
SessionStore
├── MyHashMap<String, UserSession>
│   └── uses MyLinkedList (for collision chains)
│
UserRegistry
├── MyHashMap<String, User>
│   └── uses MyLinkedList
├── MyArrayList<User> (for sorted views)
└── SortAlgorithms
    └── operates on MyList<T>
```

---

## Integration Points for Future Phases

### Phase 3 (Trees & Heaps)
- Priority queue for session expiration
- Tree-based user permission hierarchy

### Phase 4 (Graphs)
- User relationship graphs (reporting structure)
- Permission dependency resolution

### Phase 5 (Concurrency)
- Thread-safe session store
- Concurrent user registry

---

*Part of the Omni-System Grimoire - Phase 2: Organization & Speed*
