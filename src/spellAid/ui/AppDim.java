package spellAid.ui;

/**
 * This enum simply defines the dimensions of the application
 * 
 * @author Luke Tudor
 */
public enum AppDim {
	
	WIDTH(900),
	HEIGHT(700);
	
	private int value;
	
	private AppDim(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
