package com.omni.app.fs;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;

/**
 * A composite node representing a directory in the virtual file system.
 *
 * <p>Directories can contain both files and other directories, forming a tree structure.
 */
public class DirectoryNode implements FileSystemNode {

  private final String name;
  private final MyList<FileSystemNode> children;

  /**
   * Constructs a directory node.
   *
   * @param name the directory name
   * @throws IllegalArgumentException if name is null or empty
   */
  public DirectoryNode(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Directory name cannot be null or empty");
    }
    this.name = name;
    this.children = new MyArrayList<>();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public long getSize() {
    long total = 0;
    for (FileSystemNode child : children) {
      total += child.getSize();
    }
    return total;
  }

  @Override
  public boolean isDirectory() {
    return true;
  }

  @Override
  public void accept(FileSystemVisitor visitor) {
    visitor.visitDirectory(this);
  }

  /**
   * Adds a child node to this directory.
   *
   * @param child the child to add
   */
  public void addChild(FileSystemNode child) {
    children.add(child);
  }

  /**
   * Removes a child node from this directory.
   *
   * @param child the child to remove
   * @return true if the child was found and removed
   */
  public boolean removeChild(FileSystemNode child) {
    int index = children.indexOf(child);
    if (index >= 0) {
      children.remove(index);
      return true;
    }
    return false;
  }

  /**
   * Returns the children of this directory.
   *
   * @return the list of children
   */
  public MyList<FileSystemNode> getChildren() {
    return children;
  }
}
