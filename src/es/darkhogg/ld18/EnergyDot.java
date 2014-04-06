package es.darkhogg.ld18;

public final class EnergyDot {

    public double rotation;
    public double posX;
    public double posY;
    public int energy;
    public double spdX;
    public double spdY;
    public Game game;
    public boolean remove = false;

    public EnergyDot (Game game, double x, double y) {
        this.game = game;

        posX = x;
        posY = y;

        double ang = Math.random() * 2 * Math.PI;
        double pow = 3 + Math.random() * 3;

        spdX = pow * Math.sin(ang);
        spdY = -pow * Math.cos(ang);
    }

    public void tick () {
        posX += spdX;
        posY += spdY;

        spdX *= 0.9;
        spdY *= 0.9;

        double difX = game.player.posX - posX;
        double difY = game.player.posY - posY;
        double sqdist = difX * difX + difY * difY;
        double angle = Math.atan2(difX, -difY);
        spdX += 0.6 * Math.sin(angle);
        spdY -= 0.6 * Math.cos(angle);

        if (sqdist < 256) {
            remove = true;
        }
    }

}
