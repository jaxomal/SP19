package hw3.hash;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.StdRandom;

public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    /* This should pass if your OomageTestUtility.haveNiceHashCodeSpread
       is correct. This is true even though our given ComplexOomage class
       has a flawed hashCode. */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }


    /**
     * Credits to Dinqy. The problem with having 256 as the smear
     * constant is that 256^4 perfectly wraps around to 0.
     * This means that any number inserted after
     * the first four numbers will make absolutely no impact
     * into calculating the hashcode. To make a deadly list you keep
     * four numbers constant after loads of random insertions.
     * To fix this you can use a prime number which is guaranteed
     * to never wrap around to 0.
     */
    @Test
    public void testWithDeadlyParams() {
        List<Oomage> deadlyList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            List<Integer> temp = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                temp.add(StdRandom.uniform(0, 256));
            }
            temp.add(15);
            temp.add(13);
            temp.add(189);
            temp.add(176);
            deadlyList.add(new ComplexOomage(temp));
        }
        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(deadlyList, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
