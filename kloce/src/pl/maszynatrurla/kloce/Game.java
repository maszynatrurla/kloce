package pl.maszynatrurla.kloce;

import pl.maszynatrurla.kloce.game.GameWindow;

public class Game
{
    
    public void launch()
    {
        new GameWindow().open();
    }

    public static void main(String[] args)
    {
        Game app = new Game();
        app.launch();
    }

}
