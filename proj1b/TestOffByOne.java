import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testLetters() {
        // Just low
        assertTrue(offByOne.equalChars('a', 'b'));
        assertFalse(offByOne.equalChars('a', 'e'));
        assertFalse(offByOne.equalChars('z', 'a'));
        // Low and upp
        assertFalse(offByOne.equalChars('A', 'b'));
        assertTrue(offByOne.equalChars('Y', 'Z'));
    }
    @Test
    public void testNumbers() {
        assertTrue(offByOne.equalChars('0', '1'));
        assertFalse(offByOne.equalChars('0', '9'));
    }

    @Test
    public void testSpecial() {
        assertTrue(offByOne.equalChars('&', '%'));
        assertTrue(offByOne.equalChars('}', '~'));
        assertTrue(offByOne.equalChars('/', '0'));
        assertTrue(offByOne.equalChars('A', '@'));
    }
}