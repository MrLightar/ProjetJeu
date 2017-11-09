package flyingKappa;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Kappa extends Entity {
	
	Image texture;
	
	public Kappa(float x, float y, float velX, float velY) {
		super(x, y, velX, velY);
		try {
			this.texture = new Image("res/kappa.jpg");
			this.texture = this.texture.getScaledCopy(50, 50);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(GameContainer gc, int delta) {
		this.x += this.velX;
		this.y += this.velY;
		this.bounce();
//		for (Kappa kappa : Main.kappas) {
//			if (kappa != this) {
//				this.collision(kappa);
//			}
//		}
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) {
		g.drawImage(this.texture, this.x, this.y);
	}

	public void react(int cas) {
		if (cas == 0) {
			this.velX *= -1;
			if (this.velX > 0) {
				this.velX += this.accX;
			} else {
				this.velX -= this.accX;
			}
		} else {
			this.velY *= -1;
			if (this.velY > 0) {
				this.velY += this.accY;
			} else {
				this.velY -= this.accY;
			}
		}
	}

	public void bounce() {
		if (this.x < 0 || this.x > Main.width - this.texture.getWidth()) {
			this.react(0);
		}
		if (this.y < 0 || this.y > Main.height - this.texture.getHeight()) {
			this.react(1);
		}
	}
	
	public void collision(Kappa other) {
		// Collision horizontale
		if (this.x > other.x && this.x < other.x + other.texture.getWidth()
				&& (this.y + this.texture.getHeight() > other.y
						&& this.y + this.texture.getHeight() < other.y
								+ other.texture.getHeight()
						|| this.y > other.y
								&& this.y < other.y + other.texture.getHeight())) {
			this.react(0);
			other.react(0);
		}
		// Collision verticale
		if (this.y > other.y && this.y < other.y + other.texture.getHeight()
				&& (this.x + this.texture.getWidth() > other.x
						&& this.x + this.texture.getWidth() < other.x
								+ other.texture.getWidth()
						|| this.x > other.x
								&& this.x < other.x + other.texture.getWidth())) {
			this.react(1);
			other.react(1);
		}
	}
	
	public void move() {
		
	}
	
}
