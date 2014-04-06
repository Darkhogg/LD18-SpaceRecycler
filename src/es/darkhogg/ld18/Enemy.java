package es.darkhogg.ld18;

import java.awt.Image;
import java.util.Collection;

public abstract class Enemy {
	
	public double rotation;
	public double posX;
	public double posY;
	public int energy;
	public double spdX;
	public double spdY;
	public double rotationSpeed;
	public Game game;
	public boolean remove = false;
	
	public Enemy ( Game game ) {
		this.game = game;
	}
	
	public final void tick () {
		posX += spdX;
		posY += spdY;
		rotation += rotationSpeed;
		if ( energy <= 0 ) {
			remove = true;
		}
		
		// If it's too far away (more than 600 pixels it's pretty much enough,
		// ships that far will probably disappear...
		double difX = posX-game.player.posX;
		double difY = posY-game.player.posY;
		if ( (difX*difX + difY*difY) > 360000 ) {
			remove = true;
			energy++;
		}
		
		doTick();
	}
	
	public abstract void doTick ();
	public abstract Image getImage ();
	public abstract int getScore ();
	public abstract int getMass ();
	public abstract int getEnergyDots ();
	public abstract Collection<Bullet> getBullets ();
	public abstract double getRadius ();
}
