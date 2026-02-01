package com.omni.app.fs;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("JobScheduler Tests")
class JobSchedulerTest {

  private JobScheduler scheduler;
  private DirectoryNode dir;

  @BeforeEach
  void setUp() {
    scheduler = new JobScheduler();
    dir = new DirectoryNode("test");
  }

  @Nested
  @DisplayName("Scheduling Tests")
  class SchedulingTests {

    @Test
    @DisplayName("Schedule and execute single job")
    void schedule_singleJob_executes() {
      FileNode file = new FileNode("a.txt", 100);
      scheduler.schedule(new CreateFileJob(dir, file, 1));
      assertEquals(1, scheduler.pendingJobs());

      scheduler.executeNext();
      assertEquals(0, scheduler.pendingJobs());
      assertEquals(1, dir.getChildren().size());
    }

    @Test
    @DisplayName("Jobs execute in priority order")
    void schedule_multipleJobs_priorityOrder() {
      FileNode low = new FileNode("low.txt", 10);
      FileNode high = new FileNode("high.txt", 20);
      FileNode mid = new FileNode("mid.txt", 15);

      scheduler.schedule(new CreateFileJob(dir, low, 3));
      scheduler.schedule(new CreateFileJob(dir, high, 1));
      scheduler.schedule(new CreateFileJob(dir, mid, 2));

      scheduler.executeNext(); // priority 1 (high)
      assertEquals("high.txt", dir.getChildren().get(0).getName());

      scheduler.executeNext(); // priority 2 (mid)
      scheduler.executeNext(); // priority 3 (low)
      assertEquals(3, dir.getChildren().size());
    }

    @Test
    @DisplayName("Execute all jobs")
    void executeAll_executesAllJobs() {
      scheduler.schedule(new CreateFileJob(dir, new FileNode("a.txt", 10), 1));
      scheduler.schedule(new CreateFileJob(dir, new FileNode("b.txt", 20), 2));
      scheduler.schedule(new CreateFileJob(dir, new FileNode("c.txt", 30), 3));

      int count = scheduler.executeAll();
      assertEquals(3, count);
      assertEquals(0, scheduler.pendingJobs());
      assertEquals(3, dir.getChildren().size());
    }

    @Test
    @DisplayName("Execute next on empty scheduler throws exception")
    void executeNext_empty_throwsException() {
      assertThrows(NoSuchElementException.class, () -> scheduler.executeNext());
    }

    @Test
    @DisplayName("Execute all on empty scheduler returns zero")
    void executeAll_empty_returnsZero() {
      assertEquals(0, scheduler.executeAll());
    }
  }

  @Nested
  @DisplayName("Undo Tests")
  class UndoTests {

    @Test
    @DisplayName("Undo create file removes it")
    void undoCreateFile_removesFile() {
      FileNode file = new FileNode("a.txt", 100);
      CreateFileJob job = new CreateFileJob(dir, file, 1);
      job.execute();
      assertEquals(1, dir.getChildren().size());
      job.undo();
      assertEquals(0, dir.getChildren().size());
    }

    @Test
    @DisplayName("Undo delete file restores it")
    void undoDeleteFile_restoresFile() {
      FileNode file = new FileNode("a.txt", 100);
      dir.addChild(file);
      DeleteFileJob job = new DeleteFileJob(dir, file, 1);
      job.execute();
      assertEquals(0, dir.getChildren().size());
      job.undo();
      assertEquals(1, dir.getChildren().size());
    }
  }
}
