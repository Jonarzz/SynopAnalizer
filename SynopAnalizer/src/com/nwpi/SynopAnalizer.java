package com.nwpi;

import java.io.IOException;

import com.nwpi.view.SynopAnalizerController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SynopAnalizer extends Application {
	
	private final String WINDOW_TITLE = "Synop Analizer";
	
	private Stage primaryStage;
	private AnchorPane mainLayout;
	
	private SynopAnalizerController controller;

	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(WINDOW_TITLE);
		this.primaryStage.setResizable(false);
		
		initRootLayout();
	}
	
	private void initRootLayout() {
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SynopAnalizer.class.getResource("view/SynopAnalizer.fxml"));
            mainLayout = (AnchorPane)loader.load();
            
            controller = (SynopAnalizerController)loader.getController();
            controller.setStage(primaryStage);
            
            primaryStage.setOnCloseRequest(e -> Platform.exit());
            
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void stop() {
		controller.cancel();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
