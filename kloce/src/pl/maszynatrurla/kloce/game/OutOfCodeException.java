package pl.maszynatrurla.kloce.game;

public class OutOfCodeException extends Exception
{

    private static final long serialVersionUID = 1L;
    
    private final int pc;

    public OutOfCodeException(int pcValue)
    {
        super("Out of code");
        this.pc = pcValue;
    }
    
    public int getProgramCounter()
    {
        return pc;
    }
}
