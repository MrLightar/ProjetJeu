package entity;


import java.util.ArrayList;

import map.*;
import core.*;


public class Strategie {

	protected Character chara;


	public Strategie(Character chara) {
		this.chara = chara;
	}
	
	
	public void gameTurn() {

	}
	
	
	public ArrayList<Cell> findSeightView(Cell start, Cell end) {
		int dx, dy, dp, deltaE, deltaNE, x, y, ybas, yhaut, xbas, xhaut;
		
		ArrayList<Cell> path = new ArrayList<>();

		if (start.getI() < end.getI()) {
			yhaut = start.getI();
			ybas = end.getI();
			xhaut = start.getJ();
			xbas = end.getJ();
		} else {
			ybas = start.getI();
			yhaut = end.getI();
			xbas = start.getJ();
			xhaut = end.getJ();
		}

		if (xhaut >= xbas) {
			dx = xhaut - xbas;
			dy = ybas - yhaut;
			if (dx >= dy) {
				dp = 2 * dy - dx;
				deltaE = 2 * dy;
				deltaNE = 2 * (dy - dx);
				x = xbas;
				y = ybas;
				path.add(Main.gameGrid.getCell(y, x));
				while (x < xhaut) {
					if (dp <= 0) {
						dp = dp + deltaE;
						x++;
					} else {
						dp = dp + deltaNE;
						x++;
						y--;
					}
					path.add(Main.gameGrid.getCell(y, x));
				}
			} else {
				dp = 2 * dx - dy;
				deltaE = 2 * dx;
				deltaNE = 2 * (dx - dy);
				x = xbas;
				y = ybas;

				path.add(Main.gameGrid.getCell(y, x));
				while (x < xhaut) {
					if (dp <= 0) {
						dp = dp + deltaE;
						y--;

					} else {
						dp = dp + deltaNE;
						x++;
						y--;
					}
					path.add(Main.gameGrid.getCell(y, x));
				}
			}
		} else {
			dx = xbas - xhaut;
			dy = ybas - yhaut;
			if (dx >= dy) {
				dp = 2 * dy - dx;
				deltaE = 2 * dy;
				deltaNE = 2 * (dy - dx);
				x = xhaut;
				y = yhaut;

				path.add(Main.gameGrid.getCell(y, x));
				while (x < xbas) {
					if (dp <= 0) {
						dp = dp + deltaE;
						x++;

					} else {
						dp = dp + deltaNE;
						x++;
						y++;
					}
					path.add(Main.gameGrid.getCell(y, x));
				}
			} else {
				dp = 2 * dx - dy;
				deltaE = 2 * dx;
				deltaNE = 2 * (dx - dy);
				x = xhaut;
				y = yhaut;

				path.add(Main.gameGrid.getCell(y, x));
				while (x < xbas) {
					if (dp <= 0) {
						dp = dp + deltaE;
						y++;

					} else {
						dp = dp + deltaNE;
						x++;
						y++;
					}
					path.add(Main.gameGrid.getCell(y, x));
				}
			}
		}
		return path;
	}
	
	public boolean isInSeightView(ArrayList<Cell> sightView) {
		for (Cell cell : sightView) {
			if (cell.getCellType() == 1) {
				return false;
			}
		}
		return true;
	}
}
