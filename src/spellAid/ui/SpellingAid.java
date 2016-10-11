package spellAid.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import spellAid.util.IOHelper;
import spellAid.util.UniqueRandomListMaker;

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
	private static Path WORDLIST = 
			FileSystems.getDefault().getPath("user_lists/NZCER-spelling-lists.txt");

	private static String[] WORDLISTS = FileSystems.getDefault().getPath("user_lists").toFile().list();

	private static final String NZVOICE = "voices/nzvoice.scm";
	private static final String USVOICE = "voices/usvoice.scm";

	/*
	 * These fields refer to the buttons in the GUI.
	 */
	private Button newQuiz;
	private Button viewStatistics;
	private Button addList;
	private Button options;

	/*
	 * This field is a helper object to easily deal with file IO by removing the
	 * need for this object to worry about file  and IO Exceptions. 
	 */
	private IOHelper ioHelper;

	private List<Set<String>> wordlist;
	private List<String> sublists;
	private List<List<String>> masteredList;
	private List<List<String>> failedList;

	private String currentSubList;

	//currentSpeech is just the text on the combobox of the current voice
	private String speechScript;
	private String currentSpeech;

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {

		this.primaryStage = primaryStage;

		ioHelper = new IOHelper();

		createWordList();
		createStatsLists();

		speechScript = NZVOICE;
		currentSpeech = "NZ voice";

		newQuiz = new Button("New Quiz");
		viewStatistics = new Button("View Statistics");
		addList = new Button("Add List");
		options = new Button("Options");

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(50));
		grid.setVgap(5);
		grid.add(newQuiz, 0, 0);
		grid.add(viewStatistics, 0, 1);
		grid.add(addList, 0, 2);
		grid.add(options, 0, 3);
		grid.setAlignment(Pos.CENTER);

		// This simply adds this object as a listener for these buttons
		// and adds all the buttons to the frame.
		Button[] buttons = {newQuiz, viewStatistics, addList, options};

		for (Button btn : buttons){
			btn.setOnAction(this);
			btn.setPrefWidth(400);
			btn.setPrefHeight(100);
			btn.setFont(new Font(16));
		}

		Label title = new Label("Welcome to VOXSPELL!");
		title.setFont(new Font("Abyssinica SIL", 16));

		FlowPane flow = new FlowPane(title);
		flow.setAlignment(Pos.CENTER);

		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		root.setCenter(grid);
		root.setTop(flow);
		root.setPrefSize(AppDim.WIDTH, AppDim.HEIGHT);

		Scene scene = new Scene(root);
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
		} else if (e.getSource() == addList) {
			addList();
		} else if (e.getSource() == options) {
			displayOptionsWindow();
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
			alert.setContentText("Please provide a list "
					+ "in the working directory called \"NZCER-spelling-lists"
					+ ".txt\" which provides a list of words that can be "
					+ "tested");
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

		/*
		 * The Quiz class, which creates a GUI to run the quiz is being
		 * implemented by an anonymous class that implements the "New Quiz"
		 * functionality. Some methods are overridden so that words can be
		 * written to the corresponding files.
		 */
		Application newQuiz = new Quiz("New Quiz", testList, speechScript) {

			@Override
			protected void passedFirstTime() {
				masteredList.get(sublists.indexOf(currentSubList)).add(getLastTestedWord());
			}

			@Override
			protected void passedSecondTime() {
				masteredList.get(sublists.indexOf(currentSubList)).add(getLastTestedWord());
			}

			@Override
			protected void failedFirstTime() {}

			@Override
			protected void failedSecondTime() {
				failedList.get(sublists.indexOf(currentSubList)).add(getLastTestedWord());
			}

			@Override
			protected void passedQuiz() {}

		};
		try {
			/*primaryStage.hide();
			newQuiz.start(primaryStage);
			primaryStage.centerOnScreen();*/
			newQuiz.start(primaryStage);
		} catch (Exception e) {}
	}

	/*
	 * This method displays the statistics, number of failed, faulted and 
	 * mastered word for each word in the word list.
	 */
	private void displayStatistics() {
		Application displayStatistics = new DisplayStatistics(wordlist,masteredList,failedList,sublists, sublists.indexOf(currentSubList));
		try {
			displayStatistics.start(primaryStage);
		} catch (Exception e) {}
	}

	private void addList() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		fileChooser.setInitialDirectory(FileSystems.getDefault().getPath(".").toFile());
		File listToAdd = fileChooser.showOpenDialog(primaryStage);
		try {
			Files.createSymbolicLink(FileSystems.getDefault().getPath("user_lists/" + listToAdd.getName()),
					FileSystems.getDefault().getPath(listToAdd.getAbsolutePath()));
		} catch (IOException | NullPointerException e) {}
		WORDLISTS = FileSystems.getDefault().getPath("user_lists").toFile().list();
	}

	private void displayOptionsWindow() {
		Application displayOptions =
				new DisplayOptions(WORDLISTS, WORDLIST.toFile().getName(), sublists, currentSubList, currentSpeech) {

			@Override
			protected void changeSpeech(String voice) {
				if (voice.equals("NZ voice")) {
					currentSpeech= "NZ voice";
					speechScript = NZVOICE;
				} else if(voice.equals("USA voice")) {
					currentSpeech="USA voice";
					speechScript = USVOICE;
				}
			}

			@Override
			protected void changeSublist(String sublist) {
				currentSubList = sublist;
			}

			@Override
			protected void changeList(String list, ComboBox<String> sublistCombo) {
				WORDLIST = FileSystems.getDefault().getPath("user_lists/" + list);
				createWordList();
				createStatsLists();
				sublistCombo.setItems(FXCollections.observableList(sublists));
				currentSubList = sublists.get(0);
				sublistCombo.getSelectionModel().select(currentSubList);
			}

		};
		try {
			displayOptions.start(primaryStage);
		} catch (Exception e) {}
	}

	private void createWordList() {
		List<String> unfilteredList = ioHelper.readAllLines(WORDLIST);
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
		currentSubList = sublists.get(0);
	}

	private void createStatsLists() {

		masteredList = new ArrayList<List<String>>();
		failedList = new ArrayList<List<String>>();

		for (int i = 0; i < wordlist.size(); i++){
			masteredList.add(new ArrayList<String>());
			failedList.add(new ArrayList<String>());
		}
	}

	/*
	 * This method simply tells the Event Dispatch Thread to create and show the
	 * GUI.
	 */
	public static void main(String[] args) {
		launch(args);
	}


}
