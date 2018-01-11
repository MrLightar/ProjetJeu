package entity;


import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import map.Cell;
import map.Grid;


public abstract class Character extends Entity {
	
	protected Image texture;
	protected Image textureAttack;

	protected Animation[] animations = new Animation[10];
	protected Animation[] attackAnimations = new Animation[3];
	
	protected int job;
	protected int level;
	protected int pv_max;
	protected int pv;
	protected int att;
	protected int PO;
	protected int PM;
	protected int bonus;
	protected int action;
	protected int x;
	protected int y;
	protected int endMoveX;
	protected int endMoveY;
	protected boolean alive;
	protected boolean moving;
	protected boolean underAttack;
	protected boolean animationChange;

	protected int animationCount;
	

	public Character(Cell pos, int job, int lvl, int pv_max, int att, int PO, int PM) {
		super(pos);
		pos.setChara(this);
		this.job = job;
		this.level = lvl;
		this.pv_max = pv_max;
		this.pv = pv_max;
		this.att = att;
		this.PO = PO;
		this.PM = PM;
		this.bonus = 0;
		this.action = 6;
		this.alive = true;
		this.moving = false;
		this.underAttack = false;
		
		this.animationCount = 0;
		
		this.x = this.pos.getJ() * Grid.cellSize;
		this.y = this.pos.getI() * Grid.cellSize;
		
	}
		
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPv_max() {
		return pv_max;
	}

	public void setPv_max(int pv_max) {
		this.pv_max = pv_max;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getAtt() {
		return att;
	}

	public void setAtt(int att) {
		this.att = att;
	}

	public int getPO() {
		return PO;
	}

	public void setPO(int pO) {
		PO = pO;
	}
	
	public int getPM() {
		return this.PM;
	}

	public void setPM(int PM) {
		this.PM = PM;
	}
	
	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
		
	
	public void moveCharacter(Cell pos) {
		this.setPosFromCell(pos);
		this.x = this.pos.getJ() * Grid.cellSize;
		this.y = this.pos.getI() * Grid.cellSize;
		
	}	
	
	public void moveUp() {
		if(this.moving == false) {
			int rows = this.pos.getI();
			int cols = this.pos.getJ();
			this.pos.setChara(null);
			this.action = 4;
			this.endMoveY = (rows - 1) * Grid.cellSize; 
			this.moving = true;		
			this.setPosFromIndex(rows - 1, cols);
			this.pos.setChara(this);
		}		
	}	

	public void moveLeft() {
		if(this.moving == false) {
			int rows = this.pos.getI();
			int cols = this.pos.getJ();
			this.pos.setChara(null);
			this.action = 5;
			this.endMoveX = (cols - 1) * Grid.cellSize; 
			this.moving = true;
			this.setPosFromIndex(rows, cols - 1);
			this.pos.setChara(this);
		}		
	}
	
	public void moveDown() {
		if(this.moving == false) {
			int rows = this.pos.getI();
			int cols = this.pos.getJ();
			this.pos.setChara(null);
			this.action = 6;
			this.endMoveY = (rows + 1) * Grid.cellSize;	
			this.moving = true;	
			this.setPosFromIndex(rows+1, cols);
			this.pos.setChara(this);
		}			
	}	
	
	public void moveRight() {
		if(this.moving == false) {
			int rows = this.pos.getI();
			int cols = this.pos.getJ();
			this.pos.setChara(null);
			this.action = 7;
			this.endMoveX = (cols + 1) * Grid.cellSize;
			this.moving = true;
			this.setPosFromIndex(rows, cols + 1);
			this.pos.setChara(this);
		}
	}
	
	
	public void attack(Cell pos) {
		//if Port�e
		//if ! deja attack
		if(verifSelectChara()) {
			System.out.println("Atatatataa");
			
			
			if(this.getPos().getJ() > pos.getJ()) {
				action = 1;
			} 
			if(this.getPos().getJ() < pos.getJ()) {
				action = 3;
			}
			if(this.getPos().getI() > pos.getI()) {
				action = 0 ;
			}
			if(this.getPos().getI() < pos.getI()) {
				action = 2;
			}
			this.endMoveX = pos.getI() * Grid.cellSize;
			this.endMoveY = pos.getJ() * Grid.cellSize;
			
			
			pos.getChara().dommage(att + (3*((bonus==3)?1:0)));
		}
		
	}
	
	
	public void dommage(int dmg) {
		this.pv -= dmg;
		this.underAttack = true;
		if(pv <= 0) {
			pos.setChara(null);
			this.alive = false;			
			System.out.println("La cible est morte !");
			action = 8 ;
		} else {
			System.out.println("PV Cible : " + this.pv + " / " + this.pv_max);
			
		}
	}
	
	
	abstract public void init(GameContainer gc) throws SlickException;

	
	
	public Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y, int animSpeed) {
		Animation animation = new Animation();
		for( int x = startX; x < endX; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), animSpeed);
		}
		
		return animation;
	}
	

	abstract public void render(GameContainer gc, Graphics g);

	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		//System.out.println(this.x + " et " + this.y);
		
		if(this.moving == true) {
			switch (this.action) {
				case 4: 
					this.y -= 3;
					if ( this.y <= this.endMoveY) {
		        		this.moving = false;
		        		this.x = this.pos.getJ() * Grid.cellSize;
		        		this.y = this.pos.getI() * Grid.cellSize;
		        	}
					
					break;
		        case 5: 
		        	this.x -= 3;
		        	if ( this.x <= this.endMoveX) {
		        		this.moving = false;
		        		this.x = this.pos.getJ() * Grid.cellSize;
		        		this.y = this.pos.getI() * Grid.cellSize;
		        	}
					break;
		        case 6: 
		        	this.y += 3;
		        	if ( this.y >= this.endMoveY) {
		        		this.moving = false;
		        		this.x = this.pos.getJ() * Grid.cellSize;
		        		this.y = this.pos.getI() * Grid.cellSize;
		        	}
		        	break;
		        case 7: 
		        	this.x += 3;
		        	if ( this.x >= this.endMoveX) {
		        		this.moving = false;
		        		this.x = this.pos.getJ() * Grid.cellSize;
		        		this.y = this.pos.getI() * Grid.cellSize;
		        	}
					break;
		        
			}
		}
	}
	
		
	
	@Override
	public void updateCharaGrid(Cell posInit, Cell posFin) {
		posInit.setChara(null);
		posFin.setChara(this);
	}
	
}

