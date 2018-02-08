package core;

import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.particles.ConfigurableEmitter.RandomValue;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import entity.Archer;
import entity.Character;
import entity.Cursor;
import entity.Mage;
import entity.Warrior;
import map.Grid;
import selectScreen.SelectGrid;
import strategy.*;

public class Play extends BasicGameState {
	
	private StateBasedGame sbg;
	private GameContainer gc;
	
	public static Grid gameGrid;
	
	private static Cursor cursor;

	private static ArrayList<Character> chara;
	private static ArrayList<Character> enemyTeam;
	
	private int indexAlly;
	private int indexEnemy;
	private int indexTeam;
	
	private static int mapLevel;
	private static int maxPrice;
	private static int currentPrice;
	
	public static TrueTypeFont ttf1;
	public static TrueTypeFont ttf2;
	public static TrueTypeFont ttf3;
	
	private Image background;
	private Image image_pv;
	private Image image_att;
	private Image image_po;
	private Image image_pm;
	
	
	private int state;
	
	private static final int placement = 0;
	private static final int stratSelect = 1;
	private static final int playing = 2;
	private static final int win = 3;
	private static final int gameOver = 4;
	
	private int selectedStrat;
	
	private Image offensiveButton;
	private Image defensiveButton;
	private Image balancedButton;
	private Image stratCursor;
	
	private static final int offensive = 0;
	private static final int balanced = 1;
	private static final int defensive = 2;
	
	private int heightButton;
	private int widthButton;
	
	private Strategy strat;//POUR TEST
	
	public static ByteArrayOutputStream baos;
	
	
	public Play(int state) {
		
		// Create a stream to hold the output
	    baos = new ByteArrayOutputStream();
	    PrintStream ps = new PrintStream(baos);
	    // IMPORTANT: Save the old System.out!
	    PrintStream old = System.out;
	    // Tell Java to use your special stream
	    System.setOut(ps);
	    // Put things back
	    System.out.flush();
	    System.setOut(old);
	}
	
	

	public static Cursor getCursor() {
		return cursor;
	}

	public static void setCursor(Cursor cursor) {
		Play.cursor = cursor;
	}

	public static ArrayList<Character> getChara() {
		return chara;
	}

	public static void setChara(ArrayList<Character> chara) {
		Play.chara = chara;
	}

	public static int getMaxPrice() {
		return maxPrice;
	}
	
	public static int getCurrentPrice() {
		return currentPrice;
	}
	public static int getMapLevel() {
		return mapLevel;
	}

	public static void setMapLevel(int mapLevel) {
		Play.mapLevel = mapLevel;
	}



	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.sbg = sbg;
		this.gc = gc;
		
		chara = new ArrayList<>();
		enemyTeam = new ArrayList<>();
		
		indexAlly = 0;
		indexEnemy = 0;
		indexTeam = 0;
		
		//initialisation grille
		this.initGridDB(gc);

		//creation curseur
		Play.cursor = new entity.Cursor(Play.gameGrid.getCell(1, 2));
		
		//initialisation personnage	
		initCharacter();
		initEnemy();
		
		
		ttf1 = new TrueTypeFont(new java.awt.Font("Verdana", Font.BOLD, Main.height/30), true);
		ttf2 = new TrueTypeFont(new java.awt.Font("Verdana", Font.BOLD, Main.height/50), true);
		ttf3 = new TrueTypeFont(new java.awt.Font("Verdana", Font.BOLD, Main.height/11), true);
		
		background = new Image("/res/right_background.png");
		background = background.getScaledCopy(Main.width-Main.height, Main.height);
		image_pv = new Image("res/pv.png");
		image_pv = image_pv.getScaledCopy(Main.height/20, Main.height/20);
		image_att = new Image("res/att.png");
		image_att = image_att.getScaledCopy(Main.height/20, Main.height/20);
		image_po = new Image("res/po.png");
		image_po = image_po.getScaledCopy(Main.height/20, Main.height/20);
		image_pm = new Image("res/pm.png");
		image_pm = image_pm.getScaledCopy(Main.height/20, Main.height/20);
		
		
		heightButton = Main.height / 16;
		widthButton = Main.width / 6;
		
