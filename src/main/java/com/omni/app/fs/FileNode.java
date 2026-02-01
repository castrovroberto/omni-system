package com.omni.app.fs;

/**
 * A leaf node representing a file in the virtual file system.
 *
 * <p>Files have a fixed name and size and cannot contain children.
 */
public class FileNode implements FileSystemNode {

  private final String name;
  private final long size;

  /**
   * Constructs a file node.
   *
   * @param name the file name
   * @param size the file size in bytes
   * @throws IllegalArgumentException if name is null/empty or size is negative
   */
  public FileNode(String name, long size) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("File name cannot be null or empty");
    }
    if (size < 0) {
      throw new IllegalArgumentException("File size cannot be negative");
    }
    this.name = name;
    this.size = size;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public long getSize() {
    return size;
  }

  @Override
  public boolean isDirectory() {
    return false;
  }

  @Override
  public void accept(FileSystemVisitor visitor) {
    visitor.visitFile(this);
  }
}
