package com.nwpi;

import java.io.IOException;

import com.nwpi.view.SynopAnalizerController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SynopAnalizer extends Application {
	
	private final String WINDOW_TITLE = "Synop Analizer";
	
	private Stage primaryStage;
	private AnchorPane mainLayout;

	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(WINDOW_TITLE);
		
		initRootLayout();
	}
	
	private void initRootLayout() {
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SynopAnalizer.class.getResource("view/SynopAnalizer.fxml"));
            mainLayout = (AnchorPane)loader.load();
            
            SynopAnalizerController controller = (SynopAnalizerController)loader.getController();
            controller.setStage(primaryStage);
            
            primaryStage.setOnCloseRequest(e -> Platform.exit());
            
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
