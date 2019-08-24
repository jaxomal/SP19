import java.util.ArrayList;
import java.util.List;

/**
 * BnBSolver for the Bears and Beds problem. Each Bear can only be compared to Bed objects and each Bed
 * can only be compared to Bear objects. There is a one-to-one mapping between Bears and Beds, i.e.
 * each Bear has a unique size and has exactly one corresponding Bed with the same size.
 * Given a list of Bears and a list of Beds, create lists of the same Bears and Beds where the ith Bear is the same
 * size as the ith Bed.
 */
public class BnBSolver {
    Pair<List<Bear>, List<Bed>> solved;
    public BnBSolver(List<Bear> bears, List<Bed> beds) {
        //the ith Bear in your returned list
        //of Bears is the same size as the ith Bed in your returned list
        //of Beds
        // we should probably quick sort the bear and bed
        List<Bed> matchedBeds = new ArrayList<>();
        List<Bear> matchedBears = new ArrayList<>();
        sort2(bears, beds, matchedBears, matchedBeds);
        solved = new Pair(matchedBears, matchedBeds);
    }

    // Game plan we iterate through bears and bed
    // if we find matches then we remove the items form the list and add to matched Bears
    // N^2 algorithmn
    //
    private void sort(List<Bear> bears, List<Bed> beds, List<Bear> matchedBears, List<Bed> matchedBeds) {
        while (!bears.isEmpty() && !beds.isEmpty()) {
            Bear currBear = bears.remove(bears.size() - 1);
            for (int i = beds.size() - 1; i >= 0; i--) {
                if (currBear.compareTo(beds.get(i)) == 0) {
                    Bed currBed = beds.remove(i);
                    matchedBears.add(currBear);
                    matchedBeds.add(currBed);
                    break;
                }
            }
        }
    }

    // Improvement we split into sections where we have matched and we haven't
    private void sort2(List<Bear> bears, List<Bed> beds, List<Bear> matchedBears, List<Bed> matchedBeds) {
        if (bears.isEmpty() || beds.isEmpty()) {
            return;
        }
        List<Bear> largerThanBear = new ArrayList<>();
        List<Bed> largerThanBed = new ArrayList<>();
        List<Bear> smallerThanBear = new ArrayList<>();
        List<Bed> smallerThanBed = new ArrayList<>();
        for (int i = bears.size() - 1; i >= 0; i--) {
            Bear bear = bears.get(i);
            Bed bed = beds.get(i);
            int cmp = bear.compareTo(bed);
            if (cmp == 0) {
                bears.remove(bear);
                beds.remove(bed);
                matchedBears.add(bear);
                matchedBeds.add(bed);
            } else if (cmp > 0) {
                smallerThanBed.add(bed);
                largerThanBear.add(bear);
            } else {
                largerThanBed.add(bed);
                smallerThanBear.add(bear);
            }
        }
        sort2(largerThanBear, largerThanBed, matchedBears, matchedBeds);
        sort2(smallerThanBear, smallerThanBed, matchedBears, matchedBeds);

    }

    /**
     * Returns List of Bears such that the ith Bear is the same size as the ith Bed of solvedBeds().
     */
    public List<Bear> solvedBears() {
        return solved.first();
    }

    /**
     * Returns List of Beds such that the ith Bear is the same size as the ith Bear of solvedBears().
     */
    public List<Bed> solvedBeds() {
        return solved.second();
    }
}
