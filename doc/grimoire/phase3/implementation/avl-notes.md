# AVLTree Implementation Notes

## Design Decisions

### 1. Extends BinarySearchTree
```java
public class AVLTree<T extends Comparable<T>> extends BinarySearchTree<T>
```
Reuses all BST logic; only overrides insert and delete to add rebalancing.

### 2. AVLNode Extends Node
```java
protected static class AVLNode<T> extends Node<T> {
    int height;
}
```
Adds height field for O(1) balance factor calculation.

### 3. Protected Insert Method
Override pattern:
```java
@Override
public void insert(T value) {
    root = insertAvl(root, value);  // Custom insert
}
```
Keeps BST's public interface, changes internal implementation.

---

## Rotation Cases

| Imbalance | Left BF | Right BF | Rotation |
|-----------|---------|----------|----------|
| Left-heavy (bf > 1) | ≥ 0 | - | Right |
| Left-heavy (bf > 1) | < 0 | - | Left-Right |
| Right-heavy (bf < -1) | - | ≤ 0 | Left |
| Right-heavy (bf < -1) | - | > 0 | Right-Left |

### Left-Right Rotation (LR)
```java
private Node<T> rotateLeftRight(Node<T> node) {
    node.left = rotateLeft(node.left);   // Fix left child first
    return rotateRight(node);            // Then fix node
}
```

---

## Gotchas

### 1. Height Update Order
**Wrong:** Update root first, then children
**Right:** Update children (during rotation), then root

```java
private Node<T> rotateRight(Node<T> node) {
    Node<T> newRoot = node.left;
    node.left = newRoot.right;
    newRoot.right = node;
    updateHeight(node);      // Child first
    updateHeight(newRoot);   // New root second
    return newRoot;
}
```

### 2. Rebalance Returns Node
Rotations return a new subtree root. Must reassign:
```java
node.left = rebalance(node.left);  // ✓
rebalance(node.left);               // ✗ Ignores new root!
```

### 3. Delete Needs Rebalancing Too
Override `deleteRecursive` to rebalance after BST deletion:
```java
@Override
protected Node<T> deleteRecursive(Node<T> node, T value) {
    node = super.deleteRecursive(node, value);
    return node == null ? null : rebalance(node);
}
```

---

## Complexity

| Operation | Time | Rotations |
|-----------|------|-----------|
| Insert | O(log n) | ≤ 2 |
| Delete | O(log n) | ≤ O(log n) |
| Search | O(log n) | 0 |
| Rotation | O(1) | - |

Delete may require rotations at each ancestor level.
