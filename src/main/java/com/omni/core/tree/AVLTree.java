package com.omni.core.tree;

/**
 * A self-balancing AVL tree extending {@link BinarySearchTree}.
 *
 * <p>Maintains balance by ensuring the height difference between left and right subtrees of any
 * node is at most 1. Uses rotations to rebalance after insertions and deletions.
 *
 * <pre>
 *   Balanced:       Unbalanced (right-heavy):
 *       4                 2
 *      / \                 \
 *     2   6                 4
 *    / \                     \
 *   1   3                     6
 * </pre>
 *
 * @param <T> the type of elements, must be Comparable
 */
public class AVLTree<T extends Comparable<T>> extends BinarySearchTree<T> {

  /** AVL node with an additional height field. */
  protected static class AVLNode<T> extends Node<T> {
    int height;

    AVLNode(T value) {
      super(value);
      this.height = 0;
    }
  }

  @Override
  public void insert(T value) {
    if (value == null) {
      throw new IllegalArgumentException("Cannot insert null value");
    }
    root = insertAvl(root, value);
  }

  private Node<T> insertAvl(Node<T> node, T value) {
    if (node == null) {
      size++;
      return new AVLNode<>(value);
    }
    int cmp = value.compareTo(node.value);
    if (cmp < 0) {
      node.left = insertAvl(node.left, value);
    } else if (cmp > 0) {
      node.right = insertAvl(node.right, value);
    } else {
      return node; // Duplicate ignored
    }
    return rebalance(node);
  }

  @Override
  protected Node<T> deleteRecursive(Node<T> node, T value) {
    node = super.deleteRecursive(node, value);
    if (node == null) {
      return null;
    }
    return rebalance(node);
  }

  private int getHeight(Node<T> node) {
    if (node == null) {
      return -1;
    }
    if (node instanceof AVLNode) {
      return ((AVLNode<T>) node).height;
    }
    return -1;
  }

  private void updateHeight(Node<T> node) {
    if (node instanceof AVLNode) {
      ((AVLNode<T>) node).height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }
  }

  private int balanceFactor(Node<T> node) {
    if (node == null) {
      return 0;
    }
    return getHeight(node.left) - getHeight(node.right);
  }

  private Node<T> rotateRight(Node<T> node) {
    Node<T> newRoot = node.left;
    node.left = newRoot.right;
    newRoot.right = node;
    updateHeight(node);
    updateHeight(newRoot);
    return newRoot;
  }

  private Node<T> rotateLeft(Node<T> node) {
    Node<T> newRoot = node.right;
    node.right = newRoot.left;
    newRoot.left = node;
    updateHeight(node);
    updateHeight(newRoot);
    return newRoot;
  }

  private Node<T> rotateLeftRight(Node<T> node) {
    node.left = rotateLeft(node.left);
    return rotateRight(node);
  }

  private Node<T> rotateRightLeft(Node<T> node) {
    node.right = rotateRight(node.right);
    return rotateLeft(node);
  }

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
}
