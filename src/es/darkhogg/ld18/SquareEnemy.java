package es.darkhogg.ld18;

import java.awt.Image;
import java.util.Collection;

public final class SquareEnemy extends Enemy {

	long ticks;
	
	public SquareEnemy ( Game game, double x, double y ) {
		super( game );
		
		posX = x;
		posY = y;
		
		energy = 1000;
	}
	

	@Override
	public void doTick () {
		ticks++;
		
		rotation = 0.25 * Math.sin( ticks/10.0 );
		
		for ( Debris deb : game.debris ) {
			double difX = deb.posX - posX;
			double difY = deb.posY - posY;
			double sqdist = difX*difX + difY*difY;
			if ( sqdist < 64*64 ) {
				if ( deb.life > 90 ) {
					deb.life = 90;
				}
				if ( deb.held ) {
					deb.life -= 2;
				}
			}
		}
		
		double difX = game.player.posX - posX;
		double difY = game.player.posY - posY;
		double sqdist = difX*difX + difY*difY;
		double ang = Math.atan2( difX, -difY );
		double spd = sqdist > 270*270
				   ? 15
				   : ( sqdist > 32*32
				     ? 0.8
				     : 0.05 );
		
		spdX = spd*Math.sin( ang );
		spdY = -spd*Math.cos( ang );
		
		if ( sqdist < 32*32 ) {
			game.player.energy -= 1.2;
		} else if ( sqdist < 48*48 ) {
			game.player.energy -= 0.8;
		} else if ( sqdist < 64*64 ) {
			game.player.energy -= 0.4;
		}
		
		if ( sqdist < 64*64 && Math.random()<0.05 ) {
			game.particles.add( new SmallExplosionParticle( game, 
				game.player.posX-10+Math.random()*20,
				game.player.posY-10+Math.random()*20,
				12 ) );
		}
	}

	@Override
	public Collection<Bullet> getBullets () {
		return null;
	}

	@Override
	public int getEnergyDots () {
		return 15;
	}

	@Override
	public Image getImage () {
		return Bitmap.ENEMY_SQUARE.image;
	}

	@Override
	public int getMass () {
		return 100;
	}

	@Override
	public double getRadius () {
		return 12;
	}

	@Override
	public int getScore () {
		return 300;
	}

}
