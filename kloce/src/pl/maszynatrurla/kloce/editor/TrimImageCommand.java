package pl.maszynatrurla.kloce.editor;

import pl.maszynatrurla.kloce.AppGlobals;
import pl.maszynatrurla.kloce.Image;

public class TrimImageCommand implements Command
{
    private final Image backupImage;
    private Image doImage;
    private boolean doUndo = false;
    private final int startX, startY, endX, endY;
    
    public TrimImageCommand(Image image, int startX, int startY,
            int endX, int endY)
    {
        this.backupImage = new Image(image.getLength(), image.getHeight());
        for (int i = 0; i < image.getHeight(); ++i)
        {
            for (int j = 0; j < image.getLength(); ++j)
            {
                this.backupImage.setTile(j, i, image.getTile(j, i));
            }
        }
        doImage = image;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
    
    
    @Override
    public void perform()
    {
        if (doUndo)
        {
            doImage = backupImage;
            AppGlobals.getInstance().set(doImage);
        }
        else
        {
            doImage.trim(startX, startY, endX, endY);
            AppGlobals.getInstance().set(doImage);
        }
    }

    @Override
    public void invert()
    {
        doUndo ^= true;
    }

}
