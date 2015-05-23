package com.nwpi.view;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import com.nwpi.SingleFileHandler;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// TODO add Cancel button
public class SynopAnalizerController {
	
	@FXML
	private Button analizeFileButton;
	@FXML
	private Button analizeFoldersButton;
	@FXML
	private ProgressBar progressBar;
	
	private Stage stage;
	
	private SingleFileHandler sfh;
	
	private int numberOfFilesToOpen, numberOfOpenedFiles, numberOfNotOpenedFiles;
	
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
		
		showFileSummaryDialog();
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
		URI userChosenDirectory = getUserChosenDirectory();

		initializeNumbersForProgressBar(userChosenDirectory);
		startProgressBarThread();

		analizeDirectory(userChosenDirectory);
	}
	
	private URI getUserChosenDirectory() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select directory with Synop files");
		
		File defaultDirectory = new File(System.getProperty("user.home") + "/Desktop");
		directoryChooser.setInitialDirectory(defaultDirectory);
		
		return directoryChooser.showDialog(stage).toURI();
	}
	
	private void initializeNumbersForProgressBar(URI userChosenDirectory) {
		numberOfOpenedFiles = 0;
		numberOfNotOpenedFiles = 0;
		numberOfFilesToOpen = 0;
		
		File[] files = new File(userChosenDirectory).listFiles();
		for (File f : files)
			if (f.isDirectory())
				numberOfFilesToOpen += f.listFiles().length;
			else
				numberOfFilesToOpen++;
	}
	
	private void startProgressBarThread() {
		Task<Void> task = new Task<Void>() {
			protected Void call() throws Exception {				
				while (numberOfOpenedFiles <= numberOfFilesToOpen)
					updateProgress(numberOfOpenedFiles, numberOfFilesToOpen);		
				
		        return null;
			}
		};
		
		progressBar.progressProperty().bind(task.progressProperty());
		Thread th = new Thread(task); 
		th.setDaemon(true); 
		th.start(); 	
	}
	
	private void analizeDirectory(URI userChosenDirectory) {
		Task<Void> task = new Task<Void>() {
			protected Void call() throws Exception {	
				analizeFileButton.setDisable(true);
				analizeFoldersButton.setDisable(true);
				
				try {
					Files.walk(Paths.get(userChosenDirectory)).forEach(filePath -> {
						if (Files.isRegularFile(filePath)) {
							while (Thread.activeCount() > 50)
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									numberOfNotOpenedFiles++;
								}

					    	sfh = new SingleFileHandler(filePath.toFile());
					    	numberOfOpenedFiles++;
					    }
					});
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					analizeFileButton.setDisable(false);
					analizeFoldersButton.setDisable(false);
				}
								
		        return null;
			}
		};
		
		Thread th = new Thread(task); 
		th.setDaemon(true); 
		th.start();
	}
	
	private void showFileSummaryDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("File analized");
		alert.setHeaderText(null);
		alert.setContentText("Processed the file.");

		alert.showAndWait();
	}

	// TODO show dialog after analizing is done (threads problems)
	private void showDirectorySummaryDialog() {				
		String dialogText = "Processed " + Integer.toString(numberOfOpenedFiles) + " of " + Integer.toString(numberOfFilesToOpen) + " files.";
		
		if (numberOfNotOpenedFiles > 0)
			dialogText += " Couldn't process " + Integer.toString(numberOfNotOpenedFiles) + " files.";
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("Folders analized");
		alert.setHeaderText(null);
		
		alert.setContentText(dialogText);
		
		alert.showAndWait();
	}
}
