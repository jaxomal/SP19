package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.Occupant;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Random;

import static huglife.HugLifeUtils.randomEntry;

public class Clorus extends Creature {
    private int r;
    private int g;
    private int b;

    public Clorus() { this(1); }

    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    public Clorus replicate() {
        Clorus returnClorus = new Clorus(this.energy / 2);
        this.energy /= 2;
        return returnClorus;
    }

    public Color color() {
        r = 34;
        g = 0;
        b = 231;
        return color(r, g, b);
    }

    public void attack(Creature L) {
        this.energy += L.energy();
    }

    public void move() {
        manEnergy(-0.03);
    }

    public void stay() {
        manEnergy(-0.01);
    }

    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        // Rule 1
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> plippers = new ArrayDeque<>();
        for (Direction key : neighbors.keySet()) {
            if (neighbors.get(key).name().equalsIgnoreCase("empty")) {
                emptyNeighbors.addFirst(key);
            }
        }

        if (emptyNeighbors.isEmpty()) { // FIXME
            stay();
            return new Action(Action.ActionType.STAY);
        }
        // Rule 2
        for (Direction key : neighbors.keySet()) {
            if (neighbors.get(key).name().equalsIgnoreCase("plip")) {
                plippers.add(key);
            }
        }
        // Select unfortunate plip
        if (!plippers.isEmpty()) {
            Direction key = randomEntry(plippers);
            this.attack((Creature) neighbors.get(key));
            return new Action(Action.ActionType.ATTACK, key);
        }
        // Rule 3
        if (this.energy >= 1) {
            Direction key = randomEntry(emptyNeighbors);
            return new Action(Action.ActionType.REPLICATE, key);
        }

        // Rule 4
        Direction rand = randomEntry(emptyNeighbors);
        move();
        return new Action(Action.ActionType.MOVE, rand);
    }

    private void manEnergy(double energy) {
        if (energy <= 0) {
            if (this.energy + energy < 0) {
                this.energy = 0;
                return;
            }
            this.energy += energy;
        } else {
            this.energy += energy;
        }
    }
}
