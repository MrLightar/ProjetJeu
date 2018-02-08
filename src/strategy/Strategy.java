package strategy;


import java.util.ArrayList;

import core.*;
import map.*;
import entity.Character;


public abstract class Strategy {
	
	protected Character chara;
	
	public ArrayList<Cell> enemies;
	protected ArrayList<Cell> bonuses;
	
	protected ArrayList<Cell> rangeOfActionEnemies;
	protected ArrayList<Cell> rangeOfActionBonuses;
	protected ArrayList<Cell> enemiesInRange;
	protected ArrayList<Cell> bonusesInRange;

	public ArrayList<Integer> actions;
	protected Cell target;
	
	protected static boolean isPlaying;
	
	
	public Strategy(Character chara) {
		this.chara = chara;
		this.enemies = new ArrayList<>();
		this.bonuses = new ArrayList<>();
		this.rangeOfActionEnemies = new ArrayList<>();
		this.rangeOfActionBonuses = new ArrayList<>();
		this.enemiesInRange = new ArrayList<>();
		this.bonusesInRange = new ArrayList<>();
		
		this.actions = new ArrayList<>();
		this.target = null;
	}

	/* ========================================================================================== */

	public void update() {
		if (!this.actions.isEmpty()) {
			if (!this.chara.isMoving() && !this.chara.isAttacking()) {
				switch (this.actions.get(0)) {
				case 0:
					this.chara.moveUp();
					this.actions.remove(0);
					break;
				case 1:
					this.chara.moveDown();
					this.actions.remove(0);
					break;
				case 2:
					this.chara.moveRight();
					this.actions.remove(0);
					break;
				case 3:
					this.chara.moveLeft();
					this.actions.remove(0);
					break;
				case 4:
					this.chara.attack(this.target);
					this.actions.remove(0);
					break;
				}
				System.out.println("Action faite");
			} else {
				System.out.println("Pile tj remplie attente fin de mvt " + this.chara.isMoving() + "   |  " + this.chara.isAttacking());
			}
		}else {
			isPlaying = false;
		}
		System.out.println("FIN");
	}

	/* ========================================================================================== */

	public abstract void gameTurn();

	/* ========================================================================================== */
	
	public void analyseMap() {
		// Emplacement des ennemis et des bonus
		for (int i = 0; i < Play.gameGrid.getRows(); i++) {
			for (int j = 0; j < Play.gameGrid.getCols(); j++) {
				Cell current = Play.gameGrid.getCell(i, j);
				if (current.hasChara() && current.getChara().isAnEnemy(this.chara)) {
					this.enemies.add(current);
				} else if (current.getCellType() == 3 || current.getCellType() == 4) {
					this.bonuses.add(current);
				}
			}
		}

		// Determiner la zone d'action du personnage
		this.rangeOfActionEnemies = this.evaluateRangeOfAction(this.chara.getPos(), this.chara.getPM() + this.chara.getPO());
		this.rangeOfActionBonuses = this.evaluateRangeOfAction(this.chara.getPos(), this.chara.getPM());
		// Chercher les positions des adversaires et des bonus dans cette zone d'action
		this.enemiesInRange = this.getEnemiesInRange(this.rangeOfActionEnemies);
		this.bonusesInRange = this.getBonusInRange(this.rangeOfActionBonuses);
	}
	
	public void clear() {
		this.enemies.clear();
		this.bonuses.clear();
	}

	/* ========================================================================================== */

