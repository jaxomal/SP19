import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author willi
 * @param <Key> the key.
 * @param <Value> the value.
 */
public class BSTMap<Key extends Comparable<Key>, Value> implements Map61B<Key, Value> {
    /** A singular node holding a key-value pair */
    private class Node {
        /** The key */
        private Key key;
        /** The value */
        private Value value;
        /** The left and right children of the current node */
        private Node left, right;

        private Node(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }
    /** The root of the BST */
    private Node root;

    /**
     * Returns null if the key doesn't exist.
     * @param k the key
     * @return the value associated with the key
     */
    public Value get(Key k) {
        if (k == null) {
            throw new IllegalArgumentException("calls put() with a null key");
        }
        return get(root, k);
    }

    /** Helper function for get() */
    private Value get(Node x, Key k) {
        if (x == null) {
            return null;
        }
        int cmp = k.compareTo(x.key);
        if (cmp > 0) {
            return get(x.right, k);
        } else if (cmp < 0) {
            return get(x.left, k);
        }
        return x.value;
    }

    /**
     * Inserts the specified key-value pair into the symbol table.
     * @param  k the key
     * @param  v the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key k, Value v) {
        if (k == null) {
            throw new IllegalArgumentException("calls put() with a null key");
        }
        if (root == null) {
            root = new Node(k, v);
        } else {
            put(root, k, v);
        }
    }

    /** Helper function for put() */
    private Node put(Node x, Key k, Value v) {
        if (x == null) {
            return new Node(k, v);
        }
        int cmp = k.compareTo(x.key);
        if (cmp > 0) {
            x.right = put(x.right, k, v);
        } else if (cmp < 0) {
            x.left = put(x.left, k, v);
        }
        return x;
    }

    /**
     * @return the number of nodes inside your BSTMap.
     */
    public int size() {
        return size(root);
    }

    /** Helper function for size() */
    private int size(Node x) {
        if (x == null) {
            return 0;
        } else {
            return size(x.left) + size(x.right) + 1;
        }
    }

    /**
     * The main function for containsKey.
     * @param k key you're searching for.
     * @return whether the key exists
     */
    public boolean containsKey(Key k) {
        if (k == null) {
            throw new IllegalArgumentException("calls containsKey() with a null key");
        }
        return containsKey(root, k);
    }

    /** Helper function for containskey */
    private boolean containsKey(Node x, Key k) {
        if (x == null) {
            return false;
        }
        int cmp = k.compareTo(x.key);
        if (cmp > 0) {
            return containsKey(x.right, k);
        } else if (cmp < 0) {
            return containsKey(x.left, k);
        } else {
            return true;
        }
    }

    /** Gets rid of the BSTMap */
    public void clear() {
        root = null;
    }

    /**
     * the main keySet function.
     * @return a set of all the keys.
     */
    public Set<Key> keySet() {
        HashSet<Key> res = new HashSet<>();
        keySet(root, res);
        return res;
    }

    /** This is the helper function of keySet. */
    private void keySet(Node x, HashSet<Key> res) {
        if (x == null) {
            return;
        }
        res.add(x.key);
        keySet(x.left, res);
        keySet(x.right, res);
    }

    /** Not implemented. */
    public Value remove(Key key) {
        throw new IllegalArgumentException("calls remove() with a null key");
    }

    /** Not implemented. */
    public Value remove(Key key, Value value) {
        throw new IllegalArgumentException("calls containsKey() with a null key");
    }

    /** Iterator for the BSTMap class. */
    private class BSTMapIterator implements Iterator<Key> {
        /** Holds the all the keys of the BSTMap */
        private ArrayList<Key> keys;
        /** The current index of the ArrayList */
        private int index;

        private BSTMapIterator() {
            keys = new ArrayList<>();
            listKeys(root, keys);
            index = 0;
        }

        /** Is there a next key? */
        public boolean hasNext() {
            return !(index == keys.size());
        }

        /**
         * Returns the key and advances the position by 1.
         * @return the next key.
         */
        public Key next() {
            Key key = keys.get(index);
            index += 1;
            return key;
        }

        /**
         * Fills up the arraylist with keys.
         * @param x the head node.
         * @param res the arraylist that will store keys.
         */
        private void listKeys(Node x, ArrayList<Key> res) {
            if (x == null) {
                return;
            }
            res.add(x.key);
            listKeys(x.left, res);
            listKeys(x.right, res);
        }
    }

    /**
     * @return a BSTMapIterator.
     */
    public Iterator<Key> iterator() {
        return new BSTMapIterator();
    }
}
