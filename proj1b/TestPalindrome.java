import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        // Base cases, no letters or 1 letter
        assertTrue(palindrome.isPalindrome(""));// t
        assertTrue(palindrome.isPalindrome("a")); // t
        assertTrue(palindrome.isPalindrome("racecar")); // t
        assertFalse(palindrome.isPalindrome("abjeet")); // f
        assertFalse(palindrome.isPalindrome("Racecar")); // f
    }

    @Test
    public void testisOBOPalindrome() {
        OffByOne obo = new OffByOne();
        Palindrome newOne = new Palindrome();
        assertTrue(newOne.isPalindrome("flake", obo));
        assertTrue(newOne.isPalindrome("", obo));
        assertTrue(newOne.isPalindrome("d", obo));
        assertTrue(newOne.isPalindrome("ab", obo));
        assertTrue(newOne.isPalindrome("&%", obo));
        assertFalse(newOne.isPalindrome("racecar", obo));
    }
}