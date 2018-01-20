package pl.maszynatrurla.kloce.game;

public class InvalidMoveTrap extends CpuTrap
{

    private static final long serialVersionUID = 1L;
    
    public InvalidMoveTrap(CpuSnapshot snapshot, String message)
    {
        super(snapshot, message);
    }    

}
