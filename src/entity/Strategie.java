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
	
	public ArrayList<Cell> findPath(Cell start, Cell end) {
		
		ArrayList<Cell> openSet = new ArrayList<>();
		ArrayList<Cell> closedSet = new ArrayList<>();
		ArrayList<Cell> path = new ArrayList<>();
		
		for (int i = 0; i < Main.gameGrid.getRows(); i++) {
			for (int j = 0; j < Main.gameGrid.getCols(); j++) {
				Grid.grid[i][j].addNeighbors(Main.gameGrid);
			}
		}
		
		openSet.add(start);
		
		while (openSet.size() > 0) {
			// keep going
			int winner = 0;
			for (int i = 0; i < openSet.size(); i++) {
				if (openSet.get(i).getPathFScore() < openSet.get(winner).getPathFScore()) {
					winner = i;
				}
			}
			Cell current = openSet.get(winner);
			
			if (current == end) {
				// Find the path
				Cell temp = current;
				path.add(temp);
				while (temp.getPrevious() != null) {
					path.add(temp.getPrevious());
					temp = temp.getPrevious();
				}
				System.out.println("DONE !");
				return path;
			}
			
			openSet.remove(current);
			closedSet.add(current);
			
			ArrayList<Cell> neighbors = current.getNeighbors();
			for (Cell neighbor : neighbors) {
				if (!closedSet.contains(neighbor) && neighbor.getCellType() != 1 && neighbor.getCellType() != 2
						&& neighbor.getChara() == null) {
					double tempgScore = current.getPathGScore() + 1;
					
					boolean newPath = false;
					if (openSet.contains(neighbor)) {
						if (tempgScore < neighbor.getPathGScore()) {
							neighbor.setPathGScore(tempgScore);
							newPath = true;
						}
					} else {
						neighbor.setPathGScore(tempgScore);
						newPath = true;
						openSet.add(neighbor);
					}
					
					if (newPath) {
						neighbor.setPathHScore(this.heuristic(neighbor, end));
						neighbor.setPathFScore(neighbor.getPathGScore() + neighbor.getPathHScore());
						neighbor.setPrevious(current);
					}
				}
			}
		}
		if (openSet.size() == 0) {
			System.out.println("No solution");
		}
		
		return null;
	}
	
	private double heuristic(Cell a, Cell b) {
		// double dist = Math.sqrt(Math.pow(a.getI() - b.getI(), 2) + Math.pow(a.getJ() - b.getJ(), 2));
		double dist = Math.abs(a.getI() - b.getI()) + Math.abs(a.getJ() - b.getJ());
		return dist;
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
