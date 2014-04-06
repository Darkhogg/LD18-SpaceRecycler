package es.darkhogg.ld18;

import java.awt.Image;

public final class EnemyBullet extends DotBullet {

    private int ticks = 0;
    private int life = 300;

    public EnemyBullet (Game game, double x, double y, double spdX, double spdY) {
        super(game, x, y, spdX, spdY);
    }

    @Override
    public Image getImage () {
        if (life > 60 || (ticks / 2) % 2 == 0) {
            return Bitmap.ENEMY_BULLET.image;
        } else {
            return null;
        }
    }

    @Override
    public void doTick () {
        ticks++;

        if (spdX * spdX + spdY * spdY < 1) {
            life--;
        }
        if (life < 0) {
            remove = true;
        }
    }

    @Override
    public boolean canHurt () {
        return ticks > 10;
    }
}
