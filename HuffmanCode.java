
// Chris Ong
// Huffman coding is an algorithm for compressing data. 
// ASCII value character data is compressed by transforming it into binary format
// (0's and 1's). For example, the letter a can be represented with a one-bit code,
// b with a two-bit code, and so on.
// This program writes the Huffman codes of a text file to a separate output.
import java.util.*;
import java.io.*;

public class HuffmanCode {
	// Represents the root of the Huffman code tree
	private HuffmanNode root;

	// Builds a Huffman code tree with the given array of character frequency.
	// Each part of the code contains a corresponding character and a count.
	public HuffmanCode(int[] frequencies) {
		Queue<HuffmanNode> nodeQ = new PriorityQueue<HuffmanNode>();
		for (int i = 0; i < frequencies.length; i++) {
			if (frequencies[i] > 0) {
				HuffmanNode node = new HuffmanNode(frequencies[i], i);
				nodeQ.add(node);
			}
		}
		while (nodeQ.size() != 1) {
			HuffmanNode current = nodeQ.remove();
			HuffmanNode next = nodeQ.remove();
			HuffmanNode newNode = new HuffmanNode(current.frequency + next.frequency,
					-1, current, next);
			nodeQ.add(newNode);
		}
		root = nodeQ.remove();
	}

	// Builds a Huffman code in standard/traversal format, using the given file
	// scanner
	// that contains a tree in standard format.
	// Note that it is assumed that the scanner is not null and contains data
	// in the standard format.
	public HuffmanCode(Scanner input) {
		while (input.hasNextLine()) {
			int asciiValue = Integer.parseInt(input.nextLine());
			String code = input.nextLine();
			root = build(root, asciiValue, code);
		}
	}

	// Helps to build a Huffman code tree, using the given node, ASCII value, and
	// a code string. Builds the tree based on if the code string is 0 or 1.
	private HuffmanNode build(HuffmanNode root, int asciiValue, String code) {
		if (code.isEmpty()) {
			return new HuffmanNode(0, asciiValue);
		} else {
			if (root == null) {
				root = new HuffmanNode(0, -1);
			}
			if (code.charAt(0) == '0') {
				root.left = build(root.left, asciiValue, code.substring(1));
			} else if (code.charAt(0) == '1') {
				root.right = build(root.right, asciiValue, code.substring(1));
			}
		}
		return root;
	}

	// Stores the current Huffman codes to the given output, in standard format
	public void save(PrintStream output) {
		if (root != null) {
			save(root, output, "");
		}
	}

	// Helps store the current Huffman codes to the given output in standard format.
	// Uses the given node, output, and code to do so.
	private void save(HuffmanNode root, PrintStream output, String code) {
		if (root.left == null && root.right == null) {
			output.println(root.character);
			output.println(code);
		} else {
			save(root.left, output, code + "0");
			save(root.right, output, code + "1");
		}
	}

	// Reads individual bits from the given input stream and writes the
	// corresponding characters to the given output (until there are no more
	// bits to read). Assumes that the input stream is compatible with this Huffman
	// code tree.
	public void translate(BitInputStream input, PrintStream output) {
		while (input.hasNextBit()) {
			translate(input, output, root);
		}
	}

	// Given an input, output, root node of a Huffman code tree, and an individual
	// bit, helps to decode binary integers (0 and 1) into ASCII letters.
	private void translate(BitInputStream input, PrintStream output, HuffmanNode root) {
		if (root.left == null && root.right == null) {
			output.write(root.character);
		} else {
			int bit = input.nextBit();
			if (bit == 0) {
				translate(input, output, root.left);
			} else if (bit == 1) {
				translate(input, output, root.right);
			}
		}
	}

	// This class represents an individual node in the Huffman code tree.
	private class HuffmanNode implements Comparable<HuffmanNode> {
		// Represents the count of the character stored in this node
		public int frequency;
		// Represents the character stored at this node
		public int character;
		// Refers to the left subtree of this node
		public HuffmanNode left;
		// Refers to the right subtree of this node
		public HuffmanNode right;

		// Constructs a node with given frequency and character count
		public HuffmanNode(int frequency, int character) {
			this(frequency, character, null, null);
		}

		// Constructs a node with given frequency and character
		public HuffmanNode(int frequency, int character, HuffmanNode left, 
				HuffmanNode right) {
			this.frequency = frequency;
			this.character = character;
			this.left = left;
			this.right = right;
		}

		// Returns the difference between this node's frequency count and a different
		// node's frequency count
		public int compareTo(HuffmanNode diff) {
			return this.frequency - diff.frequency;
		}
	}

}
