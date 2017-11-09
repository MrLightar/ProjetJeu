package map;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;


public class Cell {

	private int i;
	private int j;


	public Cell(int i, int j) {
		this.i = i;
		this.j = j;
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
	
	
	public void render(GameContainer gc, Graphics g) {
		int x = this.i * Grid.cellSize;
		int y = this.j * Grid.cellSize;
		Rectangle rect = new Rectangle(x, y, Grid.cellSize, Grid.cellSize);
		g.draw(rect);
	}

}
