package pl.maszynatrurla.kloce.game;

public class InvalidOpTrap extends CpuTrap
{

    public InvalidOpTrap(CpuSnapshot snapshot, String op)
    {
        super(snapshot, String.format("Invalid operand: \"%s\"", op));
    }

    private static final long serialVersionUID = 1L;

}
