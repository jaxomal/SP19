import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Solver for the Flight problem (#9) from CS 61B Spring 2018 Midterm 2.
 * Assumes valid input, i.e. all Flight start times are >= end times.
 * If a flight starts at the same time as a flight's end time, they are
 * considered to be in the air at the same time.
 */
public class FlightSolver {
    private PriorityQueue<Flight> start;
    private PriorityQueue<Flight> end;
    public FlightSolver(ArrayList<Flight> flights) {
        Comparator<Flight> startTimes = (f1, f2) -> {
            int diff = f1.startTime - f2.startTime;
            return diff;
        };
        Comparator<Flight> endTimes = (f1, f2) -> {
            int diff = f1.endTime - f2.endTime;
            return diff;
        };
        start = new PriorityQueue<>(startTimes);
        end = new PriorityQueue<>(endTimes);
        for (Flight flight : flights) {
            start.add(flight);
            end.add(flight);
        }
    }

    public int solve() {
        int tally = 0;
        int best = 0;
        while (!start.isEmpty() || !end.isEmpty()) {
            Flight curr;
            if (start.isEmpty()) {
                curr = end.peek();
                end.remove(curr);
                tally -= curr.passengers;
            } else if (end.isEmpty()) {
                curr = start.peek();
                start.remove(curr);
                tally += curr.passengers;
            } else {
                if (start.peek().passengers > end.peek().passengers) {
                    curr = end.peek();
                    end.remove(curr);
                    tally -= curr.passengers;
                } else {
                    curr = start.peek();
                    start.remove(curr);
                    tally += curr.passengers;
                }
            }
            if (tally > best) {
                best = tally;
            }
        }
        return best;
    }

}
