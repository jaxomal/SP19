public class HorribleSteve {
    public static void main(String [] args) throws Exception {
        int i = 0;
        for (int j = 0; i < 500; ++i, ++j) {
            if (!Flik.isSameNumber(i, j)) {
                throw new Exception("i: " + i + " is not same as j: " + j + " ??");
            }
        }
        System.out.println("i is " + i);
    }
}
