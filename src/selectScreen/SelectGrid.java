package selectScreen;

import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import core.Main;
import map.Cell;
import map.Grid;

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
	
	
	public void init(GameContainer gc) throws SlickException {
		SelectGrid.grid = new SelectCell[this.rows][this.cols];
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				SelectGrid.grid[i][j] = new SelectCell(i, j);
			}
		}
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				SelectGrid.grid[i][j].render(gc, g);
			}
		}
	}
}
