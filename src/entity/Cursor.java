package entity;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import map.Cell;
import map.Grid;


public class Cursor extends Entity {
	
	private Image texture;
	private boolean hasCharacter;
	public Character selection;
	
	
	public Cursor(Cell pos) {
		super(pos);
		try {
			this.texture = new Image("res/cursor.png");
			this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.hasCharacter = false;
		this.selection = null;
	}
	
	
	public Image getTexture() {
		return this.texture;
	}

	public void setTexture(Image texture) {
		this.texture = texture;
	}
	
	
	public void testSelectMove() {
		if (this.hasCharacter == false) {
			if (this.verifSelectChara()) {
				this.selection = this.pos.getChara();
				// affichage info, choix (combat/d�placement)
				System.out.println("Personnage selectionné !");
				this.hasCharacter = true;
			}
		} else {
			// appel m�thode move
			if (this.getPos().distanceFrom(this.selection.getPos()) <= this.selection.getPM()) {
				if (!verifSelectChara()) {
					this.selection.moveCharacter(this.getPos());
					this.selection = null;
					this.hasCharacter = false;
					System.out.println("Perso déplacé !");
				} else {
					System.out.println("Case occupée !");
				}
				
			} else {
				System.out.println("Pas assez de PM !");
			}
		}
	}
	
	
	public void testSelectAttack() {
		if (this.hasCharacter == false) {
			if (this.verifSelectChara()) {
				this.selection = this.pos.getChara();
				// affichage info, choix (combat/d�placement)
				System.out.println("Personnage selectionné !");
				this.hasCharacter = true;
			}
		} else {
			if (this.getPos().distanceFrom(this.selection.getPos()) <= this.selection.getPO()) {
				if (verifSelectChara()) {
					this.selection.attack(this.getPos());
					this.selection = null;
					this.hasCharacter = false;
				} else {
					System.out.println("Case Vide, Attaque Impossible");
				}
			}
		}
	
	}

	

	@Override
	public void updateCharaGrid(Cell posInit, Cell posFin) {
	}


	public void render(GameContainer gc, Graphics g) {
		int x = this.pos.getJ() * Grid.cellSize + Grid.cellSize / 2;
		int y = this.pos.getI() * Grid.cellSize + Grid.cellSize / 2;
		g.drawImage(this.texture, x, y);
	}
	
}
