package flyingKappa;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;


public class Main extends BasicGame {

	public static int width = 1000;
	public static int height = 1000;
	private static boolean fullscreen = false;
	
	// public static Kappa[] kappas = new Kappa[15];
	Kappa k;

	public Main(String title) {
		super(title);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new Main("test"));
			app.setDisplayMode(Main.width, Main.height, Main.fullscreen);
			app.setTitle("Test");
			app.setForceExit(true);
			app.setTargetFrameRate(60);
			app.setShowFPS(false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_LEFT:
			this.k.setX(this.k.getX() - 5);
			this.k.setVelY(0);
			this.k.texture = this.k.texture.getFlippedCopy(true, false);
			break;
		case Input.KEY_RIGHT:
			this.k.setVelX(5);
			this.k.setVelY(0);
			this.k.texture = this.k.texture.getFlippedCopy(true, false);
			break;
		case Input.KEY_UP:
			this.k.setVelX(0);
			this.k.setVelY(-5);
			break;
		case Input.KEY_DOWN:
			this.k.setVelX(0);
			this.k.setVelY(5);
			break;
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
//		for (Kappa k : Main.kappas) {
//			k.render(gc, g);
//		}
		this.k.render(gc, g);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
//		for (int i = 0; i < Main.kappas.length; i++) {
//			float InitX = (float) Math.random() * (Main.width - 100);
//			float InitY = (float) Math.random() * (Main.height - 100);
//			float InitVelX = (float) Math.random() * 5 - 1;
//			float InitVelY = (float) Math.random() * 5 - 1;
//			Main.kappas[i] = new Kappa(InitX, InitY, InitVelX, InitVelY);
//		}
		
		
		this.k = new Kappa(500, 500, 0, 0);
		Music runningin = new Music("res/runningin.ogg");
		runningin.loop();
		runningin.setVolume((float) 0.1);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
//		for (Kappa k : Main.kappas) {
//			k.update(gc, delta);
//		}
		this.k.update(gc, delta);
	}

}
