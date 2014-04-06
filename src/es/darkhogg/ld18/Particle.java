package es.darkhogg.ld18;

import java.awt.Image;

public abstract class Particle {

	public double posX;
	public double posY;
	public double rotation;
	public double spdX;
	public double spdY;
	public double rotationSpeed;
	public Game game;
	public boolean remove;
	public int life;
	
	public Particle ( Game game ) {
		this.game = game;
	}
	
	public void tick () {
		posX += spdX;
		posY += spdY;
		rotation += rotationSpeed;
		doTick();
		
		life--;
		if ( life <= 0 ) {
			remove = true;
		}
	}
	
	public abstract void doTick ();
	
	public abstract Image getImage ();
	
}
