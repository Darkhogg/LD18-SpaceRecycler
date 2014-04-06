package es.darkhogg.ld18;

import java.awt.Image;

public abstract class Bullet {

    public double posX;
    public double posY;
    public double rotation;
    public double spdX;
    public double spdY;
    public double rotationSpeed;
    public Game game;
    public boolean remove;

    public Bullet (Game game) {
        this.game = game;
    }

    public void tick () {
        posX += spdX;
        posY += spdY;
        rotation += rotationSpeed;
        doTick();

        // If it's too far away (more than 1000 pixels it's pretty much enough,
        // ships that far will probably disappear...
        double difX = posX - game.player.posX;
        double difY = posY - game.player.posY;
        if ((difX * difX + difY * difY) > 1000000) {
            remove = true;
        }
    }

    public abstract void doTick ();

    public abstract Image getImage ();

    public boolean canHurt () {
        return true;
    }
}
