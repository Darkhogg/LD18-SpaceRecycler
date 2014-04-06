package es.darkhogg.ld18;

public final class Player {

    public double rotation = 0;
    public double rotationSpeed = 0;

    public double posX = 0;
    public double posY = 0;
    public double spdX = 0;
    public double spdY = 0;

    public static int MAX_ENERGY = 500;
    public double energy = MAX_ENERGY * 7 / 8;

    public boolean alive = true;

    public void tick () {
        rotationSpeed *= 0.93;
        rotation += rotationSpeed;

        spdX *= 0.96;
        spdY *= 0.96;
        posX += spdX;
        posY += spdY;

        energy += MAX_ENERGY / (2400.0);
        energy = Math.min(MAX_ENERGY, energy);

        if (energy <= 0) {
            alive = false;
        }

        if (rotation > Math.PI) {
            rotation -= Math.PI * 2;
        } else if (rotation < -Math.PI) {
            rotation += Math.PI * 2;
        }
    }
}
