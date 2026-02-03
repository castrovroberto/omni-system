package com.omni.core.tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TrieTest {

    private Trie trie;

    @BeforeEach
    void setUp() {
        trie = new Trie();
    }

    @Nested
    class InsertTests {

        @Test
        void insert_singleWord_searchFindsIt() {
            trie.insert("hello");
            assertTrue(trie.search("hello"));
        }

        @Test
        void insert_multipleWords_allSearchable() {
            trie.insert("hello");
            trie.insert("help");
            trie.insert("world");

            assertTrue(trie.search("hello"));
            assertTrue(trie.search("help"));
            assertTrue(trie.search("world"));
        }
    }

    @Nested
    class SearchTests {

        @BeforeEach
        void setUpWords() {
            trie.insert("apple");
            trie.insert("app");
        }

        @Test
        void search_exactWord_returnsTrue() {
            assertTrue(trie.search("apple"));
            assertTrue(trie.search("app"));
        }

        @Test
        void search_prefixOnly_returnsFalse() {
            assertFalse(trie.search("ap"));
        }

        @Test
        void search_nonExistent_returnsFalse() {
            assertFalse(trie.search("banana"));
        }

        @Test
        void search_emptyString_returnsFalse() {
            assertFalse(trie.search(""));
        }
    }

    @Nested
    class StartsWithTests {

        @BeforeEach
        void setUpWords() {
            trie.insert("apple");
            trie.insert("application");
        }

        @Test
        void startsWith_validPrefix_returnsTrue() {
            assertTrue(trie.startsWith("app"));
            assertTrue(trie.startsWith("appl"));
            assertTrue(trie.startsWith("apple"));
        }

        @Test
        void startsWith_invalidPrefix_returnsFalse() {
            assertFalse(trie.startsWith("ban"));
            assertFalse(trie.startsWith("apc"));
        }

        @Test
        void startsWith_emptyPrefix_returnsTrue() {
            assertTrue(trie.startsWith(""));
        }
    }

    @Nested
    class TrieNodeTests {

        @Test
        void getChild_existingChild_returnsNode() {
            trie.insert("abc");
            Trie.TrieNode root = trie.getRoot();

            assertNotNull(root.getChild('a'));
            assertTrue(root.hasChild('a'));
        }

        @Test
        void getChild_missingChild_returnsNull() {
            trie.insert("abc");
            Trie.TrieNode root = trie.getRoot();

            assertNull(root.getChild('z'));
            assertFalse(root.hasChild('z'));
        }
    }

    @Nested
    class EdgeCases {

        @Test
        void insert_singleCharacter_works() {
            trie.insert("a");
            assertTrue(trie.search("a"));
            assertTrue(trie.startsWith("a"));
        }

        @Test
        void insert_overlappingWords_allDistinct() {
            trie.insert("a");
            trie.insert("ab");
            trie.insert("abc");

            assertTrue(trie.search("a"));
            assertTrue(trie.search("ab"));
            assertTrue(trie.search("abc"));
        }

        @Test
        void insert_sameWordTwice_noError() {
            trie.insert("test");
            trie.insert("test");

            assertTrue(trie.search("test"));
        }
    }
}
