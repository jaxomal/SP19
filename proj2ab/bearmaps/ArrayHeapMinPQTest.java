package bearmaps;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQTest {

    @Test
    public void testInitialization() {
        ArrayHeapMinPQ<Integer> temp = new ArrayHeapMinPQ<>(10);
        ArrayHeapMinPQ<Integer> temp2 = new ArrayHeapMinPQ<>();
    }

    @Test
    public void testResizeUp() {
        ArrayHeapMinPQ<Integer> temp = new ArrayHeapMinPQ<>();
        for (int i = 10; i > 0; i--) {
            temp.add(i, i);
        }
        assertEquals(16, temp.getCapacity());
    }


    @Test
    public void testResizeDown() {
        ArrayHeapMinPQ<Integer> temp = new ArrayHeapMinPQ<>(100);
        temp.add(1, 1);
        temp.add(1, 2);
        temp.removeSmallest();
        assertEquals(50, temp.getCapacity());
    }

    @Test
    public void testGetSmall() {
        ArrayHeapMinPQ<Integer> temp = new ArrayHeapMinPQ<>(10);
        for (int i = 10; i > 0; i--) {
            temp.add(i, i);
        }
        for (int i = 1; i < 11; i++) {
            assertEquals(i, (int) temp.getSmallest());
            temp.removeSmallest();
        }
    }

    @Test
    public void testBasicAddAndRemove() {
        ArrayHeapMinPQ<Integer> temp = new ArrayHeapMinPQ<>(10);
        for (int i = 10; i > 0; i--) {
            temp.add(i, i);
        }
        for (int i = 1; i < 11; i++) {
            assertEquals(i, (int) temp.removeSmallest());
        }
    }

    @Test
    public void testChangePriority() {
        ArrayHeapMinPQ<Integer> temp = new ArrayHeapMinPQ<>();
        for (int i = 10; i > 0; i--) {
            temp.add(i, i);
        }
        temp.changePriority(1, 90);
        assertEquals(2, (int) temp.getSmallest());
        temp.changePriority(1, 1);
        assertEquals(1, (int) temp.getSmallest());
    }

    @Test
    public void testRandom() {
        ArrayHeapMinPQ<Integer> temp = new ArrayHeapMinPQ<>();
        NaiveMinPQ<Integer> temp2 = new NaiveMinPQ<>();
        for (int i = 1; i < 100000; i++) {
            int k = StdRandom.uniform(1, 1000000);
            if (!temp.contains(k)) {
                temp.add(k, k);
                temp2.add(k, k);
            }
        }
        for (int i = 1; i <= temp.size(); i++) {
            assertEquals(temp2.removeSmallest(), temp.removeSmallest());
        }
    }

    /**
     *  Add and Remove: Total time elapsed: 0.223 seconds.
     *  Add and Remove: Total time elapsed: 14.704 seconds.
     *  ChangePriority: Total time elapsed: 0.039 seconds.
     *  ChangePriority: Total time elapsed: 29.371 seconds.
     */
    @Test
    public void testTime() {
        Stopwatch sw = new Stopwatch();
        ArrayHeapMinPQ<Integer> temp = new ArrayHeapMinPQ<>(1000000);
        for (int i = 100000; i > 0; i--) {
            temp.add(i, i);
        }
        for (int i = 1; i <= 100000; i++) {
            temp.removeSmallest();
        }
        System.out.println("Add and Remove: Total time elapsed: " + sw.elapsedTime() + " seconds.");
//        sw = new Stopwatch();
//        NaiveMinPQ<Integer> temp2 = new NaiveMinPQ<>();
//        for (int i = 100000; i > 0; i--) {
//            temp2.add(i, i);
//        }
//        for (int i = 1; i <= 100000; i++) {
//            temp2.removeSmallest();
//        }
//        System.out.println("Add and Remove: Total time elapsed: " + sw.elapsedTime() + " seconds.");
        for (int i = 100000; i > 0; i--) {
            temp.add(i, i);
        }
        sw = new Stopwatch();
        for (int i = 100000; i > 0; i--) {
            temp.changePriority(i, 100000 - i);
        }
        System.out.println("ChangePriority: Total time elapsed: " + sw.elapsedTime() + " seconds.");
//        sw = new Stopwatch();
//        for (int i = 100000; i > 0; i--) {
//            temp2.changePriority(i, 100000 - i);
//        }
//        System.out.println("ChangePriority: Total time elapsed: " + sw.elapsedTime() + " seconds.");
    }
}