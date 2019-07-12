public class ArrayDeque<T> {
    /**
     * Instance variables, we need a Type array to hold generic items,
     * size tells us the amount of stuff we have, nextFirst tells us
     * the index we add to first, nextLast tells us the position that
     * we add to last.
     */
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        nextFirst = 7;
        nextLast = 0;
        size = 0;
        items = (T []) new Object[8];
    }

    /**
     * Invariants:
     * The next position for adding to the front of the deque is at nextFirst
     * The next position for adding to the last of the deque is at nextLast
     * To get the last element you look at nextLast minusOne
     * To get the first element you look at firstLast plusOne
     * You cannot add two items to the same slot
     */

    public void addFirst(T item) {
        if (items[nextFirst] != null) {
            resize(items.length * 10);
        }
        items[nextFirst] = item;
        nextFirst = minusOne(nextFirst);
        size++;
    }

    public void addLast(T item) {
        if (items[nextLast] != null) {
            resize(items.length * 10);
        }
        items[nextLast] = item;
        nextLast = plusOne(nextLast);
        size++;
    }

    public void printDeque() {
        int counter = 0;
        // To get the first element we must look at nextFirst plusOne
        int curr = plusOne(nextFirst);
        // We only need to look up to the size
        while (counter < size) {
            System.out.print(items[curr] + " ");
            curr = plusOne(curr);
            counter++;
        }
        // Print out a line after
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T first = items[plusOne(nextFirst)];
        nextFirst = plusOne(nextFirst);
        // We must destruct the reference
        items[nextFirst] = null;
        size--;
        return first;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T last = items[minusOne(nextLast)];
        nextLast = minusOne(nextLast);
        items[nextLast] = null;
        size--;
        return last;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        // Create a counter to track relative position
        int counter = 0;
        // To get to the 0th element you must go to nextFirst plusOne
        int curr = plusOne(nextFirst);
        while (counter <= index) {
            curr = plusOne(curr);
        }
        // Return the indexth element
        return items[curr];
    }

    public int size() {
        return size;
    }

    private void resize(int capacity) {
        T[] newItems = (T []) new Object[capacity];
        // Copy all items over
        int curr = plusOne(nextFirst);
        for (int i = 0; i < size; i++) {
            newItems[i] = items[curr];
            curr = plusOne(curr);
        }
        items = newItems;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    private int minusOne(int index) {
        if (index == 0) {
            return items.length - 1;
        }
        return index - 1;
    }

    private int plusOne(int index) {
        if (index == items.length - 1) {
            return 0;
        }
        return index + 1;
    }
}