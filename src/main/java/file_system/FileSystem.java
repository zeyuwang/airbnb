package file_system;

import java.util.*;

import common.Trie;
import org.junit.*;

import static org.junit.Assert.*;

public class FileSystem {
    /*
        File System
        AirBnB Interview Question
     */
    public class Solution {
        Map<String, Integer> pathMap;
        Map<String, Runnable> callbackMap;

        public Solution() {
            this.pathMap = new HashMap<>();
            this.callbackMap = new HashMap<>();
            this.pathMap.put("", 0);
        }

        public boolean create(String path, int value) {
            if (pathMap.containsKey(path)) {
                return false;
            }

            int lastSlashIndex = path.lastIndexOf("/");
            if (!pathMap.containsKey(path.substring(0, lastSlashIndex))) {
                return false;
            }

            pathMap.put(path, value);
            return true;
        }

        public boolean set(String path, int value) {
            if (!pathMap.containsKey(path)) {
                return false;
            }

            pathMap.put(path, value);

            // Trigger callbacks
            String curPath = path;
//            while (curPath.length() > 0) {
//                if (callbackMap.containsKey(curPath)) {
//                    callbackMap.get(curPath).run();
//                }
//                int lastSlashIndex = path.lastIndexOf("/");
//                curPath = curPath.substring(0, lastSlashIndex);
//            }

            return true;
        }

        public Integer get(String path) {
            return pathMap.get(path);
        }

        public boolean watch(String path, Runnable callback) {
            if (!pathMap.containsKey(path)) {
                return false;
            }

            callbackMap.put(path, callback);
            return true;
        }
    }

    // - Requirement:
    // a file system with folder only
    // each folder has a value
    // each folder has a watch
    // If a folder is watched, when the folder's value is updated, the monitor logic will be trigger.
    // If the parent folder does not exist, the create api should return false.
    // If the folder already exist, return false.
    // Similarly for set api and get api.
    // follow up - how to throw proper exceptions for false cases.

    // Design of algorithms
    // Use Trie to store the current file system structure.
    // If the parent path exist, try to create child folder.
    // If child folder exist, return false
    // if child folder doesn't return false
    // ...
    public class Solution2 {
        TrieNode root;

        class TrieNode {
            Integer content;
            Map<String, TrieNode> children;
            Runnable runnable;
            TrieNode(Integer content) {
                this.content = content;
                children = new HashMap<>();
            }
        }

        public Solution2() {
            root = new TrieNode(-1);
        }

        // path is expected to without heading slash, with trailing slash
        private TrieNode traverse(TrieNode node, String path) {
            if (path.isEmpty()){
                return node;
            }
            int firstIdx = path.indexOf("/");
            String firstLayerFolder = path.substring(0,firstIdx);
            TrieNode child = node.children.get(firstLayerFolder);
            String remainingPath = path.substring(firstIdx+1, path.length());
            if (child != null && !remainingPath.isEmpty()) {
                return traverse(child, remainingPath);
            }
            return child;
        }

        public boolean create(String path, int value) {
            int lastIdx = path.lastIndexOf("/");
            String parentPath = path.substring(1, lastIdx+1); // without heading slash, with trailing slash
            String childFolderName = path.substring(lastIdx+1, path.length());
            TrieNode parentNode = traverse(root, parentPath);
            if (parentNode != null && !parentNode.children.containsKey(childFolderName)) {
                TrieNode childNode = new TrieNode(value);
                parentNode.children.put(childFolderName, childNode);
                return true;
            }
            return false;
        }

        public boolean set(String path, int value) {
            // path Without Heading Slash With Trailing Slash
            String formattedPath = new StringBuilder(path).deleteCharAt(0).append('/').toString();
            TrieNode node = traverse(root, formattedPath);
            if (node != null) {
                node.content = value;
                if (node.runnable != null) {
                    node.runnable.run();
                }
                return true;
            }
            return false;
        }

        public Integer get(String path) {
            // path Without Heading Slash With Trailing Slash
            String formattedPath = new StringBuilder(path).deleteCharAt(0).append('/').toString();
            TrieNode node = traverse(root, formattedPath);
            if (node != null) {
                return node.content;
            }
            return null;
        }

        public boolean watch(String path, Runnable callback) {
            // path Without Heading Slash With Trailing Slash
            String formattedPath = new StringBuilder(path).deleteCharAt(0).append('/').toString();
            TrieNode node = traverse(root, formattedPath);
            if (node != null) {
                if (node.runnable != null) {
                    node.runnable.run();
                }
                node.runnable = callback;
                return true;
            }
            return false;
        }
    }

    public static class UnitTest {
        @Test
        public void test1() {
            Solution2 sol = new FileSystem().new Solution2();
            assertTrue(sol.create("/a",1));
            assertEquals(1, (int)sol.get("/a"));
            assertTrue(sol.create("/a/b",2));
            assertTrue(sol.create("/a/b/c",3));
            assertEquals(2, (int)sol.get("/a/b"));
            assertEquals(3, (int)sol.get("/a/b/c"));
            assertEquals(2, (int)sol.get("/a/b"));
            assertTrue(sol.set("/a/b",3));
            assertEquals(3, (int)sol.get("/a/b"));
            assertFalse(sol.create("/c/d",4));
            assertFalse(sol.set("/c/d",4));

            sol = new FileSystem().new Solution2();
            assertTrue(sol.create("/NA",1));
            assertTrue(sol.create("/EU",2));
            assertEquals(1, (int)sol.get("/NA"));
            assertTrue(sol.create("/NA/CA",101));
            assertEquals(101, (int)sol.get("/NA/CA"));
            assertTrue(sol.set("/NA/CA",102));
            assertEquals(102, (int)sol.get("/NA/CA"));
            assertTrue(sol.create("/NA/US",101));
            assertEquals(101, (int)sol.get("/NA/US"));
            assertFalse(sol.create("/NA/CA",101));
            assertFalse(sol.create("/SA/MX",103));
            assertFalse(sol.set("SA/MX",103));
        }
    }
}

