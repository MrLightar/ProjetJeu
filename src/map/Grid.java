package map;


import java.util.ArrayList;

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
	
	public Cell getCellContaining(int i, int j) {
		ArrayList<Cell> search=new ArrayList<>();
		search=getCell(j/cellSize,i/cellSize).getAllNeighbors(this);
		search.add(getCell(j/cellSize,i/cellSize));
		Cell res=null;
		int k=0;
		while(search!=null && k<search.size()){
			if(search.get(k).Contains(i, j)) {
				res=search.get(k);
				if(res.getCellType()==Cell.wallCell) {
					return res;
				}
			}
			
			k++;
		}
		return res;
	}
	
	public boolean isInGrid(int i, int j) {
		return i >= 0 && i < this.rows && j >= 0 && j < this.cols;
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
