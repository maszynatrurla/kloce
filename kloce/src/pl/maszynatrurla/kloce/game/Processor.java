package pl.maszynatrurla.kloce.game;


public class Processor
{
    
    private final CpuMemory memory = new CpuMemory();
    private final Robot robot;
    
    private Executable code = new EmptyCode();
    private int pc;
    private int ptr;
    private int inp;
    private int out;
    
    public Processor(final Robot robot)
    {
        this.robot = robot;
    }
    
    
    public void load(final Executable code)
    {
        this.code = code;
    }

    public void reset()
    {
        this.pc = 0;
        this.ptr = 0;
        this.out = 0;
        this.inp = 0;
        memory.reset();
        robot.setOutputValue((short) 0);
    }
    
    public void step() throws CpuTrap
    {
        try
        {
            short asm = code.getCode(this.pc);
            
            switch (asm)
            {
            case Asm.INC :
                setMemory(ptr, (short) 1);
                break;
            case Asm.AD2 :
                setMemory(ptr, (short) 2);
                break;
            case Asm.DEC :
                setMemory(ptr, (short) -1);
                break;
            case Asm.AD4 :
                setMemory(ptr, (short) 4);
                break;
            case Asm.MUP :
                incPtr();
                break;
            case Asm.MDN :
                decPtr();
                break;
            case Asm.IN  :
                inp = robot.getInputValue();
                memory.put(ptr, (short) inp);
                break;
            case Asm.OUT :
                out = memory.get(ptr);
                robot.setOutputValue((short) out);
                break;
            case Asm.JZ  :
                jumpForward();
                break;
            case Asm.JNZ :
                jumpBack();
                break;
            case Asm.GO  :
                go();
                break;
            case Asm.LT  :
                robot.left();
                break;
            case Asm.RT  :
                robot.right();
                break;
            default:
                throw new InvalidOpTrap(getState(), Short.toString(asm));
            }
            
            incPc();
        }
        catch (OutOfCodeException e) 
        {
            throw new CodeEndedTrap(getState());
        }
        catch (InvalidTokenException te)
        {
            throw new InvalidOpTrap(getState(), te.getToken());
        }
    }
    
    public CpuSnapshot getState()
    {
        return new CpuSnapshot(pc, ptr, inp, out);
    }
    
    public CpuMemory getMemory()
    {
        return this.memory;
    }
    
    private void jumpForward() throws OutOfCodeException, InvalidTokenException, NumericOverflowTrap
    {
        if (0 == memory.get(ptr))
        {
            int braces = 1;
            
            while (braces > 0)
            {
                incPc();
                if (Asm.JZ == code.getCode(pc))
                {
                    ++braces;
                }
                else if (Asm.JNZ == code.getCode(pc))
                {
                    --braces;
                }
            }
        }
    }
    
    private void go() throws CpuTrap
    {
        try
        {
            robot.go();
        }
        catch (RobotOffCourseException roce)
        {
            throw new InvalidMoveTrap(getState(), "Robot has crashed");
        } 
        catch (RobotObjectiveAccomplishedEvent roae)
        {
            throw new CodeEndedTrap(getState());
        }
    }
    
    private void jumpBack() throws OutOfCodeException, InvalidTokenException, NumericOverflowTrap
    {
        if (memory.get(ptr) != 0)
        {
            int braces = 1;
            
            while (braces > 0)
            {
                decPc();
                if (Asm.JZ == code.getCode(pc))
                {
                    --braces;
                }
                else if (Asm.JNZ == code.getCode(pc))
                {
                    ++braces;
                }
            }
        }
    }
    
    private void setMemory(int address, short addValue) throws NumericOverflowTrap
    {
        int newValue = memory.get(address) + addValue;
        
        if (newValue > Short.MAX_VALUE || newValue < Short.MIN_VALUE)
        {
            throw new NumericOverflowTrap(getState(), newValue);
        }
        memory.put(address, (short) newValue);
    }
    
    private void incPtr() throws NumericOverflowTrap
    {
        ptr += 1;
        if (ptr > Short.MAX_VALUE)
        {
            throw new NumericOverflowTrap(getState(), ptr);
        }
    }

    private void decPtr() throws NumericOverflowTrap
    {
        ptr -= 1;
        if (ptr < Short.MIN_VALUE)
        {
            throw new NumericOverflowTrap(getState(), ptr);
        }
    }
    
    private void incPc() throws NumericOverflowTrap
    {
        pc += 1;
        if (pc > Short.MAX_VALUE)
        {
            throw new NumericOverflowTrap(getState(), pc);
        }
    }
    
    private void decPc() throws NumericOverflowTrap
    {
        pc -= 1;
        if (pc < Short.MIN_VALUE)
        {
            throw new NumericOverflowTrap(getState(), pc);
        }
    }
    
    private static class EmptyCode implements Executable
    {
        @Override
        public short getCode(int pc) throws OutOfCodeException
        {
            throw new OutOfCodeException(pc);
        }
    }
}
