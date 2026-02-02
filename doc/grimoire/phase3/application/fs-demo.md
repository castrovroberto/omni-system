# File System & Job Scheduler Demo

> Phase 3 demo application showcasing Composite, Command, and Visitor patterns.

---

## Components

### VirtualFileSystem

The central facade for file operations:

```java
VirtualFileSystem fs = new VirtualFileSystem();

// Create directories
fs.mkdir("/home");
fs.mkdir("/home/user");

// Create files
fs.createFile("/home/user/notes.txt", 1024);
fs.createFile("/home/user/photo.jpg", 2048576);

// Navigate
DirectoryNode home = fs.getDirectory("/home");
FileSystemNode file = fs.get("/home/user/notes.txt");
```

### FileIndex (AVL-backed Lookup)

O(log n) file lookup by name:

```java
FileIndex index = new FileIndex();
index.add(fileNode);
Optional<FileNode> found = index.find("notes.txt");
```

Uses `AVLTree<FileNode>` internally with name-based comparator.

### JobScheduler (Heap-backed Priority Queue)

Execute operations in priority order:

```java
JobScheduler scheduler = new JobScheduler();

// Priority 1 = high, 10 = low
scheduler.schedule(new CreateFileJob(fs, "/tmp/urgent.txt", 100, 1));
scheduler.schedule(new DeleteFileJob(fs, "/tmp/old.txt", 10));

// Execute highest priority first
scheduler.executeNext();  // Creates urgent.txt (priority 1)
scheduler.executeNext();  // Deletes old.txt (priority 10)
```

### DiskUsageVisitor

Calculate total size recursively:

```java
DirectoryNode root = fs.getRoot();
DiskUsageVisitor visitor = new DiskUsageVisitor();
root.accept(visitor);
long totalBytes = visitor.getTotalBytes();
```

---

## Pattern Usage

### Composite Pattern

```
FileSystemNode (interface)
    │
    ├── FileNode (leaf)
    │       └── size: long
    │
    └── DirectoryNode (composite)
            └── children: List<FileSystemNode>
```

Both files and directories implement `FileSystemNode`. Clients treat them uniformly.

### Command Pattern

```
Job (interface)
    │
    ├── CreateFileJob
    │       ├── execute(): creates file
    │       └── undo(): deletes file
    │
    └── DeleteFileJob
            ├── execute(): deletes file  
            └── undo(): restores file
```

Operations are objects that can be scheduled, executed, and undone.

### Visitor Pattern

```
FileSystemVisitor (interface)
    │
    └── DiskUsageVisitor
            └── accumulates file sizes during traversal
```

Add new operations without modifying FileNode/DirectoryNode.

---

## Usage Scenarios

### Scenario 1: Tree Traversal
```java
// Print all files recursively
class PrintVisitor implements FileSystemVisitor {
    public void visitFile(FileNode f) {
        System.out.println(f.getName() + " (" + f.getSize() + " bytes)");
    }
    public void visitDirectory(DirectoryNode d) {
        for (FileSystemNode child : d.getChildren()) {
            child.accept(this);
        }
    }
}
```

### Scenario 2: Batch Operations
```java
// Queue multiple operations, execute in priority order
scheduler.schedule(new CreateFileJob(fs, "/a.txt", 100, 5));
scheduler.schedule(new CreateFileJob(fs, "/b.txt", 100, 3));
scheduler.schedule(new CreateFileJob(fs, "/c.txt", 100, 1));

scheduler.executeAll();  // Creates c.txt, then b.txt, then a.txt
```

### Scenario 3: Undo Support
```java
Job job = new DeleteFileJob(fs, "/important.txt", 1);
job.execute();   // File deleted
job.undo();      // File restored
```

---

*Part of the Omni-System Grimoire - Phase 3*
