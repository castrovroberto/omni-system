# Visitor Pattern

> Add operations to a hierarchy without modifying classes.

## Problem
Calculate disk usage across files and directories without adding `calculateUsage()` to every node type.

## Solution

```
<<interface>>                     <<interface>>
FileSystemNode                    FileSystemVisitor
  + accept(visitor)        ─────>   + visitFile(FileNode)
        △                           + visitDirectory(DirectoryNode)
        │                                   △
   ┌────┴────┐                             │
FileNode   DirectoryNode            DiskUsageVisitor
```

## Double Dispatch

The visitor pattern uses **double dispatch**:
1. Client calls `node.accept(visitor)`
2. Node calls `visitor.visitFile(this)` or `visitor.visitDirectory(this)`
3. Correct method chosen based on both node type AND visitor type

## Implementation

```java
public interface FileSystemVisitor {
    void visitFile(FileNode file);
    void visitDirectory(DirectoryNode dir);
}

public class DiskUsageVisitor implements FileSystemVisitor {
    private long totalBytes = 0;
    
    @Override
    public void visitFile(FileNode file) {
        totalBytes += file.getSize();
    }
    
    @Override
    public void visitDirectory(DirectoryNode dir) {
        // Recursively visit children
        for (FileSystemNode child : dir.getChildren()) {
            child.accept(this);
        }
    }
    
    public long getTotalBytes() { return totalBytes; }
}

// In FileNode:
public void accept(FileSystemVisitor visitor) {
    visitor.visitFile(this);
}

// In DirectoryNode:
public void accept(FileSystemVisitor visitor) {
    visitor.visitDirectory(this);
}
```

## Usage

```java
DiskUsageVisitor visitor = new DiskUsageVisitor();
rootDir.accept(visitor);
long totalSize = visitor.getTotalBytes();
```

## Benefits
- **Open/Closed**: Add new operations without modifying nodes
- **Single Responsibility**: Each visitor handles one concern
- **Accumulation**: Visitors can collect data across traversal

## Trade-offs
- Adding new node types requires updating all visitors
- Breaks encapsulation (visitors access node internals)

## Used In
- [DiskUsageVisitor](../../../../src/main/java/com/omni/app/fs/DiskUsageVisitor.java)
