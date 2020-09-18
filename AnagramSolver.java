
// Chris Ong
// 5/21/20
// CSE 143
// TA: Anthony Tran
// Assignment #6
//
// An anagram is a word or phrase made by rearranging the letters of 
// word or phrase. This program prints all anagram phrases of a given word or
// phrase. The anagrams will ignore spaces and capitalization, and allow 
// multiple words. 
import java.util.*;

public class AnagramSolver {
	// Represents the dictionary of words
	private List<String> wordDictionary;
	// Represents the map of letter inventories to words 
	private Map<String, LetterInventory> combinations;

	// Uses the given dictionary to create a new anagram solver
	// Assumes that the dictionary is non-empty, contains non-empty sequences
	// of letters, and contains no duplicates.
	public AnagramSolver(List<String> dictionary) {
		wordDictionary = dictionary;
		combinations = new HashMap<String, LetterInventory>();
		for (String word : wordDictionary) {
			LetterInventory letters = new LetterInventory(word);
			combinations.put(word, letters);
		}
	}

	// Takes in a word or phrase which will have its anagrams printed
	// The anagrams can have an amount of words equal to, at most, the given
	// "max." Note that if max is passed in as 0, as many anagrams as possible
	// are printed. First checks if the max is less than 0. If it is, throws an 
	// IllegalArgumentException. Otherwise, prints all the possible anagrams of 
	// the given word from the dictionary. 
	public void print(String text, int max) {
		if (max < 0) {
			throw new IllegalArgumentException();
		}
		LetterInventory currentLetters = new LetterInventory(text);
		List<String> possibleWords = subDictionary(currentLetters, wordDictionary);
		Stack<String> anagram = new Stack<String>();
		print(possibleWords, currentLetters, max, anagram);
	}

	// Takes in a list of possible words, the letter inventory of current letters 
	// being examined, the maximum amount of words to print each time, and a 
	// stack representation of the anagram. If the letter inventory is empty, the 
	// anagram is printed. Otherwise, continues to search for possible word combinations 
	// until the letter inventory is empty. 
	private void print(List<String> possibleWords, LetterInventory currentLetters, int max, 
		Stack<String> anagram) {
		if (currentLetters.size() == 0) {
			System.out.println(anagram.toString());
		} else if (anagram.size() < max || max == 0) {
			for (String word : possibleWords) {
				anagram.push(word);
				LetterInventory newLetters = currentLetters.subtract(combinations.get(word));
				List<String> unused = subDictionary(newLetters, possibleWords);
				print(unused, newLetters, max, anagram);
				anagram.pop();
			}
		}
	}

	// Takes in a letter inventory of the current letters that are being examined, 
	// and the dictionary of words. Then returns a list of the possible word 
	// combinations that can be made from the current inventory
	private List<String> subDictionary(LetterInventory currentLetters, 
		List<String> wordDictionary) {
		List<String> possibleWords = new ArrayList<String>();
		for (String word : wordDictionary) {
			LetterInventory current = combinations.get(word);
			LetterInventory remainder = currentLetters.subtract(current);
			if (remainder != null) {
				possibleWords.add(word);
			}
		}
		return possibleWords;
	}
}
