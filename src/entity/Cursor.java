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
	
	
	public void testSelect() {
		if (this.verifSelectChara()) {
			this.selection = this.pos.getChara();
			// affichage info, choix (combat/deplacement)
			System.out.println("Personnage selectionne !");
			this.hasCharacter = true;
			try {
				this.texture = new Image("res/cursor_selected.png");
				this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void testSelectMove() {
		if (this.hasCharacter == false) {
			testSelect();
		} else {
			// appel methode move
			if (this.getPos().distanceFrom(this.selection.getPos()) <= (this.selection.getPM() + (3*((selection.getBonus()==4)?1:0)))) {
				if (verifTypeCell()) {
					if (this.pos.getChara() != selection) {
						if (!verifSelectChara()) {
							this.selection.moveCharacter(this.pos);
							int cellType = this.pos.getCellType();
							if (cellType > 2) {
								selection.setBonus(cellType);
								try {
									pos.setCellType(0);
								} catch (SlickException e) {
									e.printStackTrace();
								}
							}
							this.selection = null;
							this.hasCharacter = false;
							try {
								this.texture = new Image("res/cursor.png");
								this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
							} catch (SlickException e) {
								e.printStackTrace();
							}
							System.out.println("Perso deplace !");
						} else {
							System.out.println("Case occupee !");
						}
					} else {
						this.hasCharacter = false;
						this.selection = null;
						try {
							this.texture = new Image("res/cursor.png");
							this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
						} catch (SlickException e) {
							e.printStackTrace();
						}
						System.out.println("Perso deselectionne");
					}	
				} else {
					System.out.println("Case non praticable !");
				}				
			} else {
				System.out.println("Pas assez de PM !");
			}
		}
	}

	//return si la cell est utilisable
	public boolean verifTypeCell() {
		return (this.pos.getCellType()!=1 && this.pos.getCellType()!=2); 
	}
	
	
	public void testSelectAttack() {
		if (this.hasCharacter == false) {
			System.out.println("Veullier selectionner un personnage avant");
		} else {
			if (this.getPos().distanceFrom(this.selection.getPos()) <= this.selection.getPO()) {
				if (this.pos.getChara() != selection) {
					if (verifSelectChara()) {
						this.selection.attack(this.getPos());
						this.selection = null;
						this.hasCharacter = false;
						try {
							this.texture = new Image("res/cursor.png");
							this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
						} catch (SlickException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("Case Vide, Attaque Impossible");
					}
				} else {
					System.out.println("Impossible de t'attaquer toi meme !!!");
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
