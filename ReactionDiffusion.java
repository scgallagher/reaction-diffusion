import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Random;
import java.nio.ByteBuffer;
import java.lang.Float;
import java.lang.Math;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.WindowEvent;
import javafx.scene.control.Slider;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReactionDiffusion extends Application {

  public static ImageView imgView = new ImageView();

  private static float[][] matrixA, matrixB;
  private static float[][] laplace = {{0.05f, 0.2f, 0.05f}, {0.2f, -1.0f, 0.2f},
    {0.05f, 0.2f, 0.05f}};

  // Image resolution
  //private static int resX = 960, resY = 540;
  private static int resX = 240, resY = 135;

  // private static float feedRate = 0.0367f;
  // private static float killRate = 0.0649f;
  private static float feedRate = 0.0545f;
  private static float killRate = 0.062f;
  private static float diffusionRateA = 1.0f;
  private static float diffusionRateB = 0.5f;

  private static boolean playing = false;
  private static boolean running = true;

  private static Player player;

  private static FileOutputStream values;
  private static Boolean debug = false;

  public static float min = 0, max = 0;

  public static class Laplace {

    public float A;
    public float B;

    public Laplace(float A, float B){
      this.A = A;
      this.B = B;
    }

  }

  public static class Results {

    public float A;
    public float B;

    public Results(float A, float B){
      this.A = A;
      this.B = B;
    }

  }

  public static class Player implements Runnable {

    private Thread thread;
    private String name;
    private ImageView imgView;

    public Player(String name, ImageView imgView){
      this.name = name;
      this.imgView = imgView;
    }

    public void run(){
      while(running){
        //System.out.println("Playing: " + playing);
        if(playing){
          imgView.setImage(step());
        }
        else{
          try {
            Thread.sleep(50);
          }catch(InterruptedException e){
            System.out.println("Thread was interrupted.");
          }
        }
      }
    }

    public void start(){

      if(thread == null){
        thread = new Thread(this, name);
        thread.start();
      }

    }

  }

  public void start(Stage stage){

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
        imgView.setImage(step());
      }
    });

    btnPlay.setOnAction(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent e){
        playing = true;
        System.out.println("Playing");
      }
    });

    btnStop.setOnAction(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent e){
        playing = false;
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
              playing = false;
              running = false;
              //System.out.println("Closing...");
          }
    });
    stage.setTitle("Reaction Diffusion");
    stage.setScene(scene);
    stage.show();

  }

  public static int getColor(int current){

    int r = (current & 0xFF0000) >> 16;
    //System.out.println("r: " + r);
    int g = (current & 0x00FF00) >> 8;
    //System.out.println("g: " + g);
    int b = (current & 0x0000FF);
    //System.out.println("b: " + b);

    return 0;

  }

  public BufferedImage createImage(){

    BufferedImage img = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_ARGB);
    int r = 232, g = 12, b = 12, a = 255;
    // bit pattern: |8xalpha|8xred|8xgreen|8xblue|
    int color = (a << 24) | (r << 16) | (g << 8) | b;

    // Set colorA to white
    r = 255; g = 255; b = 255; a = 255;
    int rgb = 0xFFFFFF;
    int colorA = (a << 24) | (r << 16) | (g << 8) | b;

    // Set colorB to black
    r = 0; g = 0; b = 0; a = 255;
    int colorB = (a << 24) | (r << 16) | (g << 8) | b;

    Random rnd = new Random();
    for(int y = 0; y < resY; y++){
      for(int x = 0; x < resX; x++){
        if(rnd.nextFloat() < .5){
          img.setRGB(x, y, colorA);
        }
        else {
          img.setRGB(x, y, colorB);
        }

      }
    }

    File file = new File("out.png");
    try{
      ImageIO.write(img, "PNG", file);
    } catch(IOException e){
      System.out.println("Unexpected error writing file");
    }

    return img;

  }

  public static void writeValues(float a, float b){

    try{
      values.write(Float.toString(a).getBytes());
      values.write(' ');
      values.write(Float.toString(b).getBytes());
      values.write('\n');
    } catch (IOException e){
      System.out.println("Error writing value to file");
    }

  }

  public static void updateImage(BufferedImage image, float A, float B, int x, int y){

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

  public static void getMinMax() {

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

  public static Image step(){

    //System.out.print("Processing step...");
    BufferedImage img = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_ARGB);
    getMinMax();

    for(int y = 0; y < resY; y++){
      for(int x = 0; x < resX; x++){
        Results results = calculate(y, x);
        updateImage(img, results.A, results.B, x, y);
        //updateImageBW(img, results.B, x, y);
      }
    }

    //System.out.println("Done");
    // float A = matrixA[0][0];
    // float B = matrixB[0][0];
    // System.out.println("\tA: " + (1 - B) + " B: " + B);
    //System.out.println("Step complete");
    return SwingFXUtils.toFXImage(img, null);

  }

  public static Results calculate(int y, int x){

    float A = matrixA[y][x];
    float B = matrixB[y][x];

    Laplace laplace = getLaplace(x, y);

    float newA = A + ((diffusionRateA * laplace.A) - (A * B * B) + (feedRate * (1 - A)));
    if(newA < 0)
      newA = 0;

    float newB = B + ((diffusionRateB * laplace.B) + (A * B * B) - (B * (killRate + feedRate)));
    if(newB < 0)
      newB = 0;

    // if(B >= 1)
    //   System.out.println("A: " + newA + ", B: " + newB);
    matrixA[y][x] = newA;
    matrixB[y][x] = newB;

    return new Results(newA, newB);

  }

  public static Laplace getLaplace(int x, int y){

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

    //System.out.println(lapA + " " + lapB);
    return new Laplace(lapA, lapB);

  }

  public static void initializeMatrices(){

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

  public static void initializeMatricesNew(){

    matrixA = new float[resY][resX];
    matrixB = new float[resY][resX];
    int centerX = resX / 2;
    int centerY = resY / 2;
    int radius = 10;

    for(int i = 0; i < resY; i++){
      for(int j = 0; j < resX; j++){
        matrixA[i][j] = 1;
        if(((i - centerY)*(i - centerY) + (j - centerX)*(j - centerX)) <=
          radius * radius)
          matrixB[i][j] = 1;
        else
          matrixB[i][j] = 0;

      }
    }

  }

  public static void main(String[] args){

    LocalDateTime date = LocalDateTime.now();
    String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"));
    String valFileName = "values";

    if(debug){
      try{
        values = new FileOutputStream("output/values_" + dateString + ".txt");
      }catch (FileNotFoundException e){
        System.out.println("File not found");
      }
    }
    //System.out.println(dateString);

    initializeMatrices();
    Laplace laplace = getLaplace(117, 64);
    //System.out.println("A: " + matrixA[64][117] + " B: " + matrixB[64][117]);
    //System.out.println(laplace.A + " " + laplace.B);

    player = new Player("player1", imgView);
    player.start();

    Application.launch(args);

    if(debug){
      try{
        values.close();
      } catch (IOException e){

      }
    }

  }

}
