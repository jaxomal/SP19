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
        if (word.length() == 0) {
            return true;
        } else if (word.length() == 1) {
            return true;
        } else {
            if (word.charAt(0) != word.charAt(word.length() - 1)) {
                return false;
            }
            return isPalindrome(word.substring(1, word.length() - 1));
        }
    }
}
