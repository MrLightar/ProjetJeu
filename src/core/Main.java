package core;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import entity.Character;
import entity.Cursor;
import map.Grid;


public class Main extends BasicGame {
	
	public static final int width = 1000;
	public static final int height = 1000;
	private static final boolean fullscreen = false;
	
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
				if (currentI - 1 >= 0) {
					this.cursor.setPosFromIndex(currentI - 1, currentJ);
				}
				break;

			case Input.KEY_RIGHT:
				if (currentI + 1 < Main.gameGrid.getCols()) {
					this.cursor.setPosFromIndex(currentI + 1, currentJ);
				}
				break;

			case Input.KEY_UP:
				if (currentJ - 1 >= 0) {
					this.cursor.setPosFromIndex(currentI, currentJ - 1);
				}
				break;

			case Input.KEY_DOWN:
				if (currentJ + 1 < Main.gameGrid.getRows()) {
					this.cursor.setPosFromIndex(currentI, currentJ + 1);
				}
				break;
			
			case Input.KEY_SPACE:
				this.cursor.testSelect();
				break;
		}
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		Main.gameGrid.render(gc, g);
		this.chara.render(gc, g);
		this.cursor.render(gc, g);
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		Main.gameGrid = new Grid(12, 12);
		Main.gameGrid.init(gc);
		System.out.println("Cell size : " + Grid.cellSize);

		this.cursor = new entity.Cursor(Main.gameGrid.getCell(5, 5));
		this.chara = new entity.Character(Main.gameGrid.getCell(4, 4), 6, 3);
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
	}
	
}
