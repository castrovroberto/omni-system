package com.omni.app.fs;

import com.omni.core.tree.AVLTree;
import java.util.Optional;

/**
 * An index for fast file lookup by name, backed by an {@link AVLTree}.
 *
 * <p>Wraps file names in a comparable wrapper that maps to the actual {@link FileNode}, providing
 * O(log n) lookup.
 */
public class FileIndex {

  private final AVLTree<IndexEntry> tree;

  /** Constructs an empty file index. */
  public FileIndex() {
    this.tree = new AVLTree<>();
  }

  /**
   * Adds a file to the index.
   *
   * @param file the file to index
   */
  public void add(FileNode file) {
    tree.insert(new IndexEntry(file));
  }

  /**
   * Removes a file from the index.
   *
   * @param name the file name to remove
   * @return true if the file was found and removed
   */
  public boolean remove(String name) {
    return tree.delete(new IndexEntry(name)).isPresent();
  }

  /**
   * Finds a file by name.
   *
   * @param name the file name to search for
   * @return an Optional containing the file if found
   */
  public Optional<FileNode> find(String name) {
    Optional<IndexEntry> entry = tree.find(new IndexEntry(name));
    return entry.map(e -> e.file);
  }

  /**
   * Returns the number of indexed files.
   *
   * @return the size of the index
   */
  public int size() {
    return tree.size();
  }

  /** Comparable wrapper pairing a file name with its node for AVL tree storage. */
  static class IndexEntry implements Comparable<IndexEntry> {
    final String name;
    final FileNode file;

    IndexEntry(FileNode file) {
      this.name = file.getName();
      this.file = file;
    }

    IndexEntry(String name) {
      this.name = name;
      this.file = null;
    }

    @Override
    public int compareTo(IndexEntry other) {
      return this.name.compareTo(other.name);
    }
  }
}
