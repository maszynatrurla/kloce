package pl.maszynatrurla.kloce.res;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class Graphics
{

    public static final BufferedImage robotFront;
    public static final BufferedImage robotLeft;
    public static final BufferedImage robotRight;
    public static final BufferedImage robotBack;
    public static final BufferedImage battery;
    
    static 
    {
        
        BufferedImage imageFromFile = null;
        
        try
        {
            imageFromFile = ImageIO.read(Graphics.class.getResource("front.png"));
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        
        robotFront = imageFromFile;
        
        try
        {
            imageFromFile = ImageIO.read(Graphics.class.getResource("leftside.png")); 
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        
        robotLeft = imageFromFile;
        
        try
        {
            imageFromFile = ImageIO.read(Graphics.class.getResource("rightside.png"));
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        
        robotRight = imageFromFile;
        
        try
        {
            imageFromFile = ImageIO.read(Graphics.class.getResource("back.png"));     
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        
        robotBack = imageFromFile;
        
        try
        {
            imageFromFile = ImageIO.read(Graphics.class.getResource("batt.png"));     
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        
        battery = imageFromFile;
    }
}
