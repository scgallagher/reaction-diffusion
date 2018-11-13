package com.scgallagher.reactiondiffusion;

import javafx.scene.image.ImageView;

public class Player implements Runnable {

	private Thread thread;
    private String name;
    private ImageView imgView;
    
    private boolean running;
    private boolean playing;
    
    private DiffusionGenerator generator;

    public Player(String name, ImageView imgView, DiffusionGenerator generator){
      
    	this.name = name;
    	this.imgView = imgView;
    	this.generator = generator;
    	
    	this.running = true;
    	this.playing = false;
    	
    }
	
    public void initializeGUI() {
    	
    	
    	
    }
    
	@Override
	public void run() {

		while(running){
			
	        //System.out.println("Playing: " + playing);
	        if(playing) {
	          imgView.setImage(generator.step());
	        }
	        else {
	        	
	          try {
	        	  
	            Thread.sleep(50);
	            
	          } catch(InterruptedException e) {
	        	  
	            System.out.println("Thread was interrupted.");
	            
	          }
	          
	        }
	      }

	}
	
	public void start(){

		if(thread == null) {
			
			thread = new Thread(this, name);
	        thread.start();
	        
	    }

	}
	
	public void setRunning(boolean running) {
		
		this.running = running;
		
	}
	
	public void setPlaying(boolean playing) {
		
		this.playing = playing;
		
	}

}
