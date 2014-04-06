package es.darkhogg.ld18;

import java.awt.Image;

public class DotBullet extends Bullet {

	
	public DotBullet ( Game game, double x, double y, double spdX, double spdY ) {
		super( game );
		
		posX = x;
		posY = y;
		
		this.spdX = spdX;
		this.spdY = spdY;
	}

	@Override
	public void doTick () {

	}

	@Override
	public Image getImage () {
		return Bitmap.SHIP_BULLET.image;
	}
	
}
