import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class TestSortAlgs {

    @Test
    public void testQuickSort() {
        Queue<Integer> q = new Queue<>();
        for (int i = 0; i < 1000; i++) {
            int item = StdRandom.uniform(-10000, 10000);
            q.enqueue(item);
        }
        q = QuickSort.quickSort(q);
        assertTrue(isSorted(q));
    }

    @Test
    public void testMergeSort() {
        Queue<Integer> queue = new Queue<>();
        for (int i = 0; i < 1000; i++) {
            int item = StdRandom.uniform(-10000, 10000);
            queue.enqueue(item);
        }
        MergeSort.mergeSort(queue);
        assertTrue(isSorted(queue));
    }

    /**
     * Returns whether a Queue is sorted or not.
     *
     * @param items  A Queue of items
     * @return       true/false - whether "items" is sorted
     */
    private <Item extends Comparable> boolean isSorted(Queue<Item> items) {
        if (items.size() <= 1) {
            return true;
        }
        Item curr = items.dequeue();
        Item prev = curr;
        while (!items.isEmpty()) {
            prev = curr;
            curr = items.dequeue();
            if (curr.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }
}
