package spellAid.ui.speaker;

/**
 * This interface defines the roles a Speaker should have.
 * 
 * @author Luke Tudor
 *
 */
public interface Speaker {
	
	/**
	 * This method speaks a word
	 */
	public void speak(String line);
	
	/**
	 * This method stops the speaker speaking.
	 */
	public void sock();
}
