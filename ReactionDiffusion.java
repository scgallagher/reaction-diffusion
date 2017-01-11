import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;
import java.nio.ByteBuffer;

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
public class ReactionDiffusion extends Application {

  private static float[][] grid;
  private static float[][] laplace = {{0.05f, 0.2f, 0.05f}, {0.2f, -1.0f, 0.2f},
    {0.05f, 0.2f, 0.05f}};

  private static int resX = 240, resY = 135;

  private static float feedRate = 0.0367f;
  private static float killRate = 0.0649f;
  private static float diffusionRateA = 1.0f;
  private static float diffusionRateB = 1.0f;

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

  public void start(Stage stage){

    //StackPane pane = new StackPane();
    HBox container = new HBox();
    Pane viewport = new Pane();
    VBox controls = new VBox();
    // Image img = new Image("star_wars.jpg");
    // ImageView imgView = new ImageView(img);
    ImageView imgView = new ImageView();
    Button btnStep = new Button("Step");
    btnStep.setOnAction(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent e){
        //step();
        imgView.setImage(step());
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
    controls.getChildren().addAll(btnStep);

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

  public static void updateImage(BufferedImage image, float B, int x, int y){

    int a = 255;
    int r = (int) (((float) 255) * B);
    int g = (int) (((float) 255) * B);
    int b = (int) (((float) 255) * B);

    int color = (a << 24) | (r << 16) | (g << 8) | b;
    image.setRGB(x, y, color);

  }

  public Image step(){

    BufferedImage img = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_ARGB);

    for(int y = 0; y < resY; y++){
      for(int x = 0; x < resX; x++){
        Results results = calculate(y, x);
        updateImage(img, results.B, x, y);
      }
    }

    return SwingFXUtils.toFXImage(img, null);

  }

  public static Results calculate(int y, int x){

    float B = grid[y][x];
    float A = 1 - B;

    Laplace laplace = getLaplace(x, y);

    float newA = (diffusionRateA * laplace.A) - (A * B * B) + (feedRate * (1 - A));

    float newB = (diffusionRateB * laplace.B) + (A * B * B) - (B * (killRate + feedRate));

    System.out.println("A: " + newA + ", B: " + newB);
    grid[y][x] = newB;

    return new Results(A, B);

  }

  public static Laplace getLaplace(int x, int y){

    float lapA = 0.0f, lapB = 0.0f;

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
        float B = grid[y][x];
        float A = 1 - B;
        lapA += A * laplace[i][j];
        lapB += B * laplace[i][j];
        //System.out.print(grid[row][column] + " ");
      }
      //System.out.println();
    }

    return new Laplace(lapA, lapB);

  }

  public static void initializeGrid(){

    grid = new float[resY][resX];
    for(int i = 0; i < resY; i++){
      for(int j = 0; j < resX; j++){
        if((i > ((resY / 2) - 4) && i < ((resY / 2) + 4))
          && (j > ((resX / 2) - 4) && j < ((resX / 2) + 4)))
          grid[i][j] = 1;
        else
          grid[i][j] = 0;

      }
    }

  }

  public static void main(String[] args){

    //getColor(0x11F12A);
    initializeGrid();
    //Laplace lp = getLaplace(0, 0);
    //System.out.println("Laplace A: " + lp.A + "\nLaplace B: " + lp.B);
    Application.launch(args);

  }

}
