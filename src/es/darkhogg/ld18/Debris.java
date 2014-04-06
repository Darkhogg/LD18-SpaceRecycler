package es.darkhogg.ld18;

import java.awt.Image;

public final class Debris {

    public double posX;
    public double posY;
    public double rotation;

    public double spdX;
    public double spdY;
    public double rotationSpeed;

    public Game game;
    public boolean remove;

    public int life;
    public Size size;
    private Image image;

    public boolean held = Math.random() < 0.4;

    static enum Size {
        SMALL(1), MEDIUM(6), LARGE(14);
        public final int mass;

        private Size (int mass) {
            this.mass = mass;
        }

        public static Size getRandom () {
            int value = (int) (Math.random() * 10);
            if (value < 2) {
                return Size.LARGE;
            } else if (value < 6) {
                return Size.MEDIUM;
            } else {
                return Size.SMALL;
            }
        }
    };

    public Debris (Game game, double x, double y, Size size, int life) {
        this.game = game;
        posX = x;
        posY = y;

        double ang = Math.random() * 2 * Math.PI;
        double spd = 0.05 + Math.random() * 0.6;

        spdX = spd * Math.sin(ang);
        spdY = -spd * Math.cos(ang);

        rotation = Math.random() * 2 * Math.PI;
        rotationSpeed = Math.random() * 1.4 - 0.7;

        this.life = life;
        this.size = size;

        Image[] selectFrom = new Image[] { null };
        switch (size) {
            case SMALL:
                selectFrom =
                    new Image[] {
                        Bitmap.DEB_SM_1.image, Bitmap.DEB_SM_2.image, Bitmap.DEB_SM_3.image, Bitmap.DEB_SM_4.image };
                break;
            case MEDIUM:
                selectFrom =
                    new Image[] {
                        Bitmap.DEB_MD_1.image, Bitmap.DEB_MD_2.image, Bitmap.DEB_MD_3.image, Bitmap.DEB_MD_4.image };
                break;
            case LARGE:
                selectFrom =
                    new Image[] {
                        Bitmap.DEB_LG_1.image, Bitmap.DEB_LG_2.image, Bitmap.DEB_LG_3.image, Bitmap.DEB_LG_4.image,
                        Bitmap.DEB_LG_5.image };
                break;
        }

        image = selectFrom[(int) Math.floor(Math.random() * selectFrom.length)];
    }

    public void tick () {
        if (held && game.playing) {
            double difX = game.attrX - posX;
            double difY = game.attrY - posY;
            double sqdist = difX * difX + difY * difY;
            double angle = Math.atan2(difX, -difY);

            double pow = Math.min(3, 250 / sqdist);

            if (sqdist > 12 * 12) {
                spdX += pow * Math.sin(angle);
                spdY += -pow * Math.cos(angle);
            } else if (spdX * spdX + spdY * spdY > 1) {
                spdX *= 0.97;
                spdY *= 0.97;
            }

            life++;
            if (life < 90) {
                life++;
            }

            if (sqdist > 12500) {
                held = false;
            }
        }

        posX += spdX;
        posY += spdY;
        rotation += rotationSpeed;

        spdX *= 0.995;
        spdY *= 0.995;
        rotationSpeed *= 0.995;

        life--;
        if (life <= 0) {
            remove = true;
        }
    }

    public Image getImage () {
        return (life > 60 || (life / 2) % 2 == 0) ? image : null;
    }

    public double getMass () {
        return size.mass;
    }
}
