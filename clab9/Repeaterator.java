import java.util.Iterator;

public class Repeaterator {
    private int[] data;
    private int index;
    private int repeats;
    public Repeaterator(int[] array) {
        data = array;
        index = 0;
        repeats = 1;
        advance();
    }
    private void advance() {
        repeats -= 1;
        while (hasNext() && repeats == 0) {
            repeats = data[index];
            index += 1;
        }
    }
    public boolean hasNext() {
        return index < data.length;
    }
    public int next() {
        int prev = index;
        advance();
        return data[prev];
    }
    public void remove() {
        throw new UnsupportedOperationException();
    }
    public static void main(String[] args) {
        int[] nums = new int[3];
        nums[0] = 1;
        nums[1] = 2;
        nums[2] = 3;
        Repeaterator r = new Repeaterator(nums);
        while (r.hasNext()) {
            System.out.println(r.next());
        }
    }
}