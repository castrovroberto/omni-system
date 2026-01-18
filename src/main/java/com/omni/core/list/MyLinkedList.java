package com.omni.core.list;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A custom implementation of a Doubly Linked List.
 *
 * <p>This implementation uses <b>Sentinel Nodes</b> (Head and Tail) to simplify boundary conditions
 * (no need to check for null when adding/removing at ends).
 *
 * <pre>
 *   Structure:
 *
 *      +------+       +--------+       +--------+       +------+
 *      | HEAD | <---> | Node 0 | <---> | Node 1 | <---> | TAIL |
 *      +------+       +--------+       +--------+       +------+
 *     (Sentinel)                                       (Sentinel)
 * </pre>
 */
public class MyLinkedList<T> implements MyList<T> {

  private static class Node<T> {
    T value;
    Node<T> prev;
    Node<T> next;

    Node(T value, Node<T> prev, Node<T> next) {
      this.value = value;
      this.prev = prev;
      this.next = next;
    }
  }

  private final Node<T> head;
  private final Node<T> tail;
  private int size;
  private int modCount; // For fail-fast iterators

  public MyLinkedList() {
    // Initialize Sentinel Nodes
    //
    //   +------+       +------+
    //   | HEAD | ----> | TAIL |
    //   |      | <---- |      |
    //   +------+       +------+
    //
    head = new Node<>(null, null, null); // Sentinel head node
    tail = new Node<>(null, head, null); // Sentinel tail node
    head.next = tail;
    size = 0;
    modCount = 0;
  }

  @Override
  public void add(T element) {
    add(size, element);
  }

  @Override
  public void add(int index, T element) {
    Objects.checkIndex(index, size + 1); // Allows index == size for appending
    Node<T> current = getNodeForInsertion(index);

    // Insertion Logic:
    // We are inserting 'newNode' BEFORE 'current'.
    //
    // 1. Create New Node
    //    [current.prev] <--- [NewNode] ---> [current]
    //
    // 2. Link Neighbors to New Node
    //    [current.prev] ---> [NewNode] <--- [current]
    //
    Node<T> newNode = new Node<>(element, current.prev, current);
    current.prev.next = newNode;
    current.prev = newNode;
    size++;
    modCount++;
  }

  @Override
  public T get(int index) {
    Objects.checkIndex(index, size);
    return getNode(index).value;
  }

  @Override
  public T remove(int index) {
    Objects.checkIndex(index, size);
    Node<T> nodeToRemove = getNode(index);

    // Removal Logic:
    //
    //    [Prev] <---> [NodeToRemove] <---> [Next]
    //
    //    Step 1: Bypass NodeToRemove
    //    [Prev] -------------------------> [Next]
    //    [Prev] <------------------------- [Next]
    //
    nodeToRemove.prev.next = nodeToRemove.next;
    nodeToRemove.next.prev = nodeToRemove.prev;

    // Step 2: Clear references to help GC
    nodeToRemove.prev = null;
    nodeToRemove.next = null;

    size--;
    modCount++;
    return nodeToRemove.value;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public void clear() {
    // Reset to empty state
    // [HEAD] <-> [TAIL]
    head.next = tail;
    tail.prev = head;
    size = 0;
    modCount++;
  }

  private Node<T> getNode(int index) {
    // Optimization: Traversal Direction
    //
    //  Index: 0 ... size/2 ... size
    //         |        |        |
    //       Start    Split    Start
    //     from HEAD          from TAIL
    //
    if (index < size / 2) {
      Node<T> current = head.next;
      for (int i = 0; i < index; i++) {
        current = current.next;
      }
      return current;
    } else {
      Node<T> current = tail.prev;
      for (int i = size - 1; i > index; i--) {
        current = current.prev;
      }
      return current;
    }
  }

  /**
   * Gets the node at the given index for insertion purposes. For insertion, we need the node that
   * will come AFTER the new node. When index == size or when list is empty, this returns tail.
   */
  private Node<T> getNodeForInsertion(int index) {
    if (index == size || size == 0) {
      return tail; // Insert at the end (before tail)
    }
    return getNode(index); // Insert before the node at this index
  }

  /**
   * Adds an element to the beginning of the list.
   *
   * @param element the element to add (might be null)
   */
  public void addFirst(T element) {
    add(0, element);
  }

  /**
   * Adds an element to the end of the list.
   *
   * @param element the element to add (might be null)
   */
  public void addLast(T element) {
    add(size, element);
  }

  /**
   * Removes and returns the first element of the list.
   *
   * @return the removed element
   * @throws NoSuchElementException if the list is empty
   */
  public T removeFirst() {
    if (isEmpty()) {
      throw new NoSuchElementException("List is empty");
    }
    return remove(0);
  }

  /**
   * Removes and returns the last element of the list.
   *
   * @return the removed element
   * @throws NoSuchElementException if the list is empty
   */
  public T removeLast() {
    if (isEmpty()) {
      throw new NoSuchElementException("List is empty");
    }
    return remove(size - 1);
  }

  @Override
  public Iterator<T> iterator() {
    return new MyLinkedListIterator();
  }

  /**
   * Fail-fast iterator implementation.
   *
   * <p>Checks for concurrent modifications by comparing expectedModCount with the list's modCount.
   */
  private class MyLinkedListIterator implements Iterator<T> {
    private Node<T> current = head.next;
    private Node<T> lastReturned = null;
    private int expectedModCount = modCount;

    @Override
    public boolean hasNext() {
      return current != tail;
    }

    @Override
    public T next() {
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      lastReturned = current;
      current = current.next;
      return lastReturned.value;
    }

    @Override
    public void remove() {
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
      if (lastReturned == null) {
        throw new IllegalStateException();
      }

      // Remove the last returned node
      lastReturned.prev.next = lastReturned.next;
      lastReturned.next.prev = lastReturned.prev;
      size--;
      modCount++;
      expectedModCount = modCount; // Update expectedModCount after successful modification
      lastReturned = null; // Invalidate lastReturned
    }
  }
}
