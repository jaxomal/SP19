public class PalindromeFinder {
    public static void main(String[] args) {
        int minLength = 4;
        In in = new In("../library-sp19/data/words.txt");
        Palindrome palindrome = new Palindrome();
        int bestN = -1;
        int bestTotal = 0;
        for (int i = 1; i < 26; i++) {
            int total = 0;
            OffByN currN = new OffByN(i);
            while (!in.isEmpty()) {
                String word = in.readString();
                if (word.length() >= minLength && palindrome.isPalindrome(word, currN)) {
                    System.out.println(word);
                    total++;
                }
            }
            if (total > bestTotal) {
                bestN = i;
                bestTotal = total;
            }
        }
        System.out.println("The bestN is Off By " + bestN + " with a total of " + bestTotal);
    }
}