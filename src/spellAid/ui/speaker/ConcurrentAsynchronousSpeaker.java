package spellAid.ui.speaker;

import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * This class uses thread worker to speak a particular line without freezing the
 * GUI.
 * 
 * @author Luke Tudor
 *
 */
public abstract class ConcurrentAsynchronousSpeaker implements Speaker {

	private String scriptFile;

	private AsynchronousComponentEnabler componentsList;

	public ConcurrentAsynchronousSpeaker(AsynchronousComponentEnabler list,
			String file) {
		componentsList = list;
		scriptFile = file;
	}

	@Override
	public final void speak(final String line) {
		
		componentsList.disableAllComponents();

		Thread worker = new Thread(new Runnable() {

			@Override
			public void run() {
				// Give line to script
				String cmd = "echo '\"" + line + "\"' | festival -b " 
				+ scriptFile;

				ProcessBuilder processBuilder =
						new ProcessBuilder("bash", "-c", cmd);

				try {

					Process process = processBuilder.start();

					// Wait for festival to finish speaking before continuing.
					final int exitVal = process.waitFor();

					Platform.runLater(new Runnable() {

						@Override
						public void run() {

							// If the bash command failed,
							// then bash or festival is not installed.
							if (exitVal != 0){
								Alert errorMessage = new Alert(Alert.AlertType.ERROR);
								errorMessage.setTitle("Alert!");
								errorMessage.setContentText(
										"Please install bash and/or festival"
												+ " to run this application");
								errorMessage.showAndWait();
							}
							// apply all asynchronously assigned states
							componentsList.applyAllRecommendedStates();
							asynchronousFinish();
						}
					});
				} catch (InterruptedException | IOException e) {}
			}
		});

		worker.setDaemon(true);

		worker.start();
	}

	@Override
	public void sock() {
		try {
			// Interrupt 'aplay' which festival uses to speak
			ProcessBuilder pb =
					new ProcessBuilder("bash", "-c", "killall aplay");
			pb.start();
		} catch (IOException e) {}
	}

	// template method pattern to notify children of finish
	protected abstract void asynchronousFinish();

}
