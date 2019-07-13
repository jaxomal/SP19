/** Performs some basic linked list tests. */
public class LinkedListDequeTest {
	
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

		LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		boolean passed = checkEmpty(true, lld1.isEmpty());

		lld1.addFirst("front");
		
		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
		passed = checkSize(1, lld1.size()) && passed;
		passed = checkEmpty(false, lld1.isEmpty()) && passed;

		lld1.addLast("middle");
		passed = checkSize(2, lld1.size()) && passed;

		lld1.addLast("back");
		passed = checkSize(3, lld1.size()) && passed;

		System.out.println("Printing out deque: ");
		lld1.printDeque();

		printTestStatus(passed);
	}

	/** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
	public static void addRemoveTest() {

		System.out.println("Running add/remove test.");

		LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty 
		boolean passed = checkEmpty(true, lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty 
		passed = checkEmpty(false, lld1.isEmpty()) && passed;

		lld1.removeFirst();
		// should be empty 
		passed = checkEmpty(true, lld1.isEmpty()) && passed;

		printTestStatus(passed);
	}

	/** Adds ten thousand items, removes ten thousand, ensures that it is empty */
	public static void addAlotTest() {
		System.out.println("Running big add/remove test");

		LinkedListDeque<Integer> lldl = new LinkedListDeque<>();
		boolean passed = checkEmpty(true, lldl.isEmpty());

		for (int i = 0; i < 100000; i++) {
			lldl.addFirst(1);
		}
		passed = checkEmpty(false, lldl.isEmpty()) && passed;

		for (int i = 0; i < 100000; i++) {
			lldl.removeFirst();
		}

		passed = checkEmpty(true, lldl.isEmpty()) && passed;

		printTestStatus(passed);
	}

	/** Added by czahie.
	 * Gets the ith item using get and getRecursive, and ensures the result are same. */
	public static void getTest() {
		boolean passed = false;

		System.out.println("Running get test.");

		LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

		lld1.addFirst(10);
		lld1.addLast(20);
		lld1.addLast(30);

		int result1 = lld1.get(1);
		// should be 20.
		if (result1 == 20) {
			passed = true;
		}

		int result2 = lld1.getRecursive(1);
		// should be 20.
		if (result2 == 20) {
			passed = true && passed;
		}

		// should be equal.
		if (result1 == result2) {
			passed = true && passed;
		}

		printTestStatus(passed);
	}

	public static void main(String[] args) {
		System.out.println("Running tests.\n");
		addIsEmptySizeTest();
		addRemoveTest();
		addAlotTest();
		getTest();
	}
} 