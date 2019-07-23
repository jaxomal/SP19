package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void testBasics() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer(10);
        assertEquals(10, arb.capacity());
        assertTrue(arb.isEmpty());
        arb.enqueue(5);
        assertEquals(1, arb.fillCount());
        assertEquals(5, (int) arb.peek());
        int res = arb.dequeue();
        assertEquals(5, res);
    }

    @Test
    public void testOverflow() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer(5);
        for (int i = 0; i < 5; i++) {
            arb.enqueue(1);
        }
        assertTrue(arb.isFull());
        // Should return an error
        // arb.enqueue(2);
    }
}
