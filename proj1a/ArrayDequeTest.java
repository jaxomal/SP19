/** Performs some basic linked list tests. */
public class ArrayDequeTest {

    /* Utility method for printing out empty checks. */
    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out empty checks. */
    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Prints a nice message based on whether a test passed.
     * The \n means newline. */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");
        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque<String> ad1 = new ArrayDeque<>();

        boolean passed = checkEmpty(true, ad1.isEmpty());

        ad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        passed = checkSize(1, ad1.size()) && passed;
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        ad1.addLast("middle");
        passed = checkSize(2, ad1.size()) && passed;

        ad1.addLast("back");
        passed = checkSize(3, ad1.size()) && passed;

        System.out.println("Printing out deque: ");
        ad1.printDeque();

        printTestStatus(passed);
    }

    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public static void addRemoveTest() {

        System.out.println("Running add/remove test.");

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        // should be empty
        boolean passed = checkEmpty(true, ad1.isEmpty());

        ad1.addFirst(10);
        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        ad1.removeFirst();
        // should be empty
        passed = checkEmpty(true, ad1.isEmpty()) && passed;

        printTestStatus(passed);
    }

    /** Adds ten thousand items, removes ten thousand, ensures that it is empty */
    public static void addAlotTest() {
        System.out.println("Running big add/remove test");

        ArrayDeque<Integer> adl = new ArrayDeque<>();
        boolean passed = checkEmpty(true, adl.isEmpty());

        for (int i = 0; i < 100000; i++) {
            adl.addFirst(1);
        }
        passed = checkEmpty(false, adl.isEmpty()) && passed;

        for (int i = 0; i < 100000; i++) {
            adl.removeFirst();
        }

        passed = checkEmpty(true, adl.isEmpty()) && passed;

        printTestStatus(passed);
    }

    public static void getTest() {
        System.out.println("Running get test.");
        ArrayDeque<Integer> deque1 = new ArrayDeque<>();
        deque1.addFirst(5);
        deque1.addLast(10);
        deque1.addLast(20);
        deque1.addLast(40);
        deque1.addFirst(1);
        int get1 = deque1.get(0);
        int get2 = deque1.get(1);
        int get3 = deque1.get(2);
        if(get1 == 1 && get2 == 5 && get3 == 10 && deque1.get(6) == null) {
            printTestStatus(true);
        } else {
            printTestStatus(false);
        }
    }

    public static void resizeTest() {
        System.out.println("Running get test.");
        ArrayDeque<Integer> deque1 = new ArrayDeque<>();
        int i = 0;
        while (i < 100) {
            deque1.addLast(i);
            i++;
        }
        System.out.println(deque1.size());
        deque1.printDeque();
    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addIsEmptySizeTest();
        addRemoveTest();
        addAlotTest();
        getTest();
        //resizeTest();
    }
} 