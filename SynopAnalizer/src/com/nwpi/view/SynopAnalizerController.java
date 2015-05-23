package com.nwpi.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.nwpi.SingleFileHandler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class SynopAnalizerController {
	
	@FXML
	private Button analizeFileButton;
	@FXML
	private Button analizeFoldersButton;
	
	private Stage stage;
	
	private SingleFileHandler sfh;
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	@FXML
	private void initialize() {
		analizeFileButton.setOnAction((event) -> {
			openFile();
		});
		analizeFoldersButton.setOnAction((event) -> {
			openDirectory();
		});
	}
	
	private void openFile() {
		File file = getUserChosenFile();
		
		sfh = new SingleFileHandler(file);
		sfh.getSynopObjectList();
	}
	
	private File getUserChosenFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Synop file");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Synop files", "*.new"));
		
		File defaultDirectory = new File(System.getProperty("user.home") + "/Desktop");
		fileChooser.setInitialDirectory(defaultDirectory);
		
		return fileChooser.showOpenDialog(stage);
	}
	
	private void openDirectory() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select directory with Synop files");
		
		File defaultDirectory = new File(System.getProperty("user.home") + "/Desktop");
		directoryChooser.setInitialDirectory(defaultDirectory);
		
		try {
			Files.walk(Paths.get(directoryChooser.showDialog(stage).toURI())).forEach(file -> {
			    if (Files.isRegularFile(file)) {
			    	sfh = new SingleFileHandler(file.toFile());
					sfh.getSynopObjectList();
			    }
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
