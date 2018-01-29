package strategie;


import java.util.ArrayList;

import core.*;
import map.*;
import entity.Character;


public abstract class Strategie {

	protected Character chara;

	protected ArrayList<Cell> enemies;
	protected ArrayList<Cell> bonuses;

	protected ArrayList<Cell> rangeOfActionEnemies;
	protected ArrayList<Cell> rangeOfActionBonuses;
	protected ArrayList<Cell> enemiesInRange;
	protected ArrayList<Cell> bonusesInRange;


	public Strategie(Character chara) {
		this.chara = chara;
	}
	
	/* ========================================================================================== */
	
	public abstract void gameTurn();
	
	/* ========================================================================================== */

	public void analyseMap() {
		
		for (int i = 0; i < Play.gameGrid.getRows(); i++) {
			for (int j = 0; j < Play.gameGrid.getCols(); j++) {
				Cell current = Play.gameGrid.getCell(i, j);
				if (current.hasChara() /* && current.getChara.team */) {
					this.enemies.add(current);
				} else if (current.getCellType() == 3 || current.getCellType() == 4) {
					this.bonuses.add(current);
				}
			}
		}
	}
	
	/* ========================================================================================== */
	
	public ArrayList<Cell> evaluatePath(Cell start, Cell end) {
		ArrayList<Cell> openSet = new ArrayList<>();
		ArrayList<Cell> closedSet = new ArrayList<>();
		ArrayList<Cell> path = new ArrayList<>();
		
		if (start == end || end.hasChara() && start.distanceFrom(end) == 1) {
			System.out.println("Path Finding : Already at destination\n");
			path.add(start);
			return path;
		}
		
		for (int i = 0; i < Play.gameGrid.getRows(); i++) {
			for (int j = 0; j < Play.gameGrid.getCols(); j++) {
				Grid.grid[i][j].addNeighbors(Play.gameGrid);
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
				if (end.hasChara()) {
					temp = temp.getPrevious();
				}
				path.add(temp);
				while (temp.getPrevious() != null) {
					path.add(temp.getPrevious());
					temp = temp.getPrevious();
				}
				
				// Reinitialisation of cells
				for (int i = 0; i < Play.gameGrid.getRows(); i++) {
					for (int j = 0; j < Play.gameGrid.getCols(); j++) {
						Play.gameGrid.getCell(i, j).setPrevious(null);
					}
				}
				
				System.out.println("Path Finding : DONE !\n");
				return path;
			}
			
			openSet.remove(current);
			closedSet.add(current);
			
			ArrayList<Cell> neighbors = current.getNeighbors();
			for (Cell neighbor : neighbors) {
				if (!closedSet.contains(neighbor) && neighbor.getCellType() != 1 && neighbor.getCellType() != 2
						&& !neighbor.hasChara()) {
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
			if (neighbors.contains(end)) {
				openSet.add(end);
				end.setPrevious(current);
			}
		}
		if (openSet.size() == 0) {
			System.out.println("Path Finding : No solution\n");
		}
		
		return null;
	}

	private double heuristic(Cell a, Cell b) {
		// double dist = Math.sqrt(Math.pow(a.getI() - b.getI(), 2) + Math.pow(a.getJ() - b.getJ(), 2));
		double dist = Math.abs(a.getI() - b.getI()) + Math.abs(a.getJ() - b.getJ());
		return dist;
	}

	/* ========================================================================================== */
	
	public ArrayList<Cell> evaluateSightView(Cell start, Cell end) {
		int x, y, xbas, xhaut, ybas, yhaut;
		int dx, dy;
		int dp, deltaE, deltaNE;
		
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
				path.add(Play.gameGrid.getCell(y, x));
				while (x < xhaut || y > yhaut) {
					if (dp <= 0) {
						dp = dp + deltaE;
						x++;
					} else {
						dp = dp + deltaNE;
						x++;
						y--;
					}
					path.add(Play.gameGrid.getCell(y, x));
				}
			} else {
				dp = 2 * dx - dy;
				deltaE = 2 * dx;
				deltaNE = 2 * (dx - dy);
				x = xbas;
				y = ybas;

				path.add(Play.gameGrid.getCell(y, x));
				while (x < xhaut || y > yhaut) {
					if (dp <= 0) {
						dp = dp + deltaE;
						y--;
					} else {
						dp = dp + deltaNE;
						x++;
						y--;
					}
					path.add(Play.gameGrid.getCell(y, x));
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

				path.add(Play.gameGrid.getCell(y, x));
				while (x < xbas || y < ybas) {
					if (dp <= 0) {
						dp = dp + deltaE;
						x++;
					} else {
						dp = dp + deltaNE;
						x++;
						y++;
					}
					path.add(Play.gameGrid.getCell(y, x));
				}
			} else {
				dp = 2 * dx - dy;
				deltaE = 2 * dx;
				deltaNE = 2 * (dx - dy);
				x = xhaut;
				y = yhaut;

				path.add(Play.gameGrid.getCell(y, x));
				while (x < xbas || y < ybas) {
					if (dp <= 0) {
						dp = dp + deltaE;
						y++;
					} else {
						dp = dp + deltaNE;
						x++;
						y++;
					}
					path.add(Play.gameGrid.getCell(y, x));
				}
			}
		}
		
		if (start.hasChara()) {
			path.remove(start);
		}
		if (end.hasChara()) {
			path.remove(end);
		}
		
		return path;
	}
	
	public boolean isInSightView(ArrayList<Cell> sightView) {
		for (Cell cell : sightView) {
			if (cell.getCellType() == 1 || cell.hasChara()) {
				return false;
			}
		}
		return true;
	}
	
	/* ========================================================================================== */
	
	public ArrayList<Cell> evaluateRangeOfAction(Cell pos, int dist) {
		ArrayList<Cell> rangeOfAction = new ArrayList<>();
		for (int i = -dist; i <= dist; i++) {
			for (int j = -dist; j <= dist; j++) {
				if (!(i == 0 && j == 0)) {
					if (Math.abs(i) + Math.abs(j) <= dist) {
						int neighborI = pos.getI() + i;
						int neighborJ = pos.getJ() + j;
						if (Play.gameGrid.isInGrid(neighborI, neighborJ)) {
							rangeOfAction.add(Play.gameGrid.getCell(neighborI, neighborJ));
						}
					}
				}
			}
		}
		return rangeOfAction;
	}
	
	public ArrayList<Cell> getCharactersInRange(ArrayList<Cell> range) {
		ArrayList<Cell> characters = new ArrayList<>();
		for (Cell cell : range) {
			if (cell.hasChara()) {
				characters.add(cell);
			}
		}
		return characters;
	}
	
	
	/* ====== TO DO WHEN TEAMS ARE IMPLEMENTED ====== */
	
	public ArrayList<Cell> getEnemiesInRange(ArrayList<Cell> range) {
		ArrayList<Cell> characters = new ArrayList<>();
		for (Cell cell : range) {
			// GESTION DES TEAMS A AJOUTER
			if (cell.hasChara() /* && cell.getChara().team */) {
				characters.add(cell);
			}
		}
		return characters;
	}

	/* =============================================== */

	public ArrayList<Cell> getBonusInRange(ArrayList<Cell> range) {
		ArrayList<Cell> bonus = new ArrayList<>();
		for (Cell cell : range) {
			if (cell.getCellType() == 3 || cell.getCellType() == 4) {
				bonus.add(cell);
			}
		}
		return bonus;
	}

	public Cell getClosest(Cell pos, ArrayList<Cell> collection) {
		Cell closest = null;
		int minDist = Play.gameGrid.getRows() + Play.gameGrid.getCols();
		for (Cell cell : collection) {
			if (pos.distanceFrom(cell) < minDist) {
				minDist = pos.distanceFrom(cell);
				closest = cell;
			}
		}
		return closest;
	}
	/* =============================================== */
	
	public void applyPath(ArrayList<Cell> path) {
		for(int i=0; i< path.size()-1;i++) {
			
			//si la cellule suivante est vers le bas
			if(path.get(i).getI() < path.get(i+1).getI()) {
				chara.moveDown();
			}
			
			//si la cellule suivante est vers le haut
			if(path.get(i).getI() > path.get(i+1).getI()) {
				chara.moveUp();
			}
			
			//si la cellule suivante est vers la droite
			if(path.get(i).getJ() < path.get(i+1).getJ()) {
				chara.moveRight();
			}
			
			//si la cellule suivante est vers la gauche
			if(path.get(i).getJ() > path.get(i+1).getJ()) {
				chara.moveLeft();
			}
		}
		
	}
	
}
