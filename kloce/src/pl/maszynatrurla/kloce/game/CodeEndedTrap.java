package pl.maszynatrurla.kloce.game;

public class CodeEndedTrap extends CpuTrap
{

    private static final long serialVersionUID = 1L;

    public CodeEndedTrap(CpuSnapshot snapshot)
    {
        super(snapshot, "END");
    }
    
    public CodeEndedTrap(CpuSnapshot snapshot, String message)
    {
        super(snapshot, message);
    }

}
