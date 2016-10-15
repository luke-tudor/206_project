package spellAid.util.string;

import java.net.MalformedURLException;
import java.nio.file.FileSystems;

/**
 * This class constructs a string representation of a URL from a relative path
 * 
 * @author Luke Tudor
 */
public class URLString {
	
	private String url;
	
	public URLString(String relativePath) {
		try {
			url = FileSystems.getDefault().getPath(relativePath).toUri().toURL().toString();
		} catch (MalformedURLException e) {}
	}
	
	public String getURL() {
		return url;
	}

}
