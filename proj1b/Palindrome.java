public class Palindrome {
    // LinkedList Deque is 20 ms faster, I did some bad code ig
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new LinkedListDeque<Character>();
        for (int i = 0; i < word.length(); i++) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }

    public boolean isPalindrome(String word) {
        // If the length of the word is 1 or 0 its a palindrome
        if (word.length() == 0) {
            return true;
        } else if (word.length() == 1) {
            return true;
        } else {
            // remove the ends if they are equal then check if the string with removed ends is palindrome
            if (word.charAt(0) != word.charAt(word.length() - 1)) {
                return false;
            }
            return isPalindrome(word.substring(1, word.length() - 1));
        }
    }

    // Same implementation except use equalChars to check for equality
    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word.length() == 0) {
            return true;
        } else if (word.length() == 1) {
            return true;
        } else {
            if (!cc.equalChars(word.charAt(0), word.charAt(word.length() - 1))) {
                return false;
            }
            return isPalindrome(word.substring(1, word.length() - 1), cc);
        }
    }
}
