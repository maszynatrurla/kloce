package pl.maszynatrurla.kloce.editor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;

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
        
        createToggle("Start", Tile.ROBOT_RIGHT, KeyStroke.getKeyStroke("typed s"));
        createToggle("Platform", Tile.PLATFORM, KeyStroke.getKeyStroke("typed p"));
        createToggle("Battery", Tile.BATTERY, KeyStroke.getKeyStroke("typed b"));
        createToggle("Eraser", Tile.EMPTY, KeyStroke.getKeyStroke("DELETE"));
        createRotateButton();
        createToggle("Trim", null, KeyStroke.getKeyStroke("control T"));
       
        
        JButton button = new JButton("Extend");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("typed x"), "Extend");
        ActionListener listener = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doExtend();
            }
        };
        button.addActionListener(listener);
        getActionMap().put("Extend", new AbstractAction() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                listener.actionPerformed(e);
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
    
    private void createToggle(String text, Tile tool, KeyStroke shortcut)
    {
        JToggleButton toggle = new JToggleButton(text);
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                shortcut, text);
        ActionListener listener = new ActionListener() {
            
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
        };
        toggle.addActionListener(listener);
        getActionMap().put(text, new AbstractAction() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                toggle.doClick();
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
            app.get(CommandStack.class).performCommand(new ExtendImageCommand(image));
            app.get(Canvas.class).repaint();
        }
    }
}
