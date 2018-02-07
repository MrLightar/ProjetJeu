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

	private Image texture;
	private Image textureBonus;

	private double PathFScore;
	private double PathGScore;
	private double PathHScore;

	private ArrayList<Cell> neighbors;
	private Cell previous;

	public static final int grassCell = 0;
	public static final int wallCell = 1;
	public static final int waterCell = 2;
	public static final int moveBonusCell = 3;
	public static final int attackBonusCell = 4;


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
		return PathFScore;
	}


	public void setPathFScore(double pathFScore) {
		PathFScore = pathFScore;
	}


	public double getPathGScore() {
		return PathGScore;
	}


	public void setPathGScore(double pathGScore) {
		PathGScore = pathGScore;
	}


	public double getPathHScore() {
		return PathHScore;
	}


	public void setPathHScore(double pathHScore) {
		PathHScore = pathHScore;
	}


	public ArrayList<Cell> getNeighbors() {
		return neighbors;
	}


	public void setNeighbors(ArrayList<Cell> neighbors) {
		this.neighbors = neighbors;
	}


	public Cell getPrevious() {
		return previous;
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
		return cellType;
	}


	public void setCellType(int cellType) {
		this.cellType = cellType;
//		switch (this.cellType) {
//			case 0:
//				this.texture = new Image("res/grass.png");
//				this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
//				break;
//			case 1:
//				this.texture = new Image("res/wall.png");
//				this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
//				break;
//			case 2:
//				this.texture = new Image("res/water.png");
//				this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
//				break;
//			case 3:
//				this.texture = new Image("res/grass_bonus_attack.png");
//				this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
//				break;
//			case 4:
//				this.texture = new Image("res/grass_bonus_move.png");
//				this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
//				break;
//		}
	}


	public boolean hasChara() {
		if (this.getChara() != null) {
			return true;
		} else {
			return false;
		}
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

	public void init() throws SlickException {
		switch (this.cellType) {
		case (0):
			double temp = (Math.random());
			if(temp > 0.9) {
				this.texture = new Image("res/grass3.png");
				this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
			} else {
				if(temp < 0.9 && temp >0.7) {
					this.texture = new Image("res/grass2.png");
					this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
				} else {
					if(temp < 0.6 && temp >0.4) {
						this.texture = new Image("res/grass1.png");
						this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
					} else {
						this.texture = new Image("res/grass0.png");
						this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);

					}
				}
			}
//			this.texture = new Image("res/grass.png");
//			this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
			break;
		case 1:
			this.texture = new Image("res/wall.png");
			this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
			break;
		case 2:
			temp = (Math.random());
			if(temp > 0.9) {
				this.texture = new Image("res/water2.png");
				this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
			} else {
				if(temp < 0.9 && temp >0.5) {
					this.texture = new Image("res/water1.png");
					this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
				} else {
					this.texture = new Image("res/water0.png");
					this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
				}
			}
//			this.texture = new Image("res/water.png");
//			this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
			break;
		case 3:
			this.texture = new Image("res/grass1.png");
			this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
			this.textureBonus = new Image("res/moveBonus.png");
			this.textureBonus = this.textureBonus.getScaledCopy(Grid.cellSize, Grid.cellSize);
			break;
		case 4:
			this.texture = new Image("res/grass1.png");
			this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
			this.textureBonus = new Image("res/attackBonus.png");
			this.textureBonus = this.textureBonus.getScaledCopy(Grid.cellSize, Grid.cellSize);
			break;		
		}
	}

	public void render(GameContainer gc, Graphics g) {
		int x = this.j * Grid.cellSize;
		int y = this.i * Grid.cellSize;
		g.drawImage(this.texture, x, y);
		if(this.cellType == 3) {
			g.drawImage(this.textureBonus, x, y);
		}
		if(this.cellType == 4) {
			g.drawImage(this.textureBonus, x, y);
		}
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
