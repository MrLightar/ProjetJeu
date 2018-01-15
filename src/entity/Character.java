package entity;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import map.Cell;
import map.Grid;


public class Character extends Entity {
	
	private Image texture;
	
	private int pv_max;
	private int pv;
	private int att;
	private int PO;
	private int PM;
	private int bonus;
	private boolean alive;


	public Character(Cell pos, int pv_max, int pv, int att, int PO, int PM) {
		super(pos);
		pos.setChara(this);
		this.pv_max = pv_max;
		this.pv = pv;
		this.att = att;
		this.PO = PO;
		this.PM = PM;
		this.bonus = 0;
		this.alive = true;
		try {
			this.texture = new Image("res/mage.png");
			this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}


	public int getPv_max() {
		return this.pv_max;
	}

	public void setPv_max(int pv_max) {
		this.pv_max = pv_max;
	}

	public int getPv() {
		return this.pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getAtt() {
		return this.att;
	}

	public void setAtt(int att) {
		this.att = att;
	}

	public int getPO() {
		return this.PO;
	}

	public void setPO(int pO) {
		this.PO = pO;
	}
	
	public int getPM() {
		return this.PM;
	}

	public void setPM(int PM) {
		this.PM = PM;
	}
	
	public int getBonus() {
		return this.bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}


	public void render(GameContainer gc, Graphics g) {
		int x = this.pos.getJ() * Grid.cellSize;
		int y = this.pos.getI() * Grid.cellSize;
		if (this.alive) {
			g.drawImage(this.texture, x, y);
		}
	}
	
	
	public void moveCharacter(Cell pos) {
		this.setPosFromCell(pos);
	}
	
	public void attack(Cell pos) {
		// if Port�e
		// if ! deja attack
		if (this.verifSelectChara()) {
			System.out.println("Atatatataa");
			pos.getChara().dommage(this.att + 3 * (this.bonus == 3 ? 1 : 0));
		}
		
	}
	
	public void dommage(int dmg) {
		this.pv -= dmg;
		if (this.pv <= 0) {
			this.pos.setChara(null);
			this.alive = false;
			System.out.println("La cible est morte !");
		} else {
			System.out.println("PV Cible : " + this.pv + " / " + this.pv_max);
			
		}
	}

	public boolean isKillable(int dmg) {
		if (this.pv <= dmg) {
			return true;
		} else {
			return false;
		}
	}
	
	
	@Override
	public void updateCharaGrid(Cell posInit, Cell posFin) {
		posInit.setChara(null);
		posFin.setChara(this);
	}

}
