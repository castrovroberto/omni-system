package com.omni.app.fs;

import java.util.Optional;

/**
 * A facade for virtual file system operations.
 *
 * <p>Integrates {@link DirectoryNode}, {@link FileIndex}, and navigation to provide a unified API
 * for file system manipulation.
 *
 * <p><b>Interview Note:</b> Supports absolute path operations like {@code mkdir -p /a/b/c} which is
 * a common interview question (LeetCode #588 - Design In-Memory File System).
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
   * Resolves an absolute path to a directory node.
   *
   * <p>Traverses the path components starting from root. Returns empty if any component doesn't
   * exist or is not a directory.
   *
   * @param path absolute path like "/a/b/c" or relative path like "a/b"
   * @return the directory at the path, or empty if not found
   */
  public Optional<DirectoryNode> resolve(String path) {
    if (path == null || path.isEmpty()) {
      return Optional.of(currentDirectory);
    }

    DirectoryNode current = path.startsWith("/") ? root : currentDirectory;
    String[] parts = normalizePath(path);

    for (String part : parts) {
      if (part.isEmpty() || part.equals(".")) {
        continue;
      }
      Optional<FileSystemNode> child = current.getChild(part);
      if (child.isEmpty() || !child.get().isDirectory()) {
        return Optional.empty();
      }
      current = (DirectoryNode) child.get();
    }
    return Optional.of(current);
  }

  /**
   * Creates directories along a path, similar to {@code mkdir -p}.
   *
   * <p>Creates any intermediate directories that don't exist.
   *
   * @param path the path to create, e.g., "/a/b/c"
   * @return the final directory in the path
   */
  public DirectoryNode mkdirp(String path) {
    if (path == null || path.isEmpty()) {
      return currentDirectory;
    }

    DirectoryNode current = path.startsWith("/") ? root : currentDirectory;
    String[] parts = normalizePath(path);

    for (String part : parts) {
      if (part.isEmpty() || part.equals(".")) {
        continue;
      }
      Optional<FileSystemNode> child = current.getChild(part);
      if (child.isPresent()) {
        if (!child.get().isDirectory()) {
          throw new IllegalArgumentException("Path component is not a directory: " + part);
        }
        current = (DirectoryNode) child.get();
      } else {
        DirectoryNode newDir = new DirectoryNode(part);
        current.addChild(newDir);
        current = newDir;
      }
    }
    return current;
  }

  /**
   * Creates a file at an absolute or relative path.
   *
   * <p>Creates intermediate directories if they don't exist.
   *
   * @param path the file path, e.g., "/a/b/file.txt"
   * @param size the file size in bytes
   * @return the created file
   */
  public FileNode createFileAt(String path, long size) {
    int lastSlash = path.lastIndexOf('/');
    String dirPath = lastSlash > 0 ? path.substring(0, lastSlash) : "";
    String fileName = lastSlash >= 0 ? path.substring(lastSlash + 1) : path;

    DirectoryNode dir = dirPath.isEmpty() ? currentDirectory : mkdirp(dirPath);
    FileNode file = new FileNode(fileName, size);
    dir.addChild(file);
    index.add(file);
    return file;
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
    Optional<FileSystemNode> child = currentDirectory.getChild(name);
    if (child.isPresent() && !child.get().isDirectory()) {
      currentDirectory.removeChild(name);
      index.remove(name);
      return true;
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
    Optional<FileSystemNode> child = currentDirectory.getChild(name);
    if (child.isPresent() && child.get().isDirectory()) {
      currentDirectory = (DirectoryNode) child.get();
      return true;
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

  private String[] normalizePath(String path) {
    // Remove leading slash and split
    String normalized = path.startsWith("/") ? path.substring(1) : path;
    return normalized.split("/");
  }
}
