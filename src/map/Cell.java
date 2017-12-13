package map;


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
	
	
	

	public Cell(int i, int j) {
		this.i = i;
		this.j = j;
		this.chara = null;
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
	
	public Character getChara() {
		return this.chara;
	}

	public void setChara(Character character) {
		this.chara = character;
	}
	

	public int getCellType() {
		return cellType;
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


	public int distanceFrom(Cell other) {
		return Math.abs(other.i - this.i) + Math.abs(other.j - this.j);
	}
	
	
	public void render(GameContainer gc, Graphics g) {
		int x = this.j * Grid.cellSize;
		int y = this.i * Grid.cellSize;
		Rectangle rect = new Rectangle(x + 1, y + 1, Grid.cellSize - 1, Grid.cellSize - 1);
		g.draw(rect);
		g.drawImage(this.texture, x, y);
	}


	@Override
	public String toString() {
		return "(" + this.i + ", " + this.j + ")";

	}

}
