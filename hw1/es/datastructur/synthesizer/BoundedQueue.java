package es.datastructur.synthesizer;
import java.util.Iterator;

/**
 * @author willis
 * @return A Queue that is limited in the number of items it can take
 * @param <T> Any item.
 */

public interface BoundedQueue<T> extends Iterable<T> {
    /** @return Iterator of the queue. */
    Iterator<T> iterator();
    /** return size of the buffer. */
    int capacity();
    /** return number of items currently in the buffer. */
    int fillCount();
    /** add item x to the end.
     * @param x is an item.
     */
    void enqueue(T x);
    /** delete and return item from the front. */
    T dequeue();
    /** return (but do not delete) item from the front. */
    T peek();
    /** @return is the buffer empty (fillCount equals zero)? */
    default boolean isEmpty() {
        return fillCount() == 0;
    }
    /** @return is the buffer full (fillCount is same as capacity. */
    default boolean isFull() {
        return fillCount() == capacity();
    }
}
