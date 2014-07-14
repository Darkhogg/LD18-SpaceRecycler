package es.darkhogg.ld18;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import es.darkhogg.ld18.Debris.Size;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    public static final int SCALE = 2;

    public static final int FPS = 60;
    public static double actualFps = 60;

    private volatile boolean requestStop;
    private volatile boolean paused = false;
    public boolean playing = false;
    private boolean gameOver = false;

    private final int[] intervals = { 120, 500, 3000 };
    private final int[] lasts = new int[3];
    private final int[] intmod = { 10, -20, -100 };
    private final int[] intmin = { 60, 120, 900 };

    private volatile boolean keyRight, keyLeft, keyUp, keyShoot, keyKill, keyRelease, keyAttract, keyScreenshot;
    private long screenshotTicks;

    public Player player;
    public Collection<Enemy> enemies = new HashSet<Enemy>();
    public Collection<Bullet> enemyBullets = new HashSet<Bullet>();
    public Collection<Bullet> playerBullets = new HashSet<Bullet>();
    public Collection<Particle> particles = new HashSet<Particle>();
    public Collection<Debris> debris = new HashSet<Debris>();
    public Collection<Star> stars = new ArrayList<Star>();
    public Collection<EnergyDot> dots = new HashSet<EnergyDot>();

    long ticks, lastShoot, lastAttract, lastRelease;

    long score;

    double attrX, attrY;

    private Thread runningThread;

    private long gameOverTime = -1000;

    public Game () {
        super();

        setSize(WIDTH * SCALE, HEIGHT * SCALE);
        addKeyListener(this);
        addMouseListener(this);

        requestFocus();
    }

    @Override
    public void run () {
        double tickTime = 1.0 / FPS;
        double lastTick = (System.nanoTime()) / 1000000000f;
        double now = 0;

        for (int s = 0; s < 100; s++) {
            stars.add(new Star(this));
        }
        
        // Trigger sound loading
        Sound.GRAB.toString();

        while (!requestStop) {
            synchronized (this) {
                if (!playing && keyShoot && ticks - gameOverTime > 60) {
                    playing = true;
                    gameOver = false;
                    ticks = 0;
                    lastShoot = 0;
                    lastAttract = Integer.MIN_VALUE;
                    lastRelease = Integer.MIN_VALUE;
                    lasts[0] = -intervals[0];
                    lasts[1] = -intervals[1] + 120;
                    lasts[2] = 0;
                    score = 0;

                    player = new Player();
                    enemies = new HashSet<Enemy>();
                    enemyBullets = new HashSet<Bullet>();
                    playerBullets = new HashSet<Bullet>();
                    particles = new HashSet<Particle>();
                    debris = new HashSet<Debris>();

                    System.gc();
                }
                if (!paused) {
                    // Wait tick
                    do {
                        now = (System.nanoTime()) / 1000000000f;
                        /*
                         * try { Thread.sleep( 1 ); } catch ( InterruptedException e ) { e.printStackTrace(); }
                         */
                    } while (now - lastTick < tickTime);

                    // tick and display!
                    tick();
                    display();

                    // set time
                    actualFps = 1 / (now - lastTick);
                    lastTick = now;
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void tick () {
        // Woo
        ticks++;
        if (playing && score > 0 && ticks % FPS == 0) {
            score--;
        }

        if (!gameOver && !playing) {
            // Tick stars
            for (Star star : stars) {
                star.y += star.mult * 3;
            }

            // Create explosions!
            if (ticks % 10 == 0) {
                Particle part =
                    new SmallExplosionParticle(this, Math.random() * (WIDTH - 30) - (WIDTH - 30) / 2, Math.random()
                        * (HEIGHT - 30) - (HEIGHT - 30) / 2, (int) (Math.random() * 100 + 12));
                particles.add(part);
            }
        }

        // Collision detection!
        // All-in-one enemy loop (as enemies are the main collision target...)
        if (playing) {
            for (Enemy enemy : enemies) {
                // enemies-playerBullets
                for (Bullet bullet : playerBullets) {
                    double difX = bullet.posX - enemy.posX;
                    double difY = bullet.posY - enemy.posY;
                    if ((difX * difX) + (difY * difY) <= enemy.getRadius() * enemy.getRadius()) {
                        enemy.energy -= 10;
                        bullet.remove = true;
                        Sound.HIT.play();
                        if (Math.random() < 0.45) {
                            debris.add(new Debris(this, enemy.posX + Math.random() * 8 - 4, enemy.posY
                                + Math.random() * 8 - 4, Size.SMALL, 1200));
                        }
                    }
                }
                // enemies-enemyBullets (YEAH, almost copy-paste)
                for (Bullet bullet : enemyBullets) {
                    if (bullet.canHurt()) {
                        double difX = bullet.posX - enemy.posX;
                        double difY = bullet.posY - enemy.posY;
                        if ((difX * difX) + (difY * difY) <= enemy.getRadius() * enemy.getRadius()) {
                            enemy.energy -= 10;
                            bullet.remove = true;
                            Sound.HIT.play();
                            if (Math.random() < 0.3) {
                                debris.add(new Debris(this, enemy.posX + Math.random() * 8 - 4, enemy.posY
                                    + Math.random() * 8 - 4, Size.SMALL, 1200));
                            }
                        }
                    }
                }
                // enemies-debris
                for (Debris deb : debris) {
                    double difX = deb.posX - enemy.posX;
                    double difY = deb.posY - enemy.posY;
                    if ((difX * difX) + (difY * difY) <= enemy.getRadius() * enemy.getRadius()) {
                        if (deb.spdX * deb.spdX + deb.spdY * deb.spdY > 4) {
                            deb.remove = true;

                            double spdDifX = deb.spdX - enemy.spdX;
                            double spdDifY = deb.spdY - enemy.spdY;
                            double sqSpeedDif = spdDifX * spdDifX + spdDifY * spdDifY;
                            double energy = deb.getMass() * sqSpeedDif;

                            enemy.energy -= energy;

                            Sound.HIT.play();
                        }
                    }
                }
                // enemies-player
                double difX = player.posX - enemy.posX;
                double difY = player.posY - enemy.posY;
                if ((difX * difX) + (difY * difY) <= 16 * 16) {
                    enemy.energy = 0;
                    player.energy -= Math.sqrt(enemy.getMass() * 10) * 10;
                    Sound.EXPLOSION.play();
                    Sound.HURT.play();
                }
            }
            // enemy bullets-player
            for (Bullet ebul : enemyBullets) {
                double difX = player.posX - ebul.posX;
                double difY = player.posY - ebul.posY;
                if ((difX * difX) + (difY * difY) <= 12 * 12) {
                    ebul.remove = true;
                    player.energy -= 15;
                    Sound.HURT.play();
                }
            }
        }

        // Change rotation
        if (keyLeft) {
            player.rotationSpeed -= 0.3 * Math.PI / 180;
        }
        if (keyRight) {
            player.rotationSpeed += 0.3 * Math.PI / 180;
        }

        // Move player
        if (keyUp) {
            player.spdX += 0.07 * Math.sin(player.rotation);
            player.spdY += -0.07 * Math.cos(player.rotation);
        }

        // Shoot
        if (playing && keyShoot && player.energy >= Player.MAX_ENERGY * 0.05 && ticks - lastShoot > FPS / 8) {
            final double ang = (Math.PI / 180) * 60;
            final int pow = 8;

            playerBullets.add(new DotBullet(this, player.posX + pow * Math.sin(player.rotation + ang), player.posY
                - pow * Math.cos(player.rotation + ang), 3.5 * Math.sin(player.rotation) + player.spdX, -3.5
                * Math.cos(player.rotation) + player.spdY));

            playerBullets.add(new DotBullet(this, player.posX + pow * Math.sin(player.rotation - ang), player.posY
                - pow * Math.cos(player.rotation - ang), 3.5 * Math.sin(player.rotation) + player.spdX, -3.5
                * Math.cos(player.rotation) + player.spdY));

            Sound.SHOOT.play();
            lastShoot = ticks;
            player.energy -= 5;
        }

        // Release debris
        if (playing && keyRelease) {
            if (ticks - lastRelease > FPS) {
                Sound.RELEASE.play();
                lastRelease = ticks;
            }

            double multiplier = 1.0;
            for (Debris deb : debris) {
                if (deb.held) {
                    double difX = attrX - deb.posX;
                    double difY = attrY - deb.posY;
                    double sqdist = difX * difX + difY * difY;
                    if (sqdist < 2048) {
                        double base = Math.min(5, 300 / sqdist);
                        double pow = multiplier * base;
                        deb.spdX += pow * Math.sin(player.rotation);
                        deb.spdY += -pow * Math.cos(player.rotation);
                        multiplier -= (deb.getMass() / 50) * multiplier * base;
                        multiplier = Math.max(0.0, multiplier);
                    }
                }
            }
        }

        // Grab debris
        if (playing && keyAttract && player.energy >= Player.MAX_ENERGY * 0.15 && ticks - lastAttract > FPS / 2) {
            Sound.GRAB.play();
            lastAttract = ticks;
            player.energy -= 15;
            for (Debris deb : debris) {
                double difX = player.posX - deb.posX;
                double difY = player.posY - deb.posY;
                double sqdist = difX * difX + difY * difY;
                double ang = Math.atan2(-difX, difY) - player.rotation;
                if (sqdist < 4096 || (sqdist < 256 * 256 && Math.abs(ang) < 0.3)) {
                    deb.held = true;
                }
            }
        }

        // Tick bullets
        Iterator<Bullet> ebit = enemyBullets.iterator();
        while (ebit.hasNext()) {
            Bullet ebul = ebit.next();

            ebul.tick();
            if (ebul.remove) {
                ebit.remove();
            }
        }
        Iterator<Bullet> pbit = playerBullets.iterator();
        while (pbit.hasNext()) {
            Bullet pbul = pbit.next();

            pbul.tick();
            if (pbul.remove) {
                pbit.remove();
            }
        }

        // Tick debris
        Iterator<Debris> dit = debris.iterator();
        while (dit.hasNext()) {
            Debris deb = dit.next();

            deb.tick();

            double energy = 0.5 * deb.size.mass / (double) (FPS * 5);
            if (player.energy <= energy * 3) {
                deb.held = false;
            }
            if (deb.held) {
                player.energy -= energy;
            }

            if (deb.remove) {
                dit.remove();
            }
        }

        // Tick particles
        Iterator<Particle> ptit = particles.iterator();
        while (ptit.hasNext()) {
            Particle part = ptit.next();

            part.tick();

            if (part.remove) {
                ptit.remove();
            }
        }

        // Tick dots
        Iterator<EnergyDot> edit = dots.iterator();
        while (edit.hasNext()) {
            EnergyDot dot = edit.next();

            dot.tick();

            if (dot.remove) {
                edit.remove();
                player.energy += 3;
            }
        }

        if (playing) {
            // Tick player
            player.tick();

            // Tick stars
            for (Star star : stars) {
                star.tick();
            }

            // Set attraction point!
            attrX = player.posX + 24 * Math.sin(player.rotation);
            attrY = player.posY - 24 * Math.cos(player.rotation);

            // Tick enemies
            Iterator<Enemy> eit = enemies.iterator();
            while (eit.hasNext()) {
                Enemy enemy = eit.next();
                enemy.tick();
                Collection<Bullet> ebull = enemy.getBullets();

                // Add bullets!
                if (ebull != null) {
                    enemyBullets.addAll(ebull);
                }

                if (enemy.remove) {
                    eit.remove();
                    if (enemy.energy <= 0) {
                        // Score!
                        score += enemy.getScore();

                        // Explosion sound
                        Sound.EXPLOSION.play();

                        // Explosion particles
                        int howMuch = (int) Math.round(4 + Math.random() * 3);
                        for (int i = 0; i < howMuch; i++) {
                            particles.add(new SmallExplosionParticle(
                                this, enemy.posX - 6 + Math.random() * 12, enemy.posY - 6 + Math.random() * 12,
                                (int) (10 + Math.random() * 7)));
                        }

                        // Debris
                        int mass = enemy.getMass();
                        while (mass > 0) {
                            Size size = Size.getRandom();
                            if (size.mass <= mass) {
                                debris.add(new Debris(this, enemy.posX + Math.random() * 8 - 4, enemy.posY
                                    + Math.random() * 8 - 4, size, 1000 + (int) (Math.random() * 400)));
                                mass -= size.mass;
                            }
                        }

                        // Energy
                        for (int i = 0; i < enemy.getEnergyDots(); i++) {
                            dots.add(new EnergyDot(this, enemy.posX, enemy.posY));
                        }
                    }
                }
            }

            // Add enemies
            if (ticks - lasts[0] > intervals[0]) {
                intervals[0] += intmod[0];
                lasts[0] = (int) ticks;

                double ang = Math.random() * 2 * Math.PI;
                double enX = player.posX + 250 * Math.sin(ang);
                double enY = player.posY - 250 * Math.cos(ang);

                Enemy enem = new BallEnemy(this, enX, enY);
                enemies.add(enem);
            }
            if (ticks - lasts[1] > intervals[1]) {
                lasts[1] = (int) ticks;
                if (intervals[1] > intmin[1]) {
                    intervals[1] += intmod[1];
                }

                double ang = Math.random() * 2 * Math.PI;
                double enX = player.posX + 250 * Math.sin(ang);
                double enY = player.posY - 250 * Math.cos(ang);

                Enemy enem = new TriangleEnemy(this, enX, enY);
                enemies.add(enem);
            }
            if (ticks - lasts[2] > intervals[2]) {
                lasts[2] = (int) ticks;
                if (intervals[2] > intmin[2]) {
                    intervals[2] += intmod[2];
                }

                double ang = Math.random() * 2 * Math.PI;
                double enX = player.posX + 250 * Math.sin(ang);
                double enY = player.posY - 250 * Math.cos(ang);

                Enemy enem = new SquareEnemy(this, enX, enY);
                enemies.add(enem);
            }
        }

        // Game over
        if (playing && (!player.alive || keyKill)) {
            player.alive = false;
            playing = false;
            gameOver = true;
            gameOverTime = ticks;
            Sound.DEATH.play();
            for (int i = 0; i < 25; i++) {
                particles.add(new SmallExplosionParticle(this, player.posX + 16 - Math.random() * 32, player.posY
                    + 16 - Math.random() * 32, (int) (10 + Math.random() * 40)));
            }

            int mass = 100;
            while (mass > 0) {
                Size size = Size.getRandom();
                if (size.mass <= mass) {
                    Debris deb =
                        new Debris(
                            this, player.posX + Math.random() * 8 - 4, player.posY + Math.random() * 8 - 4, size,
                            1000 + (int) (Math.random() * 400));

                    deb.spdX += player.spdX / 5.0;
                    deb.spdY += player.spdY / 5.0;

                    debris.add(deb);
                    mass -= size.mass;
                }
            }
        }
        
        // Screenshot
        if (keyScreenshot) {
            screenshotTicks++;
        } else {
            screenshotTicks = 0;
        }
    }

    private void display () {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            bs = getBufferStrategy();
        }

        Graphics bg = bs.getDrawGraphics();
        BufferedImage inner = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        miniDisplay((Graphics2D) (inner.getGraphics()));
        bg.drawImage(inner, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, 0, 0, WIDTH, HEIGHT, null);

        bs.show();
        
        if (screenshotTicks == 1) {
            ScreenshotHelper.saveScreenshot(inner);
        }
    }

    private void miniDisplay (Graphics2D g) {
        // Fill black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        /*
         * if ( playing ) { g.setColor( new Color( 1.0f, 1.0f, 1.0f, 0.2f ) ); double difXx = player.posX - attrX;
         * double difYy = player.posY - attrY; g.fillOval( WIDTH/2-(int)difXx-4, HEIGHT/2-(int)difYy-4, 8, 8 ); }
         */

        // Draw grab effect
        if (playing && ticks - lastAttract < 5 && (ticks / 2) % 2 == 0) {
            final double ang = 17.18873743; // 0.3 radians
            double rot = (Math.PI * 2 - player.rotation) * 180 / Math.PI + 90;
            g.setColor(new Color(0x001133));
            g.fillOval(WIDTH / 2 - 64, HEIGHT / 2 - 64, 128, 128);
            g.fillArc(WIDTH / 2 - 256, HEIGHT / 2 - 256, 512, 512, (int) (rot - ang), (int) (ang * 2));

        }

        // Draw stars
        g.setColor(new Color(0x777777));
        for (Star star : stars) {
            int x = (int) (star.x) % WIDTH;
            int y = (int) (star.y) % HEIGHT;
            g.drawRect(x, y, 0, 0);
        }

        // Draw player
        if (playing) {
            drawImageRotatedAt(g, Bitmap.SHIP.image, player.rotation, WIDTH / 2, HEIGHT / 2);
            if (keyUp) {
                drawImageRotatedAt(
                    g, (ticks / 4) % 2 == 0 ? Bitmap.SHIP_BIG_FIRE_1.image : Bitmap.SHIP_BIG_FIRE_2.image,
                    player.rotation, WIDTH / 2, HEIGHT / 2);
            }
            if (ticks - lastShoot <= 3) {
                drawImageRotatedAt(g, Bitmap.SHIP_FIRE.image, player.rotation, WIDTH / 2, HEIGHT / 2);
            }
        }

        // Draw bullets
        for (Bullet pbul : playerBullets) {
            Image im = pbul.getImage();
            double difX = player.posX - pbul.posX;
            double difY = player.posY - pbul.posY;

            drawImageRotatedAt(g, im, pbul.rotation, WIDTH / 2 - difX, HEIGHT / 2 - difY);
        }
        for (Bullet ebul : enemyBullets) {
            Image im = ebul.getImage();
            double difX = player.posX - ebul.posX;
            double difY = player.posY - ebul.posY;

            drawImageRotatedAt(g, im, ebul.rotation, WIDTH / 2 - difX, HEIGHT / 2 - difY);
        }

        // Draw enemies
        g.setColor(new Color(1, 0, 0, 0.2f));
        for (Enemy enemy : enemies) {
            Image im = enemy.getImage();
            double difX = player.posX - enemy.posX;
            double difY = player.posY - enemy.posY;

            if (enemy instanceof SquareEnemy && (ticks / 2) % 2 == 0) {

                g.fillOval((int) (WIDTH / 2 - difX - 64), (int) (HEIGHT / 2 - difY - 64), 128, 128);
            }

            drawImageRotatedAt(g, im, enemy.rotation, WIDTH / 2 - difX, HEIGHT / 2 - difY);
        }

        // Draw debris!
        for (Debris deb : debris) {
            Image im = deb.getImage();
            double difX = player.posX - deb.posX;
            double difY = player.posY - deb.posY;

            drawImageRotatedAt(g, im, deb.rotation, WIDTH / 2 - difX, HEIGHT / 2 - difY);
        }

        // Draw dots
        for (EnergyDot dot : dots) {
            double difX = player.posX - dot.posX;
            double difY = player.posY - dot.posY;

            drawImageRotatedAt(g, Bitmap.ENERGY_DOT.image, 0, WIDTH / 2 - difX, HEIGHT / 2 - difY);
        }

        // Draw particles
        for (Particle part : particles) {
            Image im = part.getImage();
            double difX;
            double difY;
            if (player != null) {
                difX = player.posX - part.posX;
                difY = player.posY - part.posY;
            } else {
                difX = -part.posX;
                difY = -part.posY;
            }

            drawImageRotatedAt(g, im, part.rotation, WIDTH / 2 - difX, HEIGHT / 2 - difY);
        }

        // Draw energy bar
        if (playing) {
            g.drawImage(Bitmap.ENERGY_BAR_LEFT.image, 1, 3, null);
            g.drawImage(Bitmap.ENERGY_BAR_EMPTY.image, 57, 3, WIDTH - 5, 11, 0, 0, 8, 8, null);
            double perc = (player.energy / Player.MAX_ENERGY);
            double barWidth = Math.max(0, (WIDTH - 5 - 57) * perc);
            Image useBar =
                perc < 0.15 ? Bitmap.ENERGY_BAR_FULL_RED.image : (perc < 0.5
                    ? Bitmap.ENERGY_BAR_FULL_YELLOW.image : Bitmap.ENERGY_BAR_FULL_GREEN.image);
            g.drawImage(useBar, 57, 3, 57 + (int) barWidth, 11, 0, 0, 8, 8, null);
            g.drawImage(Bitmap.ENERGY_BAR_RIGHT.image, WIDTH - 5, 3, null);
        }

        // Score
        if (playing) {
            drawIntegerAt(g, (int) score, 5, HEIGHT - 13);
        } else {
            // Click Here / Press V to Start
            if (hasFocus()) {
                g.drawImage(Bitmap.PRESS_V_TO_START.image, WIDTH / 2 - 20, HEIGHT - 80, null);
            } else {
                g.drawImage(Bitmap.CLICK_HERE.image, WIDTH / 2 - 20, HEIGHT - 76, null);
            }

            // Game title
            g.drawImage(Bitmap.TITLE.image, WIDTH / 2 - 101, 20, null);
        }

        // Game Over!
        if (gameOver) {
            g.drawImage(
                Bitmap.GAME_OVER.image, WIDTH / 2 - 65, (HEIGHT / 2 - 13) - (int) (10 * Math.cos(ticks / 30d)), null);

            drawIntegerAt(g, (int) score, WIDTH / 2 - Integer.toString((int) score).length() * 4, HEIGHT / 3);
        }

        // FPS
        /*
         * g.setColor( Color.WHITE ); g.drawString( Double.toString( Math.round( actualFps*100 )/100.0 ), 0, HEIGHT );
         */
    }

    private static void drawImageRotatedAt (Graphics2D g, Image img, double rot, double x, double y) {
        if (img != null) {
            AffineTransform gt = g.getTransform();
            AffineTransform imtr;
            AffineTransform gtr;
            imtr = new AffineTransform();
            imtr.translate(img.getWidth(null) / 2, img.getHeight(null) / 2);
            imtr.rotate(rot);
            imtr.translate(-img.getWidth(null) / 2, -img.getHeight(null) / 2);
            gtr = new AffineTransform();
            gtr.translate(x - img.getWidth(null) / 2, y - img.getHeight(null) / 2);
            g.setTransform(gtr);
            g.drawImage(img, imtr, null);
            g.setTransform(gt);
        }
    }

    private static void drawIntegerAt (Graphics2D g, int num, double x, double y) {
        final Image[] nums =
            new Image[] {
                Bitmap.FONT_0.image, Bitmap.FONT_1.image, Bitmap.FONT_2.image, Bitmap.FONT_3.image,
                Bitmap.FONT_4.image, Bitmap.FONT_5.image, Bitmap.FONT_6.image, Bitmap.FONT_7.image,
                Bitmap.FONT_8.image, Bitmap.FONT_9.image };

        String str = Integer.toString(num);
        for (int i = 0; i < str.length(); i++) {
            int c = Integer.parseInt(Character.toString(str.charAt(i)));
            g.drawImage(nums[c], (int) (x + 8 * i), (int) y, null);
        }
    }

    private void start () {
        runningThread = new Thread(this);
        runningThread.start();
    }

    public void pause () {
        paused = true;
    }

    public void unpause () {
        if (runningThread == null) {
            start();
        }
        paused = false;
    }

    public void stop () {
        requestStop = true;
    }

    @Override
    public void keyPressed (KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                keyLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                keyRight = true;
                break;
            case KeyEvent.VK_UP:
                keyUp = true;
                break;
            case KeyEvent.VK_V:
                keyShoot = true;
                break;
            case KeyEvent.VK_K:
                keyKill = true;
                break;
            case KeyEvent.VK_C:
                keyRelease = true;
                break;
            case KeyEvent.VK_X:
                keyAttract = true;
                break;
            case KeyEvent.VK_F12:
                keyScreenshot = true;
                break;
        }
    }

    @Override
    public void keyReleased (KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                keyLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                keyRight = false;
                break;
            case KeyEvent.VK_UP:
                keyUp = false;
                break;
            case KeyEvent.VK_V:
                keyShoot = false;
                break;
            case KeyEvent.VK_K:
                keyKill = false;
                break;
            case KeyEvent.VK_C:
                keyRelease = false;
                break;
            case KeyEvent.VK_X:
                keyAttract = false;
                break;
            case KeyEvent.VK_F12:
                keyScreenshot = false;
                break;
        }
    }

    @Override
    public void keyTyped (KeyEvent ke) {
    }

    @Override
    public void mouseClicked (MouseEvent arg0) {
    }

    @Override
    public void mouseEntered (MouseEvent arg0) {
    }

    @Override
    public void mouseExited (MouseEvent arg0) {
    }

    @Override
    public void mousePressed (MouseEvent arg0) {
    }

    @Override
    public void mouseReleased (MouseEvent arg0) {
    }

}
