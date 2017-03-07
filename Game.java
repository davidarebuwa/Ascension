package ca.ascension.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import ca.ascension.game.entities.LavaBoss;
import ca.ascension.game.entities.NPC;
import ca.ascension.game.entities.Player;
import ca.ascension.game.gfx.Colours;
import ca.ascension.game.gfx.Font;
import ca.ascension.game.gfx.Screen;
import ca.ascension.game.gfx.SpriteSheet;
import ca.ascension.game.level.Level;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 350;
	//Sets Height to half width divided by resolution of 12 by 9
	public static final int HEIGHT = WIDTH/12*9;
	//scale factor(Allows us to enlarge the GUI/JFrame Box)
	public static final int SCALE = 3;
	//Name of the JFrame
	public static final String NAME = "Ascension";

	private JFrame frame;

	public boolean running = false;
	public int tickCount = 0;
	
	public static int xOffset;
	public static int yOffset;

	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	private int[] colours = new int [6*6*6];
	
	
	private static Screen screen;
	public static InputHandler input;
	public static Level level;
	public static GameEvents gameEvents;
	public static Player player;
	public static NPC npc;
	public static LavaBoss lavaBoss;
	
	//For the changing of the stages
	public static int lavaChangeEventCounter = 0;
	
	
	public Game(){
		//sets size of the game canvas
		setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));

		//creates actual window/frame of the game
		frame = new JFrame(NAME);

		//Sets close operation (Clicking the red x on the top right corner closes the game)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		//Adds canvas to the frame
		frame.add(this, BorderLayout.CENTER);
		//Puts all components together
		frame.pack();

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void init(){
		//creates an index that stores the red green and blue colours
		int index = 0;
		for(int r = 0; r<6; r++){
			for(int g = 0; g<6; g++){
				for(int b = 0; b<6; b++){
					int rr =(r*255/5);
					int gg =(g*255/5);
					int bb =(b*255/5);
					
					colours[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}
		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);
		//CHANGE THIS TO START THE LEVEL
		//initial level it starts at:
		startLevel("/levels/SpaceHubMap.png", 235, 250);
	}
	
	
	//STARTING THE STAGES CODE
	
	public static void startLavaLevel(String levelPath, int x, int y){
		level = new Level(levelPath);
		player = new Player(level, x, y , input);  
		level.addEntity(player);
		gameEvents = new GameEvents();
	}
	
	public static void startLavaLevelFuture(String levelPath, int x, int y){
		level = new Level(levelPath);
		player = new Player(level, x, y , input);  
		level.addEntity(player);
		//gameEvents = new GameEvents();
		lavaChangeEventCounter = 1;
	}
	
	public static void startLavaLevelPast(String levelPath, int x, int y){
		level = new Level(levelPath);
		player = new Player(level, x, y , input);  
		level.addEntity(player);
		gameEvents = new GameEvents();
		lavaChangeEventCounter = 2;
		
	}
	
	
	
	public static void startLavaBossLevel(String levelPath, int x, int y){
		level = new Level(levelPath);
		player = new Player(level, x, y , input);  
		lavaBoss = new LavaBoss(level, 325,350);
		level.addEntity(player);
		level.addEntity(lavaBoss);
		gameEvents = new GameEvents();
	}
	public static void startLevel(String levelPath, int x, int y){
		level = new Level(levelPath);
		player = new Player(level, x, y, input);  
		level.addEntity(player);
		gameEvents = new GameEvents();
	}
	public static void startNextLevel(String levelPath, int x, int y){
		level = new Level(levelPath);    
		player = new Player(level, x, y, input);
		level.addEntity(player);
		gameEvents = new GameEvents();
	}

	
	
	
	
	
	//UseFul for when we create an applet, can be put into browser as a downlaodable link aswell.
	public synchronized void start() {

		running = true;
		//Thread is Instance of a runnable
		new Thread(this).start();

	}

	public synchronized void stop(){
		running = false;
	}

 
	public void run() {
		//nanotime gets current time in nano seconds (accuracy)
		long lastTime = System.nanoTime();
		//how many nano seconds are in 1 tick
		double nsPerTick = 1000000000D/60D;

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		
		init();
		
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime)/nsPerTick;
			lastTime = now;
			boolean shouldRender= true;
			
			while(delta>=1){
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
			
			if (shouldRender){
			frames++;
			render();
			}
			
			if(System.currentTimeMillis() - lastTimer >= 1000){
				lastTimer +=1000;
				System.out.println(ticks + " ticks" + ", " + frames + " frames");
				frames = 0;
				ticks = 0;
			}
		}			
	}

	
	
	// This function Updates the game (Internal variables/logic of game)
	public void tick(){
		tickCount++;
		level.tick();
	}

	//prints out what the tick logic is
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		
		xOffset = player.x - (screen.width/2);
		yOffset = player.y - (screen.height/2);
		
		level.renderTiles(screen, xOffset, yOffset);
		
		for(int x = 0; x <level.width; x++) {
			int colour = Colours.get(-1, -1, -1, 000);
			if (x % 10 == 0 && x != 0) {
			colour = Colours.get(-1, -1, -1, 500);
			}
			
		}

		level.renderEntities(screen);
	    gameEvents.renderPlayerEvents(screen, xOffset, yOffset, input, player, level);
	
		//Copies pixel data from screen to game
		for(int y =0; y<screen.height; y++){
			for(int x =0;x< screen.width; x++){
				int colourCode = screen.pixels[x+y*screen.width];
				if(colourCode<255) pixels[x+y* WIDTH]=colours[colourCode];
			}	
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args){
		new Game().start();
	}


}
