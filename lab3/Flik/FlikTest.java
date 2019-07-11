import static org.junit.Assert.*;
import org.junit.Test;

public class FlikTest {
    @Test
    public void testIsSameNumber() {
        assertEquals(Flik.isSameNumber(2, 2), true);
        assertNotEquals(Flik.isSameNumber(2, 3), true);
    }
}
