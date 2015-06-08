package com.nwpi.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

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
	
	private SynopProcessor processor;
	
	private File defaultDirectory;
	private File userChosenDirectory;
	
	private ArrayList<File> filesFromDirectory;
	private volatile ArrayList<ArrayList<Synop>> allSynopsToAnalize;
	
	private Task<Void> filesTask;
	private Task<Void> dbTask;
	
	private int numberOfSynopListsToProcess, numberOfProcessedSynopLists, filesNotFound;
	
	private boolean cancelled;
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	@FXML
	private void initialize() {			
		setInitialDefaultDirectory();
		setInitialButtonsClickability();
		
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
	
	private void setInitialDefaultDirectory() {
		defaultDirectory = new File(System.getProperty("user.home") + "/Desktop");
	}
	
	private void setInitialButtonsClickability() {
		analizeFileButton.setDisable(false);
		analizeFoldersButton.setDisable(false);
		cancelButton.setDisable(true);
	}
	
	private void openFile() {
		cancelled = false;
		
		File file = getUserChosenFile();
		
		if (file == null)
			return;

		numberOfSynopListsToProcess = 1;
		numberOfProcessedSynopLists = 0;
		filesNotFound = 0;
		startProgressBarThread();
		
		initializeFilesTaskForSingleFile(file);
		Thread fileThread = new Thread(filesTask);
		fileThread.start();
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

	private void initializeNumbersForProgressBar(URI userChosenDirectory) {
		initializeNumbersForProcessing();
		
		File[] files = new File(userChosenDirectory).listFiles();
		for (File f : files)
			if (f.isDirectory())
				numberOfSynopListsToProcess += f.listFiles().length;
			else
				numberOfSynopListsToProcess++;
	}

	private void initializeNumbersForProcessing() {
		filesNotFound = 0;
		numberOfProcessedSynopLists = 0;
		numberOfSynopListsToProcess = 0;
	}
	
	private void startProgressBarThread() {
		Task<Void> task = new Task<Void>() {
			protected Void call() throws Exception {	
				updateProgress(0, 1);	
				updateMessage("0/" + Integer.toString(numberOfSynopListsToProcess));
				
				while (numberOfProcessedSynopLists <= numberOfSynopListsToProcess) {
					if (cancelled)
						break;
					
					updateProgress(numberOfProcessedSynopLists, numberOfSynopListsToProcess);	
					updateMessage(Integer.toString(numberOfProcessedSynopLists) + "/" + Integer.toString(numberOfSynopListsToProcess));
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
		initializeFilesTaskForDirectory(userChosenDirectory);
		initializeDBTask();
		
		Thread filesThread = new Thread(filesTask); 
		Thread dbThread = new Thread(dbTask);
		filesThread.setDaemon(true); 
		dbThread.setDaemon(true);
		
		filesThread.start();
		
		try {
			filesThread.join();
			dbThread.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeFilesTaskForSingleFile(File file) {
		filesTask = new Task<Void>() {
			protected Void call() {				
				setBlockedButtonsClickability();

				try {
					processFile(file);
					numberOfProcessedSynopLists++;

					if (!cancelled)
						showFileSummaryDialog();
				} catch (FileNotFoundException e) {
					filesNotFound++;
				}
				
				if (filesNotFound != 0)
					showFilesNotFoundDialog();
				
				setInitialButtonsClickability();
				
				return null;
			}
		};
	}
	
	private void initializeFilesTaskForDirectory(URI userChosenDirectory) {
		filesTask = new Task<Void>() {
			protected Void call() {	
				setBlockedButtonsClickability();
				
				filesFromDirectory = new ArrayList<File>();
				getFilesFromDirectory(new File(userChosenDirectory).listFiles());
				
				processor = new SynopProcessor();
				allSynopsToAnalize = new ArrayList<ArrayList<Synop>>();
				
				for (File file : filesFromDirectory) {
					if (cancelled)
						break;
					try {
					initializeAllSynops(file);
					} catch (FileNotFoundException e) {
						filesNotFound++;
					}
				}
				
				if (filesNotFound != 0)
					showFilesNotFoundDialog();
				
		        return null;
			}
		};
	}
	
	private void processFile(File file) throws FileNotFoundException {
		processor = new SynopProcessor();
		allSynopsToAnalize = new ArrayList<ArrayList<Synop>>();

		initializeAllSynops(file);
		
		for (ArrayList<Synop> synopList : allSynopsToAnalize)
			processor.sendSynopListToDatabase(synopList);
		
		processor.closeSQLConnection();
	}

	private void initializeAllSynops(File file) throws FileNotFoundException {
		SingleFileHandler sfh = new SingleFileHandler(file);
		allSynopsToAnalize.add(sfh.getSynopObjectList());
	}
	
	private void setBlockedButtonsClickability() {
		analizeFileButton.setDisable(true);
		analizeFoldersButton.setDisable(true);
		cancelButton.setDisable(false);
	}
	
	private void getFilesFromDirectory(File[] directory) {
		for (File file : directory)
			if (file.isDirectory())
				getFilesFromDirectory(file.listFiles());
			else
				filesFromDirectory.add(file);
	}
		
	private void initializeDBTask() {
		dbTask = new Task<Void>() {
			protected Void call() throws Exception {				
				for (ArrayList<Synop> synopList : allSynopsToAnalize) {
					processor.sendSynopListToDatabase(synopList);
					numberOfProcessedSynopLists++;
				}
				
				processor.closeSQLConnection();
				
				setInitialButtonsClickability();
							
				if (!cancelled)
					showDirectorySummaryDialog();
				
				return null;
			}
		};
	}
	
	private void showFileSummaryDialog() {
		Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.initStyle(StageStyle.UTILITY);
				alert.setTitle("File analized");
				alert.setHeaderText(null);
				alert.setContentText("Processed the file.");
		
				alert.showAndWait();
		});
	}

	private void showDirectorySummaryDialog() {		
		Platform.runLater(() -> {
				String dialogText = "Processed " + numberOfProcessedSynopLists + " of " + numberOfSynopListsToProcess + " files.";
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.initStyle(StageStyle.UTILITY);
				alert.setTitle("Folders analized");
				alert.setHeaderText(null);
				
				alert.setContentText(dialogText);
				
				alert.showAndWait();
		});
	}
	
	private void showFilesNotFoundDialog() {
		Platform.runLater(() -> {
				String dialogText = filesNotFound + " files not found.";
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.initStyle(StageStyle.UTILITY);
				alert.setTitle("Files not found");
				alert.setHeaderText(null);
				
				alert.setContentText(dialogText);
				
				alert.showAndWait();
		});
	}
	
	private void cancel() {
		cancelled = true;
		unbindProgress();
		initializeNumbersForProcessing();
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
