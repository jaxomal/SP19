import java.util.NoSuchElementException;
import java.util.HashSet;

public class UnionFind {
    /** The parent array will keep track of the heads of each vertex */
    int[] parent;

    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets, we will keep track of size by keeping the value
       negative which will still allow the set to find the head. */
    public UnionFind(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = -1;
        }
    }

    /* Throws an exception if v1 is not a valid index. */
    private void validate(int v1) {
        if (v1 >= parent.length) {
            throw new IllegalArgumentException("Out of Bounds");
        }
    }

    /* Returns the size of the set v1 belongs to. */
    public int sizeOf(int v1) {
        v1 = find(v1);
        return parent(v1);
    }

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    public int parent(int v1) {
        return parent[v1];
    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean connected(int v1, int v2) {
        v1 = find(v1);
        v2 = find(v2);
        return v1 == v2;
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid 
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a 
       vertex with itself or vertices that are already connected should not 
       change the sets but may alter the internal structure of the data. */
    public void union(int v1, int v2) {
        v1 = find(v1);
        v2 = find(v2);
        int s1 = parent[v1];
        int s2 = parent[v2];
        // Now that we have the two roots we compare size
        if (s1 > s2) {
            parent[v2] = v1;
            parent[v1] += s2;
        } else {
            parent[v1] = v2;
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */
    public int find(int vertex) {
        HashSet<Integer> nodes = new HashSet<>();
        while (true) {
            if (parent(vertex) <= 0) {
                break;
            } else {
                nodes.add(vertex);
                vertex = parent(vertex);
            }
        }
        for (int node : nodes) {
            parent[node] = vertex;
        }
        return vertex;
    }

}
