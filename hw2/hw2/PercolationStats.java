package hw2;
import edu.princeton.cs.introcs.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static edu.princeton.cs.introcs.StdRandom.uniform;

public class PercolationStats {
    /** The mean of percolation threshold */
    private double mean;
    /** sample mean of percolation threshold */
    private double stddev;
    /** low endpoint of 95% confidence interval */
    private double confidenceLow;
    /** high endpoint of 95% confidence interval */
    private double confidenceHigh;

    /** Perform T independent experiments on an N-by-N grid */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        double[] results = new double[T];
        double total = 0;
        for (int i = 0; i < T; i++) {
            Percolation current = pf.make(N);
            ArrayList<Integer> arrlist = new ArrayList<>();
            for (int j = 0; j < N * N; j++) {
                arrlist.add(j);
            }
            while (!current.percolates()) {
                int num = uniform(arrlist.size());
                int pos = arrlist.get(num);
                int row = pos / N;
                int col = pos % N;
                current.open(row, col);
                arrlist.remove(num);
            }
            results[i] = ((double) current.numberOfOpenSites() / N);
        }
        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
        confidenceLow =  mean - (1.96 * Math.sqrt(stddev) / Math.sqrt(T));
        confidenceHigh =  mean + (1.96 * Math.sqrt(stddev) / Math.sqrt(T));
    }

    /** @return sample mean of percolation threshold */
    public double mean() {
        return mean;
    }

    /** @return sample standard deviation of percolation threshold */
    public double stddev() {
        return stddev;
    }

    /** @return low endpoint of 95% confidence interval */
    public double confidenceLow() {
        return confidenceLow;
    }

    /** @return high endpoint of 95% confidence interval */
    public double confidenceHigh() {
        return confidenceHigh;
    }

    public static void main(String[] args) {
        PercolationFactory pf = new PercolationFactory();
        Stopwatch stopwatch = new Stopwatch();
        int N = 50;
        int T = 1600;
        PercolationStats ps = new PercolationStats(N, T, pf);
        System.out.println("The size of N: " + N + " and the size of T: " +
                T + " took " + stopwatch.elapsedTime());
    }
}
