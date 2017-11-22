package map;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


public class Main extends BasicGame {
	
	public static int width = 600;
	public static int height = 600;
	private static boolean fullscreen = false;
	
	Grid grid;
	entity.Entity curseur;
	
	public Main(String title) {
		super(title);
	}
	
	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new Main("Grid"));
			app.setDisplayMode(Main.width, Main.height, Main.fullscreen);
			app.setTitle("Grid");
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
		int currentI = this.curseur.getPos().getI();
		int currentJ = this.curseur.getPos().getJ();

		switch (key) {
			case Input.KEY_LEFT:
				if (currentI - 1 >= 0) {
					this.curseur.setPosFromIndex(currentI - 1, currentJ);
				}
				break;

			case Input.KEY_RIGHT:
				if (currentI + 1 < this.grid.getCols()) {
					this.curseur.setPosFromIndex(currentI + 1, currentJ);
				}
				break;

			case Input.KEY_UP:
				if (currentJ - 1 >= 0) {
					this.curseur.setPosFromIndex(currentI, currentJ - 1);
				}
				break;

			case Input.KEY_DOWN:
				if (currentJ + 1 < this.grid.getRows()) {
					this.curseur.setPosFromIndex(currentI, currentJ + 1);
				}
				break;
		}
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		this.grid.render(gc, g);
		this.curseur.render(gc, g);
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		this.grid = new Grid(12, 12);
		this.grid.init(gc);
		System.out.println("Cell size : " + Grid.cellSize);
		
		this.curseur = new entity.Entity(this.grid.getCell(5, 5));
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {

	}
	
}
