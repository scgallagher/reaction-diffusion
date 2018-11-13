package com.scgallagher.reactiondiffusion;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class DiffusionGenerator {

	private int resX, resY;
	
	private float[][] matrixA, matrixB;
	
	private float diffusionRateA, diffusionRateB;
	
	private float feedRate;
	private float killRate;
	
	public float min, max;
	
	private float[][] laplace = {{0.05f, 0.2f, 0.05f}, {0.2f, -1.0f, 0.2f},
		    {0.05f, 0.2f, 0.05f}};
	
	public DiffusionGenerator(int resX, int resY, float diffusionRateA, float diffusionRateB,
			float feedRate, float killRate) {
		
		this.resX = resX;
		this.resY = resY;
		
		this.diffusionRateA = diffusionRateA;
		this.diffusionRateB = diffusionRateB;
		
		this.feedRate = feedRate;
		this.killRate = killRate;
		
		this.min = 0;
		this.max = 0;
		
		this.initializeMatrices();
		
	}
	
	
	public void initializeMatrices(){

	    matrixA = new float[resY][resX];
	    matrixB = new float[resY][resX];
	    int centerX = resX / 2;
	    int centerY = resY / 2;
	    int radius = 10;

	    for(int i = 0; i < resY; i++){
	    	
	    	for(int j = 0; j < resX; j++){
	    		
	    		matrixA[i][j] = 1;
	    		if(((i - centerY)*(i - centerY) + (j - centerX)*(j - centerX)) <= radius * radius) {
	    			matrixB[i][j] = 1;
	    		}
	    		else {
	    			matrixB[i][j] = 0;
	    		}     

	    	}
	    }

	}
	
	public void initializeMatricesOld(){

	    matrixA = new float[resY][resX];
	    matrixB = new float[resY][resX];
	    int sideLengthHalf = 4;

	    for(int i = 0; i < resY; i++){
	    	
	    	for(int j = 0; j < resX; j++){
	    		
	    		matrixA[i][j] = 1;
	    		if((i > ((resY / 2) - sideLengthHalf) && i < ((resY / 2) + sideLengthHalf))
	    				&& (j > ((resX / 2) - sideLengthHalf) && j < ((resX / 2) + sideLengthHalf)))
	    			matrixB[i][j] = 1;
	    		else
	    			matrixB[i][j] = 0;

	    	}
	    }

	}
	
	public Result getLaplace(int x, int y){

		float lapA = 0, lapB = 0;

		for(int i = 0; i < 3; i++){
			
			int row = y + (i - 1);
			if(row < 0)
				row = resY - 1;
			else if(row >= resY)
				row = 0;
			
			for(int j = 0; j < 3; j++){
				
				int column = x + (j - 1);
		        if(column < 0)
		        	column = resX - 1;
		        else if(column >= resX)
		        	column = 0;
		        
		        float A = matrixA[row][column];
		        float B = matrixB[row][column];
		        lapA += A * laplace[i][j];
		        lapB += B * laplace[i][j];
		        
			}
		}

		return new Result(lapA, lapB);

	}
	
	public Result calculate(int y, int x){

	    float A = matrixA[y][x];
	    float B = matrixB[y][x];

	    Result laplaceResult = getLaplace(x, y);

	    float newA = A + ((diffusionRateA * laplaceResult.A) - (A * B * B) + (feedRate * (1 - A)));
	    if(newA < 0)
	      newA = 0;

	    float newB = B + ((diffusionRateB * laplaceResult.B) + (A * B * B) - (B * (killRate + feedRate)));
	    if(newB < 0)
	      newB = 0;

	    matrixA[y][x] = newA;
	    matrixB[y][x] = newB;

	    return new Result(newA, newB);

	  }
	
	public Image step(){

		BufferedImage img = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_ARGB);
		getMinMax();

		for(int y = 0; y < resY; y++){	
			for(int x = 0; x < resX; x++){
				
				Result result = calculate(y, x);
				updateImage(img, result.A, result.B, x, y);

			}
		}

		return SwingFXUtils.toFXImage(img, null);

	}
	
	public void getMinMax() {

		for (int i = 0; i < matrixB.length; i++) {
			for (int j = 0; j < matrixB.length; j++) {

				if (matrixB[i][j] < min) {
					min = matrixB[i][j];
				}
				if (matrixB[i][j] > max) {
					max = matrixB[i][j];
				}

			}
		}

	}
	
	public void updateImage(BufferedImage image, float A, float B, int x, int y){

		int alpha = 255, r = 0, g = 0, b = 0;
	    float col = 0;

	    // Normalize B to make it always between 0 and 1
	    B = (B - min) / max;
	    col = 255 - B * 255;

	    r = (int) col;
	    g = (int) col;
	    b = (int) col;

	    int color = (alpha << 24) | (r << 16) | (g << 8) | b;
	    image.setRGB(x, y, color);

	  }
	
}
