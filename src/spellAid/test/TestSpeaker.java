package spellAid.test;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.stage.Stage;
import spellAid.ui.speaker.AsynchronousComponentEnabler;
import spellAid.ui.speaker.ConcurrentAsynchronousSpeaker;

public class TestSpeaker extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		ConcurrentAsynchronousSpeaker speaker =
				new ConcurrentAsynchronousSpeaker(new AsynchronousComponentEnabler(new Node[0], false), "voices/nzvoice.scm") {

			@Override
			protected void asynchronousFinish() {
				System.exit(0);
			}

		};

		speaker.speak("Hello, World");
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
