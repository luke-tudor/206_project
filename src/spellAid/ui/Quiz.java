package spellAid.ui;

import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import spellAid.ui.speaker.AsynchronousComponentEnabler;
import spellAid.ui.speaker.ConcurrentAsynchronousSpeaker;
import spellAid.ui.speaker.Speaker;
import spellAid.ui.video.VideoPanel;

/**
 * This class creates and runs the testing frame that the user sees when the 
 * "New Quiz" button is pressed. This class provides three JPanels, a graphical 
 * output panel, an input panel that contains a JButton and JTextField and a 
 * panel which displays a video player.
 * 
 * The methods in this class are in the order that these methods are used.
 * 
 * @author Luke Tudor and Aprajit Gandhi
 */
public abstract class Quiz extends Application implements EventHandler<ActionEvent> {

	// These fields represent the different states for the JButton.
	private static final String ENTER = "Enter";
	private static final String BEGIN = "Begin Test";
	private static final String REPEAT = "Repeat word";

	// These fields are the GUI components used to display the test.
	private GraphicsPanel graphicsPanel;
	private FlowPane quizPanel;
	private VideoPanel videoPane;
	private Button repeatButton;
	private Button testButton;
	private TextField textField;

	// These fields are used to run the test.
	private Speaker speaker;
	private AsynchronousComponentEnabler enabledList;
	private String[] testList;
	private int currentTestNum;
	private boolean hasChance;
	private boolean[] testResults;
	
	private Scene scene;
	
	private GraphicsFactory gFac;
	
