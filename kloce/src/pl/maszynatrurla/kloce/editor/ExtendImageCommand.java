package pl.maszynatrurla.kloce.editor;

import pl.maszynatrurla.kloce.AppGlobals;
import pl.maszynatrurla.kloce.Image;

public class ExtendImageCommand implements Command
{
    private final Image backupImage;
    private Image doImage;
    private boolean doUndo = false;
    
    public ExtendImageCommand(Image image)
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
            doImage.extend();
            AppGlobals.getInstance().set(doImage);
        }
    }

    @Override
    public void invert()
    {
        doUndo ^= true;
    }

}
