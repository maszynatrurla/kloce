package pl.maszynatrurla.kloce.game;

public class CpuTrap extends Exception
{

    private static final long serialVersionUID = 1L;
    
    private final CpuSnapshot snapshot;
    
    public CpuTrap(CpuSnapshot snapshot, String message)
    {
        super(message);
        
        this.snapshot = snapshot;
    }
    
    public CpuSnapshot getCpuState()
    {
        return this.snapshot;
    }

}
