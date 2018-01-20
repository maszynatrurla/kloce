package pl.maszynatrurla.kloce.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import pl.maszynatrurla.kloce.AppGlobals;

public class CodeView extends JPanel implements Executable
{
    private static final long serialVersionUID = 1L;
    
    private final AppGlobals app;
    private final Font font = new Font(Font.MONOSPACED, Font.BOLD, 22);
    private JTextArea textArea;
    private boolean dirty = true;
    private int previousPc, previousPos;
    
    public CodeView(final AppGlobals resources)
    {
        this.app = resources;
    }
    
    public void create()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setBackground(Color.BLACK);
        
        textArea = new JTextArea();
        textArea.setBorder(BorderFactory.createEmptyBorder(2, 30, 2, 10));
        textArea.setFont(font);
        textArea.setPreferredSize(new Dimension(1000, 100));
        textArea.setEditable(true);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.DARK_GRAY);
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);
        textArea.setSelectionColor(Color.CYAN);
        textArea.setSelectedTextColor(Color.MAGENTA);
        textArea.addInputMethodListener(new InputMethodListener() {
            
            @Override
            public void inputMethodTextChanged(InputMethodEvent event)
            {
                dirty = true;
            }
            
            @Override
            public void caretPositionChanged(InputMethodEvent event)
            {
                
            }
        });
        this.add(textArea);
        
        Processor processor = app.get(Processor.class);
        processor.load(this);
    }

    @Override
    public short getCode(int pc) throws OutOfCodeException, InvalidTokenException
    {
        if (!dirty && previousPc == pc)
        {
            previousPc = pc;
            return goToToken(previousPos, 0);
        }
        else if (!dirty && previousPc == pc - 1)
        {
            previousPc = pc;
            return goToToken(previousPos, 1);
        }
        else
        {
            dirty = false;
            previousPc = pc;
            
            return goToToken(0, pc);
            
        }
    }
    
    private short goToToken(int start, int num) throws OutOfCodeException, InvalidTokenException
    {
        String text = textArea.getText();
        int pos = start;
        
        try
        {
            for (int i = 0; i < num; ++i)
            {
                while (Character.isWhitespace(text.charAt(pos)))
                {
                    ++pos;
                }
                while (!Character.isWhitespace(text.charAt(pos)))
                {
                    ++pos;
                }
            }
            
            while (Character.isWhitespace(text.charAt(pos)))
            {
                ++pos;
            }
            
            previousPos = pos;
            
            while (pos < text.length() && !Character.isWhitespace(text.charAt(pos)))
            {
                
                ++pos;
            }
        }
        catch (StringIndexOutOfBoundsException sioobe)
        {
            throw new OutOfCodeException(previousPc);
        }

        
        textArea.select(previousPos, pos);
        
        if (text.regionMatches(previousPos, "++", 0, 2))
        {
            return (short) 1;
        }
        else if (text.regionMatches(previousPos, "+2", 0, 2))
        {
            return (short) 2;
        }
        else if (text.regionMatches(previousPos, "--", 0, 2))
        {
            return (short) 3;
        }
        else if (text.regionMatches(previousPos, "+4", 0, 2))
        {
            return (short) 4;
        }
        else if (text.regionMatches(previousPos, "m+", 0, 2))
        {
            return (short) 5;
        }
        else if (text.regionMatches(previousPos, "m-", 0, 2))
        {
            return (short) 6;
        }
        else if (text.regionMatches(previousPos, "in", 0, 2))
        {
            return (short) 7;   
        }
        else if (text.regionMatches(previousPos, "out", 0, 3))
        {
            return (short) 8;
        }
        else if (text.regionMatches(previousPos, "[", 0, 1))
        {
            return (short) 9;
        }
        else if (text.regionMatches(previousPos, "]", 0, 1))
        {
            return (short) 10;
        }
        else if (text.regionMatches(previousPos, "go", 0, 2))
        {
            return (short) 11;
        }
        else if (text.regionMatches(previousPos, "lt", 0, 2))
        {
            return (short) 12;
        }
        else if (text.regionMatches(previousPos, "rt", 0, 2))
        {
            return (short) 13;
        }
        else
        {
            throw new InvalidTokenException(text.substring(previousPos, pos));
        }
    }
}
