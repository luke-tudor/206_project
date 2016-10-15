package spellAid.util.string;

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
