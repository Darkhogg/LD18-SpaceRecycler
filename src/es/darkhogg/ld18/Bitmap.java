package es.darkhogg.ld18;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public enum Bitmap {
    // Logo
    DH_LOGO_D("/es/darkhogg/img/dh_logo.bmp", 0, 0, 32, 40, Color.CYAN), //
    DH_LOGO_H("/es/darkhogg/img/dh_logo.bmp", 32, 0, 24, 40, Color.CYAN), //
    DH_LOGO_TXT("/es/darkhogg/img/dh_logo.bmp", 0, 40, 55, 8, Color.CYAN),

    // Ship
    SHIP("/es/darkhogg/img/ship.bmp", 0, 0, 24, 24, Color.CYAN), //
    SHIP_FIRE("/es/darkhogg/img/ship.bmp", 32, 0, 24, 24, Color.CYAN), //
    SHIP_BIG_FIRE_1("/es/darkhogg/img/ship.bmp", 0, 32, 24, 24, Color.CYAN), //
    SHIP_BIG_FIRE_2("/es/darkhogg/img/ship.bmp", 24, 32, 24, 24, Color.CYAN), //
    SHIP_BULLET("/es/darkhogg/img/ship.bmp", 24, 0, 8, 8, Color.CYAN),

    // Energy Bar
    ENERGY_BAR_LEFT("/es/darkhogg/img/energy.bmp", 0, 0, 56, 8, Color.CYAN), //
    ENERGY_BAR_RIGHT("/es/darkhogg/img/energy.bmp", 56, 0, 8, 8, Color.CYAN), //
    ENERGY_BAR_EMPTY("/es/darkhogg/img/energy.bmp", 64, 0, 8, 8, Color.CYAN), //
    ENERGY_BAR_FULL_GREEN("/es/darkhogg/img/energy.bmp", 0, 8, 8, 8, Color.CYAN), //
    ENERGY_BAR_FULL_YELLOW("/es/darkhogg/img/energy.bmp", 8, 8, 8, 8, Color.CYAN), //
    ENERGY_BAR_FULL_RED("/es/darkhogg/img/energy.bmp", 16, 8, 8, 8, Color.CYAN), //
    ENERGY_DOT("/es/darkhogg/img/ship.bmp", 24, 8, 8, 8, Color.CYAN),

    // Enemies
    ENEMY_BALL("/es/darkhogg/img/enemies.bmp", 0, 0, 16, 16, Color.CYAN), //
    ENEMY_TRIANGLE("/es/darkhogg/img/enemies.bmp", 16, 0, 24, 24, Color.CYAN), //
    ENEMY_SQUARE("/es/darkhogg/img/enemies.bmp", 0, 24, 24, 24, Color.CYAN), //
    ENEMY_BULLET("/es/darkhogg/img/enemies.bmp", 0, 16, 8, 8, Color.CYAN),

    // Explosions
    EXPL_SMALL_1("/es/darkhogg/img/explosions.bmp", 0, 0, 8, 8, Color.CYAN), //
    EXPL_SMALL_2("/es/darkhogg/img/explosions.bmp", 8, 0, 8, 8, Color.CYAN), //
    EXPL_SMALL_3("/es/darkhogg/img/explosions.bmp", 16, 0, 8, 8, Color.CYAN), //
    EXPL_SMALL_4("/es/darkhogg/img/explosions.bmp", 24, 0, 8, 8, Color.CYAN), //
    EXPL_SMALL_5("/es/darkhogg/img/explosions.bmp", 32, 0, 8, 8, Color.CYAN), //
    EXPL_SMALL_6("/es/darkhogg/img/explosions.bmp", 40, 0, 8, 8, Color.CYAN),

    // Debris
    DEB_SM_1("/es/darkhogg/img/debris.bmp", 0, 0, 4, 4, Color.CYAN), //
    DEB_SM_2("/es/darkhogg/img/debris.bmp", 4, 0, 4, 4, Color.CYAN), //
    DEB_SM_3("/es/darkhogg/img/debris.bmp", 0, 4, 4, 4, Color.CYAN), //
    DEB_SM_4("/es/darkhogg/img/debris.bmp", 4, 4, 4, 4, Color.CYAN), //
    DEB_MD_1("/es/darkhogg/img/debris.bmp", 8, 0, 8, 8, Color.CYAN), //
    DEB_MD_2("/es/darkhogg/img/debris.bmp", 16, 0, 8, 8, Color.CYAN), //
    DEB_MD_3("/es/darkhogg/img/debris.bmp", 24, 0, 8, 8, Color.CYAN), //
    DEB_MD_4("/es/darkhogg/img/debris.bmp", 32, 0, 8, 8, Color.CYAN), //
    DEB_LG_1("/es/darkhogg/img/debris.bmp", 0, 8, 8, 8, Color.CYAN), //
    DEB_LG_2("/es/darkhogg/img/debris.bmp", 8, 8, 8, 8, Color.CYAN), //
    DEB_LG_3("/es/darkhogg/img/debris.bmp", 16, 8, 8, 8, Color.CYAN), //
    DEB_LG_4("/es/darkhogg/img/debris.bmp", 24, 8, 8, 8, Color.CYAN), //
    DEB_LG_5("/es/darkhogg/img/debris.bmp", 32, 8, 8, 8, Color.CYAN),

    // Numbers
    FONT_0("/es/darkhogg/img/dh_font.bmp", 16, 24, 8, 8, Color.CYAN), //
    FONT_1("/es/darkhogg/img/dh_font.bmp", 24, 24, 8, 8, Color.CYAN), //
    FONT_2("/es/darkhogg/img/dh_font.bmp", 32, 24, 8, 8, Color.CYAN), //
    FONT_3("/es/darkhogg/img/dh_font.bmp", 40, 24, 8, 8, Color.CYAN), //
    FONT_4("/es/darkhogg/img/dh_font.bmp", 48, 24, 8, 8, Color.CYAN), //
    FONT_5("/es/darkhogg/img/dh_font.bmp", 56, 24, 8, 8, Color.CYAN), //
    FONT_6("/es/darkhogg/img/dh_font.bmp", 0, 32, 8, 8, Color.CYAN), //
    FONT_7("/es/darkhogg/img/dh_font.bmp", 8, 32, 8, 8, Color.CYAN), //
    FONT_8("/es/darkhogg/img/dh_font.bmp", 16, 32, 8, 8, Color.CYAN), //
    FONT_9("/es/darkhogg/img/dh_font.bmp", 24, 32, 8, 8, Color.CYAN),

    // Misc
    PRESS_V_TO_START("/es/darkhogg/img/click.bmp", 0, 0, 40, 24, Color.CYAN), //
    CLICK_HERE("/es/darkhogg/img/click.bmp", 0, 24, 40, 16, Color.CYAN), //
    TITLE("/es/darkhogg/img/title.bmp", 0, 0, 202, 34, Color.CYAN), //
    GAME_OVER("/es/darkhogg/img/gameover.bmp", 0, 0, 130, 26, Color.CYAN);

    public final BufferedImage image;
    private static Map<URL,Image> cachedImages;

    private Bitmap (URL url, int x, int y, int w, int h, Color trans) {
        System.out.printf("Loading Bitmap.%s from '%s'...%n", this, url);
        
        Image img = readImage(url);
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics ig = newImage.getGraphics();
        ig.drawImage(img, 0, 0, w, h, x, y, x + w, y + h, null);

        int[] rgb = newImage.getRGB(0, 0, w, h, null, 0, w);
        int[] newRgb = new int[rgb.length];
        for (int i = 0; i < rgb.length; i++) {
            newRgb[i] = (rgb[i] == trans.getRGB()) ? 0x00000000 : rgb[i];
        }

        newImage.setRGB(0, 0, w, h, newRgb, 0, w);
        image = newImage;
    }

    private Bitmap (String str, int x, int y, int w, int h, Color trans) {
        this(Bitmap.class.getResource(str), x, y, w, h, trans);
    }

    private static final Image readImage (URL url) {
        try {
            if (cachedImages == null) {
                cachedImages = new HashMap<URL,Image>();
            }
            if (cachedImages.containsKey(url)) {
                return cachedImages.get(url);
            } else {
                Image img = ImageIO.read(url);
                cachedImages.put(url, img);
                return img;
            }
        } catch (IOException e) {
            System.err.println("Fatal Error: Cannot load '" + url + "'");
            System.exit(1);
            return null;
        }
    }

    public Image getImage () {
        return image;
    }
}
