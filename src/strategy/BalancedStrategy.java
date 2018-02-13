package strategy;


import java.util.ArrayList;

import entity.Character;
import map.Cell;


public class BalancedStrategy extends Strategy {

	public BalancedStrategy(Character chara) {
		super(chara);
	}
	
	
	@Override
	public void gameTurn() {
		Strategy.isPlaying = true;
//		System.out.println(this.chara);

		this.enemies = new ArrayList<>();
		this.bonuses = new ArrayList<>();
		this.rangeOfActionEnemies = new ArrayList<>();
		this.rangeOfActionBonuses = new ArrayList<>();
		this.enemiesInRange = new ArrayList<>();
		this.bonusesInRange = new ArrayList<>();


		int PM = this.chara.getPM();
		int Att = this.chara.getAtt();
		Cell celldef;
		Cell current = null;
		int tempPM;
		int i = 0;
		ArrayList<Cell> pathsave1 = new ArrayList<>();
		ArrayList<Cell> pathsave2 = new ArrayList<>();
		Cell cellsave = null;
		boolean play = false;

		// initialise PM
		if (this.chara.getBonus() == Character.attackBonus) {
			Att += 3;
		}

		if (this.chara.getBonus() == Character.moveBonus) {
			PM += 3;
		}
		tempPM = PM;


		// Pour le personnage considéré,
		// déterminer sa zone d'action
		// + chercher tous les adversaires
		// chercher les bonus
		this.analyseMap(PM);
		
		
		// si ennemi achevable
		int distance = 1000;
		for (Cell enemy : this.enemiesInRange) {
			if (enemy.getChara().isKillable(Att)) {
				pathsave1 = this.evaluatePathAttack(this.chara.getPos(), enemy);
				if (pathsave1 != null && pathsave1.size() < distance && this.attackable(pathsave1.get(0), enemy)) {
					cellsave = enemy;
					pathsave2 = pathsave1;
					distance = pathsave1.size();
					play = true;
				}
			}
		}

		// achever l’ennemi
		if (play) {
//			System.out.println(1);
//			System.out.println("chemin d'attaque =" + pathsave2);
			this.applyPath(pathsave2, tempPM);
//			System.out.println("on attaque" + cellsave);
			int min = Math.min(pathsave2.size() - 1, tempPM);
			if (this.attackable(pathsave2.get(pathsave2.size() - 1 - min), cellsave)) {
				this.attack(cellsave);
			}
			tempPM -= distance - 1;
			current = pathsave2.get(pathsave2.size() - 1 - min);
		}
		
		// sinon
		else {
			// si ennemi attaquable
			distance = 1000;
			for (Cell enemie : this.enemiesInRange) {
				pathsave1 = this.evaluatePathAttack(this.chara.getPos(), enemie);
				if (pathsave1 != null && pathsave1.size() < distance && this.attackable(pathsave1.get(0), enemie)) {
					cellsave = enemie;
					pathsave2 = pathsave1;
					distance = pathsave2.size();
					play = true;
				}
			}

			// attaquer l’ennemi
			if (play) {
//				System.out.println(2);
//				System.out.println("chemin d'attaque =" + pathsave2);
				this.applyPath(pathsave2, tempPM);
//				System.out.println("on attaque" + cellsave);
				int min = Math.min(pathsave2.size() - 1, tempPM);
				if (this.attackable(pathsave2.get(pathsave2.size() - 1 - min), cellsave)) {
					this.attack(cellsave);
				}
				tempPM -= distance - 1;
				current = pathsave2.get(pathsave2.size() - 1 - min);
			}
			
			// sinon
			else {
				// si on peut aller chercher un bonus
				distance = 1000;
				for (Cell bonus : this.bonusesInRange) {
					pathsave1 = this.evaluatePath(this.chara.getPos(), bonus);
					if (pathsave1 != null && pathsave1.size() < distance) {
						cellsave = bonus;
						pathsave2 = pathsave1;
						distance = pathsave2.size();
						play = true;
					}
				}
				
				// aller chercher bonus
				if (play && this.chara.getBonus() == 0) {
					this.applyPath(pathsave2, tempPM);
					int min = Math.min(pathsave2.size() - 1, tempPM);
					current = pathsave2.get(pathsave2.size() - 1 - min);
					tempPM -= distance - 1;
//					System.out.println(3);
				}

				// sinon
				else {
					// si il reste des PM
					// avancer vers l’ennemi le plus proche
					Cell closestEnemy = this.getClosest(this.chara.getPos(), this.enemies);
					if (this.evaluatePath(this.chara.getPos(), closestEnemy) != null) {
						this.applyPath(this.evaluatePath(this.chara.getPos(), closestEnemy), tempPM);
						int min = Math.min(this.evaluatePath(this.chara.getPos(), closestEnemy).size() - 1, tempPM);
						current = this.evaluatePath(this.chara.getPos(), closestEnemy).get(this.evaluatePath(this.chara.getPos(), closestEnemy).size() - 1 - min);
						tempPM -= this.evaluatePath(this.chara.getPos(), closestEnemy).size() - 1;

					}
//					System.out.println(4);
				}
			}
		}
		
		play = false;
		this.analyseMap(tempPM);
		// si il reste des PM
		if (tempPM > 0) {
			// si il y a un bonus atteignable
			distance = 1000;
			for (Cell bonus : this.bonusesInRange) {
				pathsave1 = this.evaluatePath(current, bonus);
				if (pathsave1 != null && pathsave1.size() < distance) {
					cellsave = bonus;
//					System.out.println(cellsave);
					pathsave2 = pathsave1;
					distance = pathsave2.size();
					play = true;
				}
			}

			// aller chercher bonus
			if (play && this.chara.getBonus() == 0) {
				this.applyPath(pathsave2, tempPM);
				int min = Math.min(pathsave2.size() - 1, tempPM);
				current = pathsave2.get(pathsave2.size() - 1 - min);
				tempPM -= distance - 1;
//				System.out.println(5);
			}

			// si il reste des PM
			if (tempPM > 0) {
				play = false;
//				System.out.println("on continue");
				// chercher la position la plus défensive
				// aller vers la position la plus défensive
				this.analyseMap(tempPM);
				this.defensSort(this.rangeOfActionBonuses);
				celldef = this.rangeOfActionBonuses.get(0);
				i = 0;
				while (!play && i < 100) {
					if (current != null && current.distanceFrom(celldef) <= tempPM && celldef.getCellType() != 1
							&& celldef.getCellType() != 2 && (celldef.getChara() == this.chara || !celldef.hasChara())) {
						pathsave2 = this.evaluatePath(current, celldef);
						if (pathsave2 != null && pathsave2.size() - 1 <= tempPM) {
							this.applyPath(pathsave2, tempPM);
							play = true;
//							System.out.println(6);
//							System.out.println(pathsave2);
						} else {
							this.rangeOfActionBonuses.add(this.rangeOfActionBonuses.get(0));
							this.rangeOfActionBonuses.remove(0);
							celldef = this.rangeOfActionBonuses.get(0);
						}
					} else {
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
