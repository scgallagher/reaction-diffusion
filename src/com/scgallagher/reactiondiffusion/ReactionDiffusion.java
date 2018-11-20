package com.scgallagher.reactiondiffusion;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReactionDiffusion extends Application {
  	
  	@Override
  	public void start(Stage staged) throws Exception {
  		
  		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(new URL("file:////C:/Users/bre54621/workspace/reaction-diffusion/src/gui.fxml"));
		Stage stage = loader.<Stage>load();
		
		GUIController controller = loader.getController();

		Scene scene = controller.scene;
		scene.getStylesheets().add("styles/ui.css");
		
		controller.container.getStyleClass().add("hbox");
	    controller.viewport.getStyleClass().add("viewport");
	    controller.controls.getStyleClass().add("controls");
	    controller.labelFeedRate.getStyleClass().add("labels");
	    controller.labelKillRate.getStyleClass().add("labels");
	    controller.sliderFeedRate.getStyleClass().add("sliders");
	    controller.sliderKillRate.getStyleClass().add("sliders");
		
		stage.setScene(scene);
		stage.setTitle("Reaction Diffusion");
		stage.show();
  		
  	}
	
	public static void main(String[] args) {

		Application.launch();
	

 	}

}
