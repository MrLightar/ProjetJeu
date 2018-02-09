package entity;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import core.Play;
import core.SelectCharaScreen;
import map.Cell;
import map.Grid;


public class Cursor extends Entity {
	
	private Image texture;
	private Character selection;
	private boolean selected;
	
	
	public Cursor(Cell pos) {
		super(pos);
		try {
			this.texture = new Image("res/cursor2.png");
			this.texture = this.texture.getScaledCopy(Grid.cellSize, Grid.cellSize);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.selection = null;
	}
	
	
	public Image getTexture() {
		return this.texture;
	}

	public void setTexture(Image texture) {
		this.texture = texture;
	}
	
	public Character getSelection() {
		return selection;
	}
	
	public void setSelection(Character selection) {
		this.selection = selection;
	}
	
	public boolean hasCharacter() {
		return selection != null;
	}
	
	public boolean onCharacter() {
		return verifPosChara();
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isPlacable() {
		if (Play.getCurrentPrice() + this.selection.getPrice() <= Play.getMaxPrice()) {
			if(this.pos.getJ() <= 3) {
				if(this.pos.getChara()==null) {
					if(verifTypeCell()) {
						return true;
					} else {
						System.out.println("Case impraticable !");
					}	
				} else {
					System.out.println("Case occupee !");
				}
			} else {
				System.out.println("Hors de la zone de spawn");
			}
		} else {
			System.out.println("Limite de prix depassee !");
		}
		
		return false;
	}

	public void placeCharacter() {
		this.selection.moveCharacter(this.pos);
		SelectCharaScreen.choiceGrid.noplacable(selection);
		Play.getChara().add(selection);
		try {
			Play.getChara().get(Play.getChara().size()-1).init();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Play.updatePrice(this.selection.getPrice());
		this.selection = null;
		System.out.println("Perso place !");
	}
	
	public void selectMove() {
		if(this.pos.getJ() <= Play.gameGrid.getCols()/5) {
			if(!this.verifPosChara()) {
				if(verifTypeCell()) {
					this.selection.moveCharacter(this.pos);
					this.selection = null;
					this.selected = false;
					System.out.println("Perso deplace !");
				} else {
					System.out.println("Case impraticable !");
				}		
			} else {
				System.out.println("Case occupee !");
			}
		} else {
			System.out.println("Hors de la zone de spawn");
		}
	}
	
	public void select() {
		if (this.verifPosChara()) {
			this.selection = this.pos.getChara();
			this.selected = true;
			System.out.println("Personnage selectionne !");
		}
	}
	
	public void deselect() {
		this.selection = null;
		this.selected = false;
	}
	
	public void supr() {
		pos.setChara(null);
//		this.selection.action = 8;
		Play.getChara().remove(selection);
		SelectCharaScreen.choiceGrid.replacable(selection);
		Play.updatePrice(- this.selection.getPrice());
		this.selection = null;
		this.selected = false;
		System.out.println("Perso suprimme !");
	}
	
	
	
	
	
	
	public void testSelect() {
		if (this.verifPosChara()) {
			this.selection = this.pos.getChara();
			// affichage info, choix (combat/deplacement)
			System.out.println("Personnage selectionne !");
		}
	}
	
	
	public void testSelectMove() {
		if (!this.hasCharacter()) {
			testSelect();
		} else {
			// appel methode move
			if (this.getPos().distanceFrom(this.selection.getPos()) <= (this.selection.getPM() + (3*((selection.getBonus()==4)?1:0)))) {
				if (verifTypeCell()) {
					if (this.pos.getChara() != selection) {
						if (!verifPosChara()) {
							this.selection.moveCharacter(this.pos);
							int cellType = this.pos.getCellType();
							if (cellType > 2) {
								selection.setBonus(cellType);
//								try {
//									pos.setCellType(0);
//								} catch (SlickException e) {
//									e.printStackTrace();
//								}
								pos.setCellType(0);
							}
							this.selection = null;
							System.out.println("Perso deplace !");
						} else {
							System.out.println("Case occupee !");
						}
					} else {
						this.selection = null;
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
		if (!this.hasCharacter()) {
			System.out.println("Veullier selectionner un personnage avant");
		} else {
			if (this.getPos().distanceFrom(this.selection.getPos()) <= this.selection.getPO()) {
				if (this.pos.getChara() != selection) {
					if (verifPosChara()) {
						this.selection.attack(this.getPos());
						this.selection = null;
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
//		int x = this.pos.getJ() * Grid.cellSize + Grid.cellSize / 2;
//		int y = this.pos.getI() * Grid.cellSize + Grid.cellSize / 2;
		int x = this.pos.getJ() * Grid.cellSize;
		int y = this.pos.getI() * Grid.cellSize;
		if(this.hasCharacter()) {
			g.drawImage(this.texture, x, y, Color.red);
			if(selection.getTeam()==Character.ally) {
				g.drawImage(selection.getTextureSimple().getScaledCopy(Grid.cellSize, Grid.cellSize), x, y, Color.lightGray);
			}
		} else {
			g.drawImage(this.texture, x, y);
		}
		
	}
	
}
