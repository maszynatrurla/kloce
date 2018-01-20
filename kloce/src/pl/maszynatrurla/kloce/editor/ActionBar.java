package pl.maszynatrurla.kloce.editor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
        button.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doSave();
            }
        });
        add(button);
        
    }
    
    private void doNew()
    {
        Image image = app.get(Image.class);
        image.zero();
        Canvas canvas = app.get(Canvas.class);
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
}
