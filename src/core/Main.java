package core;


import org.newdawn.slick.*;

import entity.Character;
import entity.Cursor;
import map.Grid;


public class Main extends BasicGame {

	public static final int width = 1920;
	public static final int height = 1080;
	private static final boolean fullscreen = true;

	public static Grid gameGrid;
	
	Cursor cursor;
	Character chara;


	public Main(String title) {
		super(title);
	}


	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new Main("Grid"));
			app.setDisplayMode(Main.width, Main.height, Main.fullscreen);
			app.setTitle("Grid");
			app.setForceExit(true);
			app.setTargetFrameRate(60);
			app.setShowFPS(false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void keyPressed(int key, char c) {
		int currentI = this.cursor.getPos().getI();
		int currentJ = this.cursor.getPos().getJ();

		switch (key) {
			case Input.KEY_LEFT:
				if (currentJ > 0) {
					this.cursor.setPosFromIndex(currentI, currentJ - 1);
				}
				break;
			
			case Input.KEY_RIGHT:
				if (currentJ < Main.gameGrid.getCols() - 1) {
					this.cursor.setPosFromIndex(currentI, currentJ + 1);
				}
				break;
			
			case Input.KEY_UP:
				if (currentI > 0) {
					this.cursor.setPosFromIndex(currentI - 1, currentJ);
				}
				break;
			
			case Input.KEY_DOWN:
				if (currentI < Main.gameGrid.getRows() - 1) {
					this.cursor.setPosFromIndex(currentI + 1, currentJ);
				}
				break;

			case Input.KEY_SPACE:
				this.cursor.testSelect();
				break;
			
			case Input.KEY_ESCAPE:
				System.exit(0);
				break;
		}
		
	}
	
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		Main.gameGrid = new Grid(16, 16);
		Main.gameGrid.init(gc);
		System.out.println("Cell size : " + Grid.cellSize);

		this.cursor = new entity.Cursor(Main.gameGrid.getCell(1, 2));
		this.chara = new entity.Character(Main.gameGrid.getCell(4, 4), 6, 3);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		Main.gameGrid.render(gc, g);
		this.chara.render(gc, g);
		this.cursor.render(gc, g);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
	}

}
