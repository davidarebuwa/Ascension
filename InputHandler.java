package ca.ascension.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;


public class InputHandler implements KeyListener {
	
	public InputHandler(Game game){
		game.addKeyListener(this);
	}
	
	public class Key{
		
		
		private boolean pressed = false;
		
		public boolean isPressed(){
			return pressed;
		}
		
		public void toggle(boolean isPressed){
			pressed = isPressed;
		}
	}
	

	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key enter = new Key();
	
	//Toggles time travel
	public Key lavaStageFuture = new Key();
	public Key lavaStagePast = new Key();
	
	
	
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(),true);
	}

	
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(),false);
	}
	
	public void keyTyped(KeyEvent e) {}
	
	public void toggleKey(int keyCode, boolean isPressed){
		//Player 1 Movements
		if (keyCode == KeyEvent.VK_UP){up.toggle(isPressed);}
		if (keyCode == KeyEvent.VK_DOWN){down.toggle(isPressed);}
		if (keyCode == KeyEvent.VK_LEFT){left.toggle(isPressed);}
		if (keyCode == KeyEvent.VK_RIGHT){right.toggle(isPressed);}
		if (keyCode == KeyEvent.VK_ENTER){enter.toggle(isPressed);}
		
		if (Game.lavaChangeEventCounter == 1){
		if (keyCode == KeyEvent.VK_Z){
			lavaStageFuture.toggle(isPressed);
			Game.lavaChangeEventCounter ++;
			
			}
		}
		
		if (Game.lavaChangeEventCounter == 2){
			if (keyCode == KeyEvent.VK_X){
				lavaStagePast.toggle(isPressed);
				Game.lavaChangeEventCounter --;
				}
			}
		
		
		
	}

}
