package com.nwpi.view;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.nwpi.SQLQuerySender;
import com.nwpi.SynopProcessorThread;

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
	
	private final int MAX_NUMBER_OF_THREADS = 5;
	
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
	
	private File defaultDirectory;
	private File userChosenDirectory;
	
	private ExecutorService executor;
	private SQLQuerySender sqlqs;
	
	private Task<Void> filesTask;
	
	private int numberOfSynopListsToProcess, numberOfProcessedSynopLists, filesNotFound;
	
	private boolean cancelled;
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	@FXML
	private void initialize() {			
		executor = Executors.newFixedThreadPool(MAX_NUMBER_OF_THREADS);
		
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
	
	public void cancel() {
		executor.shutdownNow();
		try {
			executor.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sqlqs.closeConnection();
		cancelled = true;
		unbindProgress();
		initializeNumbersForProcessing();
		clearProgress();
		setInitialButtonsClickability();
	}
	
	public void increaseProcessedSynopLists() {
		numberOfProcessedSynopLists++;
	}
	
	public void increaseFilesNotFound() {
		filesNotFound++;
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
	
	private void analizeDirectory(URI userChosenDirectory) {
		initializeFilesTaskForDirectory(userChosenDirectory);

		Thread filesThread = new Thread(filesTask); 
		filesThread.setDaemon(true); 
		
		filesThread.start();
	}
	
	private void initializeFilesTaskForSingleFile(File file) {
		filesTask = new Task<Void>() {
			protected Void call() {	
				setBlockedButtonsClickability();
				
				sqlqs = new SQLQuerySender();

				processFile(file);
				
				executor.shutdown();
				while (!executor.isTerminated()) {}
				sqlqs.closeConnection();
				
				if (filesNotFound != 0)
					showFilesNotFoundDialog();
				
				if (!cancelled)
					showFileSummaryDialog();
				
				setInitialButtonsClickability();
				
		        return null;
			}
		};
	}

	private void initializeFilesTaskForDirectory(URI userChosenDirectory) {
		filesTask = new Task<Void>() {
			protected Void call() {	
				setBlockedButtonsClickability();
				System.out.println(new Date().toString());
				sqlqs = new SQLQuerySender();

				processDirectory(new File(userChosenDirectory).listFiles());
				
				executor.shutdown();
				while (!executor.isTerminated()) {}
				sqlqs.closeConnection();
				System.out.println(new Date().toString());
				if (filesNotFound != 0)
					showFilesNotFoundDialog();
				
				if (!cancelled)
					showDirectorySummaryDialog();
				
				setInitialButtonsClickability();
				
		        return null;
			}
		};
	}
	
	private void processFile(File file) {
		SynopProcessorThread spt = new SynopProcessorThread(file, sqlqs, this);
		executor.execute(spt);
	}
	
	private void processDirectory(File[] directory) {
		for (File file : directory)
			if (file.isDirectory())
				processDirectory(file.listFiles());
			else {
				SynopProcessorThread spt = new SynopProcessorThread(file, sqlqs, this);
				executor.execute(spt);
			}		
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
					updateMessage(numberOfProcessedSynopLists + "/" + numberOfSynopListsToProcess);
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
	
	private void setBlockedButtonsClickability() {
		analizeFileButton.setDisable(true);
		analizeFoldersButton.setDisable(true);
		cancelButton.setDisable(false);
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
		
	private void unbindProgress() {
		progressBar.progressProperty().unbind();
		progressLabel.textProperty().unbind();
	}
	
	private void clearProgress() {
		progressBar.setProgress(0);
		progressLabel.setText("");
	}
}
