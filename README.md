# Omni-System

**An educational project for deep mastery of data structures, algorithms, and design patterns through hands-on implementation.**

## Overview

The Omni-System is a **pedagogical project** designed to teach fundamental computer science concepts by building a Mock Cloud Resource Orchestrator from scratch. This project follows a bottom-up approach: starting from primitive memory management and progressively building toward complex interconnected systems.

**⚠️ Important:** This is **NOT a production system**. The goal is learning and understanding, not deployment or optimization. Every implementation prioritizes clarity and educational value over performance.

## Project Structure

```
com.omni/
├── core/          # Custom implementations of fundamental data structures
├── app/           # Demo application (Cloud Resource Orchestrator)
└── test/          # Comprehensive test suite with benchmarks

doc/
├── omni-system-srd.md    # Complete Software Requirements Document
├── briefing.md           # Original project briefing
└── grimoire/            # Living documentation (theory, implementation, benchmarks)
```

## Learning Objectives

By completing this project, you will:

1. **Internalize** how data structures work at the memory/pointer level
2. **Understand** algorithmic complexity through empirical measurement
3. **Apply** design patterns in contexts where they solve real problems
4. **Develop** the ability to choose the right tool for each problem
5. **Practice** test-driven development and benchmarking methodology

## Phase Overview

| Phase | Theme | Key Components | Duration |
|-------|-------|---------------|----------|
| **Phase 1** | The Foundation | Arrays, Linked Lists, Linear/Binary Search | 2-3 weeks |
| **Phase 2** | Organization & Speed | Hash Maps, Merge/Quick Sort | 2-3 weeks |
| **Phase 3** | Hierarchies | BST, AVL Trees, Heaps | 3-4 weeks |
| **Phase 4** | Complexity | Graphs, Dijkstra's, Topological Sort | 3-4 weeks |
| **Phase 5** | Concurrency | Thread-safe wrappers, Concurrent structures | 2 weeks |

Each phase includes:
- Custom data structure implementations
- Core algorithms
- Design pattern applications
- Demo application features
- Comprehensive tests and benchmarks

## Tech Stack

- **Language:** Java 17+
- **Build Tool:** Maven/Gradle
- **Testing:** JUnit 5
- **Benchmarking:** JMH (Java Microbenchmark Harness)
- **Documentation:** Markdown with PlantUML diagrams

## Prerequisites

| Area | Expected Knowledge |
|------|-------------------|
| Java | Generics, Collections API (as reference), OOP principles |
| Algorithms | Big-O notation, basic complexity analysis |
| Tools | Git, Maven/Gradle, JUnit basics |
| Concepts | Memory model (stack vs heap), references vs values |

## Getting Started

### 1. Review the Documentation

Start with the comprehensive [Software Requirements Document](doc/omni-system-srd.md) for:
- Detailed phase specifications
- Interface contracts
- Testing strategies
- Mastery checks
- Design pattern mappings

### 2. Set Up the Project

```bash
# Clone the repository
git clone <repository-url>
cd omni-system

# Set up Maven project structure (if not already done)
mvn archetype:generate \
  -DgroupId=com.omni \
  -DartifactId=omni-system \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

### 3. Begin with Phase 1

Start by implementing the foundation:
- `MyArrayList<T>`
- `MyLinkedList<T>`
- Linear and Binary Search algorithms
- Log Management System demo

See the [SRD Phase 1 section](doc/omni-system-srd.md#51-phase-1-the-foundation) for detailed requirements.

## Design Patterns Covered

| Pattern | Phase | Application |
|---------|-------|-------------|
| Iterator | 1 | Traversing lists |
| Builder | 1 | Constructing SystemEvent objects |
| Singleton | 1 | LogManager instance |
| Strategy | 2 | Swappable hash functions |
| Factory Method | 2 | Creating User subtypes |
| Decorator | 2 | CachingHashMap wrapper |
| Composite | 3 | FileNode hierarchy |
| Command | 3 | Job encapsulation |
| Visitor | 3 | File system operations |
| Observer | 4 | Service dependency notifications |
| State | 4 | Server lifecycle management |
| Template Method | 4 | Graph traversal algorithms |

## Success Criteria

Each phase is complete when:
- ✅ All required data structures implemented
- ✅ All required algorithms implemented
- ✅ All design patterns applied correctly
- ✅ Unit tests achieve >90% code coverage
- ✅ Mastery checks pass
- ✅ Benchmarks completed and documented
- ✅ Grimoire documentation written
- ✅ Demo application functional

## Documentation

The project maintains comprehensive documentation:

- **[Software Requirements Document](doc/omni-system-srd.md)** - Complete project specification
- **[Original Briefing](doc/briefing.md)** - Initial project vision
- **Grimoire** (`doc/grimoire/`) - Living documentation including:
  - Theory explanations
  - Implementation blueprints (UML)
  - Design decisions and gotchas
  - Benchmark results
  - Application usage examples

## Contributing

This is a personal educational project. However, suggestions and improvements to documentation are welcome!

## License

Educational use only.

## Author

Roberto Castro - January 2026

---

**Remember:** The goal is understanding, not optimization. Take time to explore, experiment, and internalize each concept before moving to the next phase.
