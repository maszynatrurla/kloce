package pl.maszynatrurla.kloce.editor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import pl.maszynatrurla.kloce.AppGlobals;
import pl.maszynatrurla.kloce.Image;
import pl.maszynatrurla.kloce.Tile;

public class Palette extends JPanel
{

    private static final long serialVersionUID = 1L;

    private final AppGlobals app;
    
    private final Vector<JToggleButton> toggles = new Vector<JToggleButton>(8);
    
    
    public Palette(final AppGlobals resources)
    {
        app = resources;
    }
    
    public void create()
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        
        createToggle("Start", Tile.ROBOT_RIGHT);
        createToggle("Platform", Tile.PLATFORM);
        createToggle("Battery", Tile.BATTERY);
        createToggle("InA", Tile.INPUT_A);
        createToggle("InB", Tile.INPUT_B);
        createToggle("Out", Tile.OUTPUT);
        createToggle("Eraser", Tile.EMPTY);
        createRotateButton();
        createToggle("Trim", null);
       
        
        JButton button = new JButton("Extend");
        button.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doExtend();
            }
        });
        add(button);
    }
    
    private void createRotateButton()
    {
        JButton button = new JButton("Rotate Robot");
        button.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                app.get(Canvas.class).rotateFigure();
            }
        });
        add(button);
    }
    
    private void createToggle(String text, Tile tool)
    {
        JToggleButton toggle = new JToggleButton(text);
        toggle.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                for (JToggleButton t : toggles)
                {
                    if (t != toggle)
                    {
                        t.setSelected(false);
                    }
                }
                if (tool == null)
                {
                    if (toggle.isSelected())
                    {
                        doTrim();
                    }
                    else
                    {
                        setTool(null);
                    }
                }
                else
                {
                    if (toggle.isSelected())
                    {
                        setTool(tool);
                    }
                    else
                    {
                        setTool(null);
                    }
                }
            }
        });
        toggles.add(toggle);
        add(toggle);
    }
    
    private void setTool(Tile tool)
    {
        app.get(Canvas.class).setTool(tool);
    }
    
    private void doTrim()
    {
        app.get(Canvas.class).setTrim();
    }
    
    private void doExtend()
    {
        Image image = app.get(Image.class);
        if (image.getHeight() < 48 && image.getLength() < 48)
        {
            image.extend();
            app.get(Canvas.class).repaint();
        }
    }
}
