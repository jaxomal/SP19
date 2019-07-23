import es.datastructur.synthesizer.GuitarString;

import java.util.HashMap;

public class GuitarHero {
    public static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static GuitarString[] keys;

    public static void main(String[] args) {
        keys = new GuitarString[37];
        fillKeyBoard();
        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (keyboard.indexOf(key) < 0) {
                    continue;
                }
                keys[keyboard.indexOf(key)].pluck();
            }

            /* compute the superposition of samples */
            double sample = sampleSum();

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            advanceStrings();
        }
    }

    private static void advanceStrings() {
        for (int i = 0; i < keys.length; i++) {
            keys[i].tic();
        }
    }

    private static double sampleSum() {
        double total = 0;
        for (int i = 0; i < keys.length; i++) {
            total += keys[i].sample();
        }
        return total;
    }

    private static void fillKeyBoard() {
        for (int i = 0; i < keyboard.length(); i++) {
            keys[i] = new GuitarString(computeFreq(i));
        }
    }

    private static double computeFreq(int x) {
        return 440 * Math.pow(2, (x - 24) / 12);
    }
}
