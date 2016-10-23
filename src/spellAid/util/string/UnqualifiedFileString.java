package spellAid.util.string;

/**
 * This class turns a qualified file path into an unqualified file path.
 * For example, foo/bar.txt -> bar.txt.
 * 
 * @author Luke Tudor
 */
public class UnqualifiedFileString {
	
	private String unqualifiedFile;
	
	public UnqualifiedFileString(String qualifiedFile) {
		String[] chunks = qualifiedFile.split("/");
		unqualifiedFile = chunks[chunks.length - 1];
	}
	
	public String getUnqualifiedFile() {
		return unqualifiedFile;
	}

}
