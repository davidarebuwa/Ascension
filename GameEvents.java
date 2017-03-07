package ca.ascension.game;

import ca.ascension.game.entities.Player;
import ca.ascension.game.gfx.Colours;
import ca.ascension.game.gfx.Font;
import ca.ascension.game.gfx.Screen;
import ca.ascension.game.level.Level;

public class GameEvents {

	static long lastTime;
	static boolean playerIsInLevel = false;
	public static boolean overItem = false;
	public static boolean overCoin = false;
	
	private int green = Colours.get(-1, 555, 141, 400);
	private int blue = Colours.get(-1, 555, 115, 400);
	private int orange = Colours.get(-1, 555, 542, 400);
	private int red = Colours.get(-1, 555, 500, 400);
	private int black = Colours.get(-1, 555, 000, 400);
	
	
	
	
	public GameEvents() {
		
	}
	
	public void renderPlayerEvents(Screen screen, int x, int y, InputHandler input, Player player, Level level) {
		
	//Enter and leave space station
		if (Player.triggeredSpaceDoor == true) {  
			Font.render("ENTER!", screen, x+150 , y+100 , Colours.get(-1, 000, -1, 555), 1);
			if (input.enter.isPressed()) {
				Game.startLavaLevel("/levels/LavaMazeFuture.png", 300, 250);
				Game.lavaChangeEventCounter ++;
				Game.lavaChangeEventCounter ++;
				playerIsInLevel = true;
				
			   
			}
		}
		
		if (Player.triggeredLavaMazeEnd == true) {  
			Font.render("ENTER!", screen, x+150 , y+100 , Colours.get(-1, 000, -1, 555), 1);
			if (input.enter.isPressed()) {
				
				Game.startLavaBossLevel("/levels/LavaBossLevel.png", 330, 720);
				playerIsInLevel = true;
			}
		}
		
	}
	
}





	

