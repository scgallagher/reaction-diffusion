package com.scgallagher.reactiondiffusion;

import java.net.URL;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUI extends Application {
	
	public GUI() {
		
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(new URL("file:////C:/Users/bre54621/workspace/reaction-diffusion/src/test.fxml"));
		VBox vbox = loader.<VBox>load();
		
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		stage.setTitle("Test App");
		stage.show();

	}

}
