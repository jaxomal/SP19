package creatures;
import huglife.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;

public class TestClorus {
    @Test
    public void testAttack() {
        Clorus c = new Clorus(1.2);
        Plip b = new Plip(2.0);
        double expected = 3.2;
        HashMap<Direction, Occupant> attackPlip = new HashMap<Direction, Occupant>();
        attackPlip.put(Direction.TOP, new Empty());
        attackPlip.put(Direction.BOTTOM, b);
        attackPlip.put(Direction.LEFT, new Impassible());
        attackPlip.put(Direction.RIGHT, new Impassible());

        c.chooseAction(attackPlip);
        assertEquals(c.energy(), expected, 0.01);
    }

    @Test
    public void testChooseAction() {
        Clorus c = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        // THe Clorus sees a pip it shall attack, there is at least 1 empty space
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> attackPlip = new HashMap<Direction, Occupant>();
        attackPlip.put(Direction.TOP, new Empty());
        attackPlip.put(Direction.BOTTOM, new Plip());
        attackPlip.put(Direction.LEFT, new Impassible());
        attackPlip.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(attackPlip);
        expected = new Action(Action.ActionType.ATTACK, Direction.BOTTOM);
        assertEquals(expected, actual);

        // Energy >= 1; replicate towards an empty space.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> topEmpty = new HashMap<Direction, Occupant>();
        topEmpty.put(Direction.TOP, new Empty());
        topEmpty.put(Direction.BOTTOM, new Impassible());
        topEmpty.put(Direction.LEFT, new Impassible());
        topEmpty.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.REPLICATE, Direction.TOP);

        assertEquals(expected, actual);


        // Energy >= 1; replicate towards an empty space.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> allEmpty = new HashMap<Direction, Occupant>();
        allEmpty.put(Direction.TOP, new Empty());
        allEmpty.put(Direction.BOTTOM, new Empty());
        allEmpty.put(Direction.LEFT, new Empty());
        allEmpty.put(Direction.RIGHT, new Empty());

        actual = c.chooseAction(allEmpty);
        Action unexpected = new Action(Action.ActionType.STAY);

        assertNotEquals(unexpected, actual);
    }

    @Test
    public void testReplicate() {
        Clorus c1 = new Clorus(2);
        double energy = c1.energy();
        Clorus c2 = c1.replicate();
        assertEquals(energy / 2, c1.energy(), 0.01);
        assertEquals(energy / 2, c2.energy(), 0.01);
        assertNotEquals(c1, c2);
    }

    public void testBasics() {
        Clorus c = new Clorus(2);
        assertEquals(2, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());
        c.move();
        assertEquals(1.97, c.energy(), 0.01);
        c.move();
        assertEquals(1.94, c.energy(), 0.01);
        c.stay();
        assertEquals(1.93, c.energy(), 0.01);
        c.stay();
        assertEquals(1.92, c.energy(), 0.01);
    }
}
