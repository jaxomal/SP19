package bearmaps;

import java.util.NoSuchElementException;

/**
 * The priority is extrinsic to the object. That is, rather than relying
 * on some sort of comparison function to decide which item is less than
 * another, we simply assign a priority value using the add or
 * changePriority functions.
 * @param <T> a generic type.
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private Entry[] pq;
    private int size;

    @SuppressWarnings("unchecked")
    public ArrayHeapMinPQ(int initCapacity) {
        pq = (Entry[]) new ArrayHeapMinPQ<?>.Entry[initCapacity + 1];
        size = 0;
    }

    public ArrayHeapMinPQ() {
        this(1);
    }
    /** Adds an item with the given priority value. Throws an
     * IllegalArgumentExceptionb if item is already present.
     * You may assume that item is never null.
     */
    public void add(T item, double priority) {
        if (size == pq.length - 1) {
            resize(2 * pq.length);
        }
        pq[++size] = new Entry(item, priority);
        swim(size);
    }
    /* Returns true if the PQ contains the given item. */
    public boolean contains(T item) {
        return find(item, 1) > 0;
    }
    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    public T getSmallest() {
        return pq[1].item;
    }
    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    public T removeSmallest() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Entry min = pq[1];
        swap(1, size--);
        sink(1);
        pq[size + 1] = null;
        if ((size > 0) && (size == (pq.length - 1) / 4)) {
            resize(pq.length / 2);
        }
        return min.item;
    }
    /* Returns the number of items in the PQ. */
    public int size() {
        return size;
    }

    public int getCapacity() {
        return pq.length;
    }
    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    public void changePriority(T item, double priority) {
        int found = find(item);
        if (found < 0) {
            throw new NoSuchElementException();
        }
        pq[found].priority = priority;
    }

    private class Entry {
        private T item;
        private double priority;

        public Entry(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }
    }

    /** Helper Methods */
    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        Entry[] temp = (Entry[]) new ArrayHeapMinPQ<?>.Entry[capacity];
        for (int i = 1; i <= size; i++) {
            temp[i] = pq[i];
        }
        pq = temp;
    }

    private int find(T item) {
        return find(item, 1);
    }

    private int find(T item, int pos) {
        if (pos >= pq.length || pq[pos] == null) {
            return -1;
        }
        if (pq[pos].item.equals(item)) {
            return pos;
        }
        int posLeft = find(item, pos * 2);
        int posRight = find(item, pos * 2 + 1);
        if (posLeft > 0) {
            return posLeft;
        } else if (posRight > 0) {
            return posRight;
        } else {
            return -1;
        }
    }

    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            swap(k, k / 2);
            k = k / 2;
        }
    }

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

    private void swap(int j, int k) {
        Entry temp = pq[j];
        pq[j] = pq[k];
        pq[k] = temp;
    }

    private boolean greater(int j, int k) {
        if (pq[j] == null) {
            return false;
        } else if (pq[k] == null) {
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

    public static void main(String[] args) {
    }
}
