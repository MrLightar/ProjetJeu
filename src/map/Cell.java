package map;


import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import entity.Character;


public class Cell {

	private int i;
	private int j;
	private Character chara;
	private int cellType;
	private int cellBonus;

	private Image texture;

	private double PathFScore;
	private double PathGScore;
	private double PathHScore;

	private ArrayList<Cell> neighbors;
	private Cell previous;


	public Cell(int i, int j) {
		this.i = i;
		this.j = j;
		this.chara = null;

		this.PathFScore = 0;
		this.PathGScore = 0;
		this.PathHScore = 0;
		
		this.neighbors = new ArrayList<>();
		this.previous = null;
	}


	public int getI() {
		return this.i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return this.j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public double getPathFScore() {
		return this.PathFScore;
	}
	
	
	public void setPathFScore(double pathFScore) {
		this.PathFScore = pathFScore;
	}
	
	
	public double getPathGScore() {
		return this.PathGScore;
	}
	
	
	public void setPathGScore(double pathGScore) {
		this.PathGScore = pathGScore;
	}
	
	
	public double getPathHScore() {
		return this.PathHScore;
	}
	
	
	public void setPathHScore(double pathHScore) {
		this.PathHScore = pathHScore;
	}
	
	
	public ArrayList<Cell> getNeighbors() {
		return this.neighbors;
	}
	
	
	public void setNeighbors(ArrayList<Cell> neighbors) {
		this.neighbors = neighbors;
	}
	
	
	public Cell getPrevious() {
		return this.previous;
	}
	
	
	public void setPrevious(Cell previous) {
		this.previous = previous;
	}
	
	
	public Character getChara() {
		return this.chara;
	}
	
	public void setChara(Character character) {
		this.chara = character;
	}
	
	
	public int getCellType() {
		return this.cellType;
	}
	
	
	public void setCellType(int cellType) throws SlickException {
		this.cellType = cellType;
		switch (this.cellType) {
			case 0:
				this.texture = new Image("res/grass.png");
				this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
				break;
			case 1:
				this.texture = new Image("res/wall.png");
				this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
				break;
			case 2:
				this.texture = new Image("res/water.png");
				this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
				break;
			case 3:
				this.texture = new Image("res/grass_bonus_attack.png");
				this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
				break;
			case 4:
				this.texture = new Image("res/grass_bonus_move.png");
				this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
				break;
		}
	}


	public boolean hasChara() {
		if (this.getChara() != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getCellBonus() {
		return this.cellBonus;
	}
	
	public void setCellBonus(int cellBonus) {
		this.cellBonus = cellBonus;
	}


	public int distanceFrom(Cell other) {
		return Math.abs(other.i - this.i) + Math.abs(other.j - this.j);
	}


	public void addNeighbors(Grid grid) {
		int i = this.i;
		int j = this.j;
		if (j < grid.getCols() - 1) {
			this.neighbors.add(grid.getCell(i, j + 1));
		}
		if (j > 0) {
			this.neighbors.add(grid.getCell(i, j - 1));
		}
		if (i < grid.getRows() - 1) {
			this.neighbors.add(grid.getCell(i + 1, j));
		}
		if (i > 0) {
			this.neighbors.add(grid.getCell(i - 1, j));
		}
	}
	
	
	public void printNeighbors() {
		for (Cell cell : this.neighbors) {
			System.out.println(cell);
		}
	}

	public ArrayList<Cell> getAllNeighbors(Grid grid) {
		int i = this.i;
		int j = this.j;
		ArrayList<Cell> res = new ArrayList<>();
		if (j < grid.getCols() - 1) {
			res.add(grid.getCell(i, j + 1));
		}
		if (j > 0) {
			res.add(grid.getCell(i, j - 1));
		}

		if (i < grid.getRows() - 1) {
			res.add(grid.getCell(i + 1, j));
		}
		if (i > 0) {
			res.add(grid.getCell(i - 1, j));
		}
		if (j < grid.getCols() - 1 && i < grid.getRows() - 1) {
			res.add(grid.getCell(i + 1, j + 1));
		}
		if (j < grid.getCols() - 1 && i > 0) {
			res.add(grid.getCell(i - 1, j + 1));
		}
		if (j > 0 && i < grid.getRows() - 1) {
			res.add(grid.getCell(i + 1, j - 1));
		}
		if (j > 0 && i > 0) {
			res.add(grid.getCell(i - 1, j - 1));
		}

		return res;
	}

	public void render(GameContainer gc, Graphics g) {
		int x = this.j * Grid.cellSize;
		int y = this.i * Grid.cellSize;
		Rectangle rect = new Rectangle(x + 1, y + 1, Grid.cellSize - 1, Grid.cellSize - 1);
		g.draw(rect);
		g.drawImage(this.texture, x, y);
	}


	public void render(GameContainer gc, Graphics g, Color c) {
		g.setColor(c);

		int x = this.j * Grid.cellSize;
		int y = this.i * Grid.cellSize;
		Rectangle rect = new Rectangle(x + 1, y + 1, Grid.cellSize - 1, Grid.cellSize - 1);
		g.fill(rect);
	}
	
	
	@Override
	public String toString() {
		return "(" + this.i + ", " + this.j + ")";
		
	}
	
	
}
