package com.omni.app.fs;

/**
 * Visitor pattern interface for file system operations.
 *
 * <p>Implementations can perform operations on the file system tree without modifying the node
 * classes.
 */
public interface FileSystemVisitor {

  /**
   * Visits a file node.
   *
   * @param file the file to visit
   */
  void visitFile(FileNode file);

  /**
   * Visits a directory node.
   *
   * @param directory the directory to visit
   */
  void visitDirectory(DirectoryNode directory);
}
