package pl.maszynatrurla.kloce.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
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
    private Point trimstart;
    private Rectangle trimwin;

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
                    app.get(CommandStack.class).performJoinableCommand(
                            new ChangeTileCommand(image, x, y, Tile.ROBOT_RIGHT));
                    changed = true;
                    break;
                case ROBOT_FRONT:
                    app.get(CommandStack.class).performJoinableCommand(
                            new ChangeTileCommand(image, x, y, Tile.ROBOT_LEFT));
                    changed = true;
                    break;
                case ROBOT_LEFT:
                    app.get(CommandStack.class).performJoinableCommand(
                            new ChangeTileCommand(image, x, y, Tile.ROBOT_BACK));
                    changed = true;
                    break;
                case START:
                case ROBOT_RIGHT:
                    app.get(CommandStack.class).performJoinableCommand(
                            new ChangeTileCommand(image, x, y, Tile.ROBOT_FRONT));
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
                if (trimState > 1 && trimwin.contains(xidx, yidx))
                {
                    g.setColor(Color.RED);
                    g.drawRect(xoff + xidx * tilesize, yoff + yidx * tilesize, tilesize, tilesize);
                }
            }
        }
    }
    
    private Point getMousedTile(Image image, int mousex, int mousey)
    {
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
            return new Point(tilex, tiley);
        }
        return null;
    }
    
    private void initTrim(int mousex, int mousey)
    {
        Image image = app.get(Image.class);
        Point tileXY = getMousedTile(image, mousex, mousey);
        
        if (tileXY != null)
        {
            trimstart = new Point(tileXY.x, tileXY.y);
            trimwin = new Rectangle(tileXY.x, tileXY.y, 1, 1);
            trimState = 2;
            this.repaint();
        }
    }
    
    private void addTrim(int mousex, int mousey)
    {
        Image image = app.get(Image.class);
        Point tileXY = getMousedTile(image, mousex, mousey);
        
        if (tileXY != null && trimwin != null && !trimwin.contains(tileXY))
        {
            int startX = tileXY.x > trimstart.x ? trimstart.x : tileXY.x;
            int startY = tileXY.y > trimstart.y ? trimstart.y : tileXY.y;
            int endX = tileXY.x > trimstart.x ? tileXY.x : trimstart.x;
            int endY = tileXY.y > trimstart.y ? tileXY.y : trimstart.y;
            
            trimwin = new Rectangle(startX, startY, endX - startX, endY - startY);
            
            this.repaint();
        }
    }
    
    private void commitTrim(int mousex, int mousey)
    {
        Image image = app.get(Image.class);
        Point tileXY = getMousedTile(image, mousex, mousey);
        
        if (tileXY != null && trimwin != null)
        {
            int startX = tileXY.x > trimstart.x ? trimstart.x : tileXY.x;
            int startY = tileXY.y > trimstart.y ? trimstart.y : tileXY.y;
            int endX = tileXY.x > trimstart.x ? tileXY.x : trimstart.x;
            int endY = tileXY.y > trimstart.y ? tileXY.y : trimstart.y;
            
            trimwin = new Rectangle(startX, startY, endX - startX, endY - startY);
            
            app.get(CommandStack.class).performCommand(new TrimImageCommand(
                    image, 
                    trimwin.x, trimwin.y,
                    trimwin.x + trimwin.width,
                    trimwin.y + trimwin.height));
        }
        
        trimState = 1;
        trimwin = null;
        trimstart = null;
        
        this.repaint();
    }
    
    private void editTile(int mousex, int mousey)
    {
        Image image = app.get(Image.class);
        Point tileXY = getMousedTile(image, mousex, mousey);

        if (tileXY != null && tool != null)
        {
            if (image.getTile(tileXY.x, tileXY.y) != tool)
            {
                app.get(CommandStack.class).performJoinableCommand(
                        new ChangeTileCommand(image, tileXY.x, tileXY.y, tool));
                this.repaint();
            }
        }
    }
    
    private class MyMouseAdapter extends MouseAdapter
    {
        @Override
        public void mousePressed(MouseEvent e)
        {
            if (trimState > 0)
            {
                initTrim(e.getX(), e.getY());
            }
            else if (tool != null)
            {
                editTile(e.getX(), e.getY());
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent e)
        {
            if (trimState > 0)
            {
                addTrim(e.getX(), e.getY());
            }
            else if (tool != null)
            {
                editTile(e.getX(), e.getY());
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e)
        {
            if (trimState > 1)
            {
                commitTrim(e.getX(), e.getY());
            }
        }
    }
}
