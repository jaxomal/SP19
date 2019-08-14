import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** The {@code MyHashMap} class represents a symbol table of generic
 *  key-value pairs. This implementation uses a separate chaining hash table.
 * @author willi
 */
public class MyHashMap<Key, Value> implements Map61B<Key, Value> {
    /** Array of entry objects which act as bins to store a key value pair. */
    private LinkedList<Entry>[] bins;
    /** The amount of items currently in the map. */
    private int size;
    /** The maximum load size of the array before resizing. */
    private double loadFactor;
    /** Stores all the keys that have been inserted into the map. */
    private Set<Key> keySet;
    /**
     * @param DEFAULT_BIN_SIZE The default binSize.
     */
    private static final int DEFAULT_BIN_SIZE = 16;
    /**
     * @param DEFAULT_LOAD_FACTOR The default loadFactor.
     */
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    /**
     * Initializes the bins using binSize, and sets loadFactor. Size is
     * initially 0. If bins and loadFactor isn't given the default is
     * 16 and 0.75 respectively.
     * @param binSize the initial binsize.
     * @param loadFactor the max loadFactor.
     */
    public MyHashMap(int binSize, double loadFactor) {
        this.bins = new LinkedList[binSize];
        this.loadFactor = loadFactor;
        this.size = 0;
        this.keySet = new HashSet<>();
    }

    public MyHashMap() {
        this.bins = new LinkedList[DEFAULT_BIN_SIZE];
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.size = 0;
        this.keySet = new HashSet<>();
    }

    /**
     * Literally nukes the HashMap, destroys all entries and resizes the HashMap
     * to the default binSize.
     */
    @Override
    public void clear() {
        this.bins = new LinkedList[DEFAULT_BIN_SIZE];
        this.size = 0;
        this.keySet = new HashSet<>();
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(Key key) {
        int hash = hash(key);
        if (bins[hash] == null) {
            return false;
        }
        for (Entry entry : bins[hash]) {
            if (entry.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public Value get(Key key) {
        int hash = hash(key);
        if (bins[hash] == null) {
            return null;
        }
        for (Entry entry : bins[hash]) {
            if (entry.key.equals(key)) {
                return entry.val;
            }
        }
        return null;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(Key key, Value val) {
        int hash = hash(key);
        if (bins[hash] == null) {
            bins[hash] = new LinkedList<Entry>();
        } else {
            for (Entry entry : bins[hash]) {
                if (entry.key.equals(key)) {
                    entry.val = val;
                    return;
                }
            }
        }
        bins[hash].addFirst(new Entry(key, val));
        keySet.add(key);
        size++;
        if (size() / bins.length > loadFactor) {
            resize(size() * 2);
        }
    }

    /** Returns a Set view of the keys contained in this map. */
    @Override
    public Set<Key> keySet() {
        return keySet;
    }

    /** Unsupported Operation, will return an exception */
    @Override
    public Value remove(Key key) {
        throw new UnsupportedOperationException();
    }

    /** Unsupported Operation, will return an exception */
    @Override
    public Value remove(Key key, Value val) {
        throw new UnsupportedOperationException();
    }

    /** Returns an iterator of the map. */
    @Override
    public Iterator<Key> iterator() {
        return new MyHashMapIterator();
    }

    /**
     * Return a value in the range 0 .. bins.size ()-1, based on
     * the hash code of KEY.
     * @param key returns the hashcode of the given object.
     */
    private int hash(Object key) {
        return (key == null) ? 0 : (0x7fffffff & key.hashCode()) % bins.length;
    }

    /**
     * Returns a new array that is exactly the same but has a different size.
     * The given size must be bigger than the original.
     * @param newSize the size you want to resize to.
     */
    private void resize(int newSize) {
        ArrayList<Entry> temp = new ArrayList<>();
        for (Key key : keySet) {
            Value val = get(key);
            temp.add(new Entry(key, val));
        }
        bins = new LinkedList[newSize];
        size = 0;
        for (Entry entry : temp) {
            put(entry.key, entry.val);
        }
    }

    /** A class that defines the Iterator of the map */
    private class MyHashMapIterator implements Iterator {
        Iterator<Key> keys;

        /**
         * @return whether or not there is another entry.
         */
        public MyHashMapIterator() {
            keys = keySet.iterator();
        }

        public boolean hasNext() {
            return keys.hasNext();
        }

        /**
         * @return advances the current position and returns the current value
         */
        public Key next() {
            return keys.next();
        }
    }

    private class Entry {
        /**
         * Stores KEY as the key in this key-value pair, VAL as the value, and
         * NEXT as the next node in the linked list.
         */
        public Entry(Key key, Value val) {
            this.key = key;
            this.val = val;
        }

        /** Stores the key of the key-value pair of this node in the list. */
        private Key key;
        /** Stores the value of the key-value pair of this node in the list. */
        private Value val;
    }
}
