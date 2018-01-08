package entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import map.Cell;
import map.Grid;

public class Archer extends Character {

	public Archer(Cell pos, int job, int lvl, int pv_max, int att, int PO, int PM) {
		super(pos, job, lvl, pv_max, att, PO, PM);
		// TODO Auto-generated constructor stub
	}

	
	public void init(GameContainer gc) throws SlickException {
		
		//animation de mouvement et d'attaque du personnage
				this.texture = new Image("res/animationarcher.png");
				this.texture = this.texture.getScaledCopy(Grid.cellSize*12, Grid.cellSize*9);	
				SpriteSheet spriteSheet = new SpriteSheet(this.texture, Grid.cellSize, Grid.cellSize);
				
//				this.textureAttack = new Image("res/explosion.png");
//				this.textureAttack = this.textureAttack.getScaledCopy(Grid.cellSize*16, Grid.cellSize);
//				SpriteSheet sS2 = new SpriteSheet(this.textureAttack, Grid.cellSize, Grid.cellSize);

				
				this.animations[0] = loadAnimation(spriteSheet, 0, 12, 0, 150);
				this.animations[1] = loadAnimation(spriteSheet, 0, 12, 1, 150);
				this.animations[2] = loadAnimation(spriteSheet, 0, 12, 2, 150);
				this.animations[3] = loadAnimation(spriteSheet, 0, 12, 3, 150);
				this.animations[4] = loadAnimation(spriteSheet, 1, 9, 4, 150);
				this.animations[5] = loadAnimation(spriteSheet, 1, 9, 5, 150);
				this.animations[6] = loadAnimation(spriteSheet, 1, 9, 6, 150);
				this.animations[7] = loadAnimation(spriteSheet, 1, 9, 7, 150);
				this.animations[8] = loadAnimation(spriteSheet, 1, 7, 8, 150);
				//this.animations[9] = loadAnimation(sS2, 0, 16, 0, 50);
	}
	
	public void render(GameContainer gc, Graphics g) {
		//this.x = this.pos.getJ() * Grid.cellSize;
		//this.y = this.pos.getI() * Grid.cellSize;
		
		
					
			if(action >= 0 && action <= 3) {				
				animations[action].draw(this.x, this.y);
				if (animations[action].getFrame() == animations[action].getFrameCount()-1 ) {
					animations[action].setCurrentFrame(0);
					this.action = this.action + 4;
				}				
//				animations[9].draw(this.endMoveX, this.endMoveY);
//				animations[9].setLooping(false);
			} else {
				if(action == 8) {
					animations[8].draw(this.x, this.y);
					animations[8].setLooping(false);
				} else {
					animations[action].draw(this.x, this.y);
				}
				
			}			
	}
}
