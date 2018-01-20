package pl.maszynatrurla.kloce.game;

public interface Executable
{
    short getCode(int pc) throws OutOfCodeException, InvalidTokenException;
}
