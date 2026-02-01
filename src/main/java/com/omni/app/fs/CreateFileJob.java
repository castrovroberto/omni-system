package com.omni.app.fs;

/**
 * A job that adds a file to a directory.
 *
 * <p>Undo removes the file from the directory.
 */
public class CreateFileJob implements Job {

  private final DirectoryNode directory;
  private final FileNode file;
  private final int priority;

  /**
   * Constructs a create file job.
   *
   * @param directory the target directory
   * @param file the file to create
   * @param priority the job priority (lower = higher priority)
   */
  public CreateFileJob(DirectoryNode directory, FileNode file, int priority) {
    this.directory = directory;
    this.file = file;
    this.priority = priority;
  }

  @Override
  public void execute() {
    directory.addChild(file);
  }

  @Override
  public void undo() {
    directory.removeChild(file);
  }

  @Override
  public int getPriority() {
    return priority;
  }
}
