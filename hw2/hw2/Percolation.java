package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;

/**
 * @author willis
 * This is the percolation class used to model a percolation system.
 */
public class Percolation {
    /** Keeps track of which sites of the grid are opened and closed:
     * this is represented by a T/F relationship. */
    private boolean[][] openSites;
    /** Keeps track of connected sites */
    private WeightedQuickUnionUF sites;
    /** Keeps track of the number of open sites in the grid */
    private int numberOfOpen;

    /** Create N-by-N grid, with all sites initially blocked.
     * There are N^2 + 2 "sites" due to efficiency purposes.
     */
    public Percolation(int N) {
        if (N < 0) {
            throw new java.lang.IllegalArgumentException("Your number was less than 0");
        }
        openSites = new boolean[N][N];
        for (boolean[] arr : openSites) {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = false;
            }
        }
        sites = new WeightedQuickUnionUF(N * N + 2);
        numberOfOpen = 0;
    }

    /** Open the site (row, col) if it is not open already.
     * @param row corresponding row grid number of the site.
     * @param col corresponding col grid number of the site.
     */
    public void open(int row, int col) {
        if (!validPosition(row, col)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (isOpen(row, col)) {
            return;
        }
        if (row == 0) {
            sites.union(openSites.length * openSites.length, col);
            ArrayList<Integer> neighbors = neighbors(row, col);
            if (!neighbors.isEmpty()) {
                for (int neighbor : neighbors) {
                    sites.union(xyTo1D(row, col), neighbor);
                }
            }
            openSites[row][col] = true;
            numberOfOpen++;
        } else if (row == openSites.length - 1) {
            sites.union(openSites.length * openSites.length + 1, xyTo1D(row, col));
            ArrayList<Integer> neighbors = neighbors(row, col);
            if (!neighbors.isEmpty()) {
                for (int neighbor : neighbors) {
                    sites.union(xyTo1D(row, col), neighbor);
                }
            }
            openSites[row][col] = true;
            numberOfOpen++;
        } else {
            ArrayList<Integer> neighbors = neighbors(row, col);
            if (!neighbors.isEmpty()) {
                for (int neighbor : neighbors) {
                    sites.union(xyTo1D(row, col), neighbor);
                }
            }
            openSites[row][col] = true;
            numberOfOpen++;
        }
    }

    /**
     * @return the site (row, col) is open?
     * @param row corresponding row grid number of the site.
     * @param col corresponding col grid number of the site.
     */
    public boolean isOpen(int row, int col) {
        if (!validPosition(row, col)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return openSites[row][col];
    }

    /**
     * @param row corresponding row grid number of the site.
     * @param col corresponding col grid number of the site.
     * @return the site is full of water?
     */
    public boolean isFull(int row, int col) {
        if (!validPosition(row, col)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return sites.connected(xyTo1D(row, col), openSites.length * openSites.length);
    }

    /**
     * @return the number of open sites in the grid left.
     */
    public int numberOfOpenSites() {
        return numberOfOpen;
    }

    /**
     * @return does the system percolate?
     */
    public boolean percolates() {
        return sites.connected(openSites.length * openSites.length, openSites.length * openSites.length + 1);
    }

    /**
     * @param row corresponding row grid number of the site.
     * @param col corresponding col grid number of the site.
     * @return the 1d coordinate given a row and col of a grid.
     */
    private int xyTo1D(int row, int col) {
        return row * openSites.length + col;
    }

    /**
     * @param row corresponding row grid number of the site.
     * @param col corresponding col grid number of the site.
     * @return the open neighbors of a site of a grid.
     */
    private ArrayList<Integer> neighbors(int row, int col) {
        ArrayList<Integer> storage = new ArrayList<>();
        if (validPosition(row - 1, col)) {
            if (openSites[row - 1][col]) {
                storage.add(xyTo1D(row - 1, col));
            }
        }
        if (validPosition(row + 1, col)) {
            if (openSites[row + 1][col]) {
                storage.add(xyTo1D(row + 1, col));
            }
        }
        if (validPosition(row, col + 1)) {
            if (openSites[row][col + 1]) {
                storage.add(xyTo1D(row, col + 1));
            }
        }
        if (validPosition(row, col - 1)) {
            if (openSites[row][col - 1]) {
                storage.add(xyTo1D(row, col - 1));
            }
        }
        return storage;
    }

    /**
     * @param row corresponding row grid number of the site.
     * @param col corresponding col grid number of the site.
     * @return is the given row/col pair a valid site in the grid?
     */
    private boolean validPosition(int row, int col) {
        int maxY = openSites.length;
        int maxX = maxY;
        return row <= maxX - 1 && row >= 0 && col <= maxY - 1 && col >= 0;
    }

    /**
     * @param args used for potential unit testing
     */
    public static void main(String[] args) {
        Percolation test = new Percolation(5);
        test.open(0 ,0);
        test.open(0, 1);
        test.open(0, 2);
        System.out.println(test.neighbors(0, 1));
        System.out.println(test.isOpen(0, 0));
        System.out.println(test.isOpen(0, 1));
        System.out.println(test.isOpen(0, 2));
        test.open(1, 0);
        System.out.println(test.isFull(1, 0));
        System.out.println(test.isFull(2, 0));
    }
}