public class OffByN implements CharacterComparator {
    private int offBy;

    public OffByN(int n) {
        offBy = n;
    }
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == offBy;
    }
}
