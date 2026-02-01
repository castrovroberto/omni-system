package com.omni.core.tree;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyLinkedList;
import com.omni.core.list.MyList;
import java.util.Optional;

/**
 * A binary search tree implementation using comparable elements.
 *
 * <p>Maintains the BST invariant: for every node, all values in the left subtree are less than the
 * node's value, and all values in the right subtree are greater.
 *
 * <pre>
 *        8
 *       / \
 *      3   10
 *     / \    \
 *    1   6   14
 * </pre>
 *
 * @param <T> the type of elements, must be Comparable
 */
public class BinarySearchTree<T extends Comparable<T>> {

  /** Node in the binary search tree. */
  protected static class Node<T> {
    T value;
    Node<T> left;
    Node<T> right;

    Node(T value) {
      this.value = value;
    }
  }

  protected Node<T> root;
  protected int size;

  /** Constructs an empty binary search tree. */
  public BinarySearchTree() {
    this.root = null;
    this.size = 0;
  }

  /**
   * Inserts a value into the tree.
   *
   * @param value the value to insert
   * @throws IllegalArgumentException if value is null
   */
  public void insert(T value) {
    if (value == null) {
      throw new IllegalArgumentException("Cannot insert null value");
    }
    root = insertRecursive(root, value);
  }

  protected Node<T> insertRecursive(Node<T> node, T value) {
    if (node == null) {
      size++;
      return new Node<>(value);
    }
    int cmp = value.compareTo(node.value);
    if (cmp < 0) {
      node.left = insertRecursive(node.left, value);
    } else if (cmp > 0) {
      node.right = insertRecursive(node.right, value);
    }
    // Duplicate values are ignored
    return node;
  }

  /**
   * Finds a value in the tree.
   *
   * @param value the value to find
   * @return an Optional containing the value if found, empty otherwise
   */
  public Optional<T> find(T value) {
    if (value == null) {
      return Optional.empty();
    }
    return findRecursive(root, value);
  }

  private Optional<T> findRecursive(Node<T> node, T value) {
    if (node == null) {
      return Optional.empty();
    }
    int cmp = value.compareTo(node.value);
    if (cmp < 0) {
      return findRecursive(node.left, value);
    } else if (cmp > 0) {
      return findRecursive(node.right, value);
    }
    return Optional.of(node.value);
  }

  /**
   * Deletes a value from the tree.
   *
   * @param value the value to delete
   * @return an Optional containing the deleted value if found, empty otherwise
   */
  public Optional<T> delete(T value) {
    if (value == null || root == null) {
      return Optional.empty();
    }
    Optional<T> found = find(value);
    if (found.isPresent()) {
      root = deleteRecursive(root, value);
      size--;
    }
    return found;
  }

  protected Node<T> deleteRecursive(Node<T> node, T value) {
    if (node == null) {
      return null;
    }
    int cmp = value.compareTo(node.value);
    if (cmp < 0) {
      node.left = deleteRecursive(node.left, value);
    } else if (cmp > 0) {
      node.right = deleteRecursive(node.right, value);
    } else {
      // Case 1: No children
      if (node.left == null && node.right == null) {
        return null;
      }
      // Case 2: One child
      if (node.left == null) {
        return node.right;
      }
      if (node.right == null) {
        return node.left;
      }
      // Case 3: Two children - replace with in-order successor
      Node<T> successor = findMinNode(node.right);
      node.value = successor.value;
      node.right = deleteRecursive(node.right, successor.value);
    }
    return node;
  }

  private Node<T> findMinNode(Node<T> node) {
    while (node.left != null) {
      node = node.left;
    }
    return node;
  }

  private Node<T> findMaxNode(Node<T> node) {
    while (node.right != null) {
      node = node.right;
    }
    return node;
  }

  /**
   * Returns the minimum value in the tree.
   *
   * @return an Optional containing the minimum value, empty if tree is empty
   */
  public Optional<T> min() {
    if (root == null) {
      return Optional.empty();
    }
    return Optional.of(findMinNode(root).value);
  }

  /**
   * Returns the maximum value in the tree.
   *
   * @return an Optional containing the maximum value, empty if tree is empty
   */
  public Optional<T> max() {
    if (root == null) {
      return Optional.empty();
    }
    return Optional.of(findMaxNode(root).value);
  }

  /**
   * Returns the height of the tree.
   *
   * @return the height, or -1 if empty
   */
  public int height() {
    return heightRecursive(root);
  }

  private int heightRecursive(Node<T> node) {
    if (node == null) {
      return -1;
    }
    return 1 + Math.max(heightRecursive(node.left), heightRecursive(node.right));
  }

  /**
   * Returns the number of elements in the tree.
   *
   * @return the size
   */
  public int size() {
    return size;
  }

  /**
   * Returns true if the tree contains no elements.
   *
   * @return true if empty
   */
  public boolean isEmpty() {
    return size == 0;
  }

  // ==================== Recursive Traversals ====================

  /**
   * Returns elements in in-order (left, root, right).
   *
   * @return a list of elements in sorted order
   */
  public MyList<T> inOrder() {
    MyList<T> result = new MyArrayList<>();
    inOrderRecursive(root, result);
    return result;
  }

  private void inOrderRecursive(Node<T> node, MyList<T> result) {
    if (node == null) {
      return;
    }
    inOrderRecursive(node.left, result);
    result.add(node.value);
    inOrderRecursive(node.right, result);
  }

  /**
   * Returns elements in pre-order (root, left, right).
   *
   * @return a list of elements in pre-order
   */
  public MyList<T> preOrder() {
    MyList<T> result = new MyArrayList<>();
    preOrderRecursive(root, result);
    return result;
  }

  private void preOrderRecursive(Node<T> node, MyList<T> result) {
    if (node == null) {
      return;
    }
    result.add(node.value);
    preOrderRecursive(node.left, result);
    preOrderRecursive(node.right, result);
  }

  /**
   * Returns elements in post-order (left, right, root).
   *
   * @return a list of elements in post-order
   */
  public MyList<T> postOrder() {
    MyList<T> result = new MyArrayList<>();
    postOrderRecursive(root, result);
    return result;
  }

  private void postOrderRecursive(Node<T> node, MyList<T> result) {
    if (node == null) {
      return;
    }
    postOrderRecursive(node.left, result);
    postOrderRecursive(node.right, result);
    result.add(node.value);
  }

  /**
   * Returns elements in level-order (breadth-first).
   *
   * @return a list of elements level by level
   */
  public MyList<T> levelOrder() {
    MyList<T> result = new MyArrayList<>();
    if (root == null) {
      return result;
    }
    MyLinkedList<Node<T>> queue = new MyLinkedList<>();
    queue.addLast(root);
    while (!queue.isEmpty()) {
      Node<T> current = queue.removeFirst();
      result.add(current.value);
      if (current.left != null) {
        queue.addLast(current.left);
      }
      if (current.right != null) {
        queue.addLast(current.right);
      }
    }
    return result;
  }

  // ==================== Iterative Traversals ====================

  /**
   * Returns elements in in-order using an iterative approach.
   *
   * @return a list of elements in sorted order
   */
  public MyList<T> inOrderIterative() {
    MyList<T> result = new MyArrayList<>();
    MyLinkedList<Node<T>> stack = new MyLinkedList<>();
    Node<T> current = root;
    while (current != null || !stack.isEmpty()) {
      while (current != null) {
        stack.addLast(current);
        current = current.left;
      }
      current = stack.removeLast();
      result.add(current.value);
      current = current.right;
    }
    return result;
  }

  /**
   * Returns elements in pre-order using an iterative approach.
   *
   * @return a list of elements in pre-order
   */
  public MyList<T> preOrderIterative() {
    MyList<T> result = new MyArrayList<>();
    if (root == null) {
      return result;
    }
    MyLinkedList<Node<T>> stack = new MyLinkedList<>();
    stack.addLast(root);
    while (!stack.isEmpty()) {
      Node<T> current = stack.removeLast();
      result.add(current.value);
      if (current.right != null) {
        stack.addLast(current.right);
      }
      if (current.left != null) {
        stack.addLast(current.left);
      }
    }
    return result;
  }

  /**
   * Returns elements in post-order using an iterative approach with two stacks.
   *
   * @return a list of elements in post-order
   */
  public MyList<T> postOrderIterative() {
    MyList<T> result = new MyArrayList<>();
    if (root == null) {
      return result;
    }
    MyLinkedList<Node<T>> stack1 = new MyLinkedList<>();
    MyLinkedList<Node<T>> stack2 = new MyLinkedList<>();
    stack1.addLast(root);
    while (!stack1.isEmpty()) {
      Node<T> current = stack1.removeLast();
      stack2.addLast(current);
      if (current.left != null) {
        stack1.addLast(current.left);
      }
      if (current.right != null) {
        stack1.addLast(current.right);
      }
    }
    while (!stack2.isEmpty()) {
      result.add(stack2.removeLast().value);
    }
    return result;
  }
}
