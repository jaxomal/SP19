package es.datastructur.synthesizer;
import java.util.Iterator;

/**
 * This class will be used to create the GuitarString class of the assignment.
 * @author willis
 * @param <T> Takes in any type of item
 */
public class ArrayRingBuffer<T> implements BoundedQueue<T> {
    /** Index for the next dequeue or peek. */
    private int first;
    /** Index for the next enqueue. */
    private int last;
    /** Variable for the fillCount. */
    private int fillCount;
    /** Array for storing the buffer data. */
    private T[] rb;

    /**
     * An ArrayRingBufferIterator used to make the ArrayRingBuffer class
     * iterable. It is implemented by keeping track of the current position
     * in the array and tht total amount of items pulled out.
     */
    private class ArrayRingBufferIterator implements Iterator<T> {
        /** The total amount of items pulled. */
        private int pos;
        /** The current position inside the array. */
        private int arrayPos;

        /** Creates the iterator, the amount pulled when created is 0, and
         * the current position of the array is the instance variable first.
         */
        ArrayRingBufferIterator() {
            pos = 0;
            arrayPos = first;
        }

        @Override
        public boolean hasNext() {
            return pos != rb.length - 1;
        }

        @Override
        public T next() {
            T item = rb[arrayPos];
            pos++;
            arrayPos = plusOne(arrayPos);
            return item;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass()) {
            return false;
        }
        if (this.capacity() != ((ArrayRingBuffer) o).capacity()
                || this.fillCount() != ((ArrayRingBuffer) o).fillCount()) {
            return false;
        }
        return java.util.Arrays.equals(((ArrayRingBuffer) o).rb, this.rb);
    }

    /**
     * Create a new ArrayRingBuffer iterator to make class iterable.
     * @return a new ArrayRingBufferIterator.
     */
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     * @param capacity the maximum amount of items the ArrayRingBuffer can hold
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T []) new Object[capacity];
        first = 0;
        fillCount = 0;
        last = 0;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    @Override
    public void enqueue(T x) {
        if (fillCount == rb.length) {
            throw new RuntimeException("Ring Buffer overflow");
        }
        rb[last] = x;
        last = plusOne(last);
        fillCount++;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        if (fillCount == 0) {
            throw new RuntimeException("Ring Buffer underflow");
        }
        T item = rb[first];
        rb[first] = null;
        first = plusOne(first);
        fillCount--;
        return item;
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T peek() {
        if (fillCount == 0) {
            throw new RuntimeException("Ring Buffer underflow");
        }
        return rb[first];
    }

    @Override
    public int capacity() {
        return rb.length;
    }

    @Override
    public int fillCount() {
        return fillCount;
    }

    /** Finds the incremented array position.
     * @param x the current position
     * @return returns the incremented position
     */
    private int plusOne(int x) {
        if (x == rb.length - 1) {
            return 0;
        }
        return x += 1;
    }
}
