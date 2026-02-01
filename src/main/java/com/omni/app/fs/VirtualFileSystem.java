package com.omni.app.fs;

import java.util.Optional;

/**
 * A facade for virtual file system operations.
 *
 * <p>Integrates {@link DirectoryNode}, {@link FileIndex}, and navigation to provide a unified API
 * for file system manipulation.
 */
public class VirtualFileSystem {

  private final DirectoryNode root;
  private final FileIndex index;
  private DirectoryNode currentDirectory;

  /** Constructs a virtual file system with a root directory. */
  public VirtualFileSystem() {
    this.root = new DirectoryNode("root");
    this.index = new FileIndex();
    this.currentDirectory = root;
  }

  /**
   * Creates a file in the current directory.
   *
   * @param name the file name
   * @param size the file size in bytes
   * @return the created file
   */
  public FileNode createFile(String name, long size) {
    FileNode file = new FileNode(name, size);
    currentDirectory.addChild(file);
    index.add(file);
    return file;
  }

  /**
   * Deletes a file by name from the current directory.
   *
   * @param name the file name to delete
   * @return true if the file was found and deleted
   */
  public boolean deleteFile(String name) {
    for (FileSystemNode child : currentDirectory.getChildren()) {
      if (!child.isDirectory() && child.getName().equals(name)) {
        currentDirectory.removeChild(child);
        index.remove(name);
        return true;
      }
    }
    return false;
  }

  /**
   * Creates a subdirectory in the current directory.
   *
   * @param name the directory name
   * @return the created directory
   */
  public DirectoryNode createDirectory(String name) {
    DirectoryNode dir = new DirectoryNode(name);
    currentDirectory.addChild(dir);
    return dir;
  }

  /**
   * Navigates into a subdirectory by name.
   *
   * @param name the directory name to navigate into
   * @return true if the directory was found and navigation succeeded
   */
  public boolean navigateInto(String name) {
    for (FileSystemNode child : currentDirectory.getChildren()) {
      if (child.isDirectory() && child.getName().equals(name)) {
        currentDirectory = (DirectoryNode) child;
        return true;
      }
    }
    return false;
  }

  /** Navigates back to the root directory. */
  public void navigateToRoot() {
    currentDirectory = root;
  }

  /**
   * Finds a file by name using the index.
   *
   * @param name the file name
   * @return an Optional containing the file if found
   */
  public Optional<FileNode> findFile(String name) {
    return index.find(name);
  }

  /**
   * Returns the root directory.
   *
   * @return the root
   */
  public DirectoryNode getRoot() {
    return root;
  }

  /**
   * Returns the current directory.
   *
   * @return the current directory
   */
  public DirectoryNode getCurrentDirectory() {
    return currentDirectory;
  }

  /**
   * Returns the number of indexed files.
   *
   * @return the index size
   */
  public int indexedFileCount() {
    return index.size();
  }
}
