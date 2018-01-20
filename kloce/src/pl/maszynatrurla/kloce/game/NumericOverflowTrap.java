package pl.maszynatrurla.kloce.game;

public class NumericOverflowTrap extends CpuTrap
{
    private static final long serialVersionUID = 1L;
    
    public NumericOverflowTrap(CpuSnapshot snapshot, int value)
    {
        super(snapshot, "Numeric overflow - value = " + value);
    }
}
