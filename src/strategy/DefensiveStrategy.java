package strategy;


import java.util.ArrayList;

import entity.Character;
import map.Cell;


public class DefensiveStrategy extends Strategy {


	public DefensiveStrategy(Character chara) {
		super(chara);
	}

	@Override
	public void gameTurn() {
		this.enemies = new ArrayList<>();
		this.bonuses = new ArrayList<>();
		this.rangeOfActionEnemies = new ArrayList<>();
		this.rangeOfActionBonuses = new ArrayList<>();
		this.enemiesInRange = new ArrayList<>();
		this.bonusesInRange = new ArrayList<>();
		this.actions = new ArrayList<>();
		this.target = null;
		
		
		int PM = this.chara.getPM();
		Cell celldef;
		int tempPM;
		int i = 0;
		int j = 0;
		ArrayList<Cell> pathsave1 = new ArrayList<>();
		ArrayList<Cell> pathsave2 = new ArrayList<>();
		ArrayList<Cell> pathsave3 = new ArrayList<>();
		Cell cellsave = null;
		Cell rangenemy;
		Cell rangebonus;
		boolean play = false;
		
		// initialise PM
		if (this.chara.getBonus() == 1) {
			PM += 3;
		}
		tempPM = PM;
		
		
		// Pour le personnage consid�r�,
		// d�terminer sa zone d'action
		// + chercher tous les adversaires
		// chercher les bonus
		this.analyseMap();


		// trier les cases par niveau de d�fense (nombre d�alli� adjacent, nombre de mur adjacent, eau adjacente, nombre
		// d�ennemi pouvant nous attaquer, bonus sur la case)
		this.defensSort(this.rangeOfActionBonuses);
		// cr�e celldef
		
		celldef = this.rangeOfActionBonuses.get(0);
		i = 0;
		while ((celldef.getCellType() == 1 || celldef.getCellType() == 2
				|| celldef.getChara() != this.chara && celldef.hasChara()) && i < 100) {
			this.rangeOfActionBonuses.add(this.rangeOfActionBonuses.get(0));
			this.rangeOfActionBonuses.remove(0);
			celldef = this.rangeOfActionBonuses.get(0);
			i++;
		}
		
		// si il n'y a pas d'ennemi dans la zone, avanc� vers le plus proche
		if (this.enemiesInRange.isEmpty()) {
			this.enemies.remove(this.chara.getPos());
			Cell closestEnemy = this.getClosest(this.chara.getPos(), this.enemies);
			System.out.println(closestEnemy);
			this.applyPath(this.evaluatePath(this.chara.getPos(), closestEnemy), this.chara.getPM());
			
		}
		
		// sinon

		else {
			// si on peut achever un ennemi, aller chercher un bonus et atteindre la meilleur case
			i = 0;
			while (!play && i <= this.enemiesInRange.size() - 1) {
				rangenemy = this.enemiesInRange.get(i);
				tempPM = PM;
				if (rangenemy.getChara().isKillable(this.chara.getAtt())) {
					pathsave1 = this.evaluatePathAttack(this.chara.getPos(), rangenemy);
					tempPM -= pathsave1.size() - 1;
					j = 0;
					while (!play && j <= this.bonusesInRange.size() - 1) {
						rangebonus = this.bonusesInRange.get(j);
						pathsave2 = this.evaluatePath(pathsave1.get(0), rangebonus);
						pathsave3 = this.evaluatePath(rangebonus, celldef);

						if (pathsave1!=null && pathsave2!=null && pathsave3!=null && pathsave2.size() - 1 <= tempPM && pathsave3.size() - 1 <= tempPM - pathsave2.size() + 1) {
							play = true;
							cellsave = rangenemy;
						}
						j++;
					}
				}
				i++;
			}
			
			// alors achever, aller chercher le bonus et atteindre la case
			if (play) {
				this.applyPath(pathsave1, PM);
				this.attack(cellsave);
				this.applyPath(pathsave2, PM);
				this.applyPath(pathsave3, PM);
			}
			
			// sinon si on peut aller chercher un bonus,attaquer un ennemi, et atteindre la meilleur case
			else {
				j = 0;
				while (!play && j <= this.bonusesInRange.size() - 1) {
					rangebonus = this.bonusesInRange.get(j); // for(Cell rangebonus: bonusesInRange) {
					tempPM = PM;
					pathsave1 = this.evaluatePath(this.chara.getPos(), rangebonus);
					if (pathsave1!=null && pathsave1.size() - 1 <= tempPM) {
						tempPM -= pathsave1.size() - 1;
						i = 0;
						while (!play && i <= this.enemiesInRange.size() - 1) {
							rangenemy = this.enemiesInRange.get(i); // for(Cell rangenemy: enemiesInRange) {
							pathsave2 = this.evaluatePathAttack(rangebonus, rangenemy);
							if (pathsave2!=null && tempPM >= pathsave2.size() - 1) {
								tempPM -= pathsave2.size() - 1;
								pathsave3 = this.evaluatePath(pathsave2.get(0), celldef);
								if (pathsave3!=null && tempPM >= pathsave3.size() - 1) {
									play = true;
									cellsave = rangenemy;
								}
							}
							i++;
						}
					}
					j++;
				}
				// alors aller chercher le bonus, attaquer et atteindre la case
				if (play) {
					this.applyPath(pathsave1, PM);
					this.applyPath(pathsave2, PM);
					this.attack(cellsave);
					this.applyPath(pathsave3, PM);
				}
				
				// sinon si on peut aller chercher un bonus et aller � la meilleur case
				else {
					j = 0;
					while (!play && j <= this.bonusesInRange.size() - 1) {
						rangebonus = this.bonusesInRange.get(j); // for(Cell rangebonus: bonusesInRange) {
						tempPM = PM;
						pathsave1 = this.evaluatePath(this.chara.getPos(), rangebonus);
						if (pathsave1!=null && pathsave1.size() - 1 <= tempPM) {
							tempPM -= pathsave1.size() - 1;
							pathsave2 = this.evaluatePath(rangebonus, celldef);
							if (pathsave2!=null && tempPM >= pathsave2.size() - 1) {
								play = true;
							}
						}
						j++;
					}
					// alors chercher un bonus et aller � la meilleur case
					if (play) {
						this.applyPath(pathsave1, PM);
						this.applyPath(pathsave2, PM);
					}
					
					// sinon si on peut achever un ennemi et aller � la meilleur case
					else {
						i = 0;
						while (!play && i <= this.enemiesInRange.size() - 1) {
							rangenemy = this.enemiesInRange.get(i); // for(Cell rangenemy: enemiesInRange) {
							if (rangenemy.getChara().isKillable(this.chara.getAtt())) {
								tempPM = PM;
								pathsave1 = this.evaluatePathAttack(this.chara.getPos(), rangenemy);
								if (pathsave1!=null && pathsave1.size() - 1 <= tempPM) {
									tempPM -= pathsave1.size() - 1;
									pathsave2 = this.evaluatePath(pathsave1.get(0), celldef);
									if (pathsave2!=null && tempPM >= pathsave2.size() - 1) {
										play = true;
										cellsave = rangenemy;
									}
								}
							}
							i++;
						}
						// alors achever un ennemi et aller � la meilleur case
						if (play) {
							this.applyPath(pathsave1, PM);
							this.attack(cellsave);
							this.applyPath(pathsave2, PM);
						}
						
						// sinon si on peut attaquer un ennemi et aller � la meilleur case
						else {
							i = 0;
							while (!play && i <= this.enemiesInRange.size() - 1) {
								rangenemy = this.enemiesInRange.get(i); // for(Cell rangenemy: enemiesInRange) {
								tempPM = PM;
								pathsave1 = this.evaluatePathAttack(this.chara.getPos(), rangenemy);
								if (pathsave1!=null && pathsave1.size() - 1 <= tempPM) {
									tempPM -= pathsave1.size() - 1;
									pathsave2 = this.evaluatePath(pathsave1.get(0), celldef);
									if (pathsave2!=null && tempPM >= pathsave2.size() - 1) {
										play = true;
										cellsave = rangenemy;
									}
								}
								i++;
							}
							
							// alors attaquer un ennemi et aller � la meilleur case
							if (play) {
								this.applyPath(pathsave1, PM);
								this.attack(cellsave);
								this.applyPath(pathsave2, PM);
							}
							
							// sinon Attaquer et aller � la meilleur case atteignable
							else {
								tempPM = PM;
								cellsave = this.getClosest(this.chara.getPos(), this.enemiesInRange);
								pathsave1 = this.evaluatePathAttack(this.chara.getPos(), cellsave);
								tempPM -= pathsave1.size() - 1;
								this.applyPath(pathsave1, PM);
								this.attack(cellsave);
								i = 0;
								while (!play && i < 100) {
									if (this.chara.getPos().distanceFrom(celldef) <= tempPM) {
										pathsave2 = this.evaluatePath(this.chara.getPos(), celldef);
										if (pathsave2 !=null && pathsave2.size() - 1 <= tempPM) {
											System.out.println(pathsave2);
											this.applyPath(pathsave2, tempPM);
											play = true;
										} else {
											this.rangeOfActionBonuses.add(this.rangeOfActionBonuses.get(0));
											this.rangeOfActionBonuses.remove(0);
											celldef = this.rangeOfActionBonuses.get(0);
										}
										
									}
									
									else {
										this.rangeOfActionBonuses.add(this.rangeOfActionBonuses.get(0));
										this.rangeOfActionBonuses.remove(0);
										celldef = this.rangeOfActionBonuses.get(0);
									}
									i++;
								}
							}
						}
					}

				}
			}
		}
	}
}