# Phase 3: Hierarchies

> Tree-based data structures, priority scheduling, and hierarchical patterns

---

## Overview

Phase 3 introduces **tree-based data structures** that enable hierarchical organization and efficient ordered operations. By implementing BST, AVL, and Heap from scratch, we internalize how balanced trees maintain O(log n) performance and how heaps power priority queues.

**Theme**: Hierarchy & Scheduling
**Demo Feature**: Virtual File System & Job Scheduler
**Status**: Complete

---

## Contents

### Theory

Conceptual explanations of data structures and their complexity analysis.

| Document | Description |
|----------|-------------|
| [Binary Search Trees](theory/binary-search-trees.md) | BST property, operations, degeneration problem |
| [AVL Trees](theory/avl-trees.md) | Balance factor, rotations, rebalancing algorithm |
| [Heaps](theory/heaps.md) | Heap property, array representation, Floyd's heapify |

### Implementation Notes

Design decisions, gotchas, and lessons learned during implementation.

| Document | Description |
|----------|-------------|
| [BinarySearchTree Notes](implementation/bst-notes.md) | Node class, Optional returns, traversals |
| [AVLTree Notes](implementation/avl-notes.md) | Height tracking, rotation cases, rebalancing |
| [MyHeap Notes](implementation/heap-notes.md) | Array indexing, sift operations, heapify |

### Benchmarks

Performance measurements and mastery check validation.

| Document | Description |
|----------|-------------|
| [Benchmark Results](benchmarks/results.md) | Tree balance verification, heap performance |

### Application

How Phase 3 components power the demo application.

| Document | Description |
|----------|-------------|
| [File System Demo](application/fs-demo.md) | VirtualFileSystem, JobScheduler, FileIndex usage |

### Blueprints

UML diagrams for visual reference.

| Diagram | Description |
|---------|-------------|
| [Tree Structures](blueprints/tree-structures.puml) | BST/AVL class hierarchy |
| [Heap Structure](blueprints/heap-structure.puml) | MyHeap with array backing |
| [File System](blueprints/file-system.puml) | Composite pattern structure |
| [Design Patterns](blueprints/design-patterns.puml) | Composite, Command, Visitor |

---

## Components Built

### Data Structures

| Component | Location | Tests |
|-----------|----------|-------|
| `BinarySearchTree<T>` | `com.omni.core.tree.BinarySearchTree` | 25+ tests |
| `AVLTree<T>` | `com.omni.core.tree.AVLTree` | 15+ tests |
| `MyHeap<T>` | `com.omni.core.tree.MyHeap` | 12+ tests |

### Design Patterns

| Pattern | Implementation | Purpose |
|---------|----------------|---------|
| **Composite** | `FileSystemNode`, `FileNode`, `DirectoryNode` | Uniform tree traversal |
| **Command** | `Job`, `CreateFileJob`, `DeleteFileJob` | Schedulable operations with undo |
| **Visitor** | `FileSystemVisitor`, `DiskUsageVisitor` | Add operations without modifying nodes |

### Demo Application

| Component | Uses | Purpose |
|-----------|------|---------|
| `VirtualFileSystem` | Composite | Hierarchical file/folder storage |
| `FileIndex` | AVL Tree | O(log n) file lookup by name |
| `JobScheduler` | Min-Heap | Priority-based job execution |
| `DiskUsageVisitor` | Visitor | Calculate folder sizes recursively |

---

## Mastery Checks

| Check | Status | Evidence |
|-------|--------|----------|
| BST degrades to O(n) with sorted input | PASS | Height = n-1 for sequential insert |
| AVL maintains height ≤ 1.44 log(n) | PASS | Height ≤ 7 for 100 sequential inserts |
| Heap extract returns min/max | PASS | Sorted output from repeated extract |
| Heapify is O(n) | PASS | Floyd's algorithm benchmarked |
| Visitor accumulates across tree | PASS | DiskUsageVisitor totals all file sizes |

---

## Key Learnings

### From BinarySearchTree
1. In-order traversal yields sorted sequence
2. Delete with two children requires successor/predecessor swap
3. Degeneration to linked list shows need for balancing
4. Iterative traversal prevents stack overflow on deep trees

### From AVLTree
1. Four rotation cases: LL, RR, LR, RL
2. Height updates must propagate up after rotations
3. Balance factor = height(left) - height(right)
4. Single insertion needs at most 2 rotations

### From MyHeap
1. Array representation eliminates pointer overhead
2. Parent/child navigation via arithmetic formulas
3. Floyd's heapify builds heap in O(n), not O(n log n)
4. Configurable comparator enables min-heap or max-heap

### From Design Patterns
1. Composite enables recursive algorithms on nested structures
2. Command decouples operation definition from execution
3. Visitor adds operations without modifying the class hierarchy
4. Double dispatch enables type-specific behavior

---

## Dependencies on Phase 1 & 2

```
BinarySearchTree / AVLTree
├── standalone (no dependencies on Phase 1/2)
└── uses MyArrayList for traversal result lists

MyHeap
├── uses MyArrayList as backing store
└── uses Comparator (Strategy pattern, like Phase 2)

VirtualFileSystem
├── uses MyArrayList (for children in DirectoryNode)
└── uses AVLTree (via FileIndex)

JobScheduler
└── uses MyHeap for priority queue
```

---

## Next Steps

Phase 3 is complete. Proceed to [Phase 4: Complexity](../phase4/index.md) to build:
- Graph representations (Adjacency List, Adjacency Matrix)
- Graph algorithms (DFS, BFS, Dijkstra, Topological Sort)
- Observer, State, Template Method patterns
- Network Topology demo application

---

*Part of the Omni-System Grimoire*
