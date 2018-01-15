package core;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import selectScreen.SelectCursor;
import selectScreen.SelectGrid;

public class SelectCharaScreen extends BasicGameState {

	private StateBasedGame sbg;
	private GameContainer gc;
	
	public SelectCharaScreen (int state) {
	}
	
	public static SelectGrid choiceGrid;
	
	//selectCursor cursor;
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.sbg = sbg;
		this.gc = gc;
		
		//initialisation grille

		SelectCharaScreen.choiceGrid = new SelectGrid(6, 10);//CHANGEMENT DE LA TAILLE DE LA GRILLE
		SelectCharaScreen.choiceGrid.init(gc);
		
		System.out.println("SelectCell size : " + SelectGrid.cellSizeX + "  " + SelectGrid.cellSizeY);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		SelectCharaScreen.choiceGrid.render(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		return Main.selectCharaScreen;
	}

}
