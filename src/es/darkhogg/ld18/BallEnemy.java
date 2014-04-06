package es.darkhogg.ld18;

import java.awt.image.BufferedImage;
import java.util.Collection;

import es.darkhogg.ld18.Enemy;

public final class BallEnemy extends Enemy {

	public BallEnemy ( Game game, double x, double y ) {
		super( game );
		posX = x;
		posY = y;
		energy = 50;
	}
	

	@Override
	public void doTick () {
		double playerX = game.player.posX;
		double playerY = game.player.posY;
		
		double ang = Math.atan2( playerX-posX, -(playerY-posY) );
		
		spdX = 0.6*Math.sin( ang );
		spdY = -0.6*Math.cos( ang );
	}

	@Override
	public BufferedImage getImage () {
		return Bitmap.ENEMY_BALL.image;
	}


	@Override
	public int getScore () {
		return 10;
	}

	@Override
	public int getMass () {
		return 5;
	}


	@Override
	public Collection<Bullet> getBullets () {
		return null;
	}


	@Override
	public int getEnergyDots () {
		return 3;
	}
	
	@Override
	public double getRadius () {
		return 6;
	}

}
