package pl.maszynatrurla.kloce.game;

public class InvalidTokenException extends Exception
{

    private static final long serialVersionUID = 1L;
    
    private final String token;

    public InvalidTokenException(final String token)
    {
        super(String.format("Invalid token \"%s\"", token));
        this.token = token;
    }
    
    public String getToken()
    {
        return token;
    }
}
