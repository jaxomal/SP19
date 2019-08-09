import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by hug.
 */
public class ExperimentHelper {

    /** Returns the internal path length for an optimum binary search tree of
     *  size N. Examples:
     *  N = 1, OIPL: 0
     *  N = 2, OIPL: 1
     *  N = 3, OIPL: 2
     *  N = 4, OIPL: 4
     *  N = 5, OIPL: 6
     *  N = 6, OIPL: 8
     *  N = 7, OIPL: 10
     *  N = 8, OIPL: 13
     */
    public static int optimalIPL(int N) {
        int depth = 0;
        int totalDepth = 0;
        int levelNodes = 1;
        int totalNodes = 0;
        int highestLevel = (int) (Math.log(N) / Math.log(2));
        while (depth <= highestLevel - 1)  {
            totalDepth += depth * levelNodes;
            totalNodes += levelNodes;
            depth++;
            levelNodes *= 2;
        }
        int remainingNodes = N - ((int) Math.pow(2, highestLevel) - 1);
        totalDepth += highestLevel * remainingNodes;
        return totalDepth;
    }

    /** Returns the average depth for nodes in an optimal BST of
     *  size N.
     *  Examples:
     *  N = 1, OAD: 0
     *  N = 5, OAD: 1.2
     *  N = 8, OAD: 1.625
     * @return
     */
    public static double optimalAverageDepth(int N) {
        return (double) optimalIPL(N) / N;
    }

    public static void main(String[] args) {
        System.out.println(optimalAverageDepth(8));
    }

    public static void insertRandom(int x, BST bst) {
        double chosen = StdRandom.uniform(x);
        bst.add(chosen);
    }

    public static void deleteOrder(BST bst) {
        bst.deleteTakingSuccessor(bst.getRandomKey());
    }

    public static void deleteRandom(BST bst) {
        bst.deleteTakingRandom(bst.getRandomKey());
    }
}
