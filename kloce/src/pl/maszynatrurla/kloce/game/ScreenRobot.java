package pl.maszynatrurla.kloce.game;

import javax.swing.JOptionPane;

import pl.maszynatrurla.kloce.AppGlobals;
import pl.maszynatrurla.kloce.Image;
import pl.maszynatrurla.kloce.Tile;

public class ScreenRobot implements Robot
{
    private static final int [] NEXT = {1, 0, -1, 0}; 
    private static final Tile [] ROBS = {Tile.ROBOT_RIGHT,
            Tile.ROBOT_BACK, Tile.ROBOT_LEFT, Tile.ROBOT_FRONT};
    
    private final AppGlobals app;
    private Image image;
    private int x,y, nix, niy;
    private int batcount;
    
    public ScreenRobot(AppGlobals app)
    {
        this.app = app;
    }
    
    public void load(Image image)
    {
        
        this.image = image;
        if (null == image)
        {
            return;
        }
        nix = 0;
        niy = 1;
        x = 0;
        y = 0;
        batcount = 0;
        
        for (int iy = 0; iy < image.getHeight(); ++iy)
        {
            for (int ix = 0; ix < image.getLength(); ++ix)
            {
                Tile tile = image.getTile(ix, iy);
                
                if (Tile.START == tile)
                {
                    image.setTile(ix, iy, Tile.ROBOT_RIGHT);
                    x = ix;
                    y = iy;
                    nix = 0;
                    niy = 1;
                }
                else if (Tile.ROBOT_BACK == tile)
                {
                    x = ix;
                    y = iy;
                    nix = 1;
                    niy = 2;
                }
                else if (Tile.ROBOT_FRONT == tile)
                {
                    x = ix;
                    y = iy;
                    nix = 3;
                    niy = 0;
                }
                else if (Tile.ROBOT_LEFT == tile)
                {
                    x = ix;
                    y = iy;
                    nix = 2;
                    niy = 3;
                }
                else if (Tile.ROBOT_RIGHT == tile)
                {
                    x = ix;
                    y = iy;
                    nix = 0;
                    niy = 1;
                }
                else if (Tile.BATTERY == tile)
                {
                    ++batcount;
                }
            }
        }
    }

    @Override
    public void go() throws RobotOffCourseException, RobotObjectiveAccomplishedEvent
    {
        boolean robotFell = false;
        boolean gameWon = false;
        
        if (null == image)
        {
            return;
        }
        if (x >= 0 && x < image.getLength() && y >= 0 && y < image.getHeight())
        {
            Tile tile = image.getTile(x, y);
            if (tile == Tile.ROBOT_BACK || tile == Tile.ROBOT_FRONT
                    || tile == Tile.ROBOT_LEFT || tile == Tile.ROBOT_RIGHT)
            {
                image.setTile(x, y, Tile.PLATFORM);
                x += NEXT[nix];
                y += NEXT[niy];
                
                robotFell = true;
                
                if (x >= 0 && x < image.getLength() && y >= 0 && y < image.getHeight())
                {
                    Tile oldtile = image.getTile(x, y);
                    
                    if (Tile.PLATFORM == oldtile)
                    {
                        image.setTile(x, y, tile);
                        robotFell = false;
                    }
                    else if (Tile.BATTERY == oldtile)
                    {
                        image.setTile(x, y, tile);
                        robotFell = false;
                        if (0 == --batcount)
                        {
                            gameWon = true;
                        }
                    }
                }
                
                app.get(Canvas.class).repaint();
            }
        }

        if (robotFell)
        {
            throw new RobotOffCourseException();
        }
        if (gameWon)
        {
            throw new RobotObjectiveAccomplishedEvent();
        }
    }

    @Override
    public void left()
    {
        ++nix;
        ++niy;
        if (nix >= NEXT.length) {nix = 0;}
        if (niy >= NEXT.length) {niy = 0;}
        
        if (x >= 0 && x < image.getLength() && y >= 0 && y < image.getHeight())
        {
            Tile tile = image.getTile(x, y);
            if (tile == Tile.ROBOT_BACK || tile == Tile.ROBOT_FRONT
                    || tile == Tile.ROBOT_LEFT || tile == Tile.ROBOT_RIGHT)
            {
                image.setTile(x, y, ROBS[nix]);
                app.get(Canvas.class).repaint();
            }
        }
    }

    @Override
    public void right()
    {
        --nix;
        --niy;
        if (nix < 0) {nix = NEXT.length - 1;}
        if (niy < 0) {niy = NEXT.length - 1;}
        
        if (x >= 0 && x < image.getLength() && y >= 0 && y < image.getHeight())
        {
            Tile tile = image.getTile(x, y);
            if (tile == Tile.ROBOT_BACK || tile == Tile.ROBOT_FRONT
                    || tile == Tile.ROBOT_LEFT || tile == Tile.ROBOT_RIGHT)
            {
                image.setTile(x, y, ROBS[nix]);
                app.get(Canvas.class).repaint();
            }
        }
    }

    @Override
    public short getInputValue()
    {
        String text = JOptionPane.showInputDialog("Input?");
        if (null != text)
        {
            try
            {
                return Short.decode(text);
            }
            catch (NumberFormatException nfe)
            {
                
            }
        }
        
        return 0;
    }

    @Override
    public void setOutputValue(short value)
    {
        // intentionally empty
    }

}
