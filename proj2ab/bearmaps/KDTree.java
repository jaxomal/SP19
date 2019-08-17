package bearmaps;

import java.util.List;

/**
 * @author willi
 * This is a 2d-tree representation of points on a 2D plane.
 */
public class KDTree {
    /** The root of the tree. */
    private Node root;
    /** Representation of the horizontal comparison. */
    private static final boolean HORIZONTAL = false;

    /**
     * Creates the representation of a KDTree given points.
     * @param points a list of points.
     */
    public KDTree(List<Point> points) {
        root = add(points.get(0), root);
        for (int i = 1; i < points.size(); i++) {
            root = add(points.get(i), root);
        }
    }

    /**
     * @param x horizontal coordinate of the point.
     * @param y vertical coordinate of the point.
     * @return The point closest to these coordinates.
     */
    public Point nearest(double x, double y) {
        Point goal = new Point(x, y);
        return nearest(root, goal, root).point;
    }

    /** Helper function for nearest(). */
    private Node nearest(Node n, Point goal, Node best) {
        if (n == null) {
            return best;
        }
        if (Point.distance(n.point, goal) < Point.distance(best.point, goal)) {
            best = n;
        }
        Node goodSide;
        Node badSide;
        if (!n.orientation) {
            if (goal.getX() < n.point.getX()) {
                goodSide = n.leftChild;
                badSide = n.rightChild;
            } else {
                goodSide = n.rightChild;
                badSide = n.leftChild;
            }
        } else {
            if (goal.getY() < n.point.getY()) {
                goodSide = n.leftChild;
                badSide = n.rightChild;
            } else {
                goodSide = n.rightChild;
                badSide = n.leftChild;
            }
        }
        best = nearest(goodSide, goal, best);
        if (stillUseful(n, goal, best)) {
            best = nearest(badSide, goal, best);
        }
        return best;
    }

    /**
     * @param n the current node being checked.
     * @param goal the point for which we want to find the closest point.
     * @param best the point at the moment that is closest.
     * @return whether or not the bad side of the current node
     * still can contain a useful point.
     */
    private boolean stillUseful(Node n, Point goal, Node best) {
        double currDistance = Point.distance(best.point, goal);
        Point bestPossibleBad;
        if (n.orientation) {
            bestPossibleBad = new Point(goal.getX(), n.point.getY());
        } else {
            bestPossibleBad = new Point(n.point.getX(), goal.getY());
        }
        return Point.distance(bestPossibleBad, goal)
                < Point.distance(best.point, goal);
    }

    /**
     * @param p the point you want to add.
     * @param r the current node for the helper function
     *          but acts as a root for the main.
     * @return the root that contains the added point.
     */
    private Node add(Point p, Node r) {
        return add(r, p, HORIZONTAL);
    }

    /** Helper function for add. */
    private Node add(Node r, Point p, boolean startingOrt) {
        if (r == null) {
            return new Node(p, startingOrt);
        }
        int cmp;
        if (!startingOrt) {
            cmp = Double.compare(p.getX(), r.point.getX());
        } else {
            cmp = Double.compare(p.getY(), r.point.getY());
        }
        if (cmp > 0) {
            r.rightChild = add(r.rightChild, p, !startingOrt);
        } else if (cmp < 0) {
            r.leftChild = add(r.leftChild, p, !startingOrt);
        }
        return r;
    }

    /**
     * A private class to store the data of the KDTree contains:
     * the point, orientation, and children of the node.
     */
    private class Node {
        /** A singular point on the KDTree. */
        private Point point;
        /** The orientation of the point, whether it is horizontal
         * or vertical. */
        private boolean orientation;
        /** The left child of the node. */
        private Node leftChild;
        /** The right child of the node. */
        private Node rightChild;

        /**
         *
         * @param p the point of the node you want to create.
         * @param givenOrt the orientation of the Node you want to create.
         */
        Node(Point p, boolean givenOrt) {
            point = p;
            orientation = givenOrt;
            leftChild = null;
            rightChild = null;
        }
    }
}
