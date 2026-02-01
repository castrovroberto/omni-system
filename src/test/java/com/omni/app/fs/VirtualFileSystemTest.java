package com.omni.app.fs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("VirtualFileSystem Tests")
class VirtualFileSystemTest {

  private VirtualFileSystem vfs;

  @BeforeEach
  void setUp() {
    vfs = new VirtualFileSystem();
  }

  @Nested
  @DisplayName("File Operations")
  class FileOperationTests {

    @Test
    @DisplayName("Create file in root")
    void createFile_inRoot_addsFile() {
      FileNode file = vfs.createFile("test.txt", 100);
      assertEquals("test.txt", file.getName());
      assertEquals(1, vfs.getRoot().getChildren().size());
      assertEquals(1, vfs.indexedFileCount());
    }

    @Test
    @DisplayName("Delete file from root")
    void deleteFile_fromRoot_removesFile() {
      vfs.createFile("test.txt", 100);
      assertTrue(vfs.deleteFile("test.txt"));
      assertEquals(0, vfs.getRoot().getChildren().size());
    }

    @Test
    @DisplayName("Delete non-existing file returns false")
    void deleteFile_nonExisting_returnsFalse() {
      assertFalse(vfs.deleteFile("nope.txt"));
    }

    @Test
    @DisplayName("Find file by name using index")
    void findFile_byName_returnsFile() {
      vfs.createFile("alpha.txt", 50);
      vfs.createFile("beta.txt", 75);

      assertTrue(vfs.findFile("alpha.txt").isPresent());
      assertEquals(50, vfs.findFile("alpha.txt").get().getSize());
      assertTrue(vfs.findFile("beta.txt").isPresent());
      assertFalse(vfs.findFile("gamma.txt").isPresent());
    }
  }

  @Nested
  @DisplayName("Directory Operations")
  class DirectoryOperationTests {

    @Test
    @DisplayName("Create subdirectory")
    void createDirectory_addsDirectory() {
      DirectoryNode sub = vfs.createDirectory("subdir");
      assertEquals("subdir", sub.getName());
      assertEquals(1, vfs.getRoot().getChildren().size());
    }

    @Test
    @DisplayName("Navigate into subdirectory")
    void navigateInto_changesCurrentDir() {
      vfs.createDirectory("subdir");
      assertTrue(vfs.navigateInto("subdir"));
      assertEquals("subdir", vfs.getCurrentDirectory().getName());
    }

    @Test
    @DisplayName("Navigate into non-existing directory fails")
    void navigateInto_nonExisting_returnsFalse() {
      assertFalse(vfs.navigateInto("nope"));
    }

    @Test
    @DisplayName("Navigate to root resets current directory")
    void navigateToRoot_resetsToCurrent() {
      vfs.createDirectory("subdir");
      vfs.navigateInto("subdir");
      vfs.navigateToRoot();
      assertEquals("root", vfs.getCurrentDirectory().getName());
    }

    @Test
    @DisplayName("Create file in subdirectory")
    void createFile_inSubdirectory_addsToCurrentDir() {
      vfs.createDirectory("subdir");
      vfs.navigateInto("subdir");
      vfs.createFile("nested.txt", 200);
      assertEquals(1, vfs.getCurrentDirectory().getChildren().size());
      assertEquals("nested.txt", vfs.getCurrentDirectory().getChildren().get(0).getName());
    }
  }

  @Nested
  @DisplayName("Integration Tests")
  class IntegrationTests {

    @Test
    @DisplayName("Full workflow: create dirs, files, navigate, search")
    void fullWorkflow() {
      // Create structure
      vfs.createFile("root.txt", 10);
      vfs.createDirectory("docs");
      vfs.navigateInto("docs");
      vfs.createFile("readme.md", 500);
      vfs.createFile("guide.md", 300);
      vfs.navigateToRoot();
      vfs.createDirectory("src");
      vfs.navigateInto("src");
      vfs.createFile("main.java", 1000);
      vfs.navigateToRoot();

      // Verify index
      assertEquals(4, vfs.indexedFileCount());
      assertTrue(vfs.findFile("main.java").isPresent());
      assertTrue(vfs.findFile("readme.md").isPresent());
      assertFalse(vfs.findFile("missing.txt").isPresent());

      // Verify sizes via visitor
      DiskUsageVisitor visitor = new DiskUsageVisitor();
      vfs.getRoot().accept(visitor);
      assertEquals(1810, visitor.getTotalSize());
    }
  }
}
