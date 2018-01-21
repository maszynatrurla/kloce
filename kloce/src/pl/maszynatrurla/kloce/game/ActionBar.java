package pl.maszynatrurla.kloce.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.maszynatrurla.kloce.AppGlobals;
import pl.maszynatrurla.kloce.Image;

public class ActionBar extends JPanel 
{
    private static final long serialVersionUID = 1L;
    
    private final AppGlobals app;
    private final Levels levels = new Levels();
    private Player player;
    private int delayMs = 500;
    private String cd = System.getProperty("user.dir");
    
    public ActionBar(final AppGlobals resources)
    {
        this.app = resources;
    }
    
    public void create()
    {
        this.setBackground(Color.BLACK);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        createButton("CD", handler -> changeDir());
        createButton("Sel Lvl", Handler -> selectLevel());
        createButton("First Lvl", Handler -> firstLevel());
        createButton("Prev Lvl", Handler -> skipLevel(-1));
        createButton("Next Lvl", Handler -> skipLevel(1));
        
        add(new JSeparator(JSeparator.VERTICAL));
        
        createButton("Clear", Handler -> clear());
        createButton("Reset", Handler -> reset());
        createButton("Play", Handler -> play());
        createButton("Pause", Handler -> pause());
        createButton("Step", Handler -> step());
        
        createSpeedCtrl();
        
        addKeyboardShortcuts();
        
        levels.openDir(new File(cd));
        String first = levels.get();
        if (first != null)
        {
            load(first);
        }
    }
    
    private void load(String filename)
    {
        try
        {
            Image image = Image.load(new File(filename));
            app.set(image);
            app.get(Canvas.class).repaint();
            app.get(ScreenRobot.class).load(image);
            Processor processor = app.get(Processor.class);
            processor.reset();
            app.get(RegisterView.class).repaint();
        }
        catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(this, "Failed to load " + filename + " " + ioe,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void createButton(String txt, ActionListener handler)
    {
        JButton button = new JButton(txt);
        button.setOpaque(true);
        button.setBackground(Color.DARK_GRAY);
        
        button.setForeground(Color.LIGHT_GRAY);
        button.addActionListener(handler);
        add(button);
    }
    
    private void changeDir()
    {
        JFileChooser dialog = new JFileChooser(cd);
        dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (JFileChooser.APPROVE_OPTION == dialog.showOpenDialog(this))
        {
            levels.openDir(dialog.getSelectedFile());
            String first = levels.get();
            if (first != null)
            {
                load(first);
            }
            cd = dialog.getSelectedFile().getParent();
        }
    }
    
    private void selectLevel()
    {
        JFrame frame = new JFrame("Choose level");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 600);
        JList<String> nameList = new JList<String>();
        DefaultListModel<String> model = new DefaultListModel<String>();
        for (String name : levels.getNames())
        {
            model.addElement(name);
        }
        nameList.setModel(model);
        nameList.setAutoscrolls(true);
        nameList.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (2 == e.getClickCount())
                {
                    int idx = nameList.getSelectedIndex();
                    String name = levels.get(idx);
                    if (name != null)
                    {
                        load(name);
                        frame.dispose();
                    }
                }
            }
        });
        frame.add(nameList);
        frame.setVisible(true);
    }
    
    private void firstLevel()
    {
        String name = levels.get(0);
        if (name != null)
        {
            load(name);
        }
    }
    
    private void skipLevel(int off)
    {
        String name;
        if (off > 0)
        {
            name = levels.next();
        }
        else
        {
            name = levels.previous();
        }
        if (name != null)
        {
            load(name);
        }
    }
    
    private void clear()
    {
        app.get(CodeView.class).clear();
        reset();
    }
    
    private void reset()
    {
        String name = levels.get();

        if (name == null)
        {
            Processor processor = app.get(Processor.class);
            processor.reset();
            app.get(RegisterView.class).repaint();
        }
        else
        {
            load(name);
        }
    }
    
    private void play()
    {
        if (null == player || !player.isAlive())
        {
            player = new Player(app, delayMs);
            
            player.start();
        }
    }
    
    private void pause()
    {
        if (player != null)
        {
            player.doBreak();
        }
    }
    
    private void step()
    {
        if (player != null && player.isAlive())
        {
            player.doBreak();
        }
        
        try
        {
            app.get(Processor.class).step();
            app.get(RegisterView.class).repaint();
        } 
        catch (CpuTrap e)
        {
            app.get(Canvas.class).setMessage(e.toString());
        }
    }
    
    private void addKeyboardShortcuts()
    {
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("F5"),
                "run");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("F6"),
                "break");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("F8"),
                "step");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("F7"),
                "reset");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("F2"),
                "previous");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("F3"),
                "next");
        
        this.getActionMap().put("run", new AbstractAction() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                play();
            }
        });
        this.getActionMap().put("break", new AbstractAction() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                pause();
            }
        });
        this.getActionMap().put("step", new AbstractAction() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                step();
            }
        });
        this.getActionMap().put("reset", new AbstractAction() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                reset();
            }
        });
        this.getActionMap().put("previous", new AbstractAction() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                skipLevel(-1);
            }
        });
        this.getActionMap().put("previous", new AbstractAction() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                skipLevel(1);
            }
        });
    }
    
    private void createSpeedCtrl()
    {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 2000, 1500);
        slider.setPreferredSize(new Dimension(300, 40));
        slider.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e)
            {
                int value = slider.getValue();
                if (value <= 1000)
                {
                    delayMs = 5000 - 4 * value;
                }
                else
                {
                    value -= 1001;
                    delayMs = 1000 - value;
                }
                if (player != null)
                {
                    player.setDelay(delayMs);
                }
            }
        });
        slider.setBackground(Color.BLACK);
        add(slider);
    }
    
    private static class Player extends Thread
    {
        private final Processor processor;
        private final RegisterView view;
        private final Canvas canvas;
        private int delayMs;
        private boolean goOn;
        
        public Player(AppGlobals app, int delayMs)
        {
            this.processor = app.get(Processor.class);
            this.view = app.get(RegisterView.class);
            this.canvas = app.get(Canvas.class);
            this.delayMs = delayMs;
            this.goOn = true;
        }
        
        public void doBreak()
        {
            this.goOn = false;
        }
        
        public void setDelay(int delayMs)
        {
            this.delayMs = delayMs;
        }
        
        @Override
        public void run()
        {
            try
            {
                while (goOn)
                {
                    processor.step();
                    view.repaint();
                    
                    TimeUnit.MILLISECONDS.sleep(delayMs);
                }
            }
            catch (InterruptedException inte)
            {
                inte.printStackTrace();
            } 
            catch (CpuTrap cte)
            {
                canvas.setMessage(cte.toString());
            }
        }
    }
}

