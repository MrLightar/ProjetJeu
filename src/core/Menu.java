package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import strategy.Strategy;
import core.Main;

public class Menu extends BasicGameState {
	
	private StateBasedGame sbg;
	private GameContainer gc;
	
	
	public static int selectedButton;
	private boolean inGame;
	
	private Image mainTitle;
	private Image resumeButton;
	private Image newGameButton;
	private Image exitButton;
	private Image menuCursor;
	
	private static final int resume = 0;
	private static final int newGame = 1;
	private static final int exit = 2;
	
	private int heightButton;
	private int widthButton;
	
	private Image texture;
	private Animation animationMenu = new Animation();
	
	
	public Menu(int state) {
		
	}
	
	
	public void setSelectedButton(int i) {
		if(i==resume || i==exit || i==newGame) {
			selectedButton=i;
		}
	}
	
	
	public Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y, int animSpeed) {
		Animation animation = new Animation();
		for( int x = startX; x < endX; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), animSpeed);
		}
		
		return animation;
	}
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.sbg = sbg;
		this.gc = gc;
		
		heightButton = Main.height / 8;
		widthButton = Main.width / 3;
		
		this.mainTitle = new Image("res/titre.png");
		this.mainTitle = this.mainTitle.getScaledCopy(widthButton*2, heightButton*2);
		this.resumeButton = new Image("res/resumeButton.png");
		this.resumeButton = this.resumeButton.getScaledCopy(widthButton, heightButton);
		this.newGameButton = new Image("res/newGameButton.png");
		this.newGameButton = this.newGameButton.getScaledCopy(widthButton, heightButton);
		this.exitButton = new Image("res/exitButton.png");
		this.exitButton = this.exitButton.getScaledCopy(widthButton, heightButton);
		this.menuCursor = new Image("res/menuCursor.png");
		this.menuCursor = this.menuCursor.getScaledCopy(widthButton, heightButton);
		
		this.texture = new Image("res/AnimationWallpaper.png");
		this.texture = this.texture.getScaledCopy(Main.width*8, Main.height);	
		SpriteSheet spriteSheetMenu = new SpriteSheet(this.texture, Main.width, Main.height);

		this.animationMenu = loadAnimation(spriteSheetMenu, 0, 8, 0, 100);
		
		Music background = new Music("res/MainMenu.ogg");
		background.loop();

		
		
		selectedButton = newGame;
		inGame = false;

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		
		animationMenu.draw(0, 0);
		
		if(inGame) {
			g.drawImage(resumeButton, widthButton *1, heightButton *2);
		} else {
			g.drawImage(mainTitle, widthButton /2, heightButton);
		}
		
		g.drawImage(newGameButton, widthButton *1, heightButton *4);
		g.drawImage(exitButton, widthButton *1, heightButton *6);
		g.drawImage(menuCursor, widthButton *1, (selectedButton*2 + 2) * heightButton);
		
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
	}

	@Override
	public int getID() {
		return Main.menu;
	}
	
	
	
	
	@Override
	public void keyPressed(int key, char c) {
//		int currentI = this.cursor.getPos().getI();
//		int currentJ = this.cursor.getPos().getJ();

		switch (key) {
			case Input.KEY_UP:
				if (selectedButton != resume) {
					if(inGame || selectedButton != newGame) {
						selectedButton -= 1;
					}
				}
				break;
			
			case Input.KEY_DOWN:
				if (selectedButton != exit) {
					selectedButton += 1;
				}
				break;

			case Input.KEY_ENTER:
				switch (selectedButton) {
				case resume:
					if(inGame) {
						sbg.enterState(Main.play);
					}
					break;
				case newGame:
					startGame(sbg, gc);
					break;
				case exit:
					System.exit(0);
					break;
				}
				break;

			case Input.KEY_ESCAPE:
				if(inGame) {
					sbg.enterState(Main.play);
				}
				
				break;

		}
		
	}
	
	
	public void startGame (StateBasedGame sbg, GameContainer gc){
		try {
			try {
				Files.copy(Paths.get("res/character_default.txt"), Paths.get("res/character.txt"), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			Play.setMapLevel(0);
			Strategy.setPlaying(false);
			sbg.getState(Main.menu).init(gc, sbg);
			sbg.getState(Main.play).init(gc, sbg);
			sbg.getState(Main.selectCharaScreen).init(gc, sbg);
			inGame = true;
			selectedButton = resume;
			
			sbg.enterState(Main.play);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void mouseMoved(int oldx, int oldy,int x, int y) {

		if(x>widthButton && x<2*widthButton && y>4*heightButton && y<5*heightButton) {
				selectedButton=newGame;
		}
		
		if(x>widthButton && x<2*widthButton && y>6*heightButton && y<7*heightButton) {
			selectedButton=exit;
		}
		
		if(inGame) {
			if(x>widthButton && x<2*widthButton && y>2*heightButton && y<3*heightButton) {
				selectedButton=resume;
			}
		}
	}
	
	public void mouseClicked(int button, int x, int y,int clickcount) {
		if(clickcount<=1) {
			if(x>widthButton && x<2*widthButton && y>4*heightButton && y<5*heightButton) {
					selectedButton=newGame;
					startGame(sbg, gc);
			}
			
			if(x>widthButton && x<2*widthButton && y>6*heightButton && y<7*heightButton) {
				selectedButton=exit;
				System.exit(0);
			}
			
			if(inGame) {
				if(x>widthButton && x<2*widthButton && y>2*heightButton && y<3*heightButton) {
					selectedButton=resume;
					sbg.enterState(Main.play);
				}
			}
		}

	}

}
