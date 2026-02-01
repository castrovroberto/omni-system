package com.omni.core.tree;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("BinarySearchTree Tests")
class BinarySearchTreeTest {

  private BinarySearchTree<Integer> bst;

  @BeforeEach
  void setUp() {
    bst = new BinarySearchTree<>();
  }

  // ==================== Insert Tests ====================

  @Nested
  @DisplayName("Insert Tests")
  class InsertTests {

    @Test
    @DisplayName("Insert into empty tree")
    void insert_emptyTree_sizeIsOne() {
      bst.insert(10);
      assertEquals(1, bst.size());
      assertFalse(bst.isEmpty());
    }

    @Test
    @DisplayName("Insert multiple elements")
    void insert_multipleElements_correctSize() {
      bst.insert(10);
      bst.insert(5);
      bst.insert(15);
      assertEquals(3, bst.size());
    }

    @Test
    @DisplayName("Insert duplicate is ignored")
    void insert_duplicate_sizeUnchanged() {
      bst.insert(10);
      bst.insert(10);
      assertEquals(1, bst.size());
    }

    @Test
    @DisplayName("Insert null throws exception")
    void insert_null_throwsException() {
      assertThrows(IllegalArgumentException.class, () -> bst.insert(null));
    }
  }

  // ==================== Find Tests ====================

  @Nested
  @DisplayName("Find Tests")
  class FindTests {

    @Test
    @DisplayName("Find existing element")
    void find_existingElement_returnsValue() {
      bst.insert(10);
      bst.insert(5);
      bst.insert(15);
      assertEquals(Optional.of(5), bst.find(5));
    }

    @Test
    @DisplayName("Find non-existing element")
    void find_nonExisting_returnsEmpty() {
      bst.insert(10);
      assertEquals(Optional.empty(), bst.find(99));
    }

    @Test
    @DisplayName("Find in empty tree")
    void find_emptyTree_returnsEmpty() {
      assertEquals(Optional.empty(), bst.find(10));
    }

    @Test
    @DisplayName("Find null returns empty")
    void find_null_returnsEmpty() {
      assertEquals(Optional.empty(), bst.find(null));
    }
  }

  // ==================== Delete Tests ====================

  @Nested
  @DisplayName("Delete Tests")
  class DeleteTests {

    @Test
    @DisplayName("Delete leaf node")
    void delete_leafNode_removesElement() {
      bst.insert(10);
      bst.insert(5);
      bst.insert(15);
      assertEquals(Optional.of(5), bst.delete(5));
      assertEquals(2, bst.size());
      assertEquals(Optional.empty(), bst.find(5));
    }

    @Test
    @DisplayName("Delete node with one child")
    void delete_nodeWithOneChild_removesElement() {
      bst.insert(10);
      bst.insert(5);
      bst.insert(3);
      assertEquals(Optional.of(5), bst.delete(5));
      assertEquals(2, bst.size());
      assertEquals(Optional.of(3), bst.find(3));
    }

    @Test
    @DisplayName("Delete node with two children")
    void delete_nodeWithTwoChildren_removesElement() {
      bst.insert(10);
      bst.insert(5);
      bst.insert(15);
      bst.insert(3);
      bst.insert(7);
      assertEquals(Optional.of(5), bst.delete(5));
      assertEquals(4, bst.size());
      assertEquals(Optional.of(3), bst.find(3));
      assertEquals(Optional.of(7), bst.find(7));
    }

    @Test
    @DisplayName("Delete root node")
    void delete_rootNode_removesElement() {
      bst.insert(10);
      bst.insert(5);
      bst.insert(15);
      assertEquals(Optional.of(10), bst.delete(10));
      assertEquals(2, bst.size());
    }

    @Test
    @DisplayName("Delete non-existing element")
    void delete_nonExisting_returnsEmpty() {
      bst.insert(10);
      assertEquals(Optional.empty(), bst.delete(99));
      assertEquals(1, bst.size());
    }

    @Test
    @DisplayName("Delete from empty tree")
    void delete_emptyTree_returnsEmpty() {
      assertEquals(Optional.empty(), bst.delete(10));
    }
  }

  // ==================== Min/Max Tests ====================

  @Nested
  @DisplayName("Min/Max Tests")
  class MinMaxTests {

    @Test
    @DisplayName("Min of populated tree")
    void min_populatedTree_returnsMin() {
      bst.insert(10);
      bst.insert(5);
      bst.insert(15);
      bst.insert(3);
      assertEquals(Optional.of(3), bst.min());
    }

    @Test
    @DisplayName("Max of populated tree")
    void max_populatedTree_returnsMax() {
      bst.insert(10);
      bst.insert(5);
      bst.insert(15);
      bst.insert(20);
      assertEquals(Optional.of(20), bst.max());
    }

    @Test
    @DisplayName("Min of empty tree")
    void min_emptyTree_returnsEmpty() {
      assertEquals(Optional.empty(), bst.min());
    }

    @Test
    @DisplayName("Max of empty tree")
    void max_emptyTree_returnsEmpty() {
      assertEquals(Optional.empty(), bst.max());
    }
  }

  // ==================== Height/Size Tests ====================

  @Nested
  @DisplayName("Height and Size Tests")
  class HeightSizeTests {

    @Test
    @DisplayName("Empty tree height is -1")
    void height_emptyTree_returnsNegativeOne() {
      assertEquals(-1, bst.height());
    }

    @Test
    @DisplayName("Single element height is 0")
    void height_singleElement_returnsZero() {
      bst.insert(10);
      assertEquals(0, bst.height());
    }

