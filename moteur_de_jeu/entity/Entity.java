package entity;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Entity {

	private map.Cell pos;
	private Image texture;
	
	
	public Entity(map.Cell pos) {
		this.pos = pos;
		try {
			this.texture = new Image("res/cursor.png");
			this.texture = this.texture.getScaledCopy(map.Grid.cellSize,
					map.Grid.cellSize);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}


	public map.Cell getPos() {
		return this.pos;
	}

	public void setPosFromCell(map.Cell pos) {
		this.pos = pos;
	}

	public void setPosFromIndex(int i, int j) {
		this.pos.setI(i);
		this.pos.setJ(j);
	}

	public Image getTexture() {
		return this.texture;
	}
	
	public void setTexture(Image texture) {
		this.texture = texture;
	}
	
	
	public void render(GameContainer gc, Graphics g) {
		int x = this.pos.getI() * map.Grid.cellSize + map.Grid.cellSize / 2;
		int y = this.pos.getJ() * map.Grid.cellSize + map.Grid.cellSize / 2;
		g.drawImage(this.texture, x, y);
	}
	
}
