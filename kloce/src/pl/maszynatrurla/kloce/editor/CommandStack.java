package pl.maszynatrurla.kloce.editor;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class CommandStack
{
    private final Stack<Command> undoStack = new Stack<Command>();
    private final Stack<Command> redoStack = new Stack<Command>();
    
    private final Vector<Command> joined = new Vector<Command>();
    private Timer timer = new Timer();
    
    public void performCommand(Command cmd)
    {
        synchronized (joined)
        {
            if (!joined.isEmpty())
            {
                timer.cancel();
                if (!joined.isEmpty())
                {
                    Command joinCmd = new CompositeCommand(joined);
                    undoStack.push(joinCmd);
                    joined.clear();
                }
            }
        }
        cmd.perform();
        cmd.invert();
        undoStack.push(cmd);
    }
    
    public void performJoinableCommand(Command cmd)
    {
        cmd.perform();
        cmd.invert();
        synchronized (joined)
        {
            if (joined.isEmpty())
            {
                joined.add(cmd);
                timer.schedule(new MyTimerTask(), 200);
            }
            else
            {
                joined.add(cmd);
            }
        }
    }
    
    public void undo()
    {
        synchronized (joined)
        {
            if (!joined.isEmpty())
            {
                timer.cancel();
                if (!joined.isEmpty())
                {
                    Command cmd = new CompositeCommand(joined);
                    undoStack.push(cmd);
                    joined.clear();
                }
            }
        }
        if (!undoStack.isEmpty())
        {
            Command cmd = undoStack.pop();
            cmd.perform();
            cmd.invert();
            redoStack.push(cmd);
        }
    }
    
    public void redo()
    {
        synchronized (joined)
        {
            if (!joined.isEmpty())
            {
                timer.cancel();
                if (!joined.isEmpty())
                {
                    Command cmd = new CompositeCommand(joined);
                    undoStack.push(cmd);
                    joined.clear();
                }
            }
        }
        if (!redoStack.isEmpty())
        {
            Command cmd = redoStack.pop();
            cmd.perform();
            cmd.invert();
            undoStack.push(cmd);
        }
    }
    
    public void clear()
    {
        synchronized (joined)
        {
            if (!joined.isEmpty())
            {
                timer.cancel();
                if (!joined.isEmpty())
                {
                    Command joinCmd = new CompositeCommand(joined);
                    undoStack.push(joinCmd);
                    joined.clear();
                }
            }
        }
        this.redoStack.clear();
        this.undoStack.clear();
    }
    
    private class MyTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            synchronized(joined)
            {
                Command cmd = new CompositeCommand(joined);
                undoStack.push(cmd);
                joined.clear();
            }
        }
    }
}
