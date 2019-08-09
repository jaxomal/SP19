import edu.princeton.cs.algs4.StdRandom;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.StdRandom.*;

/**
 * Created by hug.
 */
public class Experiments {
    public static void experiment1() {
        List<Integer> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        List<Double> y2Values = new ArrayList<>();
        BST<Double> bst = new BST<>();
        for (int i = 1; i <= 5000; i++) {
            xValues.add(i);
            double chosen = StdRandom.uniform(100000);
            bst.add(chosen);
            yValues.add(bst.averageDepth());
            y2Values.add(ExperimentHelper.optimalAverageDepth(i));
        }
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("Number of Items").yAxisTitle("Average Depth").build();
        chart.addSeries("Your BST", xValues, yValues);
        chart.addSeries("Optimal BST)", xValues, y2Values);

        new SwingWrapper(chart).displayChart();
    }

    public static void experiment2() {
        BST<Integer> bst = new BST<>();
        int N = StdRandom.uniform(5000);
        int M = StdRandom.uniform(100000);
        List<Integer> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            ExperimentHelper.insertRandom(100000, bst);
        }
        double startingDepth = bst.averageDepth();
        xValues.add(0);
        yValues.add(startingDepth);
        for (int i = 1; i <= M; i++) {
            xValues.add(i);
            ExperimentHelper.deleteOrder(bst);
            ExperimentHelper.insertRandom(100000, bst);
            double currDepth = bst.averageDepth();
            yValues.add(currDepth);
        }
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("Iteration Number").yAxisTitle("Average Depth").build();
        chart.addSeries("Hibbard Deletion", xValues, yValues);
        new SwingWrapper(chart).displayChart();
    }

    public static void experiment3() {
        BST<Integer> bst = new BST<>();
        int N = StdRandom.uniform(5000);
        int M = StdRandom.uniform(100000);
        List<Integer> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            ExperimentHelper.insertRandom(100000, bst);
        }
        double startingDepth = bst.averageDepth();
        xValues.add(0);
        yValues.add(startingDepth);
        for (int i = 1; i <= 1000000; i++) {
            xValues.add(i);
            ExperimentHelper.deleteRandom(bst);
            ExperimentHelper.insertRandom(100000, bst);
            double currDepth = bst.averageDepth();
            yValues.add(currDepth);
        }
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("Iteration Number").yAxisTitle("Average Depth").build();
        chart.addSeries("Random Deletion", xValues, yValues);
        new SwingWrapper(chart).displayChart();
    }

    public static void main(String[] args) {
        experiment3();
    }
}
