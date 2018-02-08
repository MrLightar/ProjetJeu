package core;


import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import entity.Character;
import entity.Cursor;
import map.Grid;
import map.Cell;


public class Main extends StateBasedGame {

//	public static final int width = 960;
//	public static final int height = 540;
	public static final int width = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int height = width*9/16;
//	public static final int height = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private static final boolean fullscreen = true;
	private static final String gameName = "Projet Poutinator";

	public static final int menu = 0;
	public static final int play = 1;
	public static final int selectCharaScreen = 2;

	
	public Main(String name) {
		super(name);
		this.addState(new Menu(Main.menu));
		this.addState(new Play(Main.play));
		this.addState(new SelectCharaScreen(Main.selectCharaScreen));
	}
	
	
	
	public static void main(String[] args) {
		try {
			
			AppGameContainer app = new AppGameContainer(new Main(Main.gameName));
			app.setDisplayMode(Main.width, Main.height, Main.fullscreen);
			app.setTitle("Grid");
			app.setForceExit(true);
			app.setTargetFrameRate(500);
			app.setShowFPS(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(Main.menu).init(gc, this);
		this.getState(Main.play).init(gc, this);
		this.getState(Main.selectCharaScreen).init(gc, this);
		this.enterState(Main.menu);
	}


}
