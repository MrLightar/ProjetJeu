package entity;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import core.Main;
import core.Play;
import map.Cell;


public abstract class Entity {
	
	protected Cell pos;


	public Entity(Cell pos) {
		this.pos = pos;
	}
	public Entity() {
		this.pos = null;
	}
	
	
	public Cell getPos() {
		return this.pos;
	}
	
	public void setPosFromCell(Cell pos) {
		this.updateCharaGrid(this.pos, pos);
		this.pos = pos;
		
	}
	
	public void setPosFromIndex(int rows, int cols) {
		this.updateCharaGrid(this.pos, Play.gameGrid.getCell(rows, cols));
		this.pos = Play.gameGrid.getCell(rows, cols);
	}
	
	public boolean verifPosChara() {
		return this.pos.getChara() != null;
	}

	abstract public void updateCharaGrid(Cell posInit, Cell posFin);


	public void update(GameContainer gc, int delta) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
}
