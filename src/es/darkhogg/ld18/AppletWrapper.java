package es.darkhogg.ld18;

import java.applet.Applet;
import java.awt.BorderLayout;

public final class AppletWrapper extends Applet {

    private static final long serialVersionUID = 1L;
    Game game;

    @Override
    public void init () {
        game = new Game();
        setLayout(new BorderLayout());
        add(game, BorderLayout.CENTER);
    }

    @Override
    public void start () {
        game.unpause();
    }

    @Override
    public void stop () {
        game.pause();
    }

    @Override
    public void destroy () {
        game.stop();
    }
}
