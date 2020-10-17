
// Chris Ong
// This program implements a GuitarString class that models a vibrating guitar 
// string of a given frequency by keeping track of a ring buffer
import java.util.*;

public class GuitarString {
	// Represents a ring buffer (a structure that stores the guitar string's
	// displacement values at equally spaced points in time)
	private Queue<Double> ringBuffer;
	// Represents the energy decay factor
	public final double DECAY_FACTOR = 0.996;
	// Represents the sampling rate
	public final int SAMPLE_RATE = StdAudio.SAMPLE_RATE;

	// Takes in a value that represents the frequency
	// Checks if the frequency is 0 or less, or if the ring buffer's capacity is
	// less than 2. If so, an IllegalArgumentException is thrown.
	// Otherwise, a guitar string is constructed at the given frequency
	public GuitarString(double frequency) {
		int capacity = (int) Math.round(SAMPLE_RATE / frequency);
		if (frequency <= 0 || capacity < 2) {
			throw new IllegalArgumentException();
		}
		ringBuffer = new LinkedList<>();
		for (int i = 0; i < capacity; i++) {
			ringBuffer.add(0.0);
		}
	}

	// Takes in a list of numbers that represents the contents of the ring buffer
	// First checks if the list's size is less than 2
	// If it is less than 2, an IllegalArgumentException is thrown.
	// Otherwise, a guitar string is constructed from the given values from the ring
	// buffer
	public GuitarString(double[] init) {
		if (init.length < 2) {
			throw new IllegalArgumentException();
		}
		ringBuffer = new LinkedList<>();
		for (int i = 0; i < init.length; i++) {
			ringBuffer.add(init[i]);
		}
	}

	// Each number in the ring buffer is replaced with random values between -0.5
	// (inclusive) and 0.5 (exclusive)
	public void pluck() {
		int size = ringBuffer.size();
		Random rand = new Random();
		for (int i = 0; i < size; i++) {
			ringBuffer.remove();
			double random = rand.nextDouble() - 0.5;
			ringBuffer.add(random);
		}
	}

	// Applies the Karplue-Strong update once (performing one step) to the sample
	// Does so by deleting the sample at the front of the ring buffer, and adding to
	// the end of the buffer the average of the first two samples multiplied by the
	// energy decay factor.
	public void tic() {
		double first = ringBuffer.remove();
		double second = ringBuffer.peek();
		ringBuffer.add(DECAY_FACTOR * (first + second) / 2);
	}

	// Returns the current sample (value at the front of the ring buffer)
	public double sample() {
		return ringBuffer.peek();
	}
}
