package pl.maszynatrurla.kloce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

public class Image
{
    
    public static Image load(File inputFile) throws IOException
    {
        FileReader reader = new FileReader(inputFile);
        BufferedReader breader = new BufferedReader(reader);
        
        try
        {
            String magic = breader.readLine();
            if (!magic.equals("KLOCE")) {
                throw new IOException("Invalid format");
            }
            int cols = 0;
            Vector<Tile> tiles = new Vector<Tile>();
            int colsInRow = 0;
            int rows = 0;
            
            while (true)
            {
                int c = breader.read();
                
                if (c < 0)
                {
                    break;
                }
                
                switch (c)
                {
                case '\n':
                    if (cols == 0)
                    {
                        cols = colsInRow;
                    }
                    else if (cols != colsInRow)
                    {
                        throw new IOException("Invalid table dimensions");
                    }
                    colsInRow = 0;
                    ++rows;
                    break;
                case 'b':
                    tiles.add(Tile.BATTERY);
                    ++colsInRow;
                    break;
                case '_':
                    tiles.add(Tile.EMPTY);
                    ++colsInRow;
                    break;
                case 'A':
                    tiles.add(Tile.INPUT_A);
                    ++colsInRow;
                    break;
                case 'B':
                    tiles.add(Tile.INPUT_B);
                    ++colsInRow;
                    break;
                case 'O':
                    tiles.add(Tile.OUTPUT);
                    ++colsInRow;
                    break;
                case '#':
                    tiles.add(Tile.PLATFORM);
                    ++colsInRow;
                    break;
                case 'S':
                    tiles.add(Tile.START);
                    ++colsInRow;
                    break;
                case '1':
                    tiles.add(Tile.ROBOT_BACK);
                    ++colsInRow;
                    break;
                case '2':
                    tiles.add(Tile.ROBOT_FRONT);
                    ++colsInRow;
                    break;
                case '3':
                    tiles.add(Tile.ROBOT_LEFT);
                    ++colsInRow;
                    break;
                case '4':
                    tiles.add(Tile.ROBOT_RIGHT);
                    ++colsInRow;
                    break;
                default:
                    throw new IOException("Unexpected byte");
                }
            }
            if (rows != tiles.size() / cols)
            {
                throw new IOException("Invalid last row");
            }
            Image image = new Image(cols, rows);
            for (int y = 0; y < rows; ++y)
            {
                for (int x = 0; x < cols; ++x)
                {
                    image.setTile(x, y, tiles.get(cols * y + x));
                }
            }
            
            return image;
        }
        finally
        {
            breader.close();
        }
    }
    
    
    

    private int xlen;
    private int ylen;
    
    private Tile image [];
    
    public Image(int xlen, int ylen)
    {
        this.xlen = xlen;
        this.ylen = ylen;
        
        this.image = new Tile [xlen * ylen];
        zero();
    }
    
    public Image(Image other)
    {
        this.xlen = other.xlen;
        this.ylen = other.ylen;
        this.image = Arrays.copyOf(other.image, other.image.length);
    }
    
    public void store(File outputFile) throws IOException
    {
        FileWriter writer = new FileWriter(outputFile);
        
        try
        {
            writer.write("KLOCE\n");
            for (int yidx = 0; yidx < getHeight(); ++ yidx)
            {
                for (int xidx = 0; xidx < getLength(); ++xidx)
                {
                    switch (getTile(xidx, yidx))
                    {
                    case BATTERY:
                        writer.write('b');
                        break;
                    case EMPTY:
                        writer.write('_');
                        break;
                    case INPUT_A:
                        writer.write('A');
                        break;
                    case INPUT_B:
                        writer.write('B');
                        break;
                    case OUTPUT:
                        writer.write('O');
                        break;
                    case PLATFORM:
                        writer.write('#');
                        break;
                    case START:
                        writer.write('S');
                        break;
                    case ROBOT_BACK:
                        writer.write('1');
                        break;
                    case ROBOT_FRONT:
                        writer.write('2');
                        break;
                    case ROBOT_LEFT:
                        writer.write('3');
                        break;
                    case ROBOT_RIGHT:
                        writer.write('4');
                        break;
                    default:
                        break;
                    
                    }
                }
                writer.write('\n');
            }
        }
        finally
        {
            writer.close();
        }
    }
    
    public int getLength()
    {
        return xlen;
    }
    
    public int getHeight()
    {
        return ylen;
    }
    
    public void setTile(int x, int y, Tile tile)
    {
        this.image[xlen * y + x] = tile;
    }
    
    public Tile getTile(int x, int y)
    {
        return this.image[xlen * y + x];
    }
    
    public void zero()
    {
        for (int idx = 0; idx < image.length; ++idx)
        {
            this.image[idx] = Tile.EMPTY;
        }
    }
    
    public void extend()
    {
        xlen += 2;
        ylen += 2;
        
        Tile [] newImage = new Tile [xlen * ylen];
        
        for (int idx = 0; idx < xlen; ++idx)
        {
            newImage[idx] = Tile.EMPTY;
            newImage[xlen * (ylen - 1) + idx] = Tile.EMPTY;
        }
        
        for (int yidx = 0; yidx < ylen - 2; ++yidx)
        {
            newImage[(yidx + 1) * xlen] = Tile.EMPTY;
            
            for (int xidx = 0; xidx < xlen - 2; ++xidx)
            {
                newImage[(yidx + 1) * xlen + xidx + 1] = image[yidx * (xlen - 2) + xidx];
            }
            
            newImage[(yidx + 1) * xlen + xlen - 1] = Tile.EMPTY;
        }
        
        this.image = newImage;
    }
    
    public void trim(int startX, int startY, int endX, int endY)
    {
        if (startX > endX)
        {
            int tmp = startX;
            startX = endX;
            endX = tmp;
        }
        if (startY > endY)
        {
            int tmp = startY;
            startY = endY;
            endY = tmp;
        }
        int newxlen = endX - startX + 1;
        int newylen = endY - startY + 1;
        
        if (newxlen < 4 || newylen < 4)
        {
            return;
        }
        
        Tile [] newimage = new Tile [newxlen * newylen]; 
        
        for (int yidx = 0; yidx < newylen; ++yidx)
        {
            for (int xidx = 0; xidx < newxlen; ++xidx)
            {
                newimage[newxlen * yidx + xidx] = image[xlen * (yidx + startY) + startX + xidx];
            }
        }
        image = newimage;
        xlen = newxlen;
        ylen = newylen;
        
    }
    
    public void invert()
    {
        for (int idx = 0; idx < image.length; ++idx)
        {
            if (Tile.EMPTY == image[idx])
            {
                image[idx] = Tile.PLATFORM;
            }
            else if (Tile.PLATFORM == image[idx])
            {
                image[idx] = Tile.EMPTY;
            }
        }
    }

}
