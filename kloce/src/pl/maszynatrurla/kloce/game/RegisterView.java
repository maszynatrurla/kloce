package pl.maszynatrurla.kloce.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.maszynatrurla.kloce.AppGlobals;

public class RegisterView extends JPanel 
{
    private static final long serialVersionUID = 1L;
    
    private final AppGlobals app;
    
    private final Font memfont = new Font("Verdana", Font.BOLD, 26);
    
    private JTextField inputField, outputField;
    private JLabel memLabel;
    private int prevWin, winstart;
    
    public RegisterView(final AppGlobals resources)
    {
        this.app = resources;
    }
    
    public void create()
    {
        this.setBackground(Color.BLACK);
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        
        JLabel label = new JLabel("INPUT");
        label.setForeground(Color.WHITE);
        add(label);
        
        inputField = new JTextField("0", 12);
        inputField.setBackground(Color.DARK_GRAY);
        inputField.setForeground(Color.LIGHT_GRAY);
        inputField.setEditable(false);
        add(inputField);
        
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        label = new JLabel("OUTPUT");
        label.setForeground(Color.WHITE);
        add(label);
        
        outputField = new JTextField("0", 12);
        outputField.setBackground(Color.DARK_GRAY);
        outputField.setForeground(Color.LIGHT_GRAY);
        outputField.setEditable(false);
        add(outputField);
        
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        memLabel = new JLabel("MEMORY");
        memLabel.setForeground(Color.WHITE);
        add(memLabel);
        
        add(new Box.Filler(new Dimension(), new Dimension(0, 1000), new Dimension(1000, 1000)));
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        final Processor cpu = app.get(Processor.class);
        final CpuMemory mem = cpu.getMemory();
        final CpuSnapshot snap = cpu.getState();
        
        final Rectangle screen = g.getClipBounds();
        final Rectangle labb = memLabel.getBounds();
        
        int screenh = screen.height - (labb.y + labb.height) - 20;
        int yoff = labb.y + labb.height;
        
        int winsize = screenh / 32;
        
        inputField.setText(Integer.toString(snap.in));
        outputField.setText(Integer.toString(snap.out));
     
        if (winsize > 0)
        {
            if (prevWin != winsize)
            {
                winstart = snap.ptr - winsize / 2;
            }
            
            int winborder = winsize > 3 ? 1 : 0;
            
            while (snap.ptr + winborder >= winstart + winsize)
            {
                ++winstart;
            }
            while (snap.ptr - winborder < winstart)
            {
                --winstart;
            }
            
            prevWin = winsize;
            
            for (int idx = 0; idx < winsize; ++idx)
            {
                short value = mem.get(winstart + idx);
                
                if (winstart + idx == snap.ptr)
                {
                    g.setColor(Color.BLUE);
                }
                else
                {
                    g.setColor(Color.GREEN);
                }
                g.setFont(memfont);
                g.drawString(Short.toString(value), 4, yoff + screenh - idx * 32);
            }
        }
    }
}
