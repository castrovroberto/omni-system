# BinarySearchTree Implementation Notes

## Design Decisions

### Inner Node Class
```java
protected static class Node<T> {
    T value;
    Node<T> left;
    Node<T> right;
}
```
- `protected static` allows `AVLTree` to extend with `AVLNode`
- No parent pointer (simpler, recursive approach)

### Optional Return Type
`find()` returns `Optional<T>` rather than `null`:
```java
public Optional<T> find(T value)
```
This follows the SRD mandate: *"Never return null from collection operations."*

### Comparable Bound
```java
public class BinarySearchTree<T extends Comparable<T>>
```
Natural ordering required. Alternative: accept `Comparator<T>` in constructor.

## Traversal Implementations

Both **recursive** and **iterative** versions provided:

| Traversal | Recursive | Iterative |
|-----------|-----------|-----------|
| In-order | `inOrder()` | `inOrderIterative()` |
| Pre-order | `preOrder()` | `preOrderIterative()` |
| Post-order | `postOrder()` | `postOrderIterative()` |
| Level-order | - | `levelOrder()` (BFS) |

Iterative versions use explicit stack (or queue for level-order) to avoid stack overflow on deep trees.

## Delete: Three Cases

```java
// Case 1: Leaf node - just remove
if (node.left == null && node.right == null) return null;

// Case 2: One child - replace with child
if (node.left == null) return node.right;
if (node.right == null) return node.left;

// Case 3: Two children - replace with in-order successor
Node<T> successor = findMinNode(node.right);
node.value = successor.value;
node.right = deleteRecursive(node.right, successor.value);
```

## Edge Cases Handled

- Null input → `IllegalArgumentException`
- Empty tree → `Optional.empty()` or height = -1
- Duplicate values → ignored (no update)
- Single element tree → proper leaf handling

## Extension Points

`AVLTree` extends by:
1. Overriding `insert()` to call `insertAvl()` with rebalancing
2. Overriding `deleteRecursive()` to add rebalancing
3. Using `AVLNode` with height field
