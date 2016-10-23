package spellAid.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import spellAid.util.io.ExtendedIOHelper;
import spellAid.util.string.HiddenFileString;
import spellAid.util.string.URLString;
import spellAid.util.string.UniqueRandomListMaker;
import spellAid.util.string.UnqualifiedFileString;

/**
 * The SpellingAid program creates a GUI which implements the functionality
 * of the spelling aid assignment by providing the user a row of buttons which
 * subsequently implement some kind of functionality and provide additional 
 * GUIs to execute the functionality of each button. 
 * 
 * The methods in this class are listed in the order that they are first used.
 * 
 * @author Luke Tudor and Aprajit Gandhi
 */
public class SpellingAid extends Application implements EventHandler<ActionEvent> {

	/*
	 * These static fields simply provide a convenient way for the application
	 * to refer to the necessary text files. This is done so that the actual
	 * name of the files is only found in one place, here.
	 */
	private static final String NZVOICE = "voices/nzvoice.scm";
	private static final String USVOICE = "voices/usvoice.scm";

	private static final String STYLESHEET = new URLString("style/mainstyle.css").getURL();

	/*
	 * These fields refer to the buttons in the GUI.
	 */
	private Button newQuiz;
	private Button viewStatistics;
	private Button viewHighScore;
	private Button options;
	private Button quit;

	/*
	 * This field is a helper object to easily deal with file IO by removing the
	 * need for this object to worry about file  and IO Exceptions. 
	 */
	private ExtendedIOHelper ioHelper;

	private String[] wordlists;

	private List<Set<String>> wordlist;
	private List<String> sublists;

	private String currentSubList;

	private String currentWordList;  


	//currentSpeech is just the text on the combobox of the current voice
	private String currentSpeech;

	private Stage primaryStage;

	private Scene scene;

	@Override
	public void start(Stage primaryStage) throws Exception {

		this.primaryStage = primaryStage;

		ioHelper = new ExtendedIOHelper();

		wordlists = FileSystems.getDefault().getPath("user_lists").toFile().list();

		currentWordList = "user_lists/NZCER-spelling-lists.txt";

		createWordList();

		currentSpeech = "NZ voice";

		newQuiz = new Button("New Quiz");
		viewStatistics = new Button("View Statistics");
		viewHighScore = new Button("View High Scores");
		options = new Button("Options");
		quit = new Button("Quit");

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(50));
		grid.setVgap(5);
		grid.add(newQuiz, 0, 0);
		grid.add(viewStatistics, 0, 1);
		grid.add(viewHighScore, 0, 2);
		grid.add(options, 0, 3);
		grid.add(quit, 0, 4);
		grid.setAlignment(Pos.CENTER);

		// This simply adds this object as a listener for these buttons
		// and adds all the buttons to the frame.
		Button[] buttons = {newQuiz, viewStatistics, viewHighScore, quit, options};

		for (Button btn : buttons){
			btn.setOnAction(this);
			btn.setPrefWidth(400);
			btn.setPrefHeight(100);
			btn.getStylesheets().add(STYLESHEET);
		}

		Label title = new Label("Welcome to VOXSPELL!");
		title.setId("title");

		FlowPane flow = new FlowPane(title);
		flow.setAlignment(Pos.CENTER);
		flow.setPadding(new Insets(50));

		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		root.setCenter(grid);
		root.setTop(flow);
		root.setPrefSize(AppDim.WIDTH.getValue(), AppDim.HEIGHT.getValue());

		scene = new Scene(root);
		scene.getStylesheets().add(STYLESHEET);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public SpellingAid() {
		super();
	}

