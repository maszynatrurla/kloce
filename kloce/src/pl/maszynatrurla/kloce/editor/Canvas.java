package pl.maszynatrurla.kloce.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import pl.maszynatrurla.kloce.AppGlobals;
import pl.maszynatrurla.kloce.Image;
import pl.maszynatrurla.kloce.Tile;

public class Canvas extends JPanel 
{

    private static final long serialVersionUID = 1L;

    private final AppGlobals app; 
    
    private Tile tool;
    private int trimState = 0;
    private int trimx, trimy;

    public Canvas(final AppGlobals resources)
    {
        app = resources;
    }
    
    public void create()
    {
        setBackground(Color.BLACK);
        MyMouseAdapter listeners = new MyMouseAdapter();
        addMouseMotionListener(listeners);
        addMouseListener(listeners);
    }
    
    public void setTool(Tile tool)
    {
        this.tool = tool;
        this.trimState = 0;
    }
    
    public void setTrim()
    {
        this.trimState = 1;
    }
    
    public void rotateFigure()
    {
        Image image = app.get(Image.class);
        if (image == null)
        {
            return;
        }
        
        boolean changed = false;
        
        for (int y = 0; y < image.getHeight(); ++y)
        {
            for (int x = 0; x < image.getLength(); ++x)
            {
                switch (image.getTile(x, y))
                {
                case ROBOT_BACK:
                    image.setTile(x, y, Tile.ROBOT_RIGHT);
                    changed = true;
                    break;
                case ROBOT_FRONT:
                    image.setTile(x, y, Tile.ROBOT_LEFT);
                    changed = true;
                    break;
                case ROBOT_LEFT:
                    image.setTile(x, y, Tile.ROBOT_BACK);
                    changed = true;
                    break;
                case START:
                case ROBOT_RIGHT:
                    image.setTile(x, y, Tile.ROBOT_FRONT);
                    changed = true;
                    break;
                default:
                    break;
                }
            }
        }
        
        if (changed)
        {
            repaint();
        }
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        Image image = app.get(Image.class);
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
        
        for (int xidx = 0; xidx <= image.getLength(); ++xidx)
        {
            g.drawLine(xoff + xidx * tilesize, yoff, xoff + xidx * tilesize, screen.height - yoff);
        }
        
        for (int yidx = 0; yidx <= image.getHeight(); ++yidx)
        {
            g.drawLine(xoff, yoff + yidx * tilesize, screen.width - xoff, yoff + yidx * tilesize);
        }
        
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
                    g.fillRect(xoff + xidx * tilesize, yoff + yidx * tilesize, tilesize, tilesize);
                    break;
                case INPUT_B:
                    g.setColor(Color.MAGENTA);
                    g.fillRect(xoff + xidx * tilesize, yoff + yidx * tilesize, tilesize, tilesize);
                    break;
                case OUTPUT:
                    g.setColor(Color.ORANGE);
                    g.fillRect(xoff + xidx * tilesize, yoff + yidx * tilesize, tilesize, tilesize);
                    break;
                case PLATFORM:
                    g.setColor(Color.DARK_GRAY);
                    g.fillRoundRect(xoff + xidx * tilesize + 1, yoff + yidx * tilesize + 1
                            , tilesize - 2, tilesize - 2,
                            4, 4);
                    break;
                case START:
                    g.setColor(Color.GREEN);
                    g.fillRect(xoff + xidx * tilesize, yoff + yidx * tilesize, tilesize, tilesize);
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
                if (trimState > 0 && trimx == xidx && trimy == yidx)
                {
                    g.setColor(Color.RED);
                    g.drawRect(xoff + xidx * tilesize, yoff + yidx * tilesize, tilesize, tilesize);
                }
            }
        }
    }
    
    private void editTile(int mousex, int mousey)
    {
        if (trimState == 0 && tool == null)
        {
            return;
        }
        Image image = app.get(Image.class);
        Rectangle screen = this.getBounds();
        
        int tilesize = (screen.width - 2) / image.getLength();
        if (tilesize > ((screen.height - 2) / image.getHeight()))
        {
            tilesize = (screen.height - 2) / image.getHeight();
        }
        
        int xoff = screen.width / 2 - ((tilesize * image.getLength()) / 2);
        int yoff = screen.height / 2 - ((tilesize * image.getHeight()) / 2);
        
        int tilex = (mousex - xoff) / tilesize;
        int tiley = (mousey - yoff) / tilesize;
        
        if (tilex >= 0 && tiley >= 0 && tilex < image.getLength()
                && tiley < image.getHeight())
        {
            if (trimState == 1)
            {
                trimx = tilex;
                trimy = tiley;
                trimState = 2;
                this.repaint();
            }
            else if (trimState == 2)
            {
                image.trim(trimx, trimy, tilex, tiley);
                trimState = 0;
                this.repaint();
            }
            else if (tool != null)
            {
                if (image.getTile(tilex, tiley) == tool)
                {
                    image.setTile(tilex, tiley, Tile.EMPTY);
                }
                else
                {
                    image.setTile(tilex, tiley, tool);
                }
                this.repaint();
            }
        }
    }
    
    private class MyMouseAdapter extends MouseAdapter
    {
        @Override
        public void mousePressed(MouseEvent e)
        {
            editTile(e.getX(), e.getY());
        }
        
    }
}
