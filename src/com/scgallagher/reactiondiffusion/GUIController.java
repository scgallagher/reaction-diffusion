package com.scgallagher.reactiondiffusion;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUIController {
	
	public Stage stage = null;
	public Scene scene = null;
	public HBox container = null;
	public Pane viewport = null;
	public VBox controls = null;
	public ImageView imgView = null;
	public Slider sliderFeedRate = null;
	public Slider sliderKillRate = null;
	
	public Label labelFeedRate = null;
	public Label labelKillRate = null;
	
	private DiffusionGenerator generator;
	private Player player;
	
	public GUIController() {
		
 		
		
	}
	
	@FXML
	public void initialize() {
		
		int resX = 240, resY = 135;
		float feedRate = 0.0545f;
 		float killRate = 0.062f;
		float diffusionRateA = 1.0f;
 		float diffusionRateB = 0.5f;
 		
		generator = new DiffusionGenerator(resX, resY, diffusionRateA, diffusionRateB,
 				feedRate, killRate);
 		player = new Player("player", imgView, generator);
 		player.start();
 		
 		sliderFeedRate.valueProperty().addListener(new ChangeListener<Number>() {
		      public void changed(ObservableValue<? extends Number> ov,
		        Number old_val, Number new_val) {
		          float feedRate = (float) sliderFeedRate.getValue();
		          generator.setFeedRate(feedRate);
		      }
		    });

		sliderKillRate.valueProperty().addListener(new ChangeListener<Number>() {
		      public void changed(ObservableValue<? extends Number> ov,
		        Number old_val, Number new_val) {
		          float killRate = (float) sliderKillRate.getValue();
		          generator.setKillRate(killRate);
		      }
		});
		
	}
	
	@FXML
	public void buttonStepAction(Event e) {
		
		imgView.setImage(generator.step());
		
	}
	
	@FXML
	public void buttonPlayAction(Event e) {
		
		player.setPlaying(true);
		System.out.println("Playing");
		
	}
	
	@FXML
	public void buttonStopAction(Event e) {
		
		player.setPlaying(false);
		System.out.println("Stopped");
		
	}
	
	@FXML
	public void stageClosed(Event e) {
		
		player.stop();
		System.out.println("Closing");
		
	}
	
}
