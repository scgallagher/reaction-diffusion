package com.scgallagher.reactiondiffusion;
import java.io.FileOutputStream;
import java.net.URL;
import java.io.*;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReactionDiffusion extends Application {

	static GUIController controller;
	
	private float feedRate;
	private float killRate;
	
	private HBox container;
    private Pane viewport;
    private VBox controls;

	private Button btnStep;
	private Button btnPlay;
	private Button btnStop;
	private Slider sldrFeedRate;
	private Slider sldrKillRate;
	
	private ImageView imgView;
	private Player player;
	
	private Stage stage;
	
	DiffusionGenerator generator;
	
	public ReactionDiffusion(DiffusionGenerator generator, Player player, float feedRate, float killRate) {
		
		this.generator = generator;
		this.player = player;
		this.feedRate = feedRate;
		this.killRate = killRate;
		
		stage = new Stage();
		container = new HBox();
	    viewport = new Pane();
	    controls = new VBox();
	    
	    imgView = new ImageView();

	    btnStep = new Button("Step");
	    btnPlay = new Button("Play");
	    btnStop = new Button("Stop");
	    sldrFeedRate = new Slider(0, 0.1, feedRate);
	    sldrKillRate = new Slider(0, 0.08, killRate);
	    
	    this.setEventHandlers();
	    this.setStage();
		
	}
	
	public ReactionDiffusion() {}
	
	public void setEventHandlers() {
		
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
		
	}
	
  	public void setStage() {
  		
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
  		
  	}
  	
  	@Override
  	public void start(Stage stage) throws Exception {
  		
  		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(new URL("file:////C:/Users/bre54621/workspace/reaction-diffusion/src/test.fxml"));
		HBox hbox = loader.<HBox>load();
		
		controller = loader.getController();

		Scene scene = new Scene(hbox, 1200, 540);
		scene.getStylesheets().add("styles/ui.css");
		
		controller.container.getStyleClass().add("hbox");
	    controller.viewport.getStyleClass().add("viewport");
	    controller.controls.getStyleClass().add("controls");
		
		stage.setScene(scene);
		stage.setTitle("Reaction Diffusion");
		stage.show();
  		
  	}
	
	public static void main(String[] args) {

		Application.launch();
		
//  	LocalDateTime date = LocalDateTime.now();
// 		String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"));
//
// 		FileOutputStream values = null;
// 		Boolean debug = false;
//	  
// 		if(debug){
// 			try {
// 				values = new FileOutputStream("output/values_" + dateString + ".txt");
// 			} 
// 			catch (FileNotFoundException e) {
// 				System.out.println("File not found");
// 			}
// 		}
//
// 		int resX = 240, resY = 135;
// 		float feedRate = 0.0545f;
// 		float killRate = 0.062f;
// 		float diffusionRateA = 1.0f;
// 		float diffusionRateB = 0.5f;
// 		
//// 		DiffusionGenerator generator = new DiffusionGenerator(resX, resY, diffusionRateA, diffusionRateB,
//// 				feedRate, killRate);
////
//// 		Player player = new Player("player1", imgView, generator);
////		player.start();
//
//		//@SuppressWarnings("unused")
//		//GUI gui = new GUI(imgView, player, feedRate, killRate, generator);
//		Application.launch(args);
//
//		if(debug){
//			try{
//				values.close();
//			} 
//			catch (IOException e) {
//
//			}
//		}

 	}

}
