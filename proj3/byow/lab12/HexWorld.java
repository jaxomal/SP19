package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final int LOWER = 0;
    private static final int UPPER = 1;

    /**
     * @param world the given world where we create the hexagon.
     * @param p the lower left corner of the hexagon.
     * @param s the size of the hexagon.
     * @param t the tile type of the hexagon.
     */
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        Position start = p;
        drawHalf(world, start, s, t, LOWER);
        drawHalf(world, start, s, t, UPPER);
    }

    /** Helper Method to draw either the top or bottom half of the hexagon */
    private static void drawHalf(TETile[][] world, Position start, int s, TETile t, int option) {
        switch (option) {
            case LOWER:
                for (int i = 0; i < s; i++) {
                    int amountToDraw = amountToDraw(i, s);
                    Position currStart = calcStart(start, i, s);
                    drawRow(world, currStart, amountToDraw, t);
                    start.y += 1;
                }
                break;
            case UPPER:
                for (int i = s - 1; i >= 0; i--) {
                    int amountToDraw = amountToDraw(i, s);
                    Position currStart = calcStart(start, i, s);
                    drawRow(world, currStart, amountToDraw, t);
                    start.y += 1;
                }
                break;
        }
    }

    /** Draws a column of hexagons */
    private static void drawColumn(TETile[][] world, Position start, int s, int N, TETile t) {
        Position curr = start;
        for (int i = 0; i < N; i++) {
            HexWorld.addHexagon(world, curr, s, t);
        }
    }

    /** Determines the right neighbor of the given bottom hexagon,
     *  only use when neighbor is lower than current hexagon. */
    private static Position bottomRightNeighbor(Position p, int s) {
        int x = p.x;
        int y = p.y;
        int transX = x + s * 2 - 1;
        int transY = y - s;
        return new Position(transX, transY);
    }

    /** Determines the right neighbor of the given bottom hexagon */
    private static Position upperRightNeighbor(Position p, int s) {
        int x = p.x;
        int y = p.y;
        int transX = x + s * 2 - 1;
        int transY = y + s;
        return new Position(transX, transY);
    }

    /** Helper method that draws a singular row of tiles */
    private static void drawRow(TETile[][] world, Position start, int amountToDraw, TETile t) {
        int x = start.x;
        int y = start.y;
        for (int i = 0; i < amountToDraw; i++) {
            world[x][y] = t;
            x += 1;
        }
    }

    private static Position calcStart(Position start, int i, int s) {
        int initX = start.x;
        int constantY = start.y;
        int adjustedX = initX + (s - 1) - i;
        Position newPos = new Position(adjustedX, constantY);
        return newPos;
    }

    private static int amountToDraw(int i, int s) {
        return i * 2 + s;
    }

    public static void main(String[] args) {
        int WIDTH = 35;
        int HEIGHT = 35;
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        int s = 3;
        Position p1 = new Position(0, 8);
        Position p2 = bottomRightNeighbor(p1, s);
        Position p3 = bottomRightNeighbor(p2, s);
        Position p4 = upperRightNeighbor(p3, s);
        Position p5 = upperRightNeighbor(p4, s);
        HexWorld.drawColumn(world, p1, s, 3, Tileset.MOUNTAIN);
        HexWorld.drawColumn(world, p2, s, 4, Tileset.GRASS);
        HexWorld.drawColumn(world, p3, s, 5, Tileset.FLOWER);
        HexWorld.drawColumn(world, p4, s, 4, Tileset.GRASS);
        HexWorld.drawColumn(world, p5, s, 3, Tileset.MOUNTAIN);
        ter.renderFrame(world);
    }
}
