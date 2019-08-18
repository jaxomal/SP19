import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author willis
 * The TrieSet class represents an symbol table of key-value
 * pairs, with string keys and generic values.
 */
public class MyTrieSet implements TrieSet61B {
    /** Root of trie. */
    private Node root;

    /** HashMap Trie. */
    public MyTrieSet() {
        root = new Node(false);
    }

    /** Clears all items out of Trie. */
    public void clear() {
        root = new Node(false);
    }

    /** Returns true if the Trie contains KEY, false otherwise. */
    public boolean contains(String key) {
        Node current = root;
        for (int i = 0; i < key.length(); i++) {
            if (!current.next.containsKey(key.charAt(i))) {
                return false;
            }
            current = current.next.get(key.charAt(i));
        }
        if (!current.isKey) {
            return false;
        }
        return true;
    }

    /** Inserts string KEY into Trie. */
    public void add(String key) {
        Node current = root;
        for (int i = 0; i < key.length(); i++) {
            if (i == key.length() - 1) {
                if (current.next.containsKey(key.charAt(i))) {
                    current.next.get(key.charAt(i)).isKey = true;
                } else {
                    current.next.put(key.charAt(i), new Node(true));
                }
            } else if (!current.next.containsKey(key.charAt(i))) {
                current.next.put(key.charAt(i), new Node(false));
            }
            current = current.next.get(key.charAt(i));
        }
    }

    /**
     * @return Returns a list of all words that start with PREFIX.
     */
    public List<String> keysWithPrefix(String prefix) {
        List<String> res = new LinkedList<>();
        Node current = root;
        for (int i = 0; i < prefix.length(); i++) {
            if (!current.next.containsKey(prefix.charAt(i))) {
                return null;
            }
            current = current.next.get(prefix.charAt(i));
        }
        return keysWithPrefix(res, current, prefix);
    }

    /** Helper method for keysWithPrefix(String prefix). */
    private List<String> keysWithPrefix(List<String> res, Node curr,
                                        String build) {
        if (curr.isKey) {
            res.add(build);
        }
        for (Character k : curr.next.keySet()) {
            keysWithPrefix(res, curr.next.get(k), build + k);
        }
        return res;
    }

    /** Returns the longest prefix of KEY that exists in the Trie
     * Not required for Lab 9. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }

    /** A Trie node. */
    private static class Node {
        /** Whether the character ends a key. */
        private boolean isKey;
        /** Stores the next character nodes. */
        private HashMap<Character, Node> next;

        /** A Trie Node.
         * @param b whether or not this is a marked node
         */
        Node(boolean b) {
            isKey = b;
            next = new HashMap<>();
        }
    }
}
