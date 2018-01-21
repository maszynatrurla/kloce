package pl.maszynatrurla.kloce.game;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.awt.Rectangle;

public class CodeViewPositions
{
    public Rectangle paletteBounds = new Rectangle(30, 65, 0, 0);
    public Rectangle trackBounds = new Rectangle(30, 4, 0, 0);
    public short [] ops = new short [] {};
    public Dragged dragged = new Dragged();
    public List<Short> code = new ArrayList<Short>();
    public int highlightPc = -1;
    
    private Point dragOffset = new Point();
    
    public boolean setPaletteDragOn(int x, int y)
    {
        if (paletteBounds.contains(x, y))
        {
            int xo = x - paletteBounds.x;
            int idx = xo / 45;
            if (idx < ops.length)
            {
                if (xo > idx * 45 && xo < idx * 45 + 40)
                {
                    dragOffset = new Point(xo - idx * 45, y - paletteBounds.y);
                    dragged.op = ops[idx];
                    return true;
                }
            }
        } 
        return false;
    }
    
    public boolean setTrackDragOn(int x, int y)
    {
        if (trackBounds.contains(x, y))
        {
            int xo = x - trackBounds.x;
            int idx = xo / 42;
            if (idx < code.size())
            {
                if (xo > idx * 42 && xo < idx * 42 + 40)
                {
                    dragOffset = new Point(xo - idx * 42, y - trackBounds.y);
                    dragged.op = code.get(idx);
                    dragged.codeidx = idx;
                    return true;
                }
            }
        }
        return false;
    }
    
    public void setPalette(short [] ops)
    {
        paletteBounds = new Rectangle(30, 65, ops.length * 40 + (ops.length - 1) * 5,
                40);
        this.ops = ops;
    }
    
    public void drag(int x, int y)
    {
        dragged.isOn = true;
        dragged.x = x - dragOffset.x;
        dragged.y = y - dragOffset.y;
    }
    
    public void setTrackDragOff(int x, int y)
    {
        dragged.isOn = false;
        if (trackBounds.contains(x, y))
        {
            int xo = x - trackBounds.x;
            int idx = xo / 42;
            if (idx < code.size())
            {
                if (idx != dragged.codeidx)
                {
                    if (dragged.codeidx < idx)
                    {
                        idx -= 1;
                    }
                    code.remove((int) dragged.codeidx);
                    code.add(idx, dragged.op);
                }
            }
            else
            {
                code.remove((int) dragged.codeidx);
                code.add(dragged.op);
            }
        }
        else
        {
            code.remove((int) dragged.codeidx); 
        }
    }
    
    public void setPaletteDragOff(int x, int y)
    {
        dragged.isOn = false;
        if (trackBounds.contains(x, y))
        {
            int xo = x - trackBounds.x;
            int idx = xo / 42;
            if (idx < code.size())
            {
                code.add(idx, dragged.op);
            }
            else
            {
                code.add(dragged.op);
            }
        }
    }
    
    public static class Dragged
    {
        public short op = -1;
        public boolean isOn = false;
        public int x = 0;
        public int y = 0;
        public int codeidx = 0;
    }
}
