package com.scgallagher.reactiondiffusion;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUI extends Application {

	private ImageView imgView;
	private Player player;
	
	private float feedRate;
	private float killRate;
	
	private DiffusionGenerator generator;
	
	public GUI() {
		
	}
	
	public GUI(ImageView imgView, Player player, float feedRate, float killRate, DiffusionGenerator generator) {

		this.imgView = imgView;
		this.player = player;
		
		this.feedRate = feedRate;
		this.killRate = killRate;
		
		this.generator = generator;
		
	}
	
	@Override
	public void start(Stage stage) throws Exception {

		HBox container = new HBox();
	    Pane viewport = new Pane();
	    VBox controls = new VBox();

	    Button btnStep = new Button("Step");
	    Button btnPlay = new Button("Play");
	    Button btnStop = new Button("Stop");
	    Slider sldrFeedRate = new Slider(0, 0.1, feedRate);
	    Slider sldrKillRate = new Slider(0, 0.08, killRate);

	    btnStep.setOnAction(new EventHandler<ActionEvent>(){
	    	
	    	@Override
	    	public void handle(ActionEvent e){
	    		imgView.setImage(generator.step());
	    	}
	    	
	    });

	    btnPlay.setOnAction(new EventHandler<ActionEvent>(){
	      @Override
	      public void handle(ActionEvent e){
	        player.setPlaying(true);;
	        System.out.println("Playing");
	      }
	    });

	    btnStop.setOnAction(new EventHandler<ActionEvent>(){
	      @Override
	      public void handle(ActionEvent e){
	        player.setPlaying(false);;
	        System.out.println("Stopped");
	      }
	    });

	    sldrFeedRate.valueProperty().addListener(new ChangeListener<Number>() {
	      public void changed(ObservableValue<? extends Number> ov,
	        Number old_val, Number new_val) {
	          feedRate = (float) sldrFeedRate.getValue();
	      }
	    });

	    sldrKillRate.valueProperty().addListener(new ChangeListener<Number>() {
	      public void changed(ObservableValue<? extends Number> ov,
	        Number old_val, Number new_val) {
	          killRate = (float) sldrKillRate.getValue();
	      }
	    });

	    imgView.fitWidthProperty().bind(viewport.widthProperty());
	    imgView.fitHeightProperty().bind(viewport.heightProperty());
	    Scene scene = new Scene(container, 1200, 540);

	    scene.getStylesheets().add("styles/ui.css");

	    container.getStyleClass().add("hbox");
	    viewport.getStyleClass().add("viewport");
	    controls.getStyleClass().add("controls");

	    viewport.getChildren().add(imgView);
	    container.getChildren().addAll(viewport, controls);
	    controls.getChildren().addAll(btnPlay, btnStop, btnStep, sldrFeedRate,
	      sldrKillRate);

	    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent e) {
	              player.setPlaying(false);;
	              player.setPlaying(false);;
	              //System.out.println("Closing...");
	          }
	    });
	    stage.setTitle("Reaction Diffusion");
	    stage.setScene(scene);
	    stage.show();

	}

}
