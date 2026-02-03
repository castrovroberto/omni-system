package com.omni.core.tree;

/**
 * A Trie (prefix tree) for efficient word storage and retrieval.
 *
 * <p><b>Interview Question:</b> Used in "Word Search II" (LeetCode #212) and "Implement Trie"
 * (LeetCode #208) - commonly asked to test understanding of tree structures for string problems.
 *
 * <p><b>Design:</b> Uses fixed-size array for children (26 lowercase letters) for O(1) child
 * lookup.
 *
 * <p><b>Interview Talking Points:</b>
 *
 * <ul>
 *   <li>Array vs Map for children? Array is O(1) but wastes space for sparse tries
 *   <li>Why not just use HashSet? Trie enables prefix matching and autocomplete
 *   <li>Space optimization: can use Map<Character, TrieNode> for Unicode support
 * </ul>
 */
public class Trie {

  private final TrieNode root;

  /** Creates an empty Trie. */
  public Trie() {
    this.root = new TrieNode();
  }

  /**
   * Inserts a word into the trie.
   *
   * <p><b>Complexity:</b> O(m) where m is the word length.
   *
   * @param word the word to insert (lowercase letters only)
   */
  public void insert(String word) {
    TrieNode node = root;
    for (char c : word.toCharArray()) {
      int index = c - 'a';
      if (node.children[index] == null) {
        node.children[index] = new TrieNode();
      }
      node = node.children[index];
    }
    node.isEndOfWord = true;
  }

  /**
   * Returns true if the word exists in the trie.
   *
   * <p><b>Complexity:</b> O(m) where m is the word length.
   *
   * @param word the word to search for
   * @return true if the exact word exists
   */
  public boolean search(String word) {
    TrieNode node = findNode(word);
    return node != null && node.isEndOfWord;
  }

  /**
   * Returns true if any word in the trie starts with the given prefix.
   *
   * <p><b>Complexity:</b> O(m) where m is the prefix length.
   *
   * @param prefix the prefix to search for
   * @return true if a word with this prefix exists
   */
  public boolean startsWith(String prefix) {
    return findNode(prefix) != null;
  }

  /**
   * Helper method to traverse to a node for a given string.
   *
   * @param str the string path to follow
   * @return the node at the end of the path, or null if not found
   */
  private TrieNode findNode(String str) {
    TrieNode node = root;
    for (char c : str.toCharArray()) {
      int index = c - 'a';
      if (node.children[index] == null) {
        return null;
      }
      node = node.children[index];
    }
    return node;
  }

  /**
   * Returns the root node for advanced traversal (e.g., Word Search II).
   *
   * @return the root TrieNode
   */
  public TrieNode getRoot() {
    return root;
  }

  /**
   * A node in the Trie structure.
   *
   * <p><b>Memory:</b> Each node uses 26 * 8 bytes (references) + 1 byte (boolean) ≈ 209 bytes on
   * 64-bit JVM. For sparse tries, consider using Map<Character, TrieNode> instead.
   */
  public static class TrieNode {
    /** Children array for 26 lowercase letters. */
    public final TrieNode[] children;

    /** True if this node marks the end of a valid word. */
    public boolean isEndOfWord;

    /** Creates a new TrieNode with no children. */
    public TrieNode() {
      this.children = new TrieNode[26];
      this.isEndOfWord = false;
    }

    /**
     * Gets the child node for a character.
     *
     * @param c the character (must be lowercase a-z)
     * @return the child node, or null if not present
     */
    public TrieNode getChild(char c) {
      return children[c - 'a'];
    }

    /**
     * Checks if this node has a child for the given character.
     *
     * @param c the character to check
     * @return true if a child exists
     */
    public boolean hasChild(char c) {
      return children[c - 'a'] != null;
    }
  }
}
