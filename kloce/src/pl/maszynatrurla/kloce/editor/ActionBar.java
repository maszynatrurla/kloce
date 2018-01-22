package pl.maszynatrurla.kloce.editor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import pl.maszynatrurla.kloce.AppGlobals;
import pl.maszynatrurla.kloce.Image;

public class ActionBar extends JPanel 
{

    private static final long serialVersionUID = 1L;

    private final AppGlobals app;
    
    private String saveFile;
    private String cd = System.getProperty("user.dir");

    public ActionBar(final AppGlobals resources) 
    {
        app = resources;
    }
    
    public void create()
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JButton button = new JButton("New");
        button.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doNew();
            }
        });
        add(button);
        
        button = new JButton("Open");
        button.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doOpen();
            }
        });
        add(button);
        
        button = new JButton("Save As");
        button.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doSaveAs();
            }
        });
        add(button);
        
        button = new JButton("Save");
        ActionListener saveListener = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doSave();
            }
        };
        button.addActionListener(saveListener);
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"),
                "Save");
        getActionMap().put("Save", new AbstractAction() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                saveListener.actionPerformed(e);
            }
        });
        add(button);
        
        button = new JButton("Undo");
        ActionListener undoListener = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doUndo();
            }
        };
        button.addActionListener(undoListener);
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z"),
                "Undo");
        getActionMap().put("Undo", new AbstractAction() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                undoListener.actionPerformed(e);
            }
        });
        add(button);
        
        button = new JButton("Redo");
        ActionListener redoListener = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doRedo();
            }
        };
        button.addActionListener(redoListener);
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Y"),
                "Redo");
        getActionMap().put("Redo", new AbstractAction() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                redoListener.actionPerformed(e);
            }
        });
        add(button);
    }
    
    private void doNew()
    {
        Image image = app.get(Image.class);
        image.zero();
        Canvas canvas = app.get(Canvas.class);
        app.get(CommandStack.class).clear();
        canvas.repaint();
    }
    
    private void doOpen()
    {
        JFileChooser dialog = new JFileChooser(cd);
        dialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                File selected = dialog.getSelectedFile();
                Image image = Image.load(selected);
                app.set(image);
                app.get(CommandStack.class).clear();
                app.get(Canvas.class).repaint();
                this.saveFile = dialog.getSelectedFile().getAbsolutePath();
                cd = selected.getParent();
            } catch (IOException e)
            {
                JOptionPane.showMessageDialog(this, "I/O Error: " + e,
                        "Load failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void doSave()
    {
        if (null == saveFile)
        {
            doSaveAs();
        }
        else
        {
            Image image = app.get(Image.class);
            try
            {
                image.store(new File(saveFile));
                app.get(CommandStack.class).clear();
            }
            catch (IOException ioe)
            {
                JOptionPane.showMessageDialog(this, "I/O Error: " + ioe,
                        "Save failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void doSaveAs()
    {
        JFileChooser dialog = new JFileChooser(cd);
        dialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (dialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File selected = dialog.getSelectedFile();
            this.saveFile = selected.getAbsolutePath();
            
            if (this.saveFile != null)
            {
                doSave();
            }
            cd = selected.getParent();
        }
        
    }
    
    private void doUndo()
    {
        app.get(CommandStack.class).undo();
        app.get(Canvas.class).repaint();
    }
    
    private void doRedo()
    {
        app.get(CommandStack.class).redo();
        app.get(Canvas.class).repaint();
    }
}
