package es.darkhogg.ld18;

import java.awt.Image;
import java.util.Collection;
import java.util.HashSet;

public final class TriangleEnemy extends Enemy {

    long ticks, lastShoot;

    public TriangleEnemy (Game game, double x, double y) {
        super(game);

        posX = x;
        posY = y;

        energy = 250;
        rotationSpeed = (0.05 + Math.random() * 0.2) * (Math.random() < 5 ? 1 : -1);

        double ang = Math.atan2(game.player.posX - posX, -(game.player.posY - posY));
        spdX = Math.sin(ang);
        spdY = -Math.cos(ang);
    }

    @Override
    public void doTick () {
        ticks++;
    }

    @Override
    public Collection<Bullet> getBullets () {
        if (ticks - lastShoot >= Game.FPS * 1.5) {
            lastShoot = ticks;

            Collection<Bullet> shoots = new HashSet<Bullet>();
            double spd = 1.0;
            double ang = Math.random() * 2 * Math.PI;
            for (int i = 0; i < 3; i++) {
                Bullet bul =
                    new EnemyBullet(game, posX, posY, spd * Math.sin(ang + i * (Math.PI * 2 / 3)) + spdX, -spd
                        * Math.cos(ang + i * (Math.PI * 2 / 3)) + spdY);
                shoots.add(bul);
            }

            return shoots;
        }

        return null;
    }

    @Override
    public int getEnergyDots () {
        return 7;
    }

    @Override
    public Image getImage () {
        return Bitmap.ENEMY_TRIANGLE.image;
    }

    @Override
    public int getMass () {
        return 25;
    }

    @Override
    public double getRadius () {
        return 10;
    }

    @Override
    public int getScore () {
        return 100;
    }

}
