package pl.maszynatrurla.kloce.game;

import java.util.HashMap;
import java.util.Map;

public class CodeStats
{
    private final Map<Asm, Integer> codeStats = new HashMap<Asm, Integer>();
    private final Map<Asm, Integer> codeLimits = new HashMap<Asm, Integer>();
    
    public void setLimit(Asm op, Integer limit)
    {
        this.codeLimits.put(op, limit);
    }
    
    public void resetLimits()
    {
        this.codeLimits.clear();
    }
    
    public void reset()
    {
        this.codeStats.clear();
    }
    
    public void add(Asm op)
    {
        this.codeStats.put(op, this.codeStats.getOrDefault(op, Integer.valueOf(0))
                + Integer.valueOf(1));
    }
    
    public void remove(Asm op)
    {
        Integer value = this.codeStats.getOrDefault(op, Integer.valueOf(0));
        if (value > 0)
        {
            this.codeStats.put(op, value + 1);
        }
    }
    
    public int getLimit(Asm op)
    {
        if (this.codeLimits.containsKey(op))
        {
            int limit = this.codeLimits.get(op);
            if (0 == limit)
            {
                return 0;
            }
            else
            {
                int present = this.codeStats.get(op);
                if (limit - present > 0)
                {
                    return limit - present;
                }
                else
                {
                    return 0;
                }
            }
        }
        else
        {
            return -1;
        }
    }
}
