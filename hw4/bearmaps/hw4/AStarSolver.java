package bearmaps.hw4;

import bearmaps.proj2ab.DoubleMapPQ;

import java.util.*;

import edu.princeton.cs.algs4.Stopwatch;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private HashMap<Vertex, Double> distTo;
    private HashMap<Vertex, WeightedEdge> edgeTo;
    private SolverOutcome result;
    private List<Vertex> solution;
    private double explorationTime;
    private int numStatesExplored;
    private double solutionWeight;

    /**
     * Constructor which finds the solution, computing everything
     * necessary for all other methods to return their results in
     * constant time. Note that timeout passed in is in seconds.
     * @param input the graph of vertexes.
     * @param start our start vertex.
     * @param end our goal vertex.
     * @param timeout time till we stop running the algorithm.
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        DoubleMapPQ<Vertex> pq = new DoubleMapPQ();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        solution = new LinkedList<>();
        solutionWeight = 0;
        numStatesExplored = 0;
        explorationTime = 0;
        Stopwatch sw = new Stopwatch();
        pq.add(start, 0);
        distTo.put(start, 0.0);
        while (pq.size() != 0) {
            Vertex p = pq.removeSmallest();
            numStatesExplored += 1;
            if (p.equals(end)) {
                explorationTime = sw.elapsedTime();
                while (true) {
                    if (p.equals(start)) {
                        solution.add(0, p);
                        break;
                    }
                    Vertex from = (Vertex) edgeTo.get(p).from();
                    solutionWeight +=  edgeTo.get(p).weight();
                    solution.add(0, p);
                    p = from;
                }
                result = SolverOutcome.SOLVED;
                return;
            }
            if (sw.elapsedTime() > timeout) {
                explorationTime = sw.elapsedTime();
                result = SolverOutcome.TIMEOUT;
                return;
            }
            for (WeightedEdge e: input.neighbors(p)) {
                relax(input, end, pq, e);
            }
        }
        result = SolverOutcome.UNSOLVABLE;
        explorationTime = sw.elapsedTime();
    }

    /**
     * @return one of SolverOutcome.SOLVED, SolverOutcome.TIMEOUT,
     * or SolverOutcome.UNSOLVABLE. Should be SOLVED if the AStarSolver
     * was able to complete all work in the time given. UNSOLVABLE if the
     * priority queue became empty. TIMEOUT if the solver ran out of time.
     */
    public SolverOutcome outcome() {
        return result;
    }

    /**
     * @return A list of vertices corresponding to a solution. Should be
     * empty if result was TIMEOUT or UNSOLVABLE.
     */
    public List<Vertex> solution() {
        return solution;
    }

    /**
     * @return the total weight of the given solution, taking into account
     * edge weights. Should be 0 if result was TIMEOUT or UNSOLVABLE.
     */
    public double solutionWeight() {
        return solutionWeight;
    }

    /**
     * @return the total number of priority queue dequeue operations.
     */
    public int numStatesExplored() {
        return numStatesExplored - 1;
    }

    /**
     * @return the total time spent in seconds by the constructor.
     */
    public double explorationTime() {
        return explorationTime;
    }

    /** Relaxes an edge. */
    private void relax(AStarGraph<Vertex> input, Vertex goal, DoubleMapPQ<Vertex> pq, WeightedEdge<Vertex> e) {
        Vertex p = e.from();
        Vertex q = e.to();
        double w = e.weight();
        double pw = distTo.get(p);
        if (!distTo.containsKey(q) || pw + w < distTo.get(q)) {
            distTo.put(q, pw + w);
            edgeTo.put(q, e);
            if (pq.contains(q)) {
                pq.changePriority(q, distTo.get(q) + input.estimatedDistanceToGoal(q, goal));
            } else {
                pq.add(q, distTo.get(q) + input.estimatedDistanceToGoal(q, goal));
            }
        }
    }
}