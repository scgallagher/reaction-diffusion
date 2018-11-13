package com.scgallagher.reactiondiffusion;
import java.io.FileOutputStream;
import java.io.*;

import javafx.application.Application;
import javafx.scene.image.ImageView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReactionDiffusion {

  	public static void main(String[] args){

  		LocalDateTime date = LocalDateTime.now();
 		String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"));

 		FileOutputStream values = null;
 		Boolean debug = false;
	  
 		if(debug){
 			try {
 				values = new FileOutputStream("output/values_" + dateString + ".txt");
 			} 
 			catch (FileNotFoundException e) {
 				System.out.println("File not found");
 			}
 		}

 		int resX = 240, resY = 135;
 		float feedRate = 0.0545f;
 		float killRate = 0.062f;
 		float diffusionRateA = 1.0f;
 		float diffusionRateB = 0.5f;
 		
 		ImageView imgView = new ImageView();
 		DiffusionGenerator generator = new DiffusionGenerator(resX, resY, diffusionRateA, diffusionRateB,
 				feedRate, killRate);

 		Player player = new Player("player1", imgView, generator);
		player.start();

		@SuppressWarnings("unused")
		GUI gui = new GUI(imgView, player, feedRate, killRate, generator);
		Application.launch(GUI.class, args);

		if(debug){
			try{
				values.close();
			} 
			catch (IOException e) {

			}
		}

 	}

}
