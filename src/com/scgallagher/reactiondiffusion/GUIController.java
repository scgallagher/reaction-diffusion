package com.scgallagher.reactiondiffusion;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUIController {
	
	public HBox container = null;
	public Pane viewport = null;
	public VBox controls = null;
	
	public GUIController() {
		
		setTitle("hi");
		
	}
	
	public void setTitle(String title) {
		
		Stage stage = (Stage) container.getScene().getWindow();
		stage.setTitle(title);
		
	}
	
}
