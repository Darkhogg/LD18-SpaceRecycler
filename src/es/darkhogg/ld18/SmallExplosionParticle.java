package es.darkhogg.ld18;

import java.awt.Image;

public final class SmallExplosionParticle extends Particle {

    public SmallExplosionParticle (Game game, double x, double y, int life) {
        super(game);
        posX = x;
        posY = y;
        this.life = life;
    }

    @Override
    public void doTick () {
        // TODO Auto-generated method stub

    }

    @Override
    public Image getImage () {
        final int DUR = 2;
        if (life < DUR) {
            return Bitmap.EXPL_SMALL_6.image;
        } else if (life < DUR * 2) {
            return Bitmap.EXPL_SMALL_5.image;
        } else if (life < DUR * 3) {
            return Bitmap.EXPL_SMALL_4.image;
        } else if (life < DUR * 4) {
            return Bitmap.EXPL_SMALL_3.image;
        } else if (life < DUR * 5) {
            return Bitmap.EXPL_SMALL_2.image;
        } else if (life < DUR * 6) {
            return Bitmap.EXPL_SMALL_1.image;
        } else {
            return null;
        }
    }

}
