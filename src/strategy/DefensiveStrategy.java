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
		isPlaying = true;
		
		this.enemies = new ArrayList<>();
		this.bonuses = new ArrayList<>();
		this.rangeOfActionEnemies = new ArrayList<>();
		this.rangeOfActionBonuses = new ArrayList<>();
		this.enemiesInRange = new ArrayList<>();
		this.bonusesInRange = new ArrayList<>();
		
		
		int PM = this.chara.getPM();
		int Att= this.chara.getAtt();
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
		if (this.chara.getBonus() == 2) {
			Att += 3;
		}
		
		if (this.chara.getBonus() == 1) {
			PM += 3;
		}
		tempPM = PM;
		
		
		// Pour le personnage considéré,
		// déterminer sa zone d'action
		// + chercher tous les adversaires
		// chercher les bonus
		this.analyseMap(PM);


		// trier les cases par niveau de défense (nombre d’allié adjacent, nombre de mur adjacent, eau adjacente, nombre
		// d’ennemi pouvant nous attaquer, bonus sur la case)
		this.defensSort(this.rangeOfActionBonuses);
		// crée celldef
		
		celldef = this.rangeOfActionBonuses.get(0);
		i = 0;
		while ((celldef.getCellType() == 1 || celldef.getCellType() == 2
				|| celldef.getChara() != this.chara && celldef.hasChara()) && i < 100) {
			this.rangeOfActionBonuses.add(this.rangeOfActionBonuses.get(0));
			this.rangeOfActionBonuses.remove(0);
			celldef = this.rangeOfActionBonuses.get(0);
			i++;
		}
		
		// si il n'y a pas d'ennemi dans la zone, avancé vers le plus proche
		System.out.println(1);
		if (this.enemiesInRange.isEmpty()) {
			tempPM=PM;
			Cell current=chara.getPos();
			if(chara.getBonus()==0 && !bonusesInRange.isEmpty()) {
				Cell closestBonus=getClosest(chara.getPos(), bonusesInRange);
				pathsave1=evaluatePath(chara.getPos(), closestBonus);
				if(pathsave1!=null) {		
					int min=Math.min(pathsave1.size()-1, tempPM);
					current=pathsave1.get(pathsave1.size()-1-min);
					tempPM-=pathsave1.size()-1;
					this.applyPath(pathsave1, PM);
				}	
			}
			Cell closestEnemy = this.getClosest(current, this.enemies);
			if(this.evaluatePath(current, closestEnemy)!=null){
				this.applyPath(this.evaluatePath(current, closestEnemy), tempPM);
			}	
		}
		
		// sinon

		else {
			// si on peut achever un ennemi, aller chercher un bonus et atteindre la meilleur case
			System.out.println(2);
			if(chara.getBonus()==0) {
				i = 0;
				while (!play && i <= this.enemiesInRange.size() - 1) {
					rangenemy = this.enemiesInRange.get(i);
					tempPM = PM;
					if (rangenemy.getChara().isKillable(Att)) {
						pathsave1 = this.evaluatePathAttack(this.chara.getPos(), rangenemy);
						if(pathsave1!=null && attackable(pathsave1.get(0), rangenemy) ) {
							tempPM -= pathsave1.size() - 1;
							j = 0;
							while (!play && j <= this.bonusesInRange.size() - 1) {
								rangebonus = this.bonusesInRange.get(j);
								pathsave2 = this.evaluatePath(pathsave1.get(0), rangebonus);
								pathsave3 = this.evaluatePath(rangebonus, celldef);

								if ( pathsave2!=null && pathsave3!=null && pathsave2.size() - 1 <= tempPM && pathsave3.size() - 1 <= tempPM - pathsave2.size() + 1) {
									play = true;
									cellsave = rangenemy;

								}
								j++;
							}
						}
					}
					i++;
				}
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
				System.out.println(3);
				if(chara.getBonus()==0) {
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
									if (pathsave2!=null && tempPM >= pathsave2.size() - 1 && attackable(pathsave2.get(0), rangenemy)) {
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
				}

				// alors aller chercher le bonus, attaquer et atteindre la case
				if (play) {
					this.applyPath(pathsave1, PM);
					this.applyPath(pathsave2, PM);
					this.attack(cellsave);
					this.applyPath(pathsave3, PM);
				}
				
				// sinon si on peut aller chercher un bonus et aller à la meilleur case
				else {
					System.out.println(4);
					if(chara.getBonus()==0) {
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
					}

					// alors chercher un bonus et aller à la meilleur case
					if (play) {
						this.applyPath(pathsave1, PM);
						this.applyPath(pathsave2, PM);
					}
					
					// sinon si on peut achever un ennemi et aller à la meilleur case
					else {
						System.out.println(5);
						i = 0;
						while (!play && i <= this.enemiesInRange.size() - 1) {
							rangenemy = this.enemiesInRange.get(i); // for(Cell rangenemy: enemiesInRange) {
							if (rangenemy.getChara().isKillable(Att)) {
								tempPM = PM;
								pathsave1 = this.evaluatePathAttack(this.chara.getPos(), rangenemy);
									if (pathsave1!=null && pathsave1.size() - 1 <= tempPM && attackable(pathsave1.get(0), rangenemy)) {
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
						// alors achever un ennemi et aller à la meilleur case
						if (play) {
							this.applyPath(pathsave1, PM);
							this.attack(cellsave);
							this.applyPath(pathsave2, PM);
						}
						
						// sinon si on peut attaquer un ennemi et aller à la meilleur case
						else {
							System.out.println(6);
							i = 0;
							while (!play && i <= this.enemiesInRange.size() - 1) {
								rangenemy = this.enemiesInRange.get(i);
								tempPM = PM;
								pathsave1 = this.evaluatePathAttack(this.chara.getPos(), rangenemy);
									if (pathsave1!=null && pathsave1.size() - 1 <= tempPM && attackable(pathsave1.get(0), rangenemy)) {
										tempPM -= pathsave1.size() - 1;
										pathsave2 = this.evaluatePath(pathsave1.get(0), celldef);
										if ((pathsave2!=null) && tempPM >= pathsave2.size() - 1) {
											play = true;
											cellsave = rangenemy;
							
										}
									}
								i++;
							}
							
							// alors attaquer un ennemi et aller à la meilleur case
							if (play) {
								this.applyPath(pathsave1, PM);
								this.attack(cellsave);
								this.applyPath(pathsave2, PM);
							}
							
							// sinon Attaquer et aller à la meilleur case atteignable
							
							else {
								System.out.println(7);
								tempPM = PM;
								cellsave = this.getClosest(this.chara.getPos(), this.enemiesInRange);
								pathsave1 = this.evaluatePathAttack(this.chara.getPos(), cellsave);
								Cell current=chara.getPos();
								int min=Math.min(pathsave1.size()-1, tempPM);
								if(pathsave1!=null) {
									this.applyPath(pathsave1, tempPM);
									current=pathsave1.get(pathsave1.size() - 1 - min);
									
									if(attackable(current, cellsave)) {
										attack(cellsave);
									}
									tempPM -= pathsave1.size() - 1;
									play=true;
								}
								if(play) {		
									i = 0;
									play=false;
									while (!play && i < 100) {
										
										if (current.distanceFrom(celldef) <= tempPM && celldef.getCellType() != 1 && celldef.getCellType() != 2
												&& (celldef.getChara() == this.chara || !celldef.hasChara())) {
											pathsave2 = this.evaluatePath(current, celldef);
											if (pathsave2 !=null && pathsave2.size() - 1 <= tempPM ) {
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
								else {
									Cell closestEnemy = this.getClosest(this.chara.getPos(), this.enemies);
									if(this.evaluatePath(current, closestEnemy)!=null){
										this.applyPath(this.evaluatePath(current, closestEnemy), tempPM);
									}	
								}
							}
						}
					}

				}
			}
		}
	}
}
