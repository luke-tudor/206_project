package spellAid.util.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This class provides a list of list of methods for the SpellingAid class to use
 * for file processing. This class handles exceptions and more complex file I/O
 * operations so that the other classes aren't cluttered with file processing
 * statements.
 * 
 * The methods in this class are in alphabetical order.
 * 
 * Most of this class is reused from assignment 2.
 * 
 * @author Luke Tudor
 * @version Assignment 2
 */
public class IOHelper {
	
	// Character encoding set used.
	private static final Charset CHARSET = StandardCharsets.UTF_8;
	
	/*
	 * This method converts a String to a Set<String> so that the line parameter
	 * can be appended to the specified file. This is required because
	 * Files.write() expects an iterable object.
	 */
	public void addLineToFile(String line, Path filename){
		
		Set<String> theSameLine = Collections.singleton(line);
		
		try {
			Files.write(filename, theSameLine, CHARSET,
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {}
	}
	
	/*
	 * This method simply deletes a file if it exists.
	 */
	public void deleteIfExists(Path filename){
		try {
			Files.deleteIfExists(filename);
		} catch (IOException e) {}
	}
	
	/*
	 * This method reads an entire file and separates the file into a list.
	 * The file is separated by the "new line" characters in the file.
	 * If the file doesn't exist, an exception is thrown. In that case,
	 * an empty list is returned instead.
	 */
	public List<String> readAllLines(Path filename){
		List<String> fileLines = null;
		try {
			fileLines = Files.readAllLines(filename, CHARSET);
		} catch (IOException e){
			fileLines = new ArrayList<String>();
		}
		return fileLines;
	}
	
	/*
	 * This method reads all lines of a file into a list. Then, all occurrences
	 * of a line in that file are deleted. After that, the remaining lines are
	 * written to that file.
	 */
	public void removeLineFromFile(String line, Path filename){
		List<String> fileLines = null;
		try {
			fileLines = Files.readAllLines(filename, CHARSET);
		} catch (IOException e){}
		
		fileLines.removeAll(Collections.singleton(line));
		
		try {
			Files.write(filename, fileLines, CHARSET);
		} catch (IOException e) {}
	}
	
	/*
	 * This method simply writes a file using the list as input and file name as
	 * the destination.
	 */
	public void overwriteFile(List<String> lines, Path filename) {
		try {
			Files.write(filename, lines, CHARSET);
		} catch (IOException e) {}
	}
}
