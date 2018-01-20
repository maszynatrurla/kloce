package pl.maszynatrurla.kloce.game;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import pl.maszynatrurla.kloce.AppGlobals;


public class GameWindow
{
    private JFrame frame;

    public void open()
    {
        this.frame = new JFrame("--{KLOCE}--");
        
        frame.setSize(800, 600);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        AppGlobals globals = AppGlobals.getInstance();
        
        ActionBar actionBar = new ActionBar(globals);
        RegisterView registerView = new RegisterView(globals);
        Canvas canvas = new Canvas(globals);
        CodeView codeView = new CodeView(globals);
        ScreenRobot robot = new ScreenRobot(globals);
        Processor cpu = new Processor(robot);
        
        globals.set(actionBar);
        globals.set(registerView);
        globals.set(canvas);
        globals.set(codeView);
        globals.set(cpu);
        globals.set(robot);
        
        actionBar.create();
        registerView.create();
        canvas.create();
        codeView.create();
        
        frame.add(actionBar, BorderLayout.PAGE_START);
        frame.add(registerView, BorderLayout.LINE_START);
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(codeView, BorderLayout.PAGE_END);
        
        frame.setVisible(true);
    }
}
