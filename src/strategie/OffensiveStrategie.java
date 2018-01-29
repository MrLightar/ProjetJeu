package strategie;


import map.*;
import entity.Character;

import java.util.ArrayList;


public class OffensiveStrategie extends Strategie {
	
	public OffensiveStrategie(Character chara) {
		super(chara);
	}
	
	
	@Override
	public void gameTurn() {

		Cell mainTarget = null;
		Cell secondTarget = null;
		ArrayList<Cell> mainPath = new ArrayList<>();
		ArrayList<Cell> secondPath = new ArrayList<>();

		boolean bestMove = false;
		
		
		// Analyse de la carte
		this.analyseMap();
		
		// D�terminer la zone d'action du personnage
		this.rangeOfActionEnemies = this.evaluateRangeOfAction(this.chara.getPos(), this.chara.getPM() + this.chara.getPO());
		this.rangeOfActionBonuses = this.evaluateRangeOfAction(this.chara.getPos(), this.chara.getPM());
		// Chercher les positions des adversaires et des bonus dans cette zone d'action
		this.enemiesInRange = this.getEnemiesInRange(this.rangeOfActionEnemies);
		this.bonusesInRange = this.getBonusInRange(this.rangeOfActionBonuses);
		
		// S'il y a des ennemis
		if (this.enemiesInRange.size() >= 0) {
			System.out.println("Il y a des ennemis");
			// Pour chaque ennemi
			for (Cell enemyPos : this.enemiesInRange) {
				Character enemy = enemyPos.getChara();
				ArrayList<Cell> sightView = this.evaluateSightView(this.chara.getPos(), enemyPos);
				// S'il est en ligne de vue
				if (this.isInSightView(sightView)) {
					System.out.println("\tEnnemi en ligne de vue");
					// S'il est achevable
					if (enemy.isKillable(this.chara.getAtt())) {
						System.out.println("\t\tEnnemi achevable");
						bestMove = true;
						mainTarget = enemyPos;
						mainPath = this.evaluatePath(this.chara.getPos(), mainTarget);
						// S'il reste des PM apres s'�tre d�plac�
						int mTargetDist = mainPath.size() - 1;
						int remainingPM = this.chara.getPM() - mTargetDist;
						if (remainingPM > 0) {
							System.out.println("\t\t\tIl reste des PM");
							ArrayList<Cell> newRangeOfAction = this.evaluateRangeOfAction(mainTarget, remainingPM);
							ArrayList<Cell> closeBonuses = this.getBonusInRange(newRangeOfAction);
							// Si bonus accessible avec PM restants
							if (closeBonuses.size() > 0) {
								System.out.println("\t\t\t\tBonus accessible avec PM restant");
								Cell closestBonus = this.getClosest(mainTarget, closeBonuses);
								secondTarget = closestBonus;
								secondPath = this.evaluatePath(mainTarget, secondTarget);
							}
							// Sinon pas de bonus apr�s d�placement
							else {
								// Se rapprocher de l'ennemi le plus proche
								System.out.println("\t\t\t\tPas de bonus accessible, on se rapproche");
								Cell closestEnemy = this.getClosest(mainTarget, this.enemies);
								secondTarget = closestEnemy;
								secondPath = this.evaluatePath(mainTarget, secondTarget);
							}
						}
					}
				}
			}
			
			// Sinon pas achevable
			if (!bestMove) {
				System.out.println("\tPas d'ennemi achevable");
				// Si bonus accessible
				if (this.bonusesInRange.size() > 0) {
					System.out.println("\t\tBonus accessible");
					Cell closestBonus = this.getClosest(this.chara.getPos(), this.bonusesInRange);
					mainTarget = closestBonus;
					mainPath = this.evaluatePath(this.chara.getPos(), mainTarget);
					// S'il reste des PM
					int mTargetDist = mainPath.size() - 1;
					int remainingPM = this.chara.getPM() - mTargetDist;
					if (remainingPM > 0) {
						System.out.println("\t\t\tIl reste des PM");
						ArrayList<Cell> newRangeOfAction = this.evaluateRangeOfAction(mainTarget,
								remainingPM + this.chara.getPO());
						ArrayList<Cell> closeEnemies = this.getEnemiesInRange(newRangeOfAction);
						// Si ennemi accessible avec PM restants
						if (closeEnemies.size() > 0) {
							System.out.println("\t\t\t\tEnnemi accessible avec PM restants");
							// Se rapprocher de l'ennemi le plus proche
							Cell closestEnemy = this.getClosest(mainTarget, closeEnemies);
							secondTarget = closestEnemy;
							secondPath = this.evaluatePath(mainTarget, secondTarget);
						}
						// Sinon pas d'ennemis accessible apr�s d�placement
						else {
							System.out.println("\t\t\t\tPas d'ennemi accessible avec PM restants");
							// Se rapprocher de l'ennemi le plus proche
							Cell closestEnemy = this.getClosest(mainTarget, this.enemies);
							secondTarget = closestEnemy;
							secondPath = this.evaluatePath(mainTarget, secondTarget);
						}
					}
				}
				// Sinon
				else {
					// Se rapprocher de l'ennemi le plus proche
					System.out.println("\t\tPas de bonus accessible, on se rapproche");
					Cell closestEnemy = this.getClosest(mainTarget, this.enemiesInRange);
					mainTarget = closestEnemy;
					mainPath = this.evaluatePath(mainTarget, secondTarget);
				}
			}
		}
		// Sinon pas d'ennemis dans la zone d'action
		else {
			// Si bonus accessible
			if (this.bonusesInRange.size() > 0) {
				Cell closestBonus = this.getClosest(this.chara.getPos(), this.bonusesInRange);
				mainTarget = closestBonus;
				mainPath = this.evaluatePath(this.chara.getPos(), mainTarget);
				// S'il reste des PM
				int mTargetDist = mainPath.size() - 1;
				int remainingPM = this.chara.getPM() - mTargetDist;
				if (remainingPM > 0) {
					// Se rapprocher de l'ennemi le plus proche
					Cell closestEnemy = this.getClosest(mainTarget, this.enemies);
					secondTarget = closestEnemy;
					secondPath = this.evaluatePath(mainTarget, secondTarget);
				}
			} else {
				// Se rapprocher de l'ennemi le plus proche
				Cell closestEnemy = this.getClosest(mainTarget, this.enemies);
				secondTarget = closestEnemy;
				secondPath = this.evaluatePath(mainTarget, secondTarget);
			}
		}

		System.out.println("\n\n====================================");
		System.out.println(mainPath + "\n\n");
		System.out.println(secondPath);
	}
}

