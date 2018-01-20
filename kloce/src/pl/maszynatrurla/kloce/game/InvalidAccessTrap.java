package pl.maszynatrurla.kloce.game;

public class InvalidAccessTrap extends CpuTrap
{
    private static final long serialVersionUID = 1L;
    
    public InvalidAccessTrap(CpuSnapshot snapshot)
    {
        super(snapshot, "Invalid memory access");
    }

}
