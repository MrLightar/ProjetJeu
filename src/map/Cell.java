package map;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import entity.Character;


public class Cell {
	
	private int i;
	private int j;
	private Character chara;
	
	
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

	public entity.Character getChara() {
		return this.chara;
	}

	public void setChara(entity.Character character) {
		this.chara = character;
	}

	public int distanceFrom(Cell other) {
		return Math.abs(other.i - this.i) + Math.abs(other.j - this.j);
	}
	
	
	public void render(GameContainer gc, Graphics g) {
		int x = this.i * Grid.cellSize;
		int y = this.j * Grid.cellSize;
		Rectangle rect = new Rectangle(x, y, Grid.cellSize, Grid.cellSize);
		g.draw(rect);
	}

}
