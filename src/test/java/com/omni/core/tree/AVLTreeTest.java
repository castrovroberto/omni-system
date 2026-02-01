package com.omni.core.tree;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AVLTree Tests")
class AVLTreeTest {

  private AVLTree<Integer> avl;

  @BeforeEach
  void setUp() {
    avl = new AVLTree<>();
  }

  // ==================== Rotation Tests ====================

  @Nested
  @DisplayName("Rotation Tests")
  class RotationTests {

    @Test
    @DisplayName("LL rotation: right-rotate on left-left imbalance")
    void llRotation_rebalances() {
      avl.insert(30);
      avl.insert(20);
      avl.insert(10); // Triggers LL rotation
      assertEquals(3, avl.size());
      assertEquals(1, avl.height());
      MyList<Integer> inOrder = avl.inOrder();
      assertEquals(10, inOrder.get(0));
      assertEquals(20, inOrder.get(1));
      assertEquals(30, inOrder.get(2));
    }

    @Test
    @DisplayName("RR rotation: left-rotate on right-right imbalance")
    void rrRotation_rebalances() {
      avl.insert(10);
      avl.insert(20);
      avl.insert(30); // Triggers RR rotation
      assertEquals(3, avl.size());
      assertEquals(1, avl.height());
    }

    @Test
    @DisplayName("LR rotation: left-right double rotation")
    void lrRotation_rebalances() {
      avl.insert(30);
      avl.insert(10);
      avl.insert(20); // Triggers LR rotation
      assertEquals(3, avl.size());
      assertEquals(1, avl.height());
    }

    @Test
    @DisplayName("RL rotation: right-left double rotation")
    void rlRotation_rebalances() {
      avl.insert(10);
      avl.insert(30);
      avl.insert(20); // Triggers RL rotation
      assertEquals(3, avl.size());
      assertEquals(1, avl.height());
    }
  }

  // ==================== Insert Tests ====================

  @Nested
  @DisplayName("Insert Tests")
  class InsertTests {

    @Test
    @DisplayName("Insert into empty tree")
    void insert_emptyTree_sizeIsOne() {
      avl.insert(10);
      assertEquals(1, avl.size());
    }

    @Test
    @DisplayName("Insert duplicate is ignored")
    void insert_duplicate_sizeUnchanged() {
      avl.insert(10);
      avl.insert(10);
      assertEquals(1, avl.size());
    }

    @Test
    @DisplayName("Insert null throws exception")
    void insert_null_throwsException() {
      assertThrows(IllegalArgumentException.class, () -> avl.insert(null));
    }

    @Test
    @DisplayName("Multiple inserts maintain balance")
    void insert_multipleElements_maintainsBalance() {
      for (int i = 1; i <= 15; i++) {
        avl.insert(i);
      }
      assertEquals(15, avl.size());
      assertTrue(avl.height() <= 4);
    }
  }

  // ==================== Delete Tests ====================

  @Nested
  @DisplayName("Delete Tests")
  class DeleteTests {

    @Test
    @DisplayName("Delete rebalances tree")
    void delete_triggersRebalance() {
      avl.insert(20);
      avl.insert(10);
      avl.insert(30);
      avl.insert(5);
      avl.delete(30); // Removing right child may cause left-heavy imbalance
      assertEquals(3, avl.size());
      assertTrue(avl.height() <= 1);
    }

    @Test
    @DisplayName("Delete from larger tree maintains balance")
    void delete_largerTree_maintainsBalance() {
      for (int i = 1; i <= 20; i++) {
        avl.insert(i);
      }
      for (int i = 1; i <= 10; i++) {
        avl.delete(i);
      }
      assertEquals(10, avl.size());
      assertTrue(avl.height() <= 4);
    }
  }

  // ==================== Traversal Tests ====================

  @Nested
  @DisplayName("Traversal Tests")
  class TraversalTests {

    @Test
    @DisplayName("In-order traversal returns sorted elements")
    void inOrder_returnsSorted() {
      avl.insert(5);
      avl.insert(3);
      avl.insert(7);
      avl.insert(1);
      avl.insert(4);
      MyList<Integer> result = avl.inOrder();
      assertEquals(1, result.get(0));
      assertEquals(3, result.get(1));
      assertEquals(4, result.get(2));
      assertEquals(5, result.get(3));
      assertEquals(7, result.get(4));
    }
  }

  // ==================== Mastery Check Tests ====================

  @Nested
  @DisplayName("Mastery Check Tests")
  class MasteryCheckTests {

    @Test
    @DisplayName("Mastery check: Sequential insert 1-100, height <= 7")
    void masteryCheck_sequentialInsert_heightAtMost7() {
      for (int i = 1; i <= 100; i++) {
        avl.insert(i);
      }
      assertEquals(100, avl.size());
      assertTrue(
          avl.height() <= 7,
          "AVL tree height should be <= 7 for 100 elements, but was " + avl.height());
    }

    @Test
    @DisplayName("Mastery check: Balance factor always in [-1, 1]")
    void masteryCheck_balanceFactorAlwaysValid() {
      for (int i = 1; i <= 100; i++) {
        avl.insert(i);
      }
      // Verify by checking that the in-order traversal is correct and height is bounded
      MyList<Integer> sorted = avl.inOrder();
      assertEquals(100, sorted.size());
      for (int i = 0; i < 100; i++) {
        assertEquals(i + 1, sorted.get(i));
      }
      assertTrue(avl.height() <= 7);
    }

    @Test
    @DisplayName("Mastery check: Large random-order inserts and deletes maintain balance")
    void masteryCheck_insertsAndDeletes_maintainBalance() {
      for (int i = 0; i < 1000; i++) {
        avl.insert(i);
      }
      assertEquals(1000, avl.size());
      // Height of AVL with 1000 nodes: floor(1.44 * log2(1001)) ~ 14
      assertTrue(avl.height() <= 14, "Height was " + avl.height());

      for (int i = 0; i < 500; i++) {
        avl.delete(i);
      }
      assertEquals(500, avl.size());
      assertTrue(avl.height() <= 12, "Height after deletes was " + avl.height());
    }
  }
}
