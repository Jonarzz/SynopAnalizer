package com.nwpi.view;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import com.nwpi.SingleFileHandler;
import com.nwpi.SynopProcessor;
import com.nwpi.synop.Synop;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SynopAnalizerController {
	
	@FXML
	private Button analizeFileButton;
	@FXML
	private Button analizeFoldersButton;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private Label progressLabel;
	@FXML
	private Button cancelButton;
	
	private Stage stage;
	
	private SingleFileHandler sfh;
	
	private File defaultDirectory;
	private File userChosenDirectory;
	
	private ArrayList<File> filesFromDirectory;
	
	private int numberOfFilesToOpen, numberOfOpenedFiles, numberOfNotOpenedFiles;
	
	private boolean cancelled;
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void setInitialDefaultDirectory() {
		defaultDirectory = new File(System.getProperty("user.home") + "/Desktop");
	}
	
	public void setInitialButtonsClickability() {
		analizeFileButton.setDisable(false);
		analizeFoldersButton.setDisable(false);
		cancelButton.setDisable(true);
	}
	
	@FXML
	private void initialize() {		
		analizeFileButton.setOnAction((event) -> {
			openFile();
		});
		
		analizeFoldersButton.setOnAction((event) -> {
			openDirectory();
		});
		
		cancelButton.setOnAction((event) -> {
			cancel();
		});
	}
	
	private void openFile() {
		cancelled = false;
		
		File file = getUserChosenFile();
		
		if (file == null)
			return;

		createAndAnalizeSynop(file);
		
		showFileSummaryDialog();
	}
	
	private File getUserChosenFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Synop file");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Synop files", "*.new"));
		
		setDefaultDirectory();
		fileChooser.setInitialDirectory(defaultDirectory);
		
		userChosenDirectory = fileChooser.showOpenDialog(stage);
		return userChosenDirectory;
	}
	
	private void openDirectory() {	
		cancelled = false;
		
		URI userChosenDirectory = getUserChosenDirectory();
		
		if (userChosenDirectory == null)
			return;

		initializeNumbersForProgressBar(userChosenDirectory);
		startProgressBarThread();

		analizeDirectory(userChosenDirectory);
	}
	
	private URI getUserChosenDirectory() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select directory with Synop files");
		
		setDefaultDirectory();
		directoryChooser.setInitialDirectory(defaultDirectory);
		
		userChosenDirectory = directoryChooser.showDialog(stage);
		return userChosenDirectory.toURI();
	}
	
	private void setDefaultDirectory() {
		if (userChosenDirectory == null)
			return;
		
		if (userChosenDirectory.isDirectory())
			defaultDirectory = userChosenDirectory;
		else
			defaultDirectory = userChosenDirectory.getParentFile();				
	}
	
	private void initializeNumbersForProgressBar() {
		numberOfOpenedFiles = 0;
		numberOfNotOpenedFiles = 0;
		numberOfFilesToOpen = 0;
	}

	private void initializeNumbersForProgressBar(URI userChosenDirectory) {
		initializeNumbersForProgressBar();
		
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
				updateProgress(0, 1);	
				updateMessage("0/" + Integer.toString(numberOfFilesToOpen));
				
				while (numberOfOpenedFiles <= numberOfFilesToOpen) {
					if (cancelled)
						break;
					
					updateProgress(numberOfOpenedFiles, numberOfFilesToOpen);	
					updateMessage(Integer.toString(numberOfOpenedFiles) + "/" + Integer.toString(numberOfFilesToOpen));
				}
				
				updateProgress(0, 1);	
				updateMessage("");
				cancel();
				
		        return null;
			}
		};
		
		progressBar.progressProperty().bind(task.progressProperty());
		progressLabel.textProperty().bind(task.messageProperty());
		
		Thread th = new Thread(task); 
		th.setDaemon(true); 
		th.start(); 	
	}
	
	private void analizeDirectory(URI userChosenDirectory) {
		Task<Void> task = new Task<Void>() {
			protected Void call() throws Exception {	
				analizeFileButton.setDisable(true);
				analizeFoldersButton.setDisable(true);
				cancelButton.setDisable(false);
				
				filesFromDirectory = new ArrayList<File>();
				getFilesFromDirectory(new File(userChosenDirectory).listFiles());
				
				for (File file : filesFromDirectory) {
					if (cancelled)
						break;
					
					while (Thread.activeCount() > 50)
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							numberOfNotOpenedFiles++;
						}
					
					createAndAnalizeSynop(file);
					numberOfOpenedFiles++;
				}

				setInitialButtonsClickability();
				
				if (!cancelled)
					showDirectorySummaryDialog();
								
		        return null;
			}
		};
		
		Thread th = new Thread(task); 
		th.setDaemon(true); 
		th.start();
	}
	
	private void getFilesFromDirectory(File[] directory) {
		for (File file : directory)
			if (file.isDirectory())
				getFilesFromDirectory(file.listFiles());
			else
				filesFromDirectory.add(file);
	}
	
	private void createAndAnalizeSynop(File file) {
		new Thread(new SynopObjectListCreator(file)).start();
	}
	
	private class SynopObjectListCreator implements Runnable {
		
		private File file;
		
		public SynopObjectListCreator(File file) {
			this.file = file;
		}

		public void run() {
			sfh = new SingleFileHandler(file);
			ArrayList<Synop> synopList = sfh.getSynopObjectList();
			SynopProcessor.analizeSynopList(synopList);
		}
		
	}
	
	private void showFileSummaryDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("File analized");
		alert.setHeaderText(null);
		alert.setContentText("Processed the file.");

		alert.showAndWait();
	}

	private void showDirectorySummaryDialog() {		
		Platform.runLater(new Runnable() {
			public void run() {
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
		});
	}
	
	private void cancel() {
		cancelled = true;
		unbindProgress();
		initializeNumbersForProgressBar();
		clearProgress();
		setInitialButtonsClickability();
	}
	
	private void unbindProgress() {
		progressBar.progressProperty().unbind();
		progressLabel.textProperty().unbind();
	}
	
	private void clearProgress() {
		progressBar.setProgress(0);
		progressLabel.setText("");
	}
}
