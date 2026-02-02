# Command Pattern

> Encapsulate operations as objects with execute, undo, and priority.

## Problem
A job scheduler needs to:
- Queue operations for later execution
- Execute in priority order  
- Support undo functionality

## Solution

```
<<interface>>
Job
  + execute(): void
  + undo(): void
  + getPriority(): int
        △
        │
   ┌────┴────┐
   │         │
CreateFileJob  DeleteFileJob
```

## Implementation

```java
public interface Job {
    void execute();
    void undo();
    int getPriority();
}

public class CreateFileJob implements Job {
    private final VirtualFileSystem fs;
    private final String path;
    private final long size;
    private final int priority;
    
    @Override
    public void execute() {
        fs.createFile(path, size);
    }
    
    @Override
    public void undo() {
        fs.delete(path);
    }
    
    @Override
    public int getPriority() { return priority; }
}
```

## JobScheduler Integration

Uses `MyHeap` for priority-based execution:

```java
public class JobScheduler {
    private final MyHeap<Job> queue;
    
    public JobScheduler() {
        this.queue = new MyHeap<>(
            Comparator.comparingInt(Job::getPriority)
        );
    }
    
    public void schedule(Job job) {
        queue.insert(job);
    }
    
    public void executeNext() {
        if (!queue.isEmpty()) {
            queue.extractRoot().execute();
        }
    }
}
```

## Benefits
- **Decoupling**: Scheduler doesn't know operation details
- **Queueing**: Store operations for batch execution
- **Undo**: Built-in rollback capability
- **Prioritization**: Lower priority value = higher importance

## Used In
- [JobScheduler](../../../../src/main/java/com/omni/app/fs/JobScheduler.java)
- [CreateFileJob](../../../../src/main/java/com/omni/app/fs/CreateFileJob.java)
- [DeleteFileJob](../../../../src/main/java/com/omni/app/fs/DeleteFileJob.java)