	/*
	 * This method simply converts an action event, a click of a button, to the
	 * corresponding method.
	 */
	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == newQuiz) {
			runNewQuiz();
		} else if (e.getSource() == viewStatistics) {
			displayStatistics();
		} else if (e.getSource() == viewHighScore) {
			displayHighScore();
		} else if (e.getSource() == options) {
			displayOptionsWindow();
		} else if (e.getSource() == quit) {
			quit();
		}
	}

	/*
	 * This method sets up and initiates a new spelling quiz with the 
	 * help of some collaborating classes.
	 */
	private void runNewQuiz() {
		// This line converts the word list file to a list.
		Set<String> uniqueLines = wordlist.get(sublists.indexOf(currentSubList));

		/*
		 *  If the list is empty, then the file contained nothing or didn't exist.
		 *  In either case, this conditional tells the user to provide a word
		 *  list.
		 */
		if (uniqueLines.isEmpty()){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Alert!");
			alert.setContentText("Please provide some words in the selected list.");
			alert.showAndWait();
			return;
		}

		int numTests = 10;

		/*
		 * A list of random selections is created from the list of unique lines
		 * by a helper object that creates random lists.
		 */
		UniqueRandomListMaker urlm = new UniqueRandomListMaker();
		String[] testList = urlm.getUniqueList(uniqueLines, numTests);

		String hiddenFile = new HiddenFileString(currentWordList.substring(0, currentWordList.length() - 4) 
				+ "." + currentSubList).getHiddenFileString();

		/*
		 * The Quiz class, which creates a GUI to run the quiz is being
		 * implemented by an anonymous class that implements the "New Quiz"
		 * functionality. Some methods are overridden so that words can be
		 * written to the corresponding files.
		 */
		Application newQuiz = new Quiz(scene, testList, currentSpeech) {

			@Override
			protected void passedFirstTime() {
				ioHelper.addLineToFile(getLastTestedWord(),
						hiddenFile + ".mastered.txt");
			}

			@Override
			protected void passedSecondTime() {
				ioHelper.addLineToFile(getLastTestedWord(),
						hiddenFile + ".faulted.txt");
			}

			@Override
			protected void failedFirstTime() {}

			@Override
			protected void failedSecondTime() {
				ioHelper.addLineToFile(getLastTestedWord(),
						hiddenFile + ".failed.txt");
			}

			@Override
			protected String selectVoice(String selection) {
				if (selection.equals("NZ voice")) {
					return NZVOICE;
				} else if(selection.equals("USA voice")) {
					return USVOICE;
				} else {
					return null;
				}
			}

			@Override
			protected void updateHighScore(String time) {
				String unqWordList = new UnqualifiedFileString(currentWordList).getUnqualifiedFile();
				String fileName = "user_lists/." 
						+ unqWordList + "." + currentSubList + ".score.txt";
				List<String> scores = ioHelper.readAllLines(fileName);
				String userName = "Unknown";
				try {
					userName = System.getProperty("user.name");
				} catch (Exception e) {}
				String line = userName + "\t" + time ;

				int i;
				for (i = 0; i < 3; i++) {
					String oldTime = null;
					try {
						oldTime = scores.get(i).split("\t")[1];
					} catch (Exception e) {
						break;
					}
					if (time.compareTo(oldTime) < 0 ) {
						break;
					}
				}
				scores.add(i, line);
				try {
					scores.remove(3);
				} catch (Exception e) {}
				ioHelper.overwriteFile(scores, fileName);
			}

		};
		try {
			newQuiz.start(primaryStage);
		} catch (Exception e) {}
	}

	/*
	 * This method displays the statistics, number of failed, faulted and 
	 * mastered word for each word in the word list.
	 */
	private void displayStatistics() {
		Application displayStatistics = new DisplayStatistics(scene, wordlist, sublists, currentWordList, sublists.indexOf(currentSubList));
		try {
			displayStatistics.start(primaryStage);
		} catch (Exception e) {}
	}

	private void displayHighScore() {
		String unqWordList = new UnqualifiedFileString(currentWordList).getUnqualifiedFile();
		List<String> scores = ioHelper.readAllLines("user_lists/." 
				+ unqWordList + "." + currentSubList + ".score.txt");

		String heading = "Fastest Times to Complete\nList: " 
				+ unqWordList + "\nSublist: " + currentSubList + "\n";
		StringBuilder score = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			String line = null;
			try {
				String[] chunks = scores.get(i).split("\t");
				line = " Username: " + chunks[0] + "\t" + "Time: " + chunks[1];
			} catch (Exception e) {
				line = " --";
			}
			score.append(i+1 + "." + line + "\n");
		}
		Application displayHighScore = new DisplayHighScore(scene, score.toString(), heading);
		try {
			displayHighScore.start(primaryStage);
		} catch (Exception e) {}
	}

	private void displayOptionsWindow() {
		Application displayOptions =
				new DisplayOptions(scene, wordlists, currentWordList, sublists, currentSubList, currentSpeech) {

			@Override
			protected void changeSpeech(String voice) {
				if (voice.equals("NZ voice")) {
					currentSpeech= "NZ voice";
				} else if(voice.equals("USA voice")) {
					currentSpeech="USA voice";
				}
			}

			@Override
			protected void changeSublist(String sublist) {
				currentSubList = sublist;
			}

			@Override
			protected void changeList(String list) {
				currentWordList = "user_lists/" + list;
				createWordList();
				sublistCombo.setItems(FXCollections.observableList(sublists));
				try {
					currentSubList = sublists.get(0);
				} catch (Exception e) {}
				sublistCombo.getSelectionModel().select(currentSubList);
			}

			@Override
			protected void addList() {
				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files (*.txt)", "*.txt"));
				fileChooser.setInitialDirectory(FileSystems.getDefault().getPath(".").toFile());
				File listToAdd = fileChooser.showOpenDialog(primaryStage);
				try {
					if (isListValid(listToAdd)) {
						ioHelper.deleteIfExists("user_lists/" + listToAdd.getName());
						Files.createSymbolicLink(FileSystems.getDefault().getPath("user_lists/" + listToAdd.getName()),
								FileSystems.getDefault().getPath(listToAdd.getAbsolutePath()));
					} else {
						Platform.runLater(() -> {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("Alert!");
							alert.setContentText("The provided list is not valid.\n"
									+ "Please check the list formatting and try again.");
							alert.showAndWait();
						});
						return;
					}
				} catch (IOException | NullPointerException e) {
					return;
				}
				wordlists = FileSystems.getDefault().getPath("user_lists").toFile().list();
				listCombo.setItems(FXCollections.observableList(getAllVisibleFiles(wordlists)));
				listCombo.getSelectionModel().select("NZCER-spelling-lists.txt");
				changeList("NZCER-spelling-lists.txt");
			}

			@Override
			protected String getVoice(String selectedVoice) {
				if (selectedVoice.equals("NZ voice")) {
					return NZVOICE;
				} else if(selectedVoice.equals("USA voice")) {
					return USVOICE;
				} else {
					return null;
				}
			}

		};
		try {
			displayOptions.start(primaryStage);
		} catch (Exception e) {}
	}

	private void quit() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

		ButtonType yes = new ButtonType("Yes");
		ButtonType no = new ButtonType("No");

		alert.getButtonTypes().setAll(yes, no);
		alert.setTitle("Alert!");
		alert.setContentText("Are you sure you want to quit?");
		Optional<ButtonType> reply = alert.showAndWait();
		if (reply.get() == yes) {
			primaryStage.hide();
		}
	}

	private void createWordList() {
		List<String> unfilteredList = ioHelper.readAllLines(currentWordList);
		wordlist = new ArrayList<Set<String>>();
		sublists = new ArrayList<String>();

		int listNumber = -1;
		for (String line : unfilteredList) {
			if (line.startsWith("%")) {
				listNumber++;
				wordlist.add(new HashSet<String>());
				sublists.add(line.substring(1, line.length()));
			} else {
				wordlist.get(listNumber).add(line);
			}
		}
		try {
			currentSubList = sublists.get(0);
		} catch (Exception e) {}
	}

	private boolean isListValid(File file) {
		if (file == null) {
			throw new NullPointerException();
		}
		Scanner sc = null;
		try {
			sc = new Scanner(file);
			String firstLine = sc.nextLine();
			if (firstLine.startsWith("%")) {
				return true;
			}
		} catch (FileNotFoundException e) {}
		finally {
			sc.close();
		}
		return false;
	}

	/*
	 * This method simply tells the Event Dispatch Thread to create and show the
	 * GUI.
	 */
	public static void main(String[] args) {
		launch(args);
	}


}