package core;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import selectScreen.SelectCursor;
import selectScreen.SelectGrid;

public class SelectCharaScreen extends BasicGameState {

	private StateBasedGame sbg;
	private GameContainer gc;
	
	public static SelectGrid choiceGrid;
	
	private static SelectCursor cursor;
	
	

	public SelectCharaScreen (int state) {
	}
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.sbg = sbg;
		this.gc = gc;
		
		//initialisation grille

		SelectCharaScreen.choiceGrid = new SelectGrid(6, 10);//CHANGEMENT DE LA TAILLE DE LA GRILLE
		SelectCharaScreen.choiceGrid.init(gc);
		
		//creation curseur
		SelectCharaScreen.cursor = new selectScreen.SelectCursor(SelectCharaScreen.choiceGrid.getCell(0, 0));
		
		System.out.println("SelectCell size : " + SelectGrid.cellSizeX + "  " + SelectGrid.cellSizeY);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		SelectCharaScreen.choiceGrid.render(gc, g);
		SelectCharaScreen.cursor.render(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		return Main.selectCharaScreen;
	}
	
	
	@Override
	public void keyPressed(int key, char c) {
		int currentI = SelectCharaScreen.cursor.getPos().getI();
		int currentJ = SelectCharaScreen.cursor.getPos().getJ();

		switch (key) {
		case Input.KEY_LEFT:
			if (currentJ > 0) {
				SelectCharaScreen.cursor.setPosFromIndex(currentI, currentJ - 1);
			}
			break;
		
		case Input.KEY_RIGHT:
			if (currentJ < SelectCharaScreen.choiceGrid.getCols() - 1) {
				SelectCharaScreen.cursor.setPosFromIndex(currentI, currentJ + 1);
			}
			break;
		
		case Input.KEY_UP:
			if (currentI > 0) {
				SelectCharaScreen.cursor.setPosFromIndex(currentI - 1, currentJ);
			}
			break;
		
		case Input.KEY_DOWN:
			if (currentI < SelectCharaScreen.choiceGrid.getRows() - 1) {
				SelectCharaScreen.cursor.setPosFromIndex(currentI + 1, currentJ);
			}
			break;

			case Input.KEY_ENTER:
					if(cursor.getPos().hasChara() && !cursor.getPos().isPlaced()) {
//						cursor.getPos().setPlaced(true);
						Play.getCursor().setSelection(cursor.getPos().getChara());
						sbg.enterState(Main.play);
					}
				break;

			case Input.KEY_ESCAPE:
					sbg.enterState(Main.play);
				
				break;

		}
		
	}

}
