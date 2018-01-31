package core;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import map.*;
import strategy.*;
import entity.*;
import entity.Character;


public class Play extends BasicGameState {

	private StateBasedGame sbg;
	private GameContainer gc;

	public static Grid gameGrid;

	private static Cursor cursor;
	// taille du tableau defini actuellement le nb de personnage cree
	private static ArrayList<Character> chara;
	
	private Strategy strat;


	public Play(int state) {
	}
	
	
	public static Cursor getCursor() {
		return Play.cursor;
	}
	
	public static void setCursor(Cursor cursor) {
		Play.cursor = cursor;
	}
	
	public static ArrayList<Character> getChara() {
		return Play.chara;
	}
	
	public static void setChara(ArrayList<Character> chara) {
		Play.chara = chara;
	}
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.sbg = sbg;
		this.gc = gc;

		Play.chara = new ArrayList<>();

		// initialisation grille
		this.initGridDB(gc);
		System.out.println("Cell size : " + Grid.cellSize);

		// creation curseur
		Play.cursor = new entity.Cursor(Play.gameGrid.getCell(1, 2));

		// initialisation personnage
		this.initCharacter();
		Play.chara.get(0).setTeam(Character.ally);

		this.strat = new DefensiveStrategy(Play.chara.get(0));
	}
	
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		Play.gameGrid.render(gc, g);

		for (int i = 0; i < Play.chara.size(); i++) {
			Play.chara.get(i).render(gc, g);
		}
		Play.cursor.render(gc, g);

		g.drawString(Play.cursor.getPos().toString(), 900, 500);
	}
	
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		for (int i = 0; i < Play.chara.size(); i++) {
			Play.chara.get(i).update(gc, delta);
		}
		this.strat.update();
	}

	@Override
	public int getID() {
		return Main.play;
	}
	
	
	public void initCharacter() throws SlickException {
		File f = new File("../ProjetJeu/res/character.txt");

		try {
			Scanner sc = new Scanner(f);

			int stat[] = new int[6];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 6; j++) {
					stat[j] = sc.nextInt();
					sc.next(";");
				}
				switch (stat[0]) {
					case 0:
						Play.chara
								.add(new Mage(Play.gameGrid.getCell(i, i), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5]));
						break;
					
					case 1:
						Play.chara.add(
								new Warrior(Play.gameGrid.getCell(i, i), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5]));
						break;
					
					case 2:
						Play.chara.add(
								new Archer(Play.gameGrid.getCell(i, i), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5]));
						break;
				}
				Play.chara.get(Play.chara.size() - 1).init();
			}

			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}


	public void initGridDB(GameContainer gc) throws SlickException {
		File f = new File("../ProjetJeu/res/map.txt");
		
		try {
			Scanner sc = new Scanner(f);
			int type, dim, selectedMap = 1; // selectedMap = choix de map, 0 pour map 1, 1 pour map 2 etc..
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
			Play.gameGrid = new Grid(dim, dim);
			Play.gameGrid.init(gc);
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


	@Override
	public void keyPressed(int key, char c) {
		int currentI = Play.cursor.getPos().getI();
		int currentJ = Play.cursor.getPos().getJ();
		
		switch (key) {
			case Input.KEY_LEFT:
				if (currentJ > 0) {
					Play.cursor.setPosFromIndex(currentI, currentJ - 1);
				}
				break;

			case Input.KEY_RIGHT:
				if (currentJ < Play.gameGrid.getCols() - 1) {
					Play.cursor.setPosFromIndex(currentI, currentJ + 1);
				}
				break;

			case Input.KEY_UP:
				if (currentI > 0) {
					Play.cursor.setPosFromIndex(currentI - 1, currentJ);
				}
				break;

			case Input.KEY_DOWN:
				if (currentI < Play.gameGrid.getRows() - 1) {
					Play.cursor.setPosFromIndex(currentI + 1, currentJ);
				}
				break;

			case Input.KEY_ESCAPE:
				if (Play.cursor.hasCharacter()) {
					Play.cursor.deselect();
				} else {
					this.sbg.enterState(Main.menu);
				}
				break;
			
			case Input.KEY_DELETE:
				if (Play.cursor.hasCharacter()) {
					Play.cursor.supr();
				}
				break;
			
			case Input.KEY_ENTER:
				if (Play.cursor.hasCharacter()) {
					Play.cursor.placeCharacter();
				} else if (Play.cursor.onCharacter()) {
					Play.cursor.selectMove();
				} else {
					this.sbg.enterState(Main.selectCharaScreen);
				}
				break;
			
			
			case Input.KEY_A:
				this.strat.gameTurn();
				break;

			case Input.KEY_Z:
				ArrayList<Cell> path = this.strat.evaluatePath(Play.chara.get(0).getPos(), Play.chara.get(1).getPos());
				System.out.println(path);
				break;
		}

	}
	
	
}
