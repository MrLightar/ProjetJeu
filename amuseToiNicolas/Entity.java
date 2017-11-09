package flyingKappa;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;


public abstract class Entity {
	
	protected float x;
	protected float y;
	protected float velX;
	protected float velY;
	protected float accX = (float) 0.1;
	protected float accY = (float) 0.1;
	protected boolean moving = false;
	
	
	public Entity(float x, float y, float velX, float velY) {
		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;


	}
	
	public boolean isMoving() {
		return this.moving;
	}


	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	
	public abstract void update(GameContainer gc, int delta);
	
	public abstract void render(GameContainer gc, Graphics g);
	
	public float getX() {
		return this.x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getVelX() {
		return this.velX;
	}
	
	public void setVelX(float velX) {
		this.velX = velX;
	}
	
	public float getVelY() {
		return this.velY;
	}
	
	public void setVelY(float velY) {
		this.velY = velY;
	}

	public float getAccX() {
		return this.accX;
	}

	public void setAccX(float accX) {
		this.accX = accX;
	}

	public float getAccY() {
		return this.accY;
	}

	public void setAccY(float accY) {
		this.accY = accY;
	}
	
}
