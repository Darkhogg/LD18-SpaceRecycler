package es.darkhogg.ld18;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public final class GuiWrapper {

	private static Game game;
	
	public static void main ( String[] args ) {
		JFrame frame = new JFrame( "Space Recycler - Ludum Dare 18 - Darkhogg" );
		game = new Game();
		
		frame.add( game );
		frame.pack();
		frame.addWindowListener( new CloseListener() );
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		frame.setLocationRelativeTo( null );
		frame.setResizable( false );
		frame.setVisible( true );
		
		game.unpause();
	}

	private static class CloseListener extends WindowAdapter {
		@Override
		public void windowClosing ( WindowEvent we ) {
			game.stop();
		}
	}
	
}
