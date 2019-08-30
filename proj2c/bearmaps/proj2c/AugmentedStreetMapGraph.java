package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.WeirdPointSet;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private HashMap<Point, Node> convert;
    private HashMap<String, Node> names;
    private WeirdPointSet kdTree;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        List<Node> nodes = this.getNodes();
        convert = new HashMap<>();
        names = new HashMap<>();
        List<Point> pointsWithNeighbors = new ArrayList<>();
        for (Node n : nodes) {
            long id = n.id();
            double lon = n.lon();
            double lat = n.lat();
            Point nodePoint = new Point(lon, lat);
            String name = n.name();
            if (name != null) {
                names.put(name, n);
            }
            convert.put(nodePoint, n);
            if (!neighbors(id).isEmpty()) {
                pointsWithNeighbors.add(nodePoint);
            }
        }
        kdTree = new WeirdPointSet(pointsWithNeighbors);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point nearest = kdTree.nearest(lon, lat);
        Node nearestNode = convert.get(nearest);
        long id = nearestNode.id();
        return id;
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        String cleanedPrefix = cleanString(prefix);
        List<String> matchedStrings = new LinkedList<>();
        for (String name : names.keySet()) {
            System.out.println(name);
            String cleanedString = cleanString(name);
            if (cleanedString.startsWith(cleanedPrefix)) {
                matchedStrings.add(name);
            }
        }
        return matchedStrings;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        String cleanedName = cleanString(locationName);
        List<Map<String, Object>> locations = new ArrayList<>();
        for (String key : names.keySet()) {
            String cleanedKey = cleanString(key);
            if (cleanedKey.equals(cleanedName)) {
                HashMap<String, Object> params = new HashMap<>();
                Node n = names.get(locationName);
                long id = n.id();
                double lat = n.lat();
                double lon = n.lon();
                params.put("lat", lat);
                params.put("lon", lon);
                params.put("name", key);
                params.put("id", id);
                locations.add(params);
            }
        }
        return locations;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
