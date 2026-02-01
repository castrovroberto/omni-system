package com.omni.app.fs;

/**
 * Composite pattern interface for file system nodes.
 *
 * <p>Both files and directories implement this interface, allowing uniform treatment of the tree
 * structure.
 */
public interface FileSystemNode {

  /**
   * Returns the name of this node.
   *
   * @return the node name
   */
  String getName();

  /**
   * Returns the size of this node in bytes. For files, this is the file size. For directories, this
   * is the sum of all children's sizes.
   *
   * @return the size in bytes
   */
  long getSize();

  /**
   * Returns true if this node is a directory.
   *
   * @return true if directory, false if file
   */
  boolean isDirectory();

  /**
   * Accepts a visitor for the visitor pattern.
   *
   * @param visitor the visitor to accept
   */
  void accept(FileSystemVisitor visitor);
}
