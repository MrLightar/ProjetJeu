package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import entity.Archer;
import entity.Character;
import entity.Cursor;
import entity.Mage;
import entity.Warrior;
import map.Grid;

public class Play extends BasicGameState {
	
	private StateBasedGame sbg;
	private GameContainer gc;
	
	
	public Play(int state) {
	}
	
	
	public static Grid gameGrid;
	
	Cursor cursor;
	//taille du tableau defini actuellement le nb de personnage cree
	Character[] chara = new Character[6];
	
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.sbg = sbg;
		this.gc = gc;
		
		//initialisation grille
		this.initGridDB(gc);
		System.out.println("Cell size : " + Grid.cellSize);

		//creation curseur
		this.cursor = new entity.Cursor(Play.gameGrid.getCell(1, 2));
		
		//initialisation personnage	
		initCharacter();
	}
	
	

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		Play.gameGrid.render(gc, g);
		
		for( int i=0; i< chara.length; i++) {
			this.chara[i].render(gc, g);
		}
		
		this.cursor.render(gc, g);
	}

	
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		for( int i=0; i< chara.length; i++) {
			this.chara[i].update(gc, delta);			
		}
	}
	
	@Override
	public int getID() {
		return Main.play;
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
				if (currentJ < Play.gameGrid.getCols() - 1) {
					this.cursor.setPosFromIndex(currentI, currentJ + 1);
				}
				break;
			
			case Input.KEY_UP:
				if (currentI > 0) {
					this.cursor.setPosFromIndex(currentI - 1, currentJ);
				}
				break;
			
			case Input.KEY_DOWN:
				if (currentI < Play.gameGrid.getRows() - 1) {
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
				sbg.enterState(Main.menu);
				break;
				
			case Input.KEY_ENTER:
				sbg.enterState(Main.selectCharaScreen);
				break;
		}
		
	}
		
	
	
	public void initCharacter() throws SlickException {
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
						this.chara[i] = new Mage(Play.gameGrid.getCell(i, i), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5]);
						break;
				
					case 1:
						this.chara[i] = new Warrior(Play.gameGrid.getCell(i, i), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5]);
						break;
				
					case 2:
						this.chara[i] = new Archer(Play.gameGrid.getCell(i, i), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5]);
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
			int type, dim, selectedMap = 2; //selectedMap = choix de map, 0 pour map 1, 1 pour map 2 etc..			
			int countMap = 1;
			
			while(countMap!=selectedMap) {
				sc.next();
				if(sc.hasNext("&")) {
					countMap++;
					sc.next();
					sc.next();
				}
			}
						
			dim=sc.nextInt();
			Play.gameGrid = new Grid(dim, dim);
			Play.gameGrid.init(gc);
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
