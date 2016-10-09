package spellAid.ui;

import java.nio.file.FileSystems;

import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

public class NewList extends BorderPane {
	
	private FileChooser fileChooser;

	public NewList() {
		fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(FileSystems.getDefault().getPath("").toFile());
		fileChooser.showOpenDialog(null);
	}
}
