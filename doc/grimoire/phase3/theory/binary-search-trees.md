# Binary Search Trees

> An ordered tree where left < node < right, enabling O(log n) average operations.

## The BST Property

For every node in a BST:
- All values in the **left subtree** are less than the node's value
- All values in the **right subtree** are greater than the node's value

```
        8
       / \
      3   10
     / \    \
    1   6   14
       / \
      4   7
```

## Core Operations

### Search
Start at root, compare target with current node:
- Equal → found
- Less → go left
- Greater → go right

```java
Optional<T> find(T value) {
    Node<T> current = root;
    while (current != null) {
        int cmp = value.compareTo(current.value);
        if (cmp == 0) return Optional.of(current.value);
        current = (cmp < 0) ? current.left : current.right;
    }
    return Optional.empty();
}
```

### Insert
Search for position, create node at null spot.

### Delete
Three cases:
1. **Leaf** → remove directly
2. **One child** → replace with child
3. **Two children** → replace with in-order successor (or predecessor)

## Complexity

| Operation | Average | Worst |
|-----------|---------|-------|
| Search | O(log n) | O(n) |
| Insert | O(log n) | O(n) |
| Delete | O(log n) | O(n) |
| Space | O(n) | O(n) |

## The Degeneration Problem

Inserting sorted data creates a **linked list**:

```
Insert 1, 2, 3, 4, 5:

1
 \
  2
   \
    3
     \
      4
       \
        5

Height = n-1 (worst case)
```

**Solution:** Self-balancing trees (AVL, Red-Black).

## Traversals

| Type | Order | Use Case |
|------|-------|----------|
| In-order | Left, Root, Right | Sorted output |
| Pre-order | Root, Left, Right | Copy tree, serialization |
| Post-order | Left, Right, Root | Delete tree, expression evaluation |
| Level-order | BFS by level | Print tree structure |

## Mastery Check

1. Insert 1-100 sequentially → observe height = 99
2. Search for element 50 → count comparisons (should be ~50)
3. Delete node with two children → verify in-order successor replacement
