package selectScreen;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import core.SelectCharaScreen;

public class SelectCursor {
	
	private SelectCell pos;
	private Image texture;
	
	public SelectCursor(SelectCell pos) {
		this.pos = pos;
		try {
			this.texture = new Image("res/cursor2.png");
			this.texture = this.texture.getScaledCopy(SelectGrid.cellSizeX, SelectGrid.cellSizeY);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	
	public SelectCell getPos() {
		return pos;
	}

	public void setPos(SelectCell pos) {
		this.pos = pos;
	}
	
	
	public void setPosFromIndex(int rows, int cols) {
		this.pos = SelectCharaScreen.choiceGrid.getCell(rows, cols);
	}
	



	public void render(GameContainer gc, Graphics g) {
		int x = this.pos.getJ() * SelectGrid.cellSizeX;
		int y = this.pos.getI() * SelectGrid.cellSizeY;
		g.drawImage(this.texture, x, y);
		
	}
	
}
