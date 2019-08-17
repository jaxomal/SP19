package bearmaps;

import java.util.NoSuchElementException;
import java.util.HashMap;

/**
 * The priority is extrinsic to the object. That is, rather than relying
 * on some sort of comparison function to decide which item is less than
 * another, we simply assign a priority value using the add or
 * changePriority functions.
 * @param <T> a generic type.
 * @author willi
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /** The heap, bts of the PQ. */
    private Entry[] pq;
    /** The amount of things the PQ is holding at a given time. */
    private int size;
    /** Stores the corresponding array position to an item, saves time. */
    private HashMap<T, Integer> map;

    /**
     * @param initCapacity the initial size of the array.
     */
    @SuppressWarnings("unchecked")
    public ArrayHeapMinPQ(int initCapacity) {
        pq = (Entry[]) new ArrayHeapMinPQ<?>.Entry[initCapacity + 1];
        size = 0;
        map = new HashMap<>();
    }

    public ArrayHeapMinPQ() {
        this(1);
    }

    /** Adds an item with the given priority value. Throws an
     * IllegalArgumentException if item is already present.
     * You may assume that item is never null.
     * @param item the item you want to add.
     * @param priority the priority of the item.
     */
    public void add(T item, double priority) {
        if (map.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        if (size == pq.length - 1) {
            resize(2 * pq.length);
        }
        pq[++size] = new Entry(item, priority);
        swim(size);
    }

    /** Returns true if the PQ contains the given item.
     * @param item the item you are checking for
     */
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    /** Returns the minimum item. Throws NoSuchElementException
     * if the PQ is empty.
     */
    public T getSmallest() {
        return pq[1].item;
    }

    /** Removes and returns the minimum item. Throws
     * NoSuchElementException if the PQ is empty.
     */
    public T removeSmallest() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Entry min = pq[1];
        swap(1, size--);
        sink(1);
        pq[size + 1] = null;
        if ((size > 0) && (size < (pq.length - 1) / 4)) {
            resize(pq.length / 2);
        }
        map.remove(min.item);
        return min.item;
    }

    /** Returns the number of items in the PQ. */
    public int size() {
        return size;
    }

    /** Returns the capacity of the underlying heap array */
    public int getCapacity() {
        return pq.length;
    }

    /** Changes the priority of the given item.
     * Throws NoSuchElementException if the item doesn't exist.
     * @param item the thing you want to add.
     * @param priority the lower the priority the closer to the front it will be.
     */
    public void changePriority(T item, double priority) {
        if (!map.containsKey(item)) {
            throw new NoSuchElementException();
        }
        double oldPriority = pq[map.get(item)].priority;
        pq[map.get(item)].priority = priority;
        if (oldPriority > priority) {
            swim(map.get(item));
        } else if (oldPriority < priority) {
            sink(map.get(item));
        }
    }

    /**
     * A private class that allows for the pair storage of an
     * item and a priority in the Priority Queue.
     */
    private class Entry {
        /** An item of type T. */
        private T item;
        /** The priority of the item. */
        private double priority;

        /**
         * @param item the thing you want to add.
         * @param priority the lower the priority the closer to the
         * front it will be.
         */
        Entry(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }
    }

    /**
     * Given a capacity, the array will enlarge itself to hold capacity
     * amount of items, therefore the capacity should be above the
     * original size of the array.
     * @param capacity the amount of stuff the array should hold.
     */
    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        Entry[] temp = (Entry[]) new ArrayHeapMinPQ<?>.Entry[capacity];
        for (int i = 1; i <= size; i++) {
            temp[i] = pq[i];
        }
        pq = temp;
    }

    /**
     * Given the index of an entry, swim() will swim (move upwards)
     * the entry to the right place.
     * @param k the index of an entry.
     */
    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            swap(k, k / 2);
            k = k / 2;
        }
    }

    /**
     * Given the index of an entry, sink() will sink (move downwards)
     * the entry to the right place.
     * @param k the index of an entry.
     */
    private void sink(int k) {
        while (2 * k <= size) {
            int j = 2 * k;
            if (j < size && greater(j,j + 1)) {
                j++;
            }
            if (!greater(k, j)) {
                break;
            }
            swap(k, j);
            k = j;
        }
    }

    /**
     * Swaps the two entries given their index.
     * @param j index of an entry.
     * @param k index of another entry.
     */
    private void swap(int j, int k) {
        Entry temp = pq[j];
        pq[j] = pq[k];
        map.put(pq[j].item, j);
        pq[k] = temp;
        map.put(temp.item, k);
    }

    /**
     * @param j index of an entry.
     * @param k index of another entry.
     * @return true if j > k, otherwise false.
     */
    private boolean greater(int j, int k) {
        if (pq[j] == null) {
            return false;
        }
        if (pq[k] == null) {
            return true;
        }
        Entry pos0 = pq[j];
        Entry pos1 = pq[k];
        if (pos0.priority > pos1.priority) {
            return true;
        } else {
            return false;
        }
    }
}
