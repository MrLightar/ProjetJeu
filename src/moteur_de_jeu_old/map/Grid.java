package map;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public class Grid {
	
	private int rows;
	private int cols;
	public static int cellSize;
	public Cell[][] grid;
	
	
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
		return this.grid[i][j];
	}
	
	
	public void init(GameContainer gc) throws SlickException {
		this.grid = new Cell[this.rows][this.cols];
		for (int j = 0; j < this.rows; j++) {
			for (int i = 0; i < this.cols; i++) {
				Cell cell = new Cell(i, j);
				this.grid[j][i] = cell;
			}
		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (int j = 0; j < this.rows; j++) {
			for (int i = 0; i < this.cols; i++) {
				this.grid[j][i].render(gc, g);
			}
		}
	}

}
