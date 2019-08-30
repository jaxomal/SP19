package bearmaps.proj2c.server.handler.impl;

import bearmaps.proj2c.AugmentedStreetMapGraph;
import bearmaps.proj2c.server.handler.APIRouteHandler;
import bearmaps.proj2c.utils.Tuple;
import spark.Request;
import spark.Response;
import bearmaps.proj2c.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bearmaps.proj2c.utils.Constants.*;

/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 * @author rahul, Josh Hug, _________
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {

    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};

    private static final int UPPER_LEFT = 0;
    private static final int LOWER_RIGHT = 1;
    private static final int FEET_PER_DEGREE_LONGITUDE = 288200;


    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param requestParams Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @param response : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {
        Map<String, Object> results = new HashMap<>();
        int depth = 0;
        double ulLong = requestParams.get("ullon");
        double lrLong = requestParams.get("lrlon");
        double ulLat = requestParams.get("ullat");
        double lrLat = requestParams.get("lrlat");
        // querey box
        Tuple<Double, Double> l1 = new Tuple<>(ulLong, ulLat);
        Tuple<Double, Double> r1 = new Tuple<>(lrLong, lrLat);
        Tuple<Double, Double> l2 = new Tuple<>(ROOT_ULLON, ROOT_ULLAT);
        Tuple<Double, Double> r2 = new Tuple<>(ROOT_LRLON, ROOT_LRLAT);
        if (!inBounds(l1, r1, l2, r2) || invalid(ulLong, lrLong, ulLat, lrLat)) {
            return queryFail();
        }
        double currDPP = Double.MAX_VALUE;
        double goalLonDPP = pictureLonDPP(requestParams);
        while (true) {
            if (calculateResolution(depth) < goalLonDPP || depth > 6) {
                break;
            }
            depth += 1;
        }
        String[][] best = tilesInBoundary(depth, ulLong, lrLong, ulLat, lrLat);
        Tuple<Tuple<Double, Double>, Tuple<Double,Double>> ul = tileCoordinate(best[0][0]);
        Tuple<Tuple<Double, Double>, Tuple<Double,Double>> lr = tileCoordinate(best[best.length - 1][best[0].length - 1]);
        Tuple<Double, Double> ulUl = ul.getFirst();
        Tuple<Double, Double> lrLr = lr.getSecond();
        results.put("raster_ul_lon", ulUl.getFirst());
        results.put("raster_ul_lat", ulUl.getSecond());
        results.put("raster_lr_lon", lrLr.getFirst());
        results.put("raster_lr_lat", lrLr.getSecond());
        results.put("depth", depth);
        results.put("query_success", true);
        results.put("render_grid", best);
        return results;
    }

    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    /**
     * @param ulLon upper left longitude.
     * @param lrLon lower right longitude.
     * @param ulLat upper left latitude.
     * @param lrLat lower right latitude.
     * @return returns whether the query is valid.
     */
    private boolean invalid(double ulLon, double lrLon, double ulLat, double lrLat) {
        return ulLon > lrLon || ulLat < lrLat;
    }

    /**
     * @return sends back a failed query.
     */
    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                                  ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
                File in = new File(imgPath);
                tileImg = ImageIO.read(in);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }

    /**
     * @param depth how clear a photo is.
     * @return the resolution at a given depth.
     */
    private double calculateResolution(int depth) {
        double initialRes = (ROOT_LRLON - ROOT_ULLON) / (TILE_SIZE * 2) * FEET_PER_DEGREE_LONGITUDE;
        return initialRes / (Math.pow(2, depth - 1));
    }

    /**
     * @param requestParams Map of the HTTP GET request's query parameters.
     * @return the LonDDP of the picture requested by user.
     */
    private double pictureLonDPP(Map<String, Double> requestParams) {
        double ulLong = requestParams.get("ullon");
        double lrLong = requestParams.get("lrlon");
        double width = requestParams.get("w");
        return (lrLong - ulLong) / width * FEET_PER_DEGREE_LONGITUDE;
    }

    /**
     * @param depth current depth level being tested.
     * @param ulLong upper left longitude.
     * @param lrLong lower right longitude.
     * @param ulLat upper left latitude.
     * @param lrLat lower right latitude.
     * @return returns the tiles inside this boundary with the given depth and the corresponding lat/long.
     */
    public String[][] tilesInBoundary(int depth, double ulLong, double lrLong, double ulLat, double lrLat) {
        Tuple<Integer, Integer> ul = tileIndexes(depth, ulLong, ulLat);
        Tuple<Integer, Integer> lr = tileIndexes(depth, lrLong, lrLat);
        HashMap<String, Object> res = new HashMap<>();
        int rowSize = lr.getSecond() - ul.getSecond() + 1;
        int colSize = lr.getFirst() - ul.getFirst() + 1;
        int yDiff = ul.getSecond();
        int xDiff = ul.getFirst();
        String[][] tiles = new String[rowSize][colSize];
        for (int y = ul.getSecond(); y <= lr.getSecond(); y++) {
            for (int x = ul.getFirst(); x <= lr.getFirst(); x++) {
                tiles[y - yDiff][x - xDiff] = getPath(depth, x, y);
            }
        }
        return tiles;
    }

    /**
     * @param filename a photo in filename format.
     * @return the coordinates of a tile, for both the upper left and lower right.
     */
    public Tuple<Tuple<Double, Double>, Tuple<Double, Double>> tileCoordinate(String filename) {
        int depth = parse('d', filename);
        int x = parse('x', filename);
        int y = parse('y', filename);
        double totalWidth = ROOT_LRLON - ROOT_ULLON;
        double totalHeight = ROOT_ULLAT - ROOT_LRLAT;
        double widthPerTile = totalWidth / Math.pow(2, depth);
        double heightPerTile = totalHeight / Math.pow(2, depth);
        double ulxCor = x * widthPerTile + ROOT_ULLON;
        double ulyCor = ROOT_ULLAT - y * heightPerTile;
        double lrxCor = (x + 1) * widthPerTile + ROOT_ULLON;
        double lryCor = ROOT_ULLAT - (y + 1) * heightPerTile;
        Tuple<Double, Double> ul = new Tuple(ulxCor, ulyCor);
        Tuple<Double, Double> lr = new Tuple(lrxCor, lryCor);
        return new Tuple<>(ul, lr);
    }

    /**
     * @param c the variable you want to parse for.
     * @param filename the photo's filename.
     * @return the value of the variable.
     */
    private int parse(char c, String filename) {
        String build = "";
        boolean activated = false;
        for (int i = 0; i < filename.length(); i++) {
            if (activated && (filename.charAt(i) == '_' || filename.charAt(i) == '.')) {
                break;
            }
            if (activated) {
                build += filename.charAt(i);
            }
            if (filename.charAt(i) == c) {
                activated = true;
            }
        }
        return Integer.parseInt(build);
    }

    /**
     *
     * @param l1 upper left corner of rectangle 1.
     * @param r1 lower right corner of rectangle 1.
     * @param l2 upper left corner of rectangle 2.
     * @param r2 lower right corner of rectangle 2.
     * @return whether or not the query is within the range of the raster box.
     */
    private boolean inBounds(Tuple<Double, Double> l1, Tuple<Double, Double> r1, Tuple<Double, Double> l2, Tuple<Double, Double> r2) {
        if (l1.getFirst() > r2.getFirst()) {
            return false;
        }

        if (l1.getSecond() < r2.getSecond() || l2.getSecond() < r1.getSecond()) {
            return false;
        }
        return true;
    }

    /**
     * @param depth current depth level being tested.
     * @param lat the latitude of the tile we want to find.
     * @param lon the longitude of the tile we want to find.
     * @return the coordinate pair of a tile given in a (row, col) form.
     */
    public Tuple<Integer, Integer> tileIndexes(int depth, double lon, double lat) {
        int x = calcIndex(ROOT_ULLON, ROOT_LRLON, lon, depth);
        int invertedY = calcIndex(ROOT_LRLAT, ROOT_ULLAT, lat, depth);
        int y = ((int) Math.round(Math.pow(2, depth)) - 1) - invertedY;
        x = correctBounds(depth, x);
        y = correctBounds(depth, y);
        return new Tuple(x, y);
    }

    /**
     * @param depth the current depth level.
     * @param var the variable we want to adjust.
     * @return a corrected version of the variable if it is out of bounds.
     */
    public int correctBounds(int depth, int var) {
        if (var < 0) {
            return 0;
        } else if (var > Math.pow(2, depth) - 1) {
            return (int) Math.round(Math.pow(2, depth) - 1);
        }
        return var;
    }

    /**
     * @param lowerBound lower dim value.
     * @param upperBound higher dim value.
     * @param dim longitude or latitude.
     * @param depth depth you are exploring.
     * @return index of the given dim.
     */
    public int calcIndex(double lowerBound, double upperBound, double dim, int depth) {
        double size = upperBound - lowerBound;
        double boxesPerSide = (Math.pow(2, depth));
        double sizePerBox = size / boxesPerSide;
        double diffFromDim = dim - lowerBound;
        int index = (int) (diffFromDim / sizePerBox);
        return index;
    }

    /**
     * @param depth curr depth level.
     * @param i the row.
     * @param j the column.
     * @return the fully built png string.
     */
    public String getPath(int depth, int i, int j) {
        String depthStr = "d" + depth + "_";
        String rowStr = "x" + i + "_";
        String colStr = "y" + j;
        String png = ".png";
        return depthStr + rowStr + colStr + png;
    }
}
