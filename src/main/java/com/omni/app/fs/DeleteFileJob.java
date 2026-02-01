package com.omni.app.fs;

/**
 * A job that removes a file from a directory.
 *
 * <p>Stores a backup reference for undo support.
 */
public class DeleteFileJob implements Job {

  private final DirectoryNode directory;
  private final FileNode file;
  private final int priority;

  /**
   * Constructs a delete file job.
   *
   * @param directory the directory containing the file
   * @param file the file to delete
   * @param priority the job priority (lower = higher priority)
   */
  public DeleteFileJob(DirectoryNode directory, FileNode file, int priority) {
    this.directory = directory;
    this.file = file;
    this.priority = priority;
  }

  @Override
  public void execute() {
    directory.removeChild(file);
  }

  @Override
  public void undo() {
    directory.addChild(file);
  }

  @Override
  public int getPriority() {
    return priority;
  }
}
