package com.scgallagher.reactiondiffusion;

import java.io.FileOutputStream;
import java.io.IOException;

public class Utilities {

	public static String getColor(int current){

		int r = (current & 0xFF0000) >> 16;
	    int g = (current & 0x00FF00) >> 8;
	    int b = (current & 0x0000FF);

	    StringBuilder sb = new StringBuilder();
	    sb.append(Integer.toString(r) + " ");
	    sb.append(Integer.toString(g) + " ");
	    sb.append(Integer.toString(b));
	    
	    return sb.toString();

	  }
	
	public static void writeValues(FileOutputStream file, float a, float b){

	    try{
	    	file.write(Float.toString(a).getBytes());
	    	file.write(' ');
	    	file.write(Float.toString(b).getBytes());
	    	file.write('\n');
	    } 
	    catch (IOException e){
	      System.out.println("Error writing value to file");
	    }

	}
	
}
