package pl.maszynatrurla.kloce.game;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Vector;

import pl.maszynatrurla.kloce.Image;

public class Levels
{
    private final Vector<String> fileNames = new Vector<String>();
    private int index;
    
    public void openDir(File directory)
    {
        fileNames.clear();
        index = 0;
        
        for (File file : directory.listFiles(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String name)
            {
                return name.toLowerCase().endsWith(".kloc");
            }
        }))
        {
            try
            {
                Image.load(file);
                fileNames.add(file.getAbsolutePath());
            }
            catch (IOException ioe)
            {
                // ignore
            }
        }

        fileNames.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2)
            {
                return o1.compareToIgnoreCase(o2); 
            }
        });
    }
    
    public String [] getNames()
    {
        String [] names = new String [fileNames.size()];
        int idx = 0;
        
        for (String fileName : fileNames)
        {
            names[idx++] = new File(fileName).getName();
        }
        return names;
    }
    
    public String next()
    {
        ++index;
        if (index >= fileNames.size())
        {
            index = 0;
        }
        
        if (index < fileNames.size())
        {
            return fileNames.get(index);
        }
        
        return null;
    }
    
    public String previous()
    {
        --index;
        if (index < 0)
        {
            index = fileNames.size() - 1;
        }
        
        if (index < fileNames.size())
        {
            return fileNames.get(index);
        }
        
        return null;
    }
    
    public String get()
    {
        if (index < fileNames.size())
        {
            return fileNames.get(index);
        }
        return null;
    }
    
    public String get(int index)
    {
        this.index = index;
        return fileNames.get(index);
    }
}
