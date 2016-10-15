package spellAid.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import spellAid.util.io.ExtendedIOHelper;
import spellAid.util.string.HiddenFileString;
import spellAid.util.string.URLString;
/**
 * 
 * Displays the statistics window and allows users to select what statistics level they wish to view
 * @author Aprajit Gandhi and Luke Tudor
 *
 */
public class DisplayStatistics extends Application {
	
	private static final String STYLESHEET = new URLString("style/mainstyle.css").getURL();

	private final ComboBox<String> sublistSelectCombo;
	
	private TableView<Row> table;

	private Scene scene;

	private Stage primaryStage;

	private List<Set<String>> wordlist;

	private ExtendedIOHelper ioHelper;

	private String currentList;
	
	private ObservableList<Row> data;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Statistics");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public DisplayStatistics(Scene parent, List<Set<String>> wordlist, List<String> sublists, String currentList, int currentLevel) {
		super();

		this.wordlist = wordlist;
		this.currentList = currentList;

		ioHelper = new ExtendedIOHelper();

		sublistSelectCombo = new ComboBox<>(FXCollections.observableList(sublists));

		table = new TableView<>();

		sublistSelectCombo.setOnAction(e -> {
			if(e.getSource() == sublistSelectCombo){
				updateStatisticsDisplay(sublistSelectCombo.getSelectionModel().getSelectedIndex());
			}
		});

		sublistSelectCombo.getSelectionModel().select(currentLevel);
		updateStatisticsDisplay(currentLevel);

		GridPane controls = new GridPane();
		controls.setHgap(5);
		controls.add(sublistSelectCombo, 0, 0);
		
		TableColumn<Row, String> wordNameCol = new TableColumn<>("Word");
		wordNameCol.setMinWidth(100);
		wordNameCol.setCellValueFactory(new PropertyValueFactory<Row, String>("word"));
		
		TableColumn<Row, String> masteredCol = new TableColumn<>("Mastered");
		masteredCol.setMinWidth(100);
		masteredCol.setCellValueFactory(new PropertyValueFactory<Row, String>("timesMastered"));
		
		TableColumn<Row, String> faultedCol = new TableColumn<>("Faulted");
		faultedCol.setMinWidth(100);
		faultedCol.setCellValueFactory(new PropertyValueFactory<Row, String>("timesFaulted"));
		
		TableColumn<Row, String> failedCol = new TableColumn<>("Failed");
		failedCol.setMinWidth(100);
		failedCol.setCellValueFactory(new PropertyValueFactory<Row, String>("timesFailed"));
		
		table.setPlaceholder(new Label("No statistics to display"));
		table.getColumns().addAll(Arrays.asList(wordNameCol, masteredCol, faultedCol, failedCol));
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5));
		grid.setVgap(5);
		grid.add(controls, 0, 0);
		grid.add(table, 0, 1);
		grid.setAlignment(Pos.CENTER);

		Button back = new BackButton();
		back.setOnAction(e -> primaryStage.setScene(parent));

		HBox hbox = new HBox(back);
		hbox.setPadding(new Insets(5));
		hbox.setAlignment(Pos.TOP_LEFT);

		BorderPane root = new BorderPane();
		root.setTop(hbox);
		root.setCenter(grid);
		root.setPrefSize(AppDim.WIDTH.getValue(), AppDim.HEIGHT.getValue());
		root.getStylesheets().add(STYLESHEET);

		scene = new Scene(root);
	}

	private void updateStatisticsDisplay(int levelToBeShown){
		/*
		 * This is a local class that is used as a helper object to count the 
		 * number of times a word appears in a list.
		 */
		class WordCounter {
			private int wordCount(String word, List<String> list){
				int numWords = 0;
				for (String entry : list){
					if (word.equals(entry)){
						numWords++;
					}
				}
				return numWords;
			}
		}
		
		String hiddenFile = new HiddenFileString(currentList.substring(0, currentList.length() - 4) + "." 
				+ sublistSelectCombo.getSelectionModel().getSelectedItem()).getHiddenFileString();
		
		List<String> masteredList = ioHelper.readAllLines(hiddenFile + ".mastered.txt");
		List<String> faultedList = ioHelper.readAllLines(hiddenFile + ".faulted.txt");
		List<String> failedList = ioHelper.readAllLines(hiddenFile + ".failed.txt");
		
		data = FXCollections.observableArrayList();

		WordCounter wordCounter = new WordCounter();	

		for (String word : wordlist.get(levelToBeShown)){

			int numMastered = wordCounter.wordCount(word, masteredList);
			int numFaulted = wordCounter.wordCount(word, faultedList);
			int numFailed = wordCounter.wordCount(word, failedList);

			/*
			 * Only attempted words are added to the statistics, hence the if
			 * statement that evaluates as false if a word has never been tested.
			 */
			if (numMastered != 0 || numFaulted != 0 || numFailed != 0){
				data.add(new Row(word, numMastered, numFaulted, numFailed));
			}
		}
		table.setItems(data);
	}
	
	public static class Row {
		private SimpleStringProperty word;
		private SimpleStringProperty timesMastered;
		private SimpleStringProperty timesFaulted;
		private SimpleStringProperty timesFailed;
		
		public Row(String word, int timesMastered, int timesFaulted, int timesFailed) {
			this.word = new SimpleStringProperty(word);
			this.timesMastered = new SimpleStringProperty(timesMastered + "");
			this.timesFaulted = new SimpleStringProperty(timesFaulted + "");
			this.timesFailed = new SimpleStringProperty(timesFailed + "");
		}

		public String getWord() {
			return word.get();
		}

		public void setWord(String word) {
			this.word.set(word);
		}
		
		public String getTimesMastered() {
			return timesMastered.get();
		}

		public void setTimesMastered(String timesMastered) {
			this.timesMastered.set(timesMastered);
		}
		
		public String getTimesFaulted() {
			return timesFaulted.get();
		}
		
		public void setTimesFaulted(String timesFaulted) {
			this.timesMastered.set(timesFaulted);
		}

		public String getTimesFailed() {
			return timesFailed.get();
		}
		
		public void setTimesFailed(String timesFailed) {
			this.timesFailed.set(timesFailed);
		}		
	}
}
