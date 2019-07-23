package es.datastructur.synthesizer;

/**
 * @author willis
 * GuitarString class for the GuitarHero application.
 */
public class GuitarString {
    /** Sampling Rate. */
    private static final int SR = 44100;
    /** Energy decay factor. */
    private static final double DECAY = .996;

    /** Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /** Create a guitar string of the given frequency.
      @param frequency the Hz of the guitar string */
    public GuitarString(double frequency) {
        buffer = new ArrayRingBuffer<Double>((int) (SR / frequency));
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.enqueue(0.0);
        }
    }


    /** Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.dequeue();
        }
        for (int i = 0; i < buffer.capacity(); i++) {
            double r = Math.random() - 0.5;
            buffer.enqueue(r);
        }
    }

    /** Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double first = buffer.dequeue();
        buffer.enqueue((first + buffer.peek()) / 2 * DECAY);
    }

    /** Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }
}
