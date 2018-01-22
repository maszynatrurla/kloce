package pl.maszynatrurla.kloce.editor;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import pl.maszynatrurla.kloce.AppGlobals;
import pl.maszynatrurla.kloce.Image;

public class EditorWindow 
{
	private JFrame frame;

	public void open()
	{
        this.frame = new JFrame("--{KLOCE}-- Level Editor");
        
        frame.setSize(800, 600);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(4, 4));
        
        final AppGlobals globals = AppGlobals.getInstance();
        globals.set(new Image(40, 20));
        
        ActionBar actionBar = new ActionBar(globals);
        Canvas canvas = new Canvas(globals);
        Palette palette = new Palette(globals);
        
        globals.set(actionBar);
        globals.set(canvas);
        globals.set(palette);
        globals.set(new CommandStack());
        
        actionBar.create();
        canvas.create();
        palette.create();
        
        frame.add(actionBar, BorderLayout.PAGE_START);
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(palette, BorderLayout.PAGE_END);
        
        frame.setVisible(true);
	}
}
