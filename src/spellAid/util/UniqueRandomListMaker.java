package spellAid.util;

import java.util.Random;
import java.util.Set;

/**
 * This class is to help the SpellingAid class to create a unique selection
 * of words from a unique list. In short, this class creates the list that the
 * spelling aid class uses to test the user.
 * 
 * It should be noted that all entries in a "Set<T>" are unique.
 * 
 * Most of this class is reused from assignment 2.
 * 
 * @author Luke Tudor
 * @version Assignment 2
 */
public class UniqueRandomListMaker {
	
	// Create a new random number generator.
	private static final Random RNG = new Random();
	
	/*
	 * This method creates the unique random list which is the size specified in
	 * the parameter from the list supplied in the parameter.
	 */
	public String[] getUniqueList(Set<String> uniqueSet, int value) {
		
		/*
		 * This is to make sure that whatever object called this method will
		 * not accidentally create an infinite loop.
		 */
		if (value > uniqueSet.size()){
			value = uniqueSet.size();
		}
		
		/* 
		 * Sets are psudo-random, but this subroutine makes sure that values
		 * are selected randomly by using a random number generator to select
		 * values. 
		 */
		String[] uniqueList = uniqueSet.toArray(new String[0]);
		String[] newList = new String[value];
		
		for (int i = 0; i < value; i++){
			
			boolean isUnique = false;
			
			/* 
			 * Keep selecting random values from the list until those random
			 * values are not the same as any of the values in the new list.
			 */
			while (!isUnique){
				isUnique = true;
				newList[i] = uniqueList[RNG.nextInt(uniqueList.length)];
				
				// Test if the word selected is unique, if not, select another
				// word.
				for (int j = 0; j < i; j++){
					if (newList[i].equals(newList[j])){
						isUnique = false;
					}
				}
			}
		}
		return newList;
	}
}
