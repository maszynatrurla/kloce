package pl.maszynatrurla.kloce.editor;

import pl.maszynatrurla.kloce.Image;
import pl.maszynatrurla.kloce.Tile;

public class ChangeTileCommand implements Command
{
    
    private final Image image;
    private final int x;
    private final int y;
    private Tile oldTile;
    private Tile newTile;
    
    public ChangeTileCommand(Image image, int x, int y, Tile newTile)
    {
        this.image = image;
        this.x = x;
        this.y = y;
        this.oldTile = image.getTile(x, y);
        this.newTile = newTile;
    }

    @Override
    public void perform()
    {
        image.setTile(x, y, newTile);
    }

    @Override
    public void invert()
    {
        Tile tmp = newTile;
        newTile = oldTile;
        oldTile = tmp;
    }

}
