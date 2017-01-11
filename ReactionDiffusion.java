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
        imgView.setImage(SwingFXUtils.toFXImage(createImage(), null));
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

  // public static BufferedImage createImageTest(){
  //
  //   int imgWidth = 1920;
  //   int imgHeight = 1080;
  //
  //   BufferedImage img = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
  //   int rgb = 0xFFFFFF;
  //   ByteBuffer buf = ByteBuffer.allocate(4);
  //   buf.putInt(rgb);
  //   System.out.println(rgb);
  //   int a = 255;
  //   int r = (int) buf.get(1);
  //   int g = (int) buf.get(2);
  //   int b = (int) buf.get(3);
  //   System.out.println("r: " + r + "\ng: " + g + "\nb: " + b);
  //   // bit pattern: |8xalpha|8xred|8xgreen|8xblue|
  //   int color = (a << 24) | (r << 16) | (g << 8) | b;
  //   Random rnd = new Random();
  //   for(int y = 0; y < imgHeight; y++){
  //     for(int x = 0; x < imgWidth; x++){
  //       img.setRGB(x, y, color);
  //     }
  //   }
  //
  //   return img;
  //
  // }

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

    int imgWidth = 1920;
    int imgHeight = 1080;

    BufferedImage img = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
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
    for(int y = 0; y < imgHeight; y++){
      for(int x = 0; x < imgWidth; x++){
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

  public Image step(){

    int imgWidth = 1920;
    int imgHeight = 1080;

    BufferedImage img = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);

    // Set colorA to white
    int r = 255, g = 255, b = 255, a = 255;
    int colorA = (a << 24) | (r << 16) | (g << 8) | b;

    // Set colorB to black
    r = 0; g = 0; b = 0; a = 255;
    int colorB = (a << 24) | (r << 16) | (g << 8) | b;

    for(int y = 0; y < imgHeight; y++){
      for(int x = 0; x < imgWidth; x++){

      }
    }

    return SwingFXUtils.toFXImage(img, null);

  }

  public static void main(String[] args){

    getColor(0x11F12A);
    grid = new float[1920][1080];
    Application.launch(args);

  }

}
