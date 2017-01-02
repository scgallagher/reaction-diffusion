import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ReactionDiffusion {

  public static void main(String[] args){

    int imgWidth = 1920;
    int imgHeight = 1080;

    BufferedImage img = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
    int r = 232, g = 12, b = 12, a = 255;
    // bit pattern: |8xalpha|8xred|8xgreen|8xblue|
    int color = (a << 24) | (r << 16) | (g << 8) | b;

    for(int y = 0; y < imgHeight; y++){
      for(int x = 0; x < imgWidth; x++){
        img.setRGB(x, y, color);
      }
    }

    File file = new File("out.png");
    try{
      ImageIO.write(img, "PNG", file);
    } catch(IOException e){
      System.out.println("Unexpected error writing file");
    }

  }

}
