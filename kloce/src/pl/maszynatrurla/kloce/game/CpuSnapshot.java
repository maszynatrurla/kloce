package pl.maszynatrurla.kloce.game;

public class CpuSnapshot
{
    public final int pc;
    public final int ptr;
    public final int in;
    public final int out;
    
    public CpuSnapshot(final int pc, final int ptr, final int inp, final int out)
    {
        this.pc = pc;
        this.ptr = ptr;
        this.in = inp;
        this.out = out;
    }
    
    public String toString()
    {
        return String.format("PC:%d Ptr:%d", pc, ptr);
    }
}
