package spellAid.util;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

public class ExtendedIOHelper extends IOHelper{

	public void addLineToFile(String line, String path) {
		super.addLineToFile(line, string2path(path));
	}
	
	public void deleteIfExists(String path) {
		super.deleteIfExists(string2path(path));
	}
	
	public List<String> readAllLines(String path) {
		return super.readAllLines(string2path(path));
	}
	
	public void removeLineFromFile(String line, String path) {
		super.removeLineFromFile(line, string2path(path));
	}
	
	private Path string2path(String path) {
		return FileSystems.getDefault().getPath(path);
	}
}
