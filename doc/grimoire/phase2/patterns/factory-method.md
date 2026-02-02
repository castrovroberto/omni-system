# Factory Method Pattern

> Define an interface for creating objects, letting subclasses decide which class to instantiate.

## Problem
Creating users requires different initialization based on type (Admin, Developer, Viewer). Clients shouldn't know about concrete classes.

## Solution

```
User (abstract)
  + {static} create(type, name): User  ← Factory Method
        △
        │
   ┌────┼────────┐
   │    │        │
Admin  Dev    Viewer
```

## Implementation

```java
public abstract class User {
    private final String username;
    protected final Set<Permission> permissions;
    
    protected User(String username) {
        this.username = username;
        this.permissions = new HashSet<>();
    }
    
    // Factory Method
    public static User create(UserType type, String username) {
        return switch (type) {
            case ADMIN -> new AdminUser(username);
            case DEVELOPER -> new DeveloperUser(username);
            case VIEWER -> new ViewerUser(username);
        };
    }
    
    public abstract UserType getType();
    
    public boolean hasPermission(Permission p) {
        return permissions.contains(p);
    }
}

class AdminUser extends User {
    AdminUser(String username) {
        super(username);
        permissions.addAll(Set.of(
            Permission.READ, Permission.WRITE, 
            Permission.DELETE, Permission.ADMIN
        ));
    }
    
    @Override
    public UserType getType() { return UserType.ADMIN; }
}
```

## Usage

```java
// Client doesn't instantiate concrete classes
User admin = User.create(UserType.ADMIN, "alice");
User dev = User.create(UserType.DEVELOPER, "bob");

// Polymorphic behavior
admin.hasPermission(Permission.ADMIN);  // true
dev.hasPermission(Permission.ADMIN);    // false
```

## Benefits
- **Encapsulation**: Creation logic hidden from clients
- **Extensibility**: Add new types without changing client code
- **Type safety**: Compiler enforces valid UserType values

## Used In
- [User](../../../../src/main/java/com/omni/app/auth/User.java)
- [AdminUser](../../../../src/main/java/com/omni/app/auth/AdminUser.java)
- [DeveloperUser](../../../../src/main/java/com/omni/app/auth/DeveloperUser.java)
- [ViewerUser](../../../../src/main/java/com/omni/app/auth/ViewerUser.java)
