package strategy;


import map.*;
import entity.Character;

import java.util.ArrayList;


public class OffensiveStrategy extends Strategy {
	
	public OffensiveStrategy(Character chara) {
		super(chara);
	}
	
	
	@Override
	public void gameTurn() {
		Strategy.isPlaying = true;

//		System.out.println("\n\n========== OFFENSIVE ==========\n\n");
//		System.out.println("Courant : " + this.chara.getClass() + " " + this.chara.getTeam() + " " + this.chara.getPos() + "\n");

		Cell myPos = this.chara.getPos();
		Cell mainTarget = null;
		Cell secondTarget = null;
		ArrayList<Cell> mainPath = new ArrayList<>();
		ArrayList<Cell> secondPath = new ArrayList<>();

		boolean bestMove = false;

		int PM = this.chara.getPM();
		int Att = this.chara.getAtt();
		if (this.chara.getBonus() == Character.moveBonus) {
			PM += 3;
		}
		if (this.chara.getBonus() == Character.attackBonus) {
			Att += 3;
		}


		// Analyse de la carte
		this.analyseMap(PM);

//		System.out.println("Ennemis : " + this.enemies);
//		System.out.println("Bonus : " + this.bonuses);
//		System.out.println("Ennemis dans la range : " + this.enemiesInRange);
//		System.out.println("Bonus dans la range : " + this.bonusesInRange);

//		System.out.println("\n\n===== DECISION =====\n\n");

		// S'il y a des ennemis
		if (this.enemiesInRange.size() > 0) {
//			System.out.println("Il y a des ennemis : " + this.enemiesInRange.size());
			// Pour chaque ennemi
			for (Cell enemyPos : this.enemiesInRange) {
				Character enemy = enemyPos.getChara();
				// S'il est achevable
				if (enemy.isKillable(Att)) {
					if (!bestMove) {
//						System.out.println("\tEnnemi achevable");
						bestMove = true;
						mainTarget = enemyPos;
						mainPath = this.evaluatePathAttack(myPos, mainTarget);
						if (mainPath != null) {
							this.applyPath(mainPath, PM);

							int min = Math.min(mainPath.size() - 1, PM);
							myPos = mainPath.get(mainPath.size() - 1 - min);
							if (this.attackable(myPos, enemyPos)) {
//								System.out.println("\tDIE DIE DIE");
								this.attack(enemyPos);
							}

							// S'il reste des PM apres s'etre deplace
							int mTargetDist = mainPath.size() - 1;
							int remainingPM = PM - mTargetDist;
							if (remainingPM > 0) {
//								System.out.println("\t\tIl reste des PM");
								PM = remainingPM;
								if (this.bonusesInRange.size() > 0) {
									// S'il reste des bonus
//									System.out.println("\t\t\tIl reste des bonus proches");
									secondTarget = this.getClosest(myPos, this.bonusesInRange);
									secondPath = this.evaluatePath(myPos, secondTarget);
								} else {
									// Se rapprocher de l'ennemi le plus proche
//									System.out.println("\t\t\tPlus de bonus, on se rapproche d'un ennemi");
									secondTarget = this.getClosest(myPos, this.enemies);
									secondPath = this.evaluatePathAttack(myPos, secondTarget);
								}
								if (secondPath != null) {
									this.applyPath(secondPath, PM);
								}
							}
							this.enemies.remove(enemyPos);
						}
					}
				}
			}
			// Sinon pas achevable
			if (!bestMove) {
//				System.out.println("\tPas d'ennemi achevable");
				// Si bonus accessible
				if (this.bonusesInRange.size() > 0) {
//					System.out.println("\t\tBonus accessible");
					mainTarget = this.getClosest(myPos, this.bonusesInRange);
					mainPath = this.evaluatePath(myPos, mainTarget);
					if (mainPath != null) {
						this.applyPath(mainPath, PM);

						// MAJ myPos
						int min = Math.min(mainPath.size() - 1, PM);
						myPos = mainPath.get(mainPath.size() - 1 - min);

						// S'il reste des PM
						int mTargetDist = mainPath.size() - 1;
						int remainingPM = PM - mTargetDist;
						if (remainingPM > 0) {
//							System.out.println("\t\t\tIl reste des PM, on se rapproche");
							// Se rapprocher de l'ennemi le plus proche
							PM = remainingPM;
							secondTarget = this.getClosest(myPos, this.enemies);
							secondPath = this.evaluatePathAttack(myPos, secondTarget);
							if (secondPath != null) {
								this.applyPath(secondPath, PM);
								
								// MAJ myPos
								min = Math.min(secondPath.size() - 1, PM);
								myPos = secondPath.get(secondPath.size() - 1 - min);
								
								Cell enemyPos = this.getClosest(myPos, this.enemies);
								if (this.attackable(myPos, enemyPos)) {
//									System.out.println("\t\t\tTAKE THIS");
									this.attack(enemyPos);
								}
							}
						}
					}
				}
				// Sinon se rapprocher de l'ennemi le plus proche
				else {
//					System.out.println("\t\tPas de bonus accessible, on se rapproche");
					mainTarget = this.getClosest(myPos, this.enemiesInRange);
					mainPath = this.evaluatePathAttack(myPos, mainTarget);
					if (mainPath != null) {
						this.applyPath(mainPath, PM);

						// MAJ myPos
						int min = Math.min(mainPath.size() - 1, PM);
						myPos = mainPath.get(mainPath.size() - 1 - min);
						Cell enemyPos = this.getClosest(myPos, this.enemiesInRange);
						if (this.attackable(myPos, enemyPos)) {
//							System.out.println("\t\tTAKE THIS");
							this.attack(enemyPos);
						}
					}
				}
			}
		}
		// Sinon pas d'ennemis dans la zone d'action
		else {
//			System.out.println("Pas d'ennemis en zone d'action");
			// Si bonus accessible
			if (this.bonusesInRange.size() > 0) {
//				System.out.println("\tBonus accessible");
				mainTarget = this.getClosest(myPos, this.bonusesInRange);
				mainPath = this.evaluatePath(myPos, mainTarget);
				if (mainPath != null) {
					this.applyPath(mainPath, PM);
					
					// MAJ myPos
					int min = Math.min(mainPath.size() - 1, PM);
					myPos = mainPath.get(mainPath.size() - 1 - min);
					
					// S'il reste des PM
					int mTargetDist = mainPath.size() - 1;
					int remainingPM = PM - mTargetDist;
					if (remainingPM > 0) {
//						System.out.println("\t\tIl reste des PM, on se rapproche");
						// Se rapprocher de l'ennemi le plus proche
						PM = remainingPM;
						secondTarget = this.getClosest(myPos, this.enemies);
						secondPath = this.evaluatePathAttack(myPos, secondTarget);
						this.applyPath(secondPath, PM);
					}
				}
			} else {
				// Se rapprocher de l'ennemi le plus proche
//				System.out.println("\tPas de bonus accessible, on se rapproche");
				mainTarget = this.getClosest(myPos, this.enemies);
				mainPath = this.evaluatePathAttack(myPos, mainTarget);
				if (mainPath != null) {
					this.applyPath(mainPath, PM);
				}
			}
		}

		this.clear();

//		System.out.println("\n\n===== PATHS =====\n");
//		System.out.println(mainPath + "\n");
//		System.out.println(secondPath + "\n");
	}
}
