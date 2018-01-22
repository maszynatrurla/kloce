package pl.maszynatrurla.kloce.editor;

import java.util.Collection;
import java.util.Vector;

public class CompositeCommand implements Command
{
    private final Vector<Command> commands = new Vector<Command>();
    
    public CompositeCommand(final Collection<Command> cmds)
    {
        commands.addAll(cmds);
    }
    
    @Override
    public void perform()
    {
        for (Command cmd: commands)
        {
            cmd.perform();
        }
    }

    @Override
    public void invert()
    {
        for (Command cmd : commands)
        {
            cmd.invert();
        }
    }

}
