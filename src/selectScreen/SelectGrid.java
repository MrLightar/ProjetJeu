package selectScreen;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import core.Main;
import entity.Archer;
import entity.Character;
import entity.Mage;
import entity.Warrior;

public class SelectGrid {
	
	private int rows;
	private int cols;
	public static int cellSizeX;
	public static int cellSizeY;
	public static SelectCell[][] grid;
	public static TrueTypeFont ttf1;
	public static TrueTypeFont ttf2;



	public SelectGrid(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
    
		SelectGrid.cellSizeX = Main.width / this.cols;
		SelectGrid.cellSizeY = Main.height / this.rows;
		ttf1 = new TrueTypeFont(new java.awt.Font("Verdana", Font.BOLD, cellSizeY/10), true);
		ttf2 = new TrueTypeFont(new java.awt.Font("Verdana", Font.BOLD+Font.ITALIC, cellSizeY/8), true);

	}
	
	
	public int getRows() {
		return this.rows;
	}

	public int getCols() {
		return this.cols;
	}

	public SelectCell getCell(int i, int j) {
		return SelectGrid.grid[i][j];
	}
	
	public void noplacable(entity.Character selection) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				if(SelectGrid.grid[i][j].hasChara() && SelectGrid.grid[i][j].getChara() == selection) {
					SelectGrid.grid[i][j].setPlaced(true);
				}
			}
		}
	}
	
	public void replacable(entity.Character selection) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				if(SelectGrid.grid[i][j].hasChara() && SelectGrid.grid[i][j].getChara() == selection) {
					SelectGrid.grid[i][j].setPlaced(false);
				}
			}
		}
	}
	
	
	public void init(GameContainer gc) throws SlickException {
		SelectGrid.grid = new SelectCell[this.rows][this.cols];
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				SelectGrid.grid[i][j] = new SelectCell(i, j);
			}
		}
		
		File f = new File("res/character.txt");
		
		try {
			Scanner sc = new Scanner(f);
			
			int i = 0;
			int j = 0;
			int stat[] = new int[7];
			while (!sc.hasNext("&")) {
				for (int k = 0; k < 7; k++) {
					stat[k] = sc.nextInt();
					sc.next(";");
				}
				
				switch(stat[0]) {
					case 0:
						SelectGrid.grid[i][j].setChara(new Mage(stat[0], stat[1], stat[2], stat[3], stat[4], stat[5], stat[6], Character.ally));
						break;
				
					case 1:
						SelectGrid.grid[i][j].setChara(new Warrior(stat[0], stat[1], stat[2], stat[3], stat[4], stat[5], stat[6], Character.ally));
						break;
				
					case 2:
						SelectGrid.grid[i][j].setChara(new Archer(stat[0], stat[1], stat[2], stat[3], stat[4], stat[5], stat[6], Character.ally));
						break;
					}
				
				if(j<cols-1) {
					j++;
				}
				else {
					i++;
					j = 0;
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				SelectGrid.grid[i][j].render(gc, g);
			}
		}
	}
	
	public void writeNewCharaDB() {
		File f = new File("res/temporary.txt");
		File ff = new File("res/character.txt");
		
		
		
		try {
			Scanner sc = new Scanner(ff);
			FileWriter fw=new FileWriter(f);
			for (int i = 0; i < this.rows; i++) {
				for (int j = 0; j < this.cols; j++) {
					if(SelectGrid.grid[i][j].hasChara()) {
						if(!SelectGrid.grid[i][j].isPlaced()) {
							fw.write(sc.nextLine());
							fw.write("\n");
						}else {
							sc.nextLine();
						}
					}
				}
			}
		
			fw.close();
			sc.close();
			
			Files.copy(Paths.get("res/temporary.txt"), Paths.get("res/character.txt"), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean verifPool() {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				if(SelectGrid.grid[i][j].hasChara()) {
					if(!SelectGrid.grid[i][j].isPlaced()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
