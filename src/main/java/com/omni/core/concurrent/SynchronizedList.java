package com.omni.core.concurrent;

import com.omni.core.list.MyList;
import java.util.Iterator;

/**
 * Thread-safe wrapper around any {@link MyList} implementation.
 *
 * <p>All operations are synchronized on an internal lock. Note that the iterator returned by {@link
 * #iterator()} is NOT thread-safe - callers must synchronize externally when iterating.
 *
 * <p>For compound operations (check-then-act), use {@link #getLock()} to synchronize:
 *
 * <pre>{@code
 * SynchronizedList<String> list = new SynchronizedList<>(new MyArrayList<>());
 * synchronized (list.getLock()) {
 *     if (!list.isEmpty()) {
 *         String first = list.get(0);
 *         // ... use first safely
 *     }
 * }
 * }</pre>
 *
 * @param <T> the element type
 */
public class SynchronizedList<T> implements MyList<T> {

  private final MyList<T> delegate;
  private final Object lock;

  /**
   * Creates a synchronized wrapper around the given list.
   *
   * @param delegate the list to wrap
   */
  public SynchronizedList(MyList<T> delegate) {
    this.delegate = delegate;
    this.lock = new Object();
  }

  /**
   * Returns the lock object used for synchronization. Use this for compound operations.
   *
   * @return the internal lock object
   */
  public Object getLock() {
    return lock;
  }

  @Override
  public void add(T element) {
    synchronized (lock) {
      delegate.add(element);
    }
  }

  @Override
  public void add(int index, T element) {
    synchronized (lock) {
      delegate.add(index, element);
    }
  }

  @Override
  public T get(int index) {
    synchronized (lock) {
      return delegate.get(index);
    }
  }

  @Override
  public T set(int index, T element) {
    synchronized (lock) {
      return delegate.set(index, element);
    }
  }

  @Override
  public T remove(int index) {
    synchronized (lock) {
      return delegate.remove(index);
    }
  }

  /**
   * Removes the first occurrence of the specified element from the list.
   *
   * @param element the element to remove
   * @return true if the element was found and removed
   */
  public boolean removeElement(T element) {
    synchronized (lock) {
      int index = delegate.indexOf(element);
      if (index >= 0) {
        delegate.remove(index);
        return true;
      }
      return false;
    }
  }

  @Override
  public int indexOf(T element) {
    synchronized (lock) {
      return delegate.indexOf(element);
    }
  }

  @Override
  public boolean contains(T element) {
    synchronized (lock) {
      return delegate.contains(element);
    }
  }

  @Override
  public int size() {
    synchronized (lock) {
      return delegate.size();
    }
  }

  @Override
  public boolean isEmpty() {
    synchronized (lock) {
      return delegate.isEmpty();
    }
  }

  @Override
  public void clear() {
    synchronized (lock) {
      delegate.clear();
    }
  }

  @Override
  public Object[] toArray() {
    synchronized (lock) {
      return delegate.toArray();
    }
  }

  /**
   * Returns an iterator over the elements. WARNING: The iterator is NOT thread-safe. Callers must
   * synchronize on {@link #getLock()} during iteration.
   *
   * @return an iterator
   */
  @Override
  public Iterator<T> iterator() {
    // Note: returning delegate's iterator - NOT thread-safe
    // Caller must synchronize externally
    return delegate.iterator();
  }
}
