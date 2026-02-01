package com.omni.app.fs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("FileSystem Composite Pattern Tests")
class FileSystemCompositeTest {

  private DirectoryNode root;

  @BeforeEach
  void setUp() {
    root = new DirectoryNode("root");
  }

  @Nested
  @DisplayName("FileNode Tests")
  class FileNodeTests {

    @Test
    @DisplayName("File has correct name and size")
    void fileNode_correctProperties() {
      FileNode file = new FileNode("test.txt", 100);
      assertEquals("test.txt", file.getName());
      assertEquals(100, file.getSize());
      assertFalse(file.isDirectory());
    }

    @Test
    @DisplayName("Null name throws exception")
    void fileNode_nullName_throwsException() {
      assertThrows(IllegalArgumentException.class, () -> new FileNode(null, 100));
    }

    @Test
    @DisplayName("Empty name throws exception")
    void fileNode_emptyName_throwsException() {
      assertThrows(IllegalArgumentException.class, () -> new FileNode("", 100));
    }

    @Test
    @DisplayName("Negative size throws exception")
    void fileNode_negativeSize_throwsException() {
      assertThrows(IllegalArgumentException.class, () -> new FileNode("test.txt", -1));
    }

    @Test
    @DisplayName("Zero size is valid")
    void fileNode_zeroSize_valid() {
      FileNode file = new FileNode("empty.txt", 0);
      assertEquals(0, file.getSize());
    }
  }

  @Nested
  @DisplayName("DirectoryNode Tests")
  class DirectoryNodeTests {

    @Test
    @DisplayName("Directory is empty by default")
    void directoryNode_emptyByDefault() {
      assertTrue(root.isDirectory());
      assertTrue(root.getChildren().isEmpty());
      assertEquals(0, root.getSize());
    }

    @Test
    @DisplayName("Null name throws exception")
    void directoryNode_nullName_throwsException() {
      assertThrows(IllegalArgumentException.class, () -> new DirectoryNode(null));
    }

    @Test
    @DisplayName("Add and remove children")
    void directoryNode_addRemoveChildren() {
      FileNode file = new FileNode("a.txt", 50);
      root.addChild(file);
      assertEquals(1, root.getChildren().size());
      assertTrue(root.removeChild(file));
      assertEquals(0, root.getChildren().size());
    }

    @Test
    @DisplayName("Remove non-existing child returns false")
    void directoryNode_removeNonExisting_returnsFalse() {
      assertFalse(root.removeChild(new FileNode("nope.txt", 10)));
    }

    @Test
    @DisplayName("Directory size is sum of children sizes")
    void directoryNode_sizeIsSumOfChildren() {
      root.addChild(new FileNode("a.txt", 100));
      root.addChild(new FileNode("b.txt", 200));
      DirectoryNode sub = new DirectoryNode("sub");
      sub.addChild(new FileNode("c.txt", 300));
      root.addChild(sub);
      assertEquals(600, root.getSize());
    }
  }
}
