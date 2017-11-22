package map;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import core.Main;


public class Grid {

	private int rows;
	private int cols;
	public static int cellSize;
	public static Cell[][] grid;


	public Grid(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		if (rows >= cols) {
			Grid.cellSize = Main.height / this.rows;
		} else {
			Grid.cellSize = Main.width / this.cols;
		}
	}
	
	
	public int getRows() {
		return this.rows;
	}

	public int getCols() {
		return this.cols;
	}

	public Cell getCell(int i, int j) {
		return Grid.grid[i][j];
	}
	
	
	public void init(GameContainer gc) throws SlickException {
		Grid.grid = new Cell[this.rows][this.cols];
		for (int j = 0; j < this.rows; j++) {
			for (int i = 0; i < this.cols; i++) {
				Cell cell = new Cell(i, j);
				Grid.grid[j][i] = cell;
			}
		}
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (int j = 0; j < this.rows; j++) {
			for (int i = 0; i < this.cols; i++) {
				Grid.grid[j][i].render(gc, g);
			}
		}
	}
	
}
