package core;


import org.newdawn.slick.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import entity.*;
import entity.Character;
import map.*;
import strategie.*;


public class Main extends BasicGame {

	public static final int width = 1200;
	public static final int height = 900;
	private static final boolean fullscreen = false;

	public static Grid gameGrid;
	
	Cursor cursor;
	Character[] chara = new Character[2];
	Strategie strat;


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
				this.cursor.testSelectMove();
				break;

			case Input.KEY_A:
				this.cursor.testSelectAttack();
				break;
			
			case Input.KEY_ESCAPE:
				System.exit(0);
				break;
			
			case Input.KEY_Z:
				ArrayList<Cell> path = this.strat.evaluatePath(this.chara[0].getPos(), this.cursor.getPos());
				if (path != null) {
					for (Cell cell : path) {
						System.out.println(cell);
					}
				}
				break;

			case Input.KEY_E:
				ArrayList<Cell> sight = this.strat.evaluateSightView(this.chara[0].getPos(), this.cursor.getPos());
				for (Cell cell : sight) {
					try {
						cell.setCellType(2);
					} catch (SlickException e) {
						e.printStackTrace();
					}
				}
				break;
			
			case Input.KEY_R:
				ArrayList<Cell> sight2 = this.strat.evaluateSightView(this.chara[0].getPos(), this.cursor.getPos());
				for (Cell cell : sight2) {
					try {
						cell.setCellType(0);
					} catch (SlickException e) {
						e.printStackTrace();
					}
				}
				break;

			case Input.KEY_T:
				int dist = this.chara[0].getPos().distanceFrom(this.cursor.getPos());
				ArrayList<Cell> rangeOfAction = this.strat.evaluateRangeOfAction(this.chara[0].getPos(), dist);
				System.out.println("Chara : " + this.strat.getCharactersInRange(rangeOfAction).size());
				System.out.println("Bonus : " + this.strat.getBonusInRange(rangeOfAction).size());
				for (Cell cell : rangeOfAction) {
					try {
						cell.setCellType(1);
					} catch (SlickException e) {
						e.printStackTrace();
					}
				}
				break;

			case Input.KEY_Y:
				ArrayList<Cell> rangeOfAction2 = new ArrayList<>();
				int dist2 = this.chara[0].getPos().distanceFrom(this.cursor.getPos());
				rangeOfAction2 = this.strat.evaluateRangeOfAction(this.chara[0].getPos(), dist2);
				for (Cell cell : rangeOfAction2) {
					try {
						cell.setCellType(0);
					} catch (SlickException e) {
						e.printStackTrace();
					}
				}
				break;
			
			case Input.KEY_Q:
				int dist3 = this.chara[0].getPos().distanceFrom(this.cursor.getPos());
				System.out.println(dist3);
				break;
		}
		
	}
	
	
	@Override
	public void init(GameContainer gc) throws SlickException {

		this.initGridDB(gc);
		System.out.println("Cell size : " + Grid.cellSize);

		this.cursor = new entity.Cursor(Main.gameGrid.getCell(25, 9));
		
		this.chara[0] = new entity.Character(Main.gameGrid.getCell(24, 7), 10, 10, 4, 1, 100);
		this.chara[1] = new entity.Character(Main.gameGrid.getCell(26, 11), 10, 10, 4, 1, 100);

		this.strat = new OffensiveStrategie(this.chara[0]);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		Main.gameGrid.render(gc, g);

		for (Character element : this.chara) {
			element.render(gc, g);
		}

		this.cursor.render(gc, g);
		g.drawString(this.cursor.getPos().toString(), gc.getWidth() - 100, gc.getHeight() - 50);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {

	}


	public void initGridDB(GameContainer gc) throws SlickException {
		File f = new File("../ProjetJeu/res/test.txt");
		try {
			Scanner sc = new Scanner(f);
			int type, dim, selectedMap = 2; // selectedMap = choix de map, 0 pour map 1, 1 pour map 2 etc..
			int countMap = 0;
			
			while (countMap != selectedMap) {
				sc.next();
				if (sc.hasNext("&")) {
					countMap++;
					sc.next();
					sc.next();
				}
			}

			dim = sc.nextInt();
			Main.gameGrid = new Grid(dim, dim);
			Main.gameGrid.init(gc);
			sc.next(";");
			
			for (int i = 0; i < dim; i++) {
				for (int j = 0; j < dim; j++) {
					type = sc.nextInt();
					Grid.grid[i][j].setCellType(type);
					sc.next(";");
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
