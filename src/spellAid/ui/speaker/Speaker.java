package spellAid.ui.speaker;

/**
 * This defines the roles a Speaker should have.
 * 
 * @author Luke Tudor
 *
 */
public interface Speaker {
	
	public void speak(String line);
	
	/**
	 * This stops the speaker speaking.
	 */
	public void sock();
}
