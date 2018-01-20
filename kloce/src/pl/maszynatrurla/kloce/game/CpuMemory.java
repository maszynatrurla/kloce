package pl.maszynatrurla.kloce.game;

import java.util.HashMap;
import java.util.Map;

public class CpuMemory
{
    private final Map<Integer, Short> memory = new HashMap<Integer, Short>();

    public void reset()
    {
        memory.clear();
    }

    public short get(int address)
    {
        return memory.getOrDefault(Integer.valueOf(address), Short.valueOf((short) 0));
    }
    
    public void put(int address, short value)
    {
        memory.put(Integer.valueOf(address), Short.valueOf(value));
    }
}
