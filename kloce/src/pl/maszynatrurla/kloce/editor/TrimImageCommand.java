package pl.maszynatrurla.kloce.editor;

import pl.maszynatrurla.kloce.AppGlobals;
import pl.maszynatrurla.kloce.Image;

public class TrimImageCommand implements Command
{
    private Image backupImage;
    private boolean doUndo = false;
    private final int startX, startY, endX, endY;
    
    public TrimImageCommand(int startX, int startY,
            int endX, int endY)
    {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
    
    
    @Override
    public void perform()
    {
        AppGlobals app = AppGlobals.getInstance();
        
        if (doUndo)
        {
            app.set(backupImage);
        }
        else
        {
            Image image = app.get(Image.class);
            backupImage = new Image(image);
            image.trim(startX, startY, endX, endY);
        }
    }

    @Override
    public void invert()
    {
        doUndo ^= true;
    }

}
