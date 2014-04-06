package es.darkhogg.ld18;

public final class Star {

    public double x, y, mult;
    Game game;

    public Star (Game game) {
        this.game = game;
        x = Math.random() * Game.WIDTH;
        y = Math.random() * Game.HEIGHT;
        mult = 0.2 + Math.random() * 0.7;
    }

    public void tick () {
        x -= game.player.spdX * mult;
        y -= game.player.spdY * mult;

        // precision problems, ugh, let's fix them
        if (x > Game.WIDTH) {
            x -= Game.WIDTH;
        } else if (x < 0) {
            x += Game.WIDTH;
        }

        if (y > Game.HEIGHT) {
            y -= Game.HEIGHT;
        } else if (y < 0) {
            y += Game.HEIGHT;
        }
    }
}