		this.offensiveButton = new Image("res/offensiveButton.png");
		this.offensiveButton = this.offensiveButton.getScaledCopy(widthButton, heightButton);
		this.balancedButton = new Image("res/balancedButton.png");
		this.balancedButton = this.balancedButton.getScaledCopy(widthButton, heightButton);
		this.defensiveButton = new Image("res/defensiveButton.png");
		this.defensiveButton = this.defensiveButton.getScaledCopy(widthButton, heightButton);
		this.stratCursor = new Image("res/stratCursor.png");
		this.stratCursor = this.stratCursor.getScaledCopy(widthButton, heightButton);
		
		selectedStrat = offensive;
		state = placement;
		
		
		this.strat = new DefensiveStrategy(Play.chara.get(0)); //POUR TEST
		
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("\n");
	}
	
	

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		Play.gameGrid.render(gc, g);
		
		for( int i=0; i< chara.size(); i++) {
			Play.chara.get(i).render(gc, g);
		}
		for( int i=0; i< enemyTeam.size(); i++) {
			Play.enemyTeam.get(i).renderEnemy(gc, g);
		}
//		Play.chara.get(1).render(gc, g);
		
		g.drawImage(background, Main.height, 0);
		
		
		
		String[] words = baos.toString().split("\n");
		for (int i=1; i<5; i++) {
			ttf2.drawString(Main.width*15/24, Main.height*15/20, words[words.length-1]);
			if(words.length>5) {
				ttf2.drawString(Main.width*15/24, Main.height*(15+i)/20, words[words.length-1 -i], Color.lightGray);
			}
		}
		
		if(state == placement || state == stratSelect) {
			Play.cursor.render(gc, g);
			
			g.drawRect(Main.width*15/24, Main.height*4/20, Main.width*4/24, Main.height*1/20);
			g.fillRect(Main.width*15/24, Main.height*4/20, Main.width*4/24 * (Play.currentPrice/(float)Play.maxPrice), Main.height*1/20);
			ttf1.drawString(Main.width*15/24, Main.height*5/20, new Integer(Play.currentPrice).toString() + " / " + new Integer(Play.maxPrice).toString() + " $");
		}
		
		if(cursor.hasCharacter()) {
			g.drawImage(image_pv, Main.width*15/24, Main.height*10/20);
			g.drawImage(image_att, Main.width*17/24, Main.height*10/20);
			g.drawImage(image_po, Main.width*15/24, Main.height*12/20);
			g.drawImage(image_pm, Main.width*17/24, Main.height*12/20);
			ttf1.drawString(Main.width*16/24, Main.height*10/20, new Integer(cursor.getSelection().getPv_max()).toString());
			ttf1.drawString(Main.width*18/24, Main.height*10/20, new Integer(cursor.getSelection().getAtt()).toString());
			ttf1.drawString(Main.width*16/24, Main.height*12/20, new Integer(cursor.getSelection().getPO()).toString());
			ttf1.drawString(Main.width*18/24, Main.height*12/20, new Integer(cursor.getSelection().getPM()).toString());
			ttf1.drawString(Main.width*15/24, Main.height*13/20, "Lvl." + new Integer(cursor.getSelection().getLevel()).toString());
			ttf1.drawString(Main.width*17/24, Main.height*13/20, new Integer(cursor.getSelection().getPrice()).toString() + "$");
		}
		
		if(cursor.isSelected()) {
			
			switch(cursor.getSelection().getStrategy().getClass().getSimpleName()) {
			case "OffensiveStrategy":
				g.drawImage(offensiveButton, Main.width*15/24, Main.height/16*7);
				break;
			case "DefensiveStrategy":
				g.drawImage(defensiveButton, Main.width*15/24, Main.height/16*7);
				break;
			case "BalancedStrategy":
				g.drawImage(balancedButton, Main.width*15/24, Main.height/16*7);
				break;
			}
		}
		
		
		if(state == stratSelect) {
			g.drawImage(offensiveButton, Main.width*15/24, Main.height/16*5);
			g.drawImage(balancedButton, Main.width*15/24, Main.height/16*6);
			g.drawImage(defensiveButton, Main.width*15/24, Main.height/16*7);
			g.drawImage(stratCursor, Main.width*15/24, Main.height/16 * (selectedStrat + 5));
		}
		
		if(state == win) {
			ttf3.drawString(Main.width/8, Main.height/3, "YOU WIN", Color.red);
		}
		
		if(state == gameOver) {
			ttf3.drawString(Main.width/8, Main.height/3, "GAME OVER", Color.red);
		}
		
	}

	
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		for( int i=0; i< chara.size(); i++) {
			Play.chara.get(i).update(gc, delta);			
		}
		for( int i=0; i< enemyTeam.size(); i++) {
			Play.enemyTeam.get(i).update(gc, delta);
		}
		
		//fonction de boucle de jeu du niveau	A METTRE DANS SA PROPRE METHODE
		if(state == playing) {
			levelPlay();
		}
		
		
	}
	
	public void levelPlay() {
		if (!Strategy.isPlaying()) {
			switch(checkLevelEnd()) {
			case 0 :
				switch(indexTeam%2) {
				case 0 :
					if(chara.get(indexAlly).isAlive()) {
						strat = chara.get(indexAlly).getStrategy();
						strat.gameTurn();
						indexTeam++;
					}
					indexAlly = (indexAlly+1)%chara.size();
					break;
				case 1 :
					if(enemyTeam.get(indexEnemy).isAlive()) {
						strat = enemyTeam.get(indexEnemy).getStrategy();
						strat.gameTurn();
						indexTeam++;
					}
					indexEnemy = (indexEnemy+1)%enemyTeam.size();
					break;
				}
				
				break;
			case 1 : 
				nextLevel(sbg, gc);
				break;
			case 2 :
				gameOver();
				break;
			}
		}
		this.strat.update();
	}
	
	public int checkLevelEnd() {
		boolean check = false;
		for (int i=0; i<enemyTeam.size(); i++) {
			if(enemyTeam.get(i).isAlive()) {
				check = true;
			}
		}
		if(check == false) {
			return 1;
		}
		
		check = false;
		for (int i=0; i<chara.size(); i++) {
			if(chara.get(i).isAlive()) {
				check = true;
			}
		}
		if(check == false) {
			return 2;
		}
		return 0;
	}
	
	public void nextLevel (StateBasedGame sbg, GameContainer gc){
		if(mapLevel == 9) {
			System.out.println("YOU WIN !!!");
			state = win;
		}
		else {
			writeNewCharaDB();
			try {		
				mapLevel++;
				sbg.getState(Main.play).init(gc, sbg);
				sbg.getState(Main.selectCharaScreen).init(gc, sbg);
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void gameOver (){
		System.out.println("GAME OVER !!!");
		state = gameOver;
	}
	
	
	public void writeNewCharaDB() {
		File f = new File("../ProjetJeu/res/character.txt");
		if(f.exists()) {
			f.delete();
		}
		f = new File("../ProjetJeu/res/character.txt");
		try {
			f.createNewFile();
			FileWriter fw=new FileWriter(f);
			
			for(int i=0; i<chara.size(); i++) {
				if(chara.get(i).isAlive()) {
					fw.write(chara.get(i).getJob() + " ; " + (chara.get(i).getLevel()+1) + " ; " + (chara.get(i).getPv_max()+5) + " ; " + (chara.get(i).getAtt()+1) + " ; " + chara.get(i).getPO() + " ; " + chara.get(i).getPM() + " ; " + (chara.get(i).getPrice()+10) + " ; ");  // écrire une ligne dans le fichier resultat.txt
					fw.write("\n"); // forcer le passage à la ligne
				}
			}
			fw.write("&");
			
			fw.close(); // fermer le fichier à la fin des traitements
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public int getID() {
		return Main.play;
	}
	
	public static void updatePrice(int price) {
		Play.currentPrice += price;
	}
	
	
	public void initCharacter() throws SlickException {
		File f = new File("../ProjetJeu/res/character.txt");
		
		try {
			Scanner sc = new Scanner(f);
			
			int stat[] = new int[7];
			int i = 0;
			while(!sc.hasNext("&")) {
				for (int j = 0; j < 7; j++) {
					stat[j] = sc.nextInt();
					sc.next(";");
				}
				switch(stat[0]) {
				case 0:
					Play.chara.add(new Mage(Play.gameGrid.getCell(i, i), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5], Character.ally));
					break;
				
					case 1:
					Play.chara.add(new Warrior(Play.gameGrid.getCell(i, i), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5], Character.ally));
					break;
				
				case 2:
					Play.chara.add(new Archer(Play.gameGrid.getCell(i, i), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5], Character.ally));
					break;
				}
				
				switch(new Random().nextInt(3)) {
				case offensive:
					Play.chara.get(Play.chara.size()-1).setStrategie(new OffensiveStrategy(chara.get(Play.chara.size()-1)));
					break;
				
				case balanced:
					Play.chara.get(Play.chara.size()-1).setStrategie(new OffensiveStrategy(chara.get(Play.chara.size()-1)));//A CHANGER EN BALANCED QUAND PRET
					break;
				
				case defensive:
					Play.chara.get(Play.chara.size()-1).setStrategie(new OffensiveStrategy(chara.get(Play.chara.size()-1)));
					break;
				}
				
				Play.chara.get(Play.chara.size()-1).init();
				i++;
			}
			
			
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void initEnemy() throws SlickException {
		File f = new File("../ProjetJeu/res/enemy.txt");
		
		try {
			Scanner sc = new Scanner(f);
	
			int nbStat = 8;
			int stat[] = new int[nbStat];
			
			int countMap = 0;
			
			while(countMap != mapLevel /*mapLevel*/) {
				sc.next();
				if(sc.hasNext("&")) {
					countMap++;
					sc.next();
					sc.next();
				}
			}
			
			while(!sc.hasNext("&")) {
				
				for (int j = 0; j < nbStat; j++) {
					stat[j] = sc.nextInt();
					sc.next(";");
				}
				switch(stat[0]) {
				case 0:
					Play.enemyTeam.add(new Mage(Play.gameGrid.getCell(stat[6], stat[7]), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5], Character.enemy));
					break;
				
				case 1:
					Play.enemyTeam.add(new Warrior(Play.gameGrid.getCell(stat[6], stat[7]), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5], Character.enemy));
					break;
				
				case 2:
					Play.enemyTeam.add(new Archer(Play.gameGrid.getCell(stat[6], stat[7]), stat[0], stat[1], stat[2], stat[3], stat[4], stat[5], Character.enemy));
					break;
				}
				
				switch(new Random().nextInt(3)) {
				case offensive:
					Play.enemyTeam.get(Play.enemyTeam.size()-1).setStrategie(new DefensiveStrategy(Play.enemyTeam.get(Play.enemyTeam.size()-1)));//A CHANGER
					break;
				
				case balanced:
					Play.enemyTeam.get(Play.enemyTeam.size()-1).setStrategie(new DefensiveStrategy(Play.enemyTeam.get(Play.enemyTeam.size()-1)));//A CHANGER EN BALANCED QUAND PRET
					break;
				
				case defensive:
					Play.enemyTeam.get(Play.enemyTeam.size()-1).setStrategie(new DefensiveStrategy(Play.enemyTeam.get(Play.enemyTeam.size()-1)));
					break;
				}
				
				Play.enemyTeam.get(Play.enemyTeam.size()-1).setTeam(Character.enemy);
				Play.enemyTeam.get(Play.enemyTeam.size()-1).init();
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
			int type, dim;		
			int countMap = 0;
			
			while(countMap!=mapLevel) {
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
			
			Play.maxPrice = sc.nextInt();
			Play.currentPrice = 0;
			sc.next(";");
			
			for(int i = 0; i < dim; i++) {
				for(int j = 0; j < dim; j++) {
					type = sc.nextInt();					
					Grid.grid[i][j].setCellType(type);	
					Grid.grid[i][j].init();
					sc.next(";");
				}				
			}
			sc.close();			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	@Override
	public void keyPressed(int key, char c) {
		int currentI = Play.cursor.getPos().getI();
		int currentJ = Play.cursor.getPos().getJ();

		switch (key) {
			case Input.KEY_LEFT:
				switch(state) {
					case placement:
						if (currentJ > 0) {
							Play.cursor.setPosFromIndex(currentI, currentJ - 1);
						}
						break;
				
					case stratSelect:
						
						break;
				
					case playing:
						
						break;
				}
				break;
			
			case Input.KEY_RIGHT:
				switch(state) {
					case placement:
						if (currentJ < Play.gameGrid.getCols() - 1) {
							Play.cursor.setPosFromIndex(currentI, currentJ + 1);
						}
						break;
			
					case stratSelect:
						
						break;
			
					case playing:
						
						break;
				}
				break;
			
			case Input.KEY_UP:
				switch(state) {
					case placement:
						if (currentI > 0) {
							Play.cursor.setPosFromIndex(currentI - 1, currentJ);
						}
						break;
						
					case stratSelect:
						if (selectedStrat != offensive) {
							selectedStrat -= 1;
						}
						break;
						
					case playing:
						
						break;
				}
				break;
			
			case Input.KEY_DOWN:
				switch(state) {
					case placement:
						if (currentI < Play.gameGrid.getRows() - 1) {
							Play.cursor.setPosFromIndex(currentI + 1, currentJ);
						}
						break;
						
					case stratSelect:
						if (selectedStrat != defensive) {
							selectedStrat += 1;
						}
						break;
						
					case playing:
						
						break;
				}
				break;

			case Input.KEY_SPACE:
				//Play.cursor.testSelectMove();
				state = playing;
				break;
				
			case Input.KEY_A:
				Play.cursor.testSelectAttack();
				break;
				
			case Input.KEY_Z:
				Play.chara.get(1).moveUp();
				break;
				
			case Input.KEY_Q:
				Play.chara.get(1).moveLeft();
				break;
				
			case Input.KEY_S:
				Play.chara.get(1).moveDown();
				break;
				
			case Input.KEY_D:
				Play.chara.get(1).moveRight();
				break;
				
			case Input.KEY_I:
				this.strat.gameTurn();
				break;
				
			case Input.KEY_P:
				writeNewCharaDB();
				break;
			
			case Input.KEY_ESCAPE:
				switch(state) {
				case placement:
					if(cursor.hasCharacter()) {
						cursor.deselect();
					}
					else {
						sbg.enterState(Main.menu);
					}
					break;
				
				case stratSelect:
					selectedStrat = offensive;
					state = placement;
					break;
				
				case playing:
					state = placement;//POUR TEST
					break;
				
				case win:
					sbg.enterState(Main.menu);
					break;
				
				case gameOver:
					sbg.enterState(Main.menu);
					break;
				}
				break;
				
			case Input.KEY_DELETE:
				if(cursor.hasCharacter()) {
					cursor.supr();
				}
				break;
				
			case Input.KEY_ENTER:
				switch(state) {
					case placement:
						if(cursor.isSelected()) {
							if(cursor.getSelection().getTeam()==Character.ally) {
								cursor.selectMove();
							}
						}
						else if(cursor.hasCharacter()) {
							if (cursor.isPlacable()) {
								state = stratSelect;
							}
						}
						else if(cursor.onCharacter()){
							cursor.select();
						}
						else {
							sbg.enterState(Main.selectCharaScreen);
						}
						break;
					
					case stratSelect:
						switch (selectedStrat) {
						case offensive:
							cursor.getSelection().setStrategie(new OffensiveStrategy(cursor.getSelection()));
							cursor.placeCharacter();
							break;
						case balanced:
							cursor.getSelection().setStrategie(new DefensiveStrategy(cursor.getSelection()));//A CHANGER EN BALANCED QUAND PRET
							cursor.placeCharacter();
							break;
						case defensive:
							cursor.getSelection().setStrategie(new DefensiveStrategy(cursor.getSelection()));
							cursor.placeCharacter();
							break;
						}
						selectedStrat = offensive;
						state = placement;
						break;
					
					case playing:
						
						break;
				}
				break;
		}
		
	}
	

}
