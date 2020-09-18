
// Chris Ong
// 4/30/20
// CSE 143
// TA: Anthony Tran
// Assignment #4
//
// This program models a manager for the game "evil hangman," 
// where the player attempts to guess an answer word, one letter at a time.
// However, the computer delays picking a word until there is only one 
// possible word in its dictionary in an attempt to maximize the number of 
// wrong guesses. The HangmanManager manages a game of evil hangman by 
// taking guesses, updating possible words, and tracking the number of 
// remaining guesses.
import java.util.*;

public class HangmanManager {
	// Represents a set of the current words that are possible to guess
	private Set<String> currentWords;
	// Represents a set of the current letters that the player has guessed
	private Set<Character> currentLetters;
	// Represents the letter pattern of the answer word
	private String pattern;
	// Represents the amount of remaining guesses the player has
	private int remainingGuesses;

	// Constructs a new HangmanManager
	// Takes in a collection of words, a length that represents the length of
	// each word from the collection that should go into the set of possible
	// words, and a maximum that represents the maximum amount of guesses the
	// player has in the game.
	// First checks if the given length is less than 1 or if the given maximum
	// amount is less than 0. If so, throws an IllegalArgumentException.
	// Then makes a set that contains all the words (and eliminates duplicates),
	// and another set that will be used to hold guessed letters.
	public HangmanManager(Collection<String> dictionary, int length, int max) {
		if (length < 1 || max < 0) {
			throw new IllegalArgumentException();
		}
		currentLetters = new TreeSet<Character>();
		currentWords = new TreeSet<String>();
		pattern = "-";
		for (int i = 0; i < length - 1; i++) {
			pattern += " " + "-";
		}
		remainingGuesses = max;
		for (String word : dictionary) {
			if (word.length() == length) {
				currentWords.add(word);
			}
		}
	}

	// Returns the current set of words that are possible answers
	public Set<String> words() {
		return currentWords;
	}

	// Returns the amount of wrong guesses the player has left
	public int guessesLeft() {
		return remainingGuesses;
	}

	// Returns the current set of letters that the player has already guessed
	public Set<Character> guesses() {
		return currentLetters;
	}

	// First checks if the set of words that can be guessed is empty
	// If it is, throws an IllegalStateException
	// Otherwise, returns the current pattern that the player sees
	// after guessing a letter.
	// There are spaces between each letter, and no leading or trailing spaces.
	public String pattern() {
		if (currentWords.isEmpty()) {
			throw new IllegalStateException();
		}
		return pattern;
	}

	// If the amount of remaining guesses is less than 1, or if the
	// set of words is empty, throws an IllegalStateException.
	// If you guess an already-guessed letter, throws an IllegalArgumentException
	// Otherwise, adds the guess to the set of current guessed letters
	// Then uses the guess to decide which pattern has the most words resembled with
	// that pattern, and chooses that pattern to display to the player
	// Returns the amount of occurences of the guessed letter in the pattern
	// (Assumes that the guessed letter is a lowercase letter)
	public int record(char guess) {
		if (guessesLeft() < 1 || currentWords.isEmpty()) {
			throw new IllegalStateException();
		}
		if (currentLetters.contains(guess)) {
			throw new IllegalArgumentException();
		}
		int occurrences = 0;
		currentLetters.add(guess);
		Map<String, Set<String>> patternMap = new TreeMap<String, Set<String>>();
		makeMap(guess, patternMap);
		findNextPattern(patternMap);
		for (int i = 0; i < pattern.length(); i++) {
			if (pattern.charAt(i) == guess) {
				occurrences++;
			}
		}
		if (occurrences == 0) {
			remainingGuesses--;
		}
		return occurrences;
	}

	// Takes in a guess and a map, and forms a map based off the guess and the
	// patterns available based off the guesses
	// Then updates the pattern depending on which pattern contains the most words
	private void makeMap(char guess, Map<String, Set<String>> temp) {
		for (String word : currentWords) {
			String key = pattern;
			int index = 0;
			for (int i = 0; i < word.length(); i++) {
				if (word.charAt(i) == guess) {
					key = key.substring(0, index) + guess + key.substring(index + 1);
				}
				index += 2;
			}
			if (!temp.containsKey(key)) {
				Set<String> words = new TreeSet<String>();
				words.add(word);
				temp.put(key, words);
			} else {
				temp.get(key).add(word);
			}
		}
	}

	// Takes in a map that has a pattern or patterns with words corresponding
	// to each pattern.
	// Finds the pattern with the most words and updates the words that
	// can be used as a possible answer word
	private void findNextPattern(Map<String, Set<String>> words) {
		int largest = 0;
		for (String word : words.keySet()) {
			Set<String> wordSet = words.get(word);
			if (wordSet.size() > largest) {
				largest = wordSet.size();
				currentWords = wordSet;
				pattern = word;
			}
		}
	}
}
