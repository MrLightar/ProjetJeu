package selectScreen;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
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
	
	private Image background;
	private Image image_pv;
	private Image image_att;
	private Image image_po;
	private Image image_pm;
	//COUT DE l'UNITE
	Integer testStat = new Integer(20);
	Integer testCout = new Integer(200);
	
	

	public SelectCell(int i, int j) throws SlickException {
		this.i = i;
		this.j = j;
		this.chara = null;
		this.chara = new Mage(new Cell(1, 1), 1, 1, 15, 6, 5, 4);//TEST
		
		background = new Image("/res/wood_wall.png");
		image_pv = new Image("res/pv.png");
		image_att = new Image("res/att.png");
		image_po = new Image("res/po.png");
		image_pm = new Image("res/pm.png");
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
	
	
	public void render(GameContainer gc, Graphics g) {
		int x = this.j * SelectGrid.cellSizeX;
		int y = this.i * SelectGrid.cellSizeY;
		
		g.drawImage(this.background.getScaledCopy(SelectGrid.cellSizeX, SelectGrid.cellSizeY), x, y);
		Rectangle rect = new Rectangle(x + 1, y + 1, SelectGrid.cellSizeX - 1, SelectGrid.cellSizeY - 1);
		g.draw(rect);
		//g.drawImage(this.chara.getTexture().getScaledCopy(SelectGrid.cellSizeY/2, SelectGrid.cellSizeY/2), x, y);
		g.drawImage(image_pv.getScaledCopy(SelectGrid.cellSizeX/8, SelectGrid.cellSizeX/8), x + SelectGrid.cellSizeX/8*1, y + SelectGrid.cellSizeY/6*3);
		g.drawImage(image_att.getScaledCopy(SelectGrid.cellSizeX/8, SelectGrid.cellSizeX/8), x + SelectGrid.cellSizeX/8*4, y + SelectGrid.cellSizeY/6*3);
		g.drawImage(image_po.getScaledCopy(SelectGrid.cellSizeX/8, SelectGrid.cellSizeX/8), x + SelectGrid.cellSizeX/8*1, y + SelectGrid.cellSizeY/6*4);
		g.drawImage(image_pm.getScaledCopy(SelectGrid.cellSizeX/8, SelectGrid.cellSizeX/8), x + SelectGrid.cellSizeX/8*4, y + SelectGrid.cellSizeY/6*4);
		
		
		SelectGrid.ttf1.drawString(x + SelectGrid.cellSizeX/8*2, y + SelectGrid.cellSizeY/6*3, new Integer(this.chara.getPv_max()).toString());//testStat.toString()
		SelectGrid.ttf1.drawString(x + SelectGrid.cellSizeX/8*5, y + SelectGrid.cellSizeY/6*3, new Integer(this.chara.getAtt()).toString());//testStat.toString()
		SelectGrid.ttf1.drawString(x + SelectGrid.cellSizeX/8*2, y + SelectGrid.cellSizeY/6*4, new Integer(this.chara.getPO()).toString());//testStat.toString()
		SelectGrid.ttf1.drawString(x + SelectGrid.cellSizeX/8*5, y + SelectGrid.cellSizeY/6*4, new Integer(this.chara.getPM()).toString());//testStat.toString()
		
		SelectGrid.ttf2.drawString(x + SelectGrid.cellSizeX/8*3, y + SelectGrid.cellSizeY/6*5, testCout.toString());
	}

}
