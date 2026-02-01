package com.omni.app.fs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DiskUsageVisitor Tests")
class DiskUsageVisitorTest {

  @Test
  @DisplayName("Total size of single file")
  void singleFile_returnsFileSize() {
    FileNode file = new FileNode("test.txt", 500);
    DiskUsageVisitor visitor = new DiskUsageVisitor();
    file.accept(visitor);
    assertEquals(500, visitor.getTotalSize());
  }

  @Test
  @DisplayName("Total size of empty directory")
  void emptyDirectory_returnsZero() {
    DirectoryNode dir = new DirectoryNode("empty");
    DiskUsageVisitor visitor = new DiskUsageVisitor();
    dir.accept(visitor);
    assertEquals(0, visitor.getTotalSize());
  }

  @Test
  @DisplayName("Total size of nested directories")
  void nestedDirectories_sumAllFiles() {
    DirectoryNode root = new DirectoryNode("root");
    root.addChild(new FileNode("a.txt", 100));
    root.addChild(new FileNode("b.txt", 200));

    DirectoryNode sub = new DirectoryNode("sub");
    sub.addChild(new FileNode("c.txt", 300));
    sub.addChild(new FileNode("d.txt", 400));
    root.addChild(sub);

    DirectoryNode deep = new DirectoryNode("deep");
    deep.addChild(new FileNode("e.txt", 500));
    sub.addChild(deep);

    DiskUsageVisitor visitor = new DiskUsageVisitor();
    root.accept(visitor);
    assertEquals(1500, visitor.getTotalSize());
  }

  @Test
  @DisplayName("Visitor can be reused - accumulates across calls")
  void visitor_accumulates() {
    DiskUsageVisitor visitor = new DiskUsageVisitor();
    new FileNode("a.txt", 100).accept(visitor);
    new FileNode("b.txt", 200).accept(visitor);
    assertEquals(300, visitor.getTotalSize());
  }
}
