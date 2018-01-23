package pl.maszynatrurla.kloce;

import java.util.EnumSet;

public enum Tile
{
    EMPTY,
    START,
    PLATFORM,
    BATTERY,
    INPUT_A,
    INPUT_B,
    OUTPUT,
    ROBOT_FRONT,
    ROBOT_LEFT,
    ROBOT_RIGHT,
    ROBOT_BACK,
    ;
    
    private static final EnumSet<Tile> START_TILES = EnumSet.of(
            Tile.START, Tile.ROBOT_BACK, Tile.ROBOT_FRONT,
            Tile.ROBOT_LEFT, Tile.ROBOT_RIGHT);
    
    public boolean isStartTile()
    {
        return START_TILES.contains(this);
    }
}
