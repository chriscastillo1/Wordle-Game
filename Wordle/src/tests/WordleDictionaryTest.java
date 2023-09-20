/**
 * WordleDictionaryTest
 * Tests Wordle Dictionary functionality.
 * Because Wordle Dictionary uses randomness, we only test validity of results.
 */
package tests;

import org.junit.jupiter.api.Test;

import model.WordleDictionary;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;

class WordleDictionaryTest {
	
	WordleDictionary testDictionary = new WordleDictionary();
	
	WordleDictionary guesser = new WordleDictionary();

	String guess1 = "crane";
	String guess2 = "happy";
	String guess3 = "hello";
	String guess4 = "apple";
	String guess5 = "whelp";
	String guess6 = "while";
	String guess7 = "treat";

	int[] guess1Check = {1, 1, 1, 1, 1};
	int[] guess2Check = {-1, 0, -1, -1, -1};
	int[] guess3Check = {-1, 0, -1, -1, -1};
	int[] guess4Check = {0, -1, -1, -1, 0};
	int[] guess5Check = {-1, -1, 1, -1, -1};
	int[] guess6Check = {-1, -1, -1, -1, 0};
	int[] guess7Check = {1, 1, 1, 1, 1};



	@Test
	void test1() {
		guesser.setWord("crane");
		guesser.isValidWord(guess1);
		//word is crane, and guess 1 is crane, make sure the returned guess is equal to [1, 1, 1, 1, 1]
		printArr(guesser.checkAccuracy(guess1));
		Assert.assertArrayEquals(guesser.checkAccuracy(guess1), guess1Check);

		guesser.isGuessCorrect();

		//set the word to treat
		guesser.setWord("treat");

		//check guess2 to status array
		printArr(guesser.checkAccuracy(guess2));
		Assert.assertArrayEquals(guesser.checkAccuracy(guess2), guess2Check);

		//check guess3 to status array
		printArr(guesser.checkAccuracy(guess3));
		Assert.assertArrayEquals(guesser.checkAccuracy(guess3), guess3Check);

		//check guess4 to status array
		printArr(guesser.checkAccuracy(guess4));
		Assert.assertArrayEquals(guesser.checkAccuracy(guess4), guess4Check);

		//check guess5 to status array
		System.out.println(guesser.checkAccuracy(guess5));
		Assert.assertArrayEquals(guesser.checkAccuracy(guess5), guess5Check);

		//check guess6 to status array
		System.out.println(guesser.checkAccuracy(guess6));
		Assert.assertArrayEquals(guesser.checkAccuracy(guess6), guess6Check);

		//check guess7 to status array
		System.out.println(guesser.checkAccuracy(guess7));
		Assert.assertArrayEquals(guesser.checkAccuracy(guess7), guess7Check);
	}

	public void printArr(int[] array) {
		System.out.print("[");
		for (int i = 0; i <= 4; i++) {
			System.out.print(array[i]);
		}
		System.out.println("]");
	}

	@Test
	void test2() {
		testDictionary.initialize();
		testDictionary.setWord();
		assertTrue(testDictionary.getWord().length() == 5);
	}

}
