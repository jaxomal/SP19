package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import edu.princeton.cs.algs4.StdRandom;

import static org.junit.Assert.assertEquals;

/**
 * @source https://github.com/dingqy
 * @author willis
 * I wrote half of the tests, my KDTree passed so I copied the rest of
 * dingqy's tests.
 */
public class KDTreeTest {
    /**
     * Inserts the same 10000 items into both the NativePointSet and
     * the KDTree and queries the nearest function 1000 times.
     */
    @Test
    public void randomizedTest() {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            double chosenX = StdRandom.uniform(-300000.0, 300001.0);
            double chosenY = StdRandom.uniform(-300000.0, 300001.0);
            points.add(new Point(chosenX, chosenY));
        }
        KDTree kd = new KDTree(points);
        NaivePointSet np = new NaivePointSet(points);
        for (int i = 0; i < 10000; i++) {
            double chosenX = StdRandom.uniform(-300000.0, 300001.0);
            double chosenY = StdRandom.uniform(-300000.0, 300001.0);
            Point kdPoint = kd.nearest(chosenX, chosenY);
            Point npPoint = np.nearest(chosenX, chosenY);
            assertEquals(kdPoint, npPoint);
        }
    }

    @Test
    public void stopWatchTest() {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            double chosenX = StdRandom.uniform(-300000.0, 300001.0);
            double chosenY = StdRandom.uniform(-300000.0, 300001.0);
            points.add(new Point(chosenX, chosenY));
        }
        KDTree kd = new KDTree(points);
        NaivePointSet np = new NaivePointSet(points);
        List<Point> testPoints = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            double chosenX = StdRandom.uniform(-300000.0, 300001.0);
            double chosenY = StdRandom.uniform(-300000.0, 300001.0);
            testPoints.add(new Point(chosenX, chosenY));;
        }
        Stopwatch sw = new Stopwatch();
        for (Point p : testPoints) {
            np.nearest(p.getX(), p.getY());
        }
        System.out.println("NPS Time: " + sw.elapsedTime());
        sw = new Stopwatch();
        for (Point p : testPoints) {
            kd.nearest(p.getX(), p.getY());
        }
        System.out.println("KD Time: " + sw.elapsedTime());
    }

    @Test
    public void basicKDTest() {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        KDTree kd = new KDTree(List.of(p1, p2, p3));
        Point ret = kd.nearest(3.0, 4.0); // returns p2
        System.out.println(ret.getX()); // evaluates to 3.3
        System.out.println(ret.getY()); // evaluates to 4.4
    }

    @Test
    public void testNaivePointSet() {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);
        NaivePointSet temp = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = temp.nearest(3.0, 4.0); // returns p2
        assertEquals(3.3, ret.getX(), 0.0001);
        assertEquals(4.4, ret.getY(), 0.0001);
    }

    @Test
    public void testBasicRandom() {
        HashSet<Double> X = new HashSet<>();
        HashSet<Double> Y = new HashSet<>();
        List<Point> temp = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            double x = StdRandom.uniform(-400000.0, 400000);
            double y = StdRandom.uniform(-400000.0, 400000);
            if (!X.contains(x) && !Y.contains(y)) {
                X.add(x);
                Y.add(y);
                temp.add(new Point(x, y));
            }
        }
        KDTree test = new KDTree(temp);
        NaivePointSet test2 = new NaivePointSet(temp);
        for (int i = 0; i < 10000; i++) {
            double x2 = StdRandom.uniform(-4000000, 4000000);
            double y2 = StdRandom.uniform(-4000000, 4000000);
            Point result1 = test.nearest(x2, y2);
            Point result2 = test2.nearest(x2, y2);
            assertEquals(result1.getX(), result2.getX(), 0.000001);
            assertEquals(result1.getY(), result2.getY(), 0.000001);
        }
    }

    /**
     * NaivePointSet: Total time elapsed: 2.284 seconds.
     * KDTree: Total time elapsed: 0.019 seconds.
     */
    @Test
    public void testTime() {
        ArrayList<Point> temp3 = new ArrayList<>();
        HashSet<Double> X = new HashSet<>();
        HashSet<Double> Y = new HashSet<>();
        List<Point> temp = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            double x = StdRandom.uniform(-300000.0, 300000);
            double y = StdRandom.uniform(-300000.0, 300000);
            if (!X.contains(x) && !Y.contains(y)) {
                X.add(x);
                Y.add(y);
                temp.add(new Point(x, y));
            }
        }
        NaivePointSet test1 = new NaivePointSet(temp);
        KDTree test2 = new KDTree(temp);
        for (int i = 0; i < 10000; i++) {
            double x2 = StdRandom.uniform(-300000, 300000);
            double y2 = StdRandom.uniform(-300000, 300000);
            temp3.add(new Point(x2, y2));
        }
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < 10000; i++) {
            Point temp2 = temp3.get(i);
            test1.nearest(temp2.getX(), temp2.getY());
        }
        System.out.println("Total time elapsed: " + sw.elapsedTime() + " seconds.");

        sw = new Stopwatch();
        for (int i = 0; i < 10000; i++) {
            Point temp2 = temp3.get(i);
            test2.nearest(temp2.getX(), temp2.getY());
        }
        System.out.println("Total time elapsed: " + sw.elapsedTime() + " seconds.");
    }
}
