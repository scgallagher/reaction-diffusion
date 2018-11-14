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
		


	}

}
