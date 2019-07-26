import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnionFindTest {
    @Test
    public void basicUnionFind() {
        UnionFind union = new UnionFind(10);
        assertEquals(1, union.sizeOf(1));
        union.union(0, 1);
        union.union(2, 3);
        union.union(0, 3);
        assertEquals(4, union.sizeOf(0));
    }
}