package selectScreen;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import core.SelectCharaScreen;
import entity.Character;
import entity.Mage;
import map.Cell;

import java.util.*;


public class SelectCell {
	
	private int i;
	private int j;
	private Character chara;
	
	private boolean placed;
	
	private Image background;
	private Image image_pv;
	private Image image_att;
	private Image image_po;
	private Image image_pm;
	
	//Prix DE l'UNITE
	Integer testPrice = new Integer(200);
		

	public SelectCell(int i, int j) throws SlickException {
		this.i = i;
		this.j = j;
		this.chara = null;
		
		placed = false;
		
		background = new Image("res/wood_wall.png");
		background = background.getScaledCopy(SelectGrid.cellSizeX, SelectGrid.cellSizeY);
		image_pv = new Image("res/pv.png");
		image_pv = image_pv.getScaledCopy(SelectGrid.cellSizeX/8, SelectGrid.cellSizeX/8);
		image_att = new Image("res/att.png");
		image_att = image_att.getScaledCopy(SelectGrid.cellSizeX/8, SelectGrid.cellSizeX/8);
		image_po = new Image("res/po.png");
		image_po = image_po.getScaledCopy(SelectGrid.cellSizeX/8, SelectGrid.cellSizeX/8);
		image_pm = new Image("res/pm.png");
		image_pm = image_pm.getScaledCopy(SelectGrid.cellSizeX/8, SelectGrid.cellSizeX/8);
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
	
	public Character getChara() {
		return this.chara;
	}

	public void setChara(Character character) {
		this.chara = character;
	}
	
	public boolean hasChara() {
		return (chara != null);
	}
	
	public boolean isPlaced() {
		return placed;
	}

	public void setPlaced(boolean placed) {
		this.placed = placed;
	}

	
	
	public void render(GameContainer gc, Graphics g) {
		int x = this.j * SelectGrid.cellSizeX;
		int y = this.i * SelectGrid.cellSizeY;
		
		if (!placed) {
			g.drawImage(this.background, x, y);
		}
		else {
			g.drawImage(this.background, x, y, Color.gray);
		}
		Rectangle rect = new Rectangle(x + 1, y + 1, SelectGrid.cellSizeX - 1, SelectGrid.cellSizeY - 1);
		g.draw(rect);
		
		if (this.hasChara()) {
			if (!placed) {
				g.drawImage(this.chara.getTextureSimple().getScaledCopy(SelectGrid.cellSizeY/2, SelectGrid.cellSizeY/2), x + SelectGrid.cellSizeY/4, y);
				g.drawImage(image_pv, x + SelectGrid.cellSizeX/8*1, y + SelectGrid.cellSizeY/6*3);
				g.drawImage(image_att, x + SelectGrid.cellSizeX/8*4, y + SelectGrid.cellSizeY/6*3);
				g.drawImage(image_po, x + SelectGrid.cellSizeX/8*1, y + SelectGrid.cellSizeY/6*4);
				g.drawImage(image_pm, x + SelectGrid.cellSizeX/8*4, y + SelectGrid.cellSizeY/6*4);
			
				SelectGrid.ttf1.drawString(x + SelectGrid.cellSizeX/8*2, y + SelectGrid.cellSizeY/6*3, new Integer(this.chara.getPv_max()).toString());
				SelectGrid.ttf1.drawString(x + SelectGrid.cellSizeX/8*5, y + SelectGrid.cellSizeY/6*3, new Integer(this.chara.getAtt()).toString());
				SelectGrid.ttf1.drawString(x + SelectGrid.cellSizeX/8*2, y + SelectGrid.cellSizeY/6*4, new Integer(this.chara.getPO()).toString());
				SelectGrid.ttf1.drawString(x + SelectGrid.cellSizeX/8*5, y + SelectGrid.cellSizeY/6*4, new Integer(this.chara.getPM()).toString());
				SelectGrid.ttf2.drawString(x + SelectGrid.cellSizeX/8*5, y + SelectGrid.cellSizeY/6*5, new Integer(this.chara.getPrice()).toString() + "$");
				SelectGrid.ttf2.drawString(x + SelectGrid.cellSizeX/8*1, y + SelectGrid.cellSizeY/6*5, "Lvl." + new Integer(this.chara.getLevel()).toString());
			}
			else {
				g.drawImage(this.chara.getTextureSimple().getScaledCopy(SelectGrid.cellSizeY/2, SelectGrid.cellSizeY/2), x + SelectGrid.cellSizeY/4, y, Color.gray);
				g.drawImage(image_pv, x + SelectGrid.cellSizeX/8*1, y + SelectGrid.cellSizeY/6*3, Color.gray);
				g.drawImage(image_att, x + SelectGrid.cellSizeX/8*4, y + SelectGrid.cellSizeY/6*3, Color.gray);
				g.drawImage(image_po, x + SelectGrid.cellSizeX/8*1, y + SelectGrid.cellSizeY/6*4, Color.gray);
				g.drawImage(image_pm, x + SelectGrid.cellSizeX/8*4, y + SelectGrid.cellSizeY/6*4, Color.gray);
			
				SelectGrid.ttf1.drawString(x + SelectGrid.cellSizeX/8*2, y + SelectGrid.cellSizeY/6*3, new Integer(this.chara.getPv_max()).toString(), Color.gray);
				SelectGrid.ttf1.drawString(x + SelectGrid.cellSizeX/8*5, y + SelectGrid.cellSizeY/6*3, new Integer(this.chara.getAtt()).toString(), Color.gray);
				SelectGrid.ttf1.drawString(x + SelectGrid.cellSizeX/8*2, y + SelectGrid.cellSizeY/6*4, new Integer(this.chara.getPO()).toString(), Color.gray);
				SelectGrid.ttf1.drawString(x + SelectGrid.cellSizeX/8*5, y + SelectGrid.cellSizeY/6*4, new Integer(this.chara.getPM()).toString(), Color.gray);
				SelectGrid.ttf2.drawString(x + SelectGrid.cellSizeX/8*5, y + SelectGrid.cellSizeY/6*5, new Integer(this.chara.getPrice()).toString() + "$", Color.gray);
				SelectGrid.ttf2.drawString(x + SelectGrid.cellSizeX/8*1, y + SelectGrid.cellSizeY/6*5, "Lvl." + new Integer(this.chara.getLevel()).toString(), Color.gray);
			}
			
		}
		
	}

}
