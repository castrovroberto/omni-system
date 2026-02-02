# AVL Trees

> Self-balancing BST with guaranteed O(log n) operations via rotations.

## The Balance Factor

For each node: `balance = height(left) - height(right)`

An AVL tree maintains: **|balance| ≤ 1** for all nodes.

```
Balanced (valid AVL):        Unbalanced (invalid):
      4 (bf=0)                   2 (bf=-2)
     / \                          \
    2   6 (bf=0)                   4 (bf=-1)
   / \                              \
  1   3                              6
```

## Rotations

### Right Rotation (LL case)
Left subtree is too heavy on the left.

```
Before:          After:
    z               y
   / \             / \
  y   T4    →     x   z
 / \             /   / \
x   T3          T1  T3  T4
```

### Left Rotation (RR case)
Right subtree is too heavy on the right.

```
Before:          After:
  z                 y
 / \               / \
T1  y      →      z   x
   / \           / \   \
  T2  x         T1 T2  T3
```

### Left-Right Rotation (LR case)
Left subtree is heavy on the right → left rotate left child, then right rotate root.

### Right-Left Rotation (RL case)
Right subtree is heavy on the left → right rotate right child, then left rotate root.

## Rebalancing Algorithm

After every insert/delete:
1. Update heights bottom-up
2. Calculate balance factor
3. If |balance| > 1, apply rotation:
   - balance > 1 & left child balance ≥ 0 → Right rotation
   - balance > 1 & left child balance < 0 → Left-Right rotation
   - balance < -1 & right child balance ≤ 0 → Left rotation
   - balance < -1 & right child balance > 0 → Right-Left rotation

```java
private Node<T> rebalance(Node<T> node) {
    updateHeight(node);
    int balance = balanceFactor(node);
    
    if (balance > 1) {
        if (balanceFactor(node.left) < 0) {
            return rotateLeftRight(node);
        }
        return rotateRight(node);
    }
    if (balance < -1) {
        if (balanceFactor(node.right) > 0) {
            return rotateRightLeft(node);
        }
        return rotateLeft(node);
    }
    return node;
}
```

## Complexity

| Operation | Time | Space |
|-----------|------|-------|
| Search | O(log n) | O(1) |
| Insert | O(log n) | O(log n) stack |
| Delete | O(log n) | O(log n) stack |
| Rotation | O(1) | O(1) |

**Height bound:** h ≤ 1.44 × log₂(n + 2)

## Mastery Check

1. Insert 1-100 sequentially → verify height ≤ 7
2. Insert/delete random values → verify balance factor always ≤ 1
3. Trace a Left-Right rotation step-by-step
