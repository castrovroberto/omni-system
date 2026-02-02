# Composite Pattern

> Treat individual objects and compositions uniformly.

## Problem
A file system has files (leaves) and directories (composites). Clients need to work with both without knowing which is which.

## Solution

```
<<interface>>
FileSystemNode
  + getName(): String
  + getSize(): long
  + isDirectory(): boolean
  + accept(visitor): void
        △
        │
   ┌────┴────┐
   │         │
FileNode   DirectoryNode
(leaf)     (composite)
             │
             └─ children: List<FileSystemNode>
```

## Implementation

```java
public interface FileSystemNode {
    String getName();
    long getSize();
    boolean isDirectory();
    void accept(FileSystemVisitor visitor);
}

public class FileNode implements FileSystemNode {
    private final String name;
    private final long size;
    
    @Override
    public long getSize() { return size; }
    
    @Override
    public boolean isDirectory() { return false; }
}

public class DirectoryNode implements FileSystemNode {
    private final String name;
    private final MyList<FileSystemNode> children;
    
    @Override
    public long getSize() {
        long total = 0;
        for (FileSystemNode child : children) {
            total += child.getSize();  // Recursive!
        }
        return total;
    }
    
    @Override
    public boolean isDirectory() { return true; }
    
    public void addChild(FileSystemNode node) {
        children.add(node);
    }
}
```

## Benefits
- **Uniform treatment**: Same interface for files and folders
- **Recursive operations**: `getSize()` naturally recurses through tree
- **Open for extension**: Add new node types without changing interface

## Used In
- [VirtualFileSystem](../../../../src/main/java/com/omni/app/fs/VirtualFileSystem.java)
