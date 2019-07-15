import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testArrayDeque() {
        StudentArrayDeque<Integer> failDeque = new StudentArrayDeque<>();
        StudentArrayDeque<Integer> solDeque = new StudentArrayDeque<>();
        String msg = "";

        for (int i = 0; i < 5; i++) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                msg += "addLast(" + i + ")\n";
                failDeque.addLast(i);
                solDeque.addLast(i);
            } else {
                msg += "addFirst(" + i + ")\n";
                failDeque.addFirst(i);
                solDeque.addLast(i);
            }
        }
        for (int i = 0; i < 5; i++) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                msg += "removeLast()\n";
                assertEquals(msg, failDeque.removeLast(), solDeque.removeLast());
            } else {
                msg += "removeFirst()\n";
                assertEquals(msg, failDeque.removeFirst(), solDeque.removeFirst());
            }
        }
    }
}
