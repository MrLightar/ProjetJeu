package entity;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import map.Cell;
import map.Grid;


public class Character extends Entity {

	private Image texture;
	private int PA;
	private int PM;


	public Character(Cell pos, int PA, int PM) {
		super(pos);
		pos.setChara(this);
		this.PA = PA;
		this.PM = PM;
		try {
			this.texture = new Image("res/mage.png");
			this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}


	public int getPA() {
		return this.PA;
	}
	
	public void setPA(int PA) {
		this.PA = PA;
	}
	
	public int getPM() {
		return this.PM;
	}
	
	public void setPM(int PM) {
		this.PM = PM;
	}
	
	
	public void render(GameContainer gc, Graphics g) {
		int x = this.pos.getI() * Grid.cellSize;
		int y = this.pos.getJ() * Grid.cellSize;
		g.drawImage(this.texture, x, y);
	}

	public void moveCharacter(Cell pos) {
		this.setPosFromCell(pos);
	}


	@Override
	public void updateCharaGrid(Cell posInit, Cell posFin) {
		posInit.setChara(null);
		posFin.setChara(this);
	}

}