	private final String windowName;
	
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){

			@Override
			public void handle(WindowEvent e){
				// stop speaking when window is closed
				speaker.sock();
				videoPane.release();
				primaryStage.hide();
			}

		});
		primaryStage.setTitle(windowName);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// This constructor takes two parameters, the name of the JFrame, and the
	// list of words to be tested.
	public Quiz(Scene parent, String frameName, String[] list, String scriptFile) {
		// Creates a JFrame and sets all the GUI fields.
		super();
		
		windowName = frameName;
		
		gFac = new GraphicsFactory();
		
		graphicsPanel = new GraphicsPanel(list.length);

		videoPane = new VideoPanel();
		repeatButton = new Button(REPEAT);
		testButton = new Button(BEGIN);
		textField = new TextField("Enter text here");
		//textField = new TextField("Enter text here", 20);

		// creates a list of components that are disabled asynchronously
		enabledList = new AsynchronousComponentEnabler(new Node[]{
				repeatButton,
				testButton
		}, true);
		speaker = new ConcurrentAsynchronousSpeaker(enabledList, scriptFile) {

			@Override
			protected void asynchronousFinish() {
				// This is invoked when the speaker finishes
				if (currentTestNum == testList.length){
					quizComplete();
				}
			}

		};
		// Sets all the testing fields.
		testList = list;
		currentTestNum = 0;
		hasChance = true;
		testResults = new boolean[list.length];

		repeatButton.setOnAction(this);
		repeatButton.setDisable(true);

		testButton.setOnAction(this);
		// Configure the button to be a constant size.
		testButton.setPrefWidth(150);

		// Layout the panel which does the testing
		quizPanel = new FlowPane(new Node[]{textField, repeatButton, testButton});
		quizPanel.setAlignment(Pos.CENTER);
		quizPanel.setPadding(new Insets(5));
		quizPanel.setHgap(5);
		
		// Layout panels
		BorderPane internalPanel = new BorderPane();
		internalPanel.setTop(videoPane);
		internalPanel.setBottom(quizPanel);

		// Add borders to panels
		//videoPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		//graphicsPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

		BackButton back = new BackButton();
		back.setOnAction(e -> primaryStage.setScene(parent));
		
		HBox hbox = new HBox(back);
		hbox.setPadding(new Insets(5));
		hbox.setAlignment(Pos.TOP_LEFT);
		
		BorderPane root = new BorderPane();
		root.setRight(internalPanel);
		root.setLeft(graphicsPanel);
		root.setTop(hbox);
		root.setPrefSize(AppDim.WIDTH, AppDim.HEIGHT);
		
		scene = new Scene(root);

		// Select text field
		textField.requestFocus();
		textField.selectAll();
	}

	/*
	 * This method runs every time the button is pressed.
	 */
	@Override
	public final void handle(ActionEvent e) {

		if (e.getSource() == testButton){
			/*
			 * If the button is prompting the user to enter text, this object tests
			 * the word in the JTextField by calling a helper method
			 * isSpelledCorrectly(). There is different behavior if the user failed
			 * the same word previously hence the "hasChance" field.
			 */
			if (testButton.getText().equals(ENTER)) {

				if (hasChance){

					if (isSpelledCorrectly()){
						firstTimePass();
					} else {
						firstTimeFailure();
					}

				} else {

					if (isSpelledCorrectly()){
						secondTimePass();
					} else {
						secondTimeFailure();
					}

				}

				textField.requestFocus();
				textField.selectAll();

				// If we have run out of words to test, the button is disabled.
				if (currentTestNum == testList.length){
					enabledList.disableAllComponents();
					enabledList.setShouldComponentBeEnabled(repeatButton, false);
					enabledList.setShouldComponentBeEnabled(testButton, false);
				}

				/*
				 * If the button is not prompting the user to enter text, the button
				 * speaks the word that is to be tested next, and then changes the
				 * button so that the user is now prompted to enter the answer.
				 */
			} else {
				testButton.setText(ENTER);
				enabledList.setShouldComponentBeEnabled(repeatButton, true);
				speaker.speak(testList[currentTestNum]);
			}
		} else {
			repeatButton.setDisable(true);
			enabledList.setShouldComponentBeEnabled(repeatButton, false);
			speaker.speak(testList[currentTestNum] + "... " +
					testList[currentTestNum]);
		}
	}

	/*
	 * Helper method for subclasses to find out which word was last tested.
	 */
	protected String getLastTestedWord() {
		return testList[currentTestNum];
	}
	
	protected abstract void passedFirstTime();
	
	protected abstract void failedFirstTime();
	
	protected abstract void passedSecondTime();
	
	protected abstract void failedSecondTime();
	
	protected abstract void passedQuiz();
	
	/*
	 * Invoked when the quiz is complete
	 */
	private void quizComplete() {

		// number of correct words
		int numCorrect = 0;
		for (boolean isCorrect : testResults) {
			if (isCorrect) {
				numCorrect++;
			}
		}

		if (numCorrect > 8) {
			passedQuiz();
			
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			
			ButtonType yes = new ButtonType("Yes");
			ButtonType no = new ButtonType("No");
			
			alert.getButtonTypes().setAll(yes, no);
			alert.setTitle("Test Results");
			alert.setContentText("Well done!"
					+ " You got " + numCorrect + " out of " 
					+ testResults.length + ".\nWould you like to see a "
					+ "video?");
			
			// Asks if user wants a video
			Optional<ButtonType> reply = alert.showAndWait();
			
			// if yes, then play it
			if (reply.get() == yes) {
				videoPane.start();
			}

		} else {
			// tells user how many they got correct
			
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			
			alert.setTitle("Test Results");
			alert.setContentText("Too bad, you only got "
					+ numCorrect + " out of " + testResults.length + ".");
			
			alert.showAndWait();
		}

	}

	/*
	 * These next four methods are similar but perform different tasks
	 * depending on how the user answered the question, and how many tries
	 * the user needed. In all cases, the state of the quiz object is updated.
	 * This is either done by incrementing the test number or removing a chance
	 * the user had for answering the question.
	 */

	// If user passed first time, tell the user they are correct and display
	// a green graphic with the word underneath. Green means pass.
	private void firstTimePass(){
		passedFirstTime();
		graphicsPanel.updateGraphic(currentTestNum, gFac.getNewTickShape(),
				testList[currentTestNum]);
		testResults[currentTestNum] = true;
		currentTestNum++;
		enabledList.setShouldComponentBeEnabled(repeatButton, true);
		textField.clear();
		if (currentTestNum != testList.length)
			speaker.speak("correct... please spell " + testList[currentTestNum]);
		else
			speaker.speak("correct");
	}

	// If a user failed the first time, give them another chance to answer
	// correctly.
	private void firstTimeFailure(){
		failedFirstTime();
		speaker.speak("incorrect, try once more. " + testList[currentTestNum] +
				". " + testList[currentTestNum]);
		hasChance = false;
	}

	// If a user failed, then passed the same word, display a yellow graphic.
	// Yellow means fault.
	private void secondTimePass(){
		passedSecondTime();
		enabledList.setShouldComponentBeEnabled(repeatButton, false);
		repeatButton.setDisable(true);
		speaker.speak("correct");
		graphicsPanel.updateGraphic(currentTestNum, gFac.getNewTickShape(),
				testList[currentTestNum]);
		testResults[currentTestNum] = true;
		currentTestNum++;
		hasChance = true;
		//speaker.speak(testList[currentTestNum]);
	}

	// If a user failed both times, display a red graphic. Red means fail.
	private void secondTimeFailure(){
		failedSecondTime();
		enabledList.setShouldComponentBeEnabled(repeatButton, false);
		repeatButton.setDisable(true);
		textField.clear();
		speaker.speak("incorrect");
		graphicsPanel.updateGraphic(currentTestNum, gFac.getNewCrossShape(),
				testList[currentTestNum]);
		testResults[currentTestNum] = false;
		currentTestNum++;
		hasChance = true;
		//speaker.speak(testList[currentTestNum]);
	}

	/*
	 * Helper method to verify that the user spelled the word correctly
	 * regardless of spelling.
	 */
	private boolean isSpelledCorrectly(){
		return textField.getText().toLowerCase().equals(
				testList[currentTestNum].toLowerCase());
	}
}