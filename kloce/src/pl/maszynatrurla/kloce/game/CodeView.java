package pl.maszynatrurla.kloce.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import pl.maszynatrurla.kloce.AppGlobals;
import pl.maszynatrurla.kloce.res.StyleChoices;

public class CodeView extends JPanel implements Executable
{
    private static final long serialVersionUID = 1L;
    
    private final AppGlobals app;
    
    private final CodeStats stats = new CodeStats();
    private final CodeViewPositions positions = new CodeViewPositions();
    
    public CodeView(final AppGlobals resources)
    {
        this.app = resources;
        this.app.set(stats);
    }
    
    public void create()
    {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        this.setPreferredSize(new Dimension(3000, 120));
        MyMouseAdapter mouseHandler = new MyMouseAdapter();
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
        this.recalculate();
 
        Processor processor = app.get(Processor.class);
        processor.load(this);
    }
    
    public void clear()
    {
        recalculate();
        positions.code.clear();
        positions.highlightPc = -1;
        repaint();
    }
    
    private void recalculate()
    {
        final short [] toolsInOrder = new short [] {
                Asm.GO, Asm.LT, Asm.RT, Asm.INC, Asm.AD2, Asm.AD4,
                Asm.AD8, Asm.DEC, Asm.JZ, Asm.JNZ, Asm.MUP, Asm.MDN,
                Asm.IN, Asm.OUT
        };  
        
        positions.setPalette(toolsInOrder);
    }
    
    @Override
    public short getCode(int pc) throws OutOfCodeException, InvalidTokenException
    {
        if (pc < positions.code.size())
        {
            positions.highlightPc = pc;
            repaint();
            return positions.code.get(pc);
        }
        else
        {
            throw new OutOfCodeException(pc);
        }
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        paintPalette(g);
        paintTrack(g);
        
        if (positions.dragged.isOn)
        {
            paintOp(positions.dragged.op, g,
                    positions.dragged.x,
                    positions.dragged.y, false);
        }
    }
    
    private void paintTrack(Graphics g)
    {
        Rectangle bnds = g.getClipBounds();

        int xoff = positions.trackBounds.x;
        positions.trackBounds = new Rectangle(xoff,
                positions.trackBounds.y, bnds.width - 50, 44);
        
        g.setColor(Color.DARK_GRAY);
        g.fillRect(positions.trackBounds.x, positions.trackBounds.y,
                positions.trackBounds.width, positions.trackBounds.height);

        for (int i = 0; i < positions.code.size(); ++i)
        {
            paintOp(positions.code.get(i), g, xoff + 42 * i,
                    positions.trackBounds.y, i == positions.highlightPc);
        }
    }
    
    private void paintPalette(Graphics g)
    {
        int xoff = positions.paletteBounds.x;
        
        for (int i = 0; i < positions.ops.length; ++i)
        {
            paintOp(positions.ops[i], g, xoff + 45 * i, 65, false);
        }
    }
    
    private void paintOp(short op, Graphics g, int x, int y, boolean highlight)
    {
        if (highlight)
        {
            g.setColor(Color.BLACK);
        }
        else
        {
            switch (op)
            {
            case Asm.INC: 
            case Asm.AD2: 
            case Asm.DEC: 
            case Asm.AD4:
            case Asm.AD8:
                g.setColor(new Color(155, 255, 112));
                break;
            case Asm.MUP: 
            case Asm.MDN:
                g.setColor(Color.YELLOW);
                break;
            case Asm.IN : 
            case Asm.OUT: 
                g.setColor(new Color(255, 99, 99));
                break;
            case Asm.JZ : 
            case Asm.JNZ:
                g.setColor(new Color(255, 112, 207));
                break;
            case Asm.GO : 
            case Asm.LT : 
            case Asm.RT :
                g.setColor(new Color(112, 166, 255));
                break;
            }
        }

        g.fillRoundRect(x, y, 40, 40, 6, 6);
        g.setColor(Color.WHITE);
        g.drawRoundRect(x, y, 40, 40, 6, 6);
        
        if (highlight)
        {
            g.setColor(Color.WHITE);
            g.setFont(StyleChoices.CODE_BLOCK_H_FONT);
        }
        else
        {
            g.setColor(Color.BLACK);
            g.setFont(StyleChoices.CODE_BLOCK_FONT);   
        }
        
        switch (op)
        {
        case Asm.INC: g.drawString("+1" , x + 6,  y + 27); break;
        case Asm.AD2: g.drawString("+2" , x + 6,  y + 27); break;
        case Asm.DEC: g.drawString("-1" , x + 6,  y + 27); break;
        case Asm.AD4: g.drawString("+4" , x + 6,  y + 27); break;
        case Asm.AD8: g.drawString("+8" , x + 6,  y + 27); break;
        case Asm.MUP: g.drawString("M>" , x + 6,  y + 27); break;
        case Asm.MDN: g.drawString("<M" , x + 6,  y + 27); break;
        case Asm.IN : g.drawString("in" , x + 9,  y + 27); break;
        case Asm.OUT: g.drawString("out", x + 4,  y + 27); break;
        case Asm.JZ : g.drawString("["  , x + 14, y + 27); break;
        case Asm.JNZ: g.drawString("]"  , x + 14, y + 27); break;
        case Asm.GO : g.drawString("go" , x + 6,  y + 27); break;
        case Asm.LT : g.drawString("lt" , x + 10, y + 27); break;
        case Asm.RT : g.drawString("rt" , x + 10, y + 27); break;
        }
    }

    private class MyMouseAdapter extends MouseInputAdapter
    {
        private int dragState = 0;
        
        @Override
        public void mousePressed(MouseEvent e)
        {
            if (0 == dragState)
            {
                if (positions.setPaletteDragOn(e.getX(), e.getY()))
                {
                    dragState = 1;
                }
                else if (positions.setTrackDragOn(e.getX(), e.getY()))
                {
                    dragState = 2;
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
            if (dragState > 0)
            {
                positions.drag(e.getX(), e.getY());
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            if (1 == dragState)
            {
                positions.setPaletteDragOff(e.getX(), e.getY());
                repaint();
            }
            else if (2 == dragState)
            {
                positions.setTrackDragOff(e.getX(), e.getY());
                repaint();
            }
            dragState = 0;
        }
    }
}
