package entity;


import core.Main;
import map.Cell;


public abstract class Entity {
	
	protected Cell pos;


	public Entity(Cell pos) {
		this.pos = pos;
	}
	
	
	public Cell getPos() {
		return this.pos;
	}
	
	public void setPosFromCell(Cell pos) {
		this.updateCharaGrid(this.pos, pos);
		this.pos = pos;
		
	}
	
	public void setPosFromIndex(int rows, int cols) {
		this.updateCharaGrid(this.pos, Main.gameGrid.getCell(rows, cols));
		this.pos = Main.gameGrid.getCell(rows, cols);
	}

	abstract public void updateCharaGrid(Cell posInit, Cell posFin);
	
}