	public ArrayList<Cell> evaluatePath(Cell start, Cell end) {
		ArrayList<Cell> openSet = new ArrayList<>();
		ArrayList<Cell> closedSet = new ArrayList<>();
		ArrayList<Cell> path = new ArrayList<>();
		if (start == end || end.hasChara() && end.getChara() != this.chara && start.distanceFrom(end) == 1) {
//			System.out.println("Path Finding : Already at destination\n");
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
				if (end.hasChara() && end.getChara() != this.chara) {
					temp = temp.getPrevious();
				}
				path.add(temp);
				while (temp != start/* temp.getPrevious() != null */) {
					path.add(temp.getPrevious());
					temp = temp.getPrevious();
				}

				// Reinitialisation of cells
				for (int i = 0; i < Play.gameGrid.getRows(); i++) {
					for (int j = 0; j < Play.gameGrid.getCols(); j++) {
						Play.gameGrid.getCell(i, j).setPrevious(null);
					}
				}
				openSet.clear();
				closedSet.clear();
//				System.out.println("Path Finding : DONE !\n");
				return path;
			}

			openSet.remove(current);
			closedSet.add(current);


			ArrayList<Cell> neighbors = current.getNeighbors();
			for (Cell neighbor : neighbors) {
				if (!closedSet.contains(neighbor) && neighbor.getCellType() != 1 && neighbor.getCellType() != 2
						&& (!neighbor.hasChara() || neighbor.getChara() == this.chara)) {
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
//			System.out.println("Path Finding : No solution\n");
		}

		return null;
	}
	
	private double heuristic(Cell a, Cell b) {
		// double dist = Math.sqrt(Math.pow(a.getI() - b.getI(), 2) + Math.pow(a.getJ() - b.getJ(), 2));
		double dist = Math.abs(a.getI() - b.getI()) + Math.abs(a.getJ() - b.getJ());
		return dist;
	}

	public ArrayList<Cell> evaluatePathAttack(Cell start, Cell end) {
		ArrayList<Cell> res = this.evaluatePath(start, end);
		while (res.size() > 1 && this.isInSightView(this.evaluateSightView(res.get(1), end))
				&& res.get(1).distanceFrom(end) <= this.chara.getPO()) {
			res.remove(0);
		}
		return res;
	}

	public void attack(Cell pos) {
		this.target = pos;
		this.actions.add(4);
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

	public ArrayList<Cell> getEnemiesInRange(ArrayList<Cell> range) {
		ArrayList<Cell> characters = new ArrayList<>();
		for (Cell cell : range) {
			if (cell.hasChara() && cell.getChara().isAnEnemy(this.chara)) {
				characters.add(cell);
			}
		}
		return characters;
	}
	
	public ArrayList<Cell> getBonusInRange(ArrayList<Cell> range) {
		ArrayList<Cell> bonus = new ArrayList<>();
		for (Cell cell : range) {
			if (cell.getCellType() == 3 || cell.getCellType() == 4) {
				bonus.add(cell);
			}
		}
		return bonus;
	}

	/* =============================================== */
	
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

	public ArrayList<Cell> getPathToClosest(Cell pos, ArrayList<Cell> collection) {
		ArrayList<Cell> currentPath, bestPath = null;
		int minDist = Play.gameGrid.getRows() + Play.gameGrid.getCols();
		for (Cell cell : collection) {
			currentPath = this.evaluatePath(pos, cell);
			if (minDist > currentPath.size() - 1) {
				minDist = currentPath.size() - 1;
				bestPath = currentPath;
			}
		}
		return bestPath;
	}

	/* =============================================== */

	public void applyPath(ArrayList<Cell> path, int nummove) {
		int end = Math.min(nummove - 1, path.size() - 2);
		for (int i = path.size() - 2; i >= path.size() - 2 - end; i--) {
			// si la cellule suivante est vers le haut
			if (path.get(i).getI() < path.get(i + 1).getI()) {
				this.actions.add(0);
			}
			
			// si la cellule suivante est vers le bas
			if (path.get(i).getI() > path.get(i + 1).getI()) {
				this.actions.add(1);
			}
			
			// si la cellule suivante est vers la gauche
			if (path.get(i).getJ() < path.get(i + 1).getJ()) {
				this.actions.add(3);
			}
			
			// si la cellule suivante est vers la droite
			if (path.get(i).getJ() > path.get(i + 1).getJ()) {
				this.actions.add(2);
			}
		}
	}
	
	/* =============================================== */
	
	public int attackableBy(Cell charaPos) {
		int res = 0;
		for (Cell enemyPos : this.enemies) {
			if (charaPos.distanceFrom(enemyPos) < enemyPos.getChara().getPO() + enemyPos.getChara().getPM()) {
				res++;
			}
		}
		return res;
	}
	
	public int defensvalue(Cell cell) {
		int res = 0;
		ArrayList<Cell> neighbors = new ArrayList<>();

		if (cell.getCellType() != 1 && cell.getCellType() != 2 && (!cell.hasChara() || cell.getChara() == this.chara)) {
			neighbors = cell.getAllNeighbors(Play.gameGrid);
			for (Cell othercell : neighbors) {
				// si il y a un allie
				if (othercell.hasChara()) {
					if (!othercell.getChara().isAnEnemy(this.chara) && othercell.getChara() != this.chara) {
						res += 5;
					}
				}
				// si il y a un mur
				if (othercell.getCellType() == 1) {
					res += 2;
				}
				// si il y a de l'eau
				if (othercell.getCellType() == 2) {
					res += 1;
				}
			}

			if ((cell.getCellType() == 3 || cell.getCellType() == 4) && this.chara.getBonus() == 0) { // si il y a un bonus sur la
																										// case
				res += 3;
			}

			// si on est sur une case attaquable
			res -= this.attackableBy(cell);
		}


		return res;

	}
	
	public void defensSort(ArrayList<Cell> rangegrid) {
		// on cree le tableau de valeur defensive
		rangegrid.add(this.chara.getPos());
		int tab[] = new int[rangegrid.size()];
		boolean sorted = false;
		int save;

		for (int i = 0; i < rangegrid.size(); i++) {
			tab[i] = this.defensvalue(rangegrid.get(i));
		}


		// tri bulle
		int i = rangegrid.size() - 1;

		while (!sorted && i > 0) {
			sorted = true;
			for (int j = 0; j < i; j++) {
				if (tab[j + 1] > tab[j]) {
					save = tab[j];
					tab[j] = tab[j + 1];
					tab[j + 1] = save;
					sorted = false;

					rangegrid.add(j + 2, rangegrid.get(j));
					rangegrid.remove(rangegrid.get(j));

				}
				if (tab[j + 1] == tab[j]) {
					if (rangegrid.get(j + 1).distanceFrom(this.chara.getPos()) < rangegrid.get(j)
							.distanceFrom(this.chara.getPos())) {
						save = tab[j];
						tab[j] = tab[j + 1];
						tab[j + 1] = save;
						sorted = false;

						rangegrid.add(j + 2, rangegrid.get(j));
						rangegrid.remove(rangegrid.get(j));
					}
				}
			}
			i--;
		}
//		for (int k = 0; k < rangegrid.size(); k++) {
//			System.out.println("i= " + rangegrid.get(k).getI() + " j= " + rangegrid.get(k).getJ() + " value= " + tab[k]);
//		}
	}
	
	public static boolean isPlaying() {
		return isPlaying;
	}
	
}
