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

		Grid.cellSize = Main.height / this.rows;
		if(cols*Grid.cellSize > Main.width) {
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
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				Grid.grid[i][j] = new Cell(i, j);
			}
		}
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				Grid.grid[i][j].render(gc, g);
			}
		}
	}
	
}
