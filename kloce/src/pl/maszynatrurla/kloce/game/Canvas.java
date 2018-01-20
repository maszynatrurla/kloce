package pl.maszynatrurla.kloce.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


import pl.maszynatrurla.kloce.AppGlobals;
import pl.maszynatrurla.kloce.Image;

public class Canvas extends JPanel 
{
    private static final long serialVersionUID = 1L;
    
    private final AppGlobals app;
    
    public Canvas(final AppGlobals resources)
    {
        this.app = resources;
    }
    
    public void create()
    {
        setBackground(Color.BLACK);
    }
    
    public void setMessage(String msg)
    {
        JOptionPane.showMessageDialog(this, msg);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        final Image image = app.get(Image.class);
        
        if (null == image)
        {
            return;
        }
        
        Rectangle screen = g.getClipBounds();
        //System.out.println("W:" + screen.width + " H:" + screen.height + " X:" + screen.x + " Y:" + screen.y);
        
        int tilesize = (screen.width - 2) / image.getLength();
        if (tilesize > ((screen.height - 2) / image.getHeight()))
        {
            tilesize = (screen.height - 2) / image.getHeight();
        }
        
        g.setColor(Color.WHITE);
        int xoff = screen.width / 2 - ((tilesize * image.getLength()) / 2);
        int yoff = screen.height / 2 - ((tilesize * image.getHeight()) / 2);
                
        for (int yidx = 0; yidx < image.getHeight(); ++yidx)
        {
            for (int xidx = 0; xidx < image.getLength(); ++xidx)
            {
                switch (image.getTile(xidx, yidx))
                {
                case BATTERY:
                    g.setColor(Color.DARK_GRAY);
                    g.fillRoundRect(xoff + xidx * tilesize + 1, yoff + yidx * tilesize + 1
                            , tilesize - 2, tilesize - 2,
                            4, 4);
                    g.drawImage(pl.maszynatrurla.kloce.res.Graphics.battery,
                            xoff + xidx * tilesize + 1,
                            yoff + yidx * tilesize + 1,
                            xoff + xidx * tilesize + tilesize - 1,
                            yoff + yidx * tilesize + tilesize - 1,
                            0, 0, 388, 388, null);
                    break;
                case EMPTY:
                    break;
                case INPUT_A:
                    g.setColor(Color.CYAN);
                    g.fillRoundRect(xoff + xidx * tilesize + 1, yoff + yidx * tilesize + 1
                            , tilesize - 2, tilesize - 2,
                            4, 4);
                    break;
                case INPUT_B:
                    g.setColor(Color.MAGENTA);
                    g.fillRoundRect(xoff + xidx * tilesize + 1, yoff + yidx * tilesize + 1
                            , tilesize - 2, tilesize - 2,
                            4, 4);
                    break;
                case OUTPUT:
                    g.setColor(Color.ORANGE);
                    g.fillRoundRect(xoff + xidx * tilesize + 1, yoff + yidx * tilesize + 1
                            , tilesize - 2, tilesize - 2,
                            4, 4);
                    break;
                case PLATFORM:
                    g.setColor(Color.DARK_GRAY);
                    g.fillRoundRect(xoff + xidx * tilesize + 1, yoff + yidx * tilesize + 1
                            , tilesize - 2, tilesize - 2,
                            4, 4);
                    break;
                case START:
                    g.setColor(Color.GREEN);
                    g.fillRoundRect(xoff + xidx * tilesize + 1, yoff + yidx * tilesize + 1
                            , tilesize - 2, tilesize - 2,
                            4, 4);
                    break;
                case ROBOT_BACK:
                    g.setColor(Color.DARK_GRAY);
                    g.fillRoundRect(xoff + xidx * tilesize + 1, yoff + yidx * tilesize + 1
                            , tilesize - 2, tilesize - 2,
                            4, 4);
                    g.drawImage(pl.maszynatrurla.kloce.res.Graphics.robotBack,
                            xoff + xidx * tilesize + 1,
                            yoff + yidx * tilesize + 1,
                            xoff + xidx * tilesize + tilesize - 1,
                            yoff + yidx * tilesize + tilesize - 1,
                            0, 0, 388, 388, null);
                    break;
                case ROBOT_FRONT:
                    g.setColor(Color.DARK_GRAY);
                    g.fillRoundRect(xoff + xidx * tilesize + 1, yoff + yidx * tilesize + 1
                            , tilesize - 2, tilesize - 2,
                            4, 4);
                    g.drawImage(pl.maszynatrurla.kloce.res.Graphics.robotFront,
                            xoff + xidx * tilesize + 1,
                            yoff + yidx * tilesize + 1,
                            xoff + xidx * tilesize + tilesize - 1,
                            yoff + yidx * tilesize + tilesize - 1,
                            0, 0, 388, 388, null);
                    break;
                case ROBOT_LEFT:
                    g.setColor(Color.DARK_GRAY);
                    g.fillRoundRect(xoff + xidx * tilesize + 1, yoff + yidx * tilesize + 1
                            , tilesize - 2, tilesize - 2,
                            4, 4);
                    g.drawImage(pl.maszynatrurla.kloce.res.Graphics.robotLeft,
                            xoff + xidx * tilesize + 1,
                            yoff + yidx * tilesize + 1,
                            xoff + xidx * tilesize + tilesize - 1,
                            yoff + yidx * tilesize + tilesize - 1,
                            0, 0, 388, 388, null);
                    break;
                case ROBOT_RIGHT:
                    g.setColor(Color.DARK_GRAY);
                    g.fillRoundRect(xoff + xidx * tilesize + 1, yoff + yidx * tilesize + 1
                            , tilesize - 2, tilesize - 2,
                            4, 4);
                    g.drawImage(pl.maszynatrurla.kloce.res.Graphics.robotRight,
                            xoff + xidx * tilesize + 1,
                            yoff + yidx * tilesize + 1,
                            xoff + xidx * tilesize + tilesize - 1,
                            yoff + yidx * tilesize + tilesize - 1,
                            0, 0, 388, 388, null);
                    break;
                default:
                    break;
                
                }
            }
        }
        
    }
}
