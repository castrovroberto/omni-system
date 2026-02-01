package com.omni.app.fs;

/**
 * A visitor that calculates total disk usage by summing all file sizes.
 *
 * <p>Recursively traverses directory children to compute the total size.
 */
public class DiskUsageVisitor implements FileSystemVisitor {

  private long totalSize;

  @Override
  public void visitFile(FileNode file) {
    totalSize += file.getSize();
  }

  @Override
  public void visitDirectory(DirectoryNode directory) {
    for (FileSystemNode child : directory.getChildren()) {
      child.accept(this);
    }
  }

  /**
   * Returns the total size accumulated by visiting nodes.
   *
   * @return the total size in bytes
   */
  public long getTotalSize() {
    return totalSize;
  }
}
