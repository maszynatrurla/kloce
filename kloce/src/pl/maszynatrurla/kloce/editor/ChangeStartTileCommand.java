package pl.maszynatrurla.kloce.editor;

import pl.maszynatrurla.kloce.AppGlobals;
import pl.maszynatrurla.kloce.Image;
import pl.maszynatrurla.kloce.Tile;

public class ChangeStartTileCommand implements Command
{
    private Tile oldTile;
    private Tile newTile;
    private int x,y;
    
    private Tile previousStartTile;
    private int prevx,prevy;
    private boolean undo = false;
    
    public ChangeStartTileCommand(Image image, int x, int y, Tile startTile)
    {
        for (int i = 0; i < image.getHeight(); ++i)
        {
            for (int j = 0; j < image.getLength(); ++j)
            {
                if (image.getTile(j, i).isStartTile() && (i != y  || j != x))
                {
                    prevx = j;
                    prevy = i;
                    previousStartTile = image.getTile(j, i);
                    break;
                }
            }
        }
        
        oldTile = image.getTile(x, y);
        newTile = startTile;
        this.x = x;
        this.y = y;
    }

    @Override
    public void perform()
    {
        Image image = AppGlobals.getInstance().get(Image.class);
        if (undo)
        {
            image.setTile(x, y, oldTile);
            if (previousStartTile != null)
            {
                image.setTile(prevx, prevy, previousStartTile);
            }
        }
        else
        {
            image.setTile(x, y, newTile);
            if (previousStartTile != null)
            {
                image.setTile(prevx, prevy, Tile.PLATFORM);
            }
        }
    }

    @Override
    public void invert()
    {
        undo ^= true;
    }

}
