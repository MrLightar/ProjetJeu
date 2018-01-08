package core;


import org.newdawn.slick.*;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import entity.Archer;
import entity.Character;
import entity.Cursor;
import entity.Mage;
import entity.Warrior;
import map.Grid;
import map.Cell;


public class Main extends BasicGame {

//	public static final int width = 960;
//	public static final int height = 540;
	public static final int width = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int height = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private static final boolean fullscreen = false;

	public static Grid gameGrid;
	
	
	Cursor cursor;
	//taille du tableau défini actuellement le nb de personnage crée
	Character[] chara = new Character[6];


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
				
			case Input.KEY_Z:
				this.chara[1].moveUp();
				break;
				
			case Input.KEY_Q:
				this.chara[1].moveLeft();
				break;
				
			case Input.KEY_S:
				this.chara[1].moveDown();
				break;
				
			case Input.KEY_D:
				this.chara[1].moveRight();
				break;
			
			case Input.KEY_ESCAPE:
				System.exit(0);
				break;
		}
		
	}
	
	
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		
		//initialisation grille
		this.initGridDB(gc);				
		System.out.println("Cell size : " + Grid.cellSize);
		//création curseur
		this.cursor = new entity.Cursor(Main.gameGrid.getCell(1, 2));
		//initialisation personnage		
		initCharacter(gc);
		
		
	}
	
	

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		Main.gameGrid.render(gc, g);
		
		for( int i=0; i< chara.length; i++) {
			this.chara[i].render(gc, g);
			
		}
		
		this.cursor.render(gc, g);
		
	}

	
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		for( int i=0; i< chara.length; i++) {
			this.chara[i].update(gc, delta);			
		}
	}
	
	
	public void initCharacter(GameContainer gc) throws SlickException {
		File f = new File("../ProjetJeu/res/character.txt");
		
		try {
			Scanner sc = new Scanner(f);
			
			int stat[] = new int[6];
			for (int i = 0; i < chara.length; i++) {
				for (int j = 0; j < 6; j++) {
					stat[j] = sc.nextInt();
					sc.next(";");
				}
				switch(stat[0]) {
					case 0:
						this.chara[i] = new Mage(Main.gameGrid.getCell(i, i), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5]);
						break;
					
					case 1:
						this.chara[i] = new Warrior(Main.gameGrid.getCell(i, i), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5]);
						break;
					
					case 2:
						this.chara[i] = new Archer(Main.gameGrid.getCell(i, i), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5]);
						break;
				}
			//	this.chara[i] = new entity.Character(Main.gameGrid.getCell(i, i), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5]);
				this.chara[i].init(gc);
			}
			
			
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
		
	
	public void initGridDB (GameContainer gc) throws SlickException {
		File f = new File("../ProjetJeu/res/map.txt");
			
		try {	    	
			Scanner sc = new Scanner(f);
			int type, dim, selectedMap = 1; //selectedMap = choix de map, 0 pour map 1, 1 pour map 2 etc..			
			int countMap = 0;
			
			while(countMap!=selectedMap) {
				sc.next();
				if(sc.hasNext("&")) {
					countMap++;
					sc.next();
					sc.next();
				}
			}
						
			dim=sc.nextInt();
			Main.gameGrid = new Grid(dim, dim);
			Main.gameGrid.init(gc);
			sc.next(";");
			
			for(int i = 0; i < dim; i++) {
				for(int j = 0; j < dim; j++) {
					type = sc.nextInt();					
					Grid.grid[i][j].setCellType(type);					
					sc.next(";");
				}				
			}
			sc.close();			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