    @Test
    @DisplayName("Balanced tree height")
    void height_balancedTree_correct() {
      bst.insert(10);
      bst.insert(5);
      bst.insert(15);
      assertEquals(1, bst.height());
    }

    @Test
    @DisplayName("Empty tree is empty")
    void isEmpty_emptyTree_returnsTrue() {
      assertTrue(bst.isEmpty());
    }

    @Test
    @DisplayName("Non-empty tree is not empty")
    void isEmpty_nonEmptyTree_returnsFalse() {
      bst.insert(10);
      assertFalse(bst.isEmpty());
    }
  }

  // ==================== Traversal Tests ====================

  @Nested
  @DisplayName("Traversal Tests")
  class TraversalTests {

    @BeforeEach
    void setUpTree() {
      //        8
      //       / \
      //      3   10
      //     / \    \
      //    1   6   14
      bst.insert(8);
      bst.insert(3);
      bst.insert(10);
      bst.insert(1);
      bst.insert(6);
      bst.insert(14);
    }

    @Test
    @DisplayName("In-order traversal returns sorted elements")
    void inOrder_returnsSorted() {
      MyList<Integer> result = bst.inOrder();
      assertEquals(6, result.size());
      assertEquals(1, result.get(0));
      assertEquals(3, result.get(1));
      assertEquals(6, result.get(2));
      assertEquals(8, result.get(3));
      assertEquals(10, result.get(4));
      assertEquals(14, result.get(5));
    }

    @Test
    @DisplayName("Pre-order traversal")
    void preOrder_correct() {
      MyList<Integer> result = bst.preOrder();
      assertEquals(8, result.get(0));
      assertEquals(3, result.get(1));
      assertEquals(1, result.get(2));
      assertEquals(6, result.get(3));
      assertEquals(10, result.get(4));
      assertEquals(14, result.get(5));
    }

    @Test
    @DisplayName("Post-order traversal")
    void postOrder_correct() {
      MyList<Integer> result = bst.postOrder();
      assertEquals(1, result.get(0));
      assertEquals(6, result.get(1));
      assertEquals(3, result.get(2));
      assertEquals(14, result.get(3));
      assertEquals(10, result.get(4));
      assertEquals(8, result.get(5));
    }

    @Test
    @DisplayName("Level-order traversal")
    void levelOrder_correct() {
      MyList<Integer> result = bst.levelOrder();
      assertEquals(8, result.get(0));
      assertEquals(3, result.get(1));
      assertEquals(10, result.get(2));
      assertEquals(1, result.get(3));
      assertEquals(6, result.get(4));
      assertEquals(14, result.get(5));
    }

    @Test
    @DisplayName("Iterative in-order matches recursive")
    void inOrderIterative_matchesRecursive() {
      MyList<Integer> recursive = bst.inOrder();
      MyList<Integer> iterative = bst.inOrderIterative();
      assertEquals(recursive.size(), iterative.size());
      for (int i = 0; i < recursive.size(); i++) {
        assertEquals(recursive.get(i), iterative.get(i));
      }
    }

    @Test
    @DisplayName("Iterative pre-order matches recursive")
    void preOrderIterative_matchesRecursive() {
      MyList<Integer> recursive = bst.preOrder();
      MyList<Integer> iterative = bst.preOrderIterative();
      assertEquals(recursive.size(), iterative.size());
      for (int i = 0; i < recursive.size(); i++) {
        assertEquals(recursive.get(i), iterative.get(i));
      }
    }

    @Test
    @DisplayName("Iterative post-order matches recursive")
    void postOrderIterative_matchesRecursive() {
      MyList<Integer> recursive = bst.postOrder();
      MyList<Integer> iterative = bst.postOrderIterative();
      assertEquals(recursive.size(), iterative.size());
      for (int i = 0; i < recursive.size(); i++) {
        assertEquals(recursive.get(i), iterative.get(i));
      }
    }

    @Test
    @DisplayName("Traversals on empty tree return empty lists")
    void traversals_emptyTree_returnEmptyLists() {
      BinarySearchTree<Integer> empty = new BinarySearchTree<>();
      assertTrue(empty.inOrder().isEmpty());
      assertTrue(empty.preOrder().isEmpty());
      assertTrue(empty.postOrder().isEmpty());
      assertTrue(empty.levelOrder().isEmpty());
      assertTrue(empty.inOrderIterative().isEmpty());
      assertTrue(empty.preOrderIterative().isEmpty());
      assertTrue(empty.postOrderIterative().isEmpty());
    }
  }

  // ==================== Mastery Check Tests ====================

  @Nested
  @DisplayName("Mastery Check Tests")
  class MasteryCheckTests {

    @Test
    @DisplayName("Mastery check: Sequential insert 1-100 produces degenerate tree with height 99")
    void masteryCheck_sequentialInsert_heightIs99() {
      for (int i = 1; i <= 100; i++) {
        bst.insert(i);
      }
      assertEquals(100, bst.size());
      assertEquals(99, bst.height());
    }

    @Test
    @DisplayName("Mastery check: In-order traversal of large tree is sorted")
    void masteryCheck_largeTree_inOrderIsSorted() {
      int[] values = {50, 25, 75, 12, 37, 62, 87, 6, 18, 31, 43, 56, 68, 81, 93};
      for (int v : values) {
        bst.insert(v);
      }
      MyList<Integer> sorted = bst.inOrder();
      for (int i = 1; i < sorted.size(); i++) {
        assertTrue(sorted.get(i - 1) < sorted.get(i));
      }
    }
  }
}
