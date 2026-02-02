package com.omni.app.fs;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import com.omni.core.map.MyHashMap;
import com.omni.core.map.MyMap;
import java.util.Optional;

/**
 * A composite node representing a directory in the virtual file system.
 *
 * <p>Directories can contain both files and other directories, forming a tree structure.
 *
 * <p><b>Interview Note:</b> Children are stored in a {@link MyHashMap} for O(1) lookup by name,
 * addressing the "millions of files" scalability question.
 */
public class DirectoryNode implements FileSystemNode {

  private final String name;
  private final MyMap<String, FileSystemNode> children;

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
    this.children = new MyHashMap<>();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public long getSize() {
    long total = 0;
    for (FileSystemNode child : children.values()) {
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
   * <p>If a child with the same name exists, it is replaced.
   *
   * @param child the child to add
   */
  public void addChild(FileSystemNode child) {
    children.put(child.getName(), child);
  }

  /**
   * Removes a child node from this directory by reference.
   *
   * @param child the child to remove
   * @return true if the child was found and removed
   */
  public boolean removeChild(FileSystemNode child) {
    return removeChild(child.getName());
  }

  /**
   * Removes a child node from this directory by name.
   *
   * @param name the name of the child to remove
   * @return true if the child was found and removed
   */
  public boolean removeChild(String name) {
    return children.remove(name).isPresent();
  }

  /**
   * Gets a child node by name in O(1) time.
   *
   * @param name the child name
   * @return an Optional containing the child if found
   */
  public Optional<FileSystemNode> getChild(String name) {
    return children.get(name);
  }

  /**
   * Checks if a child with the given name exists.
   *
   * @param name the child name
   * @return true if the child exists
   */
  public boolean hasChild(String name) {
    return children.containsKey(name);
  }

  /**
   * Returns all children of this directory.
   *
   * <p>Returns a list for iteration compatibility with existing code.
   *
   * @return the list of children
   */
  public MyList<FileSystemNode> getChildren() {
    MyList<FileSystemNode> list = new MyArrayList<>();
    for (FileSystemNode child : children.values()) {
      list.add(child);
    }
    return list;
  }

  /**
   * Returns the number of children in this directory.
   *
   * @return the child count
   */
  public int getChildCount() {
    return children.size();
  }
}
