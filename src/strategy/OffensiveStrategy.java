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
		isPlaying = true;
		
		System.out.println("\n\n========== OFFENSIVE ==========\n\n");

		Cell mainTarget = null;
		Cell secondTarget = null;
		ArrayList<Cell> mainPath = new ArrayList<>();
		ArrayList<Cell> secondPath = new ArrayList<>();

		boolean bestMove = false;

		int PM = this.chara.getPM();
		int Att = this.chara.getAtt();
		if (this.chara.getBonus() == 1) {
			PM += 3;
		}
		if (this.chara.getBonus() == 2) {
			Att += 3;
		}


		// Analyse de la carte
		this.analyseMap();

		System.out.println("Ennemis : " + this.enemies);
		System.out.println("Bonus : " + this.bonuses);
		System.out.println("Ennemis dans la range : " + this.enemiesInRange);
		System.out.println("Bonus dans la range : " + this.bonusesInRange);

		System.out.println("\n\n===== DECISION =====\n\n");

		// S'il y a des ennemis
		if (this.enemiesInRange.size() > 0) {
			System.out.println("Il y a des ennemis : " + this.enemiesInRange.size());
			// Pour chaque ennemi
			for (Cell enemyPos : this.enemiesInRange) {
				Character enemy = enemyPos.getChara();
				// S'il est achevable
				if (enemy.isKillable(Att)) {
					System.out.println("\tEnnemi achevable : " + Att);
					bestMove = true;
					mainTarget = enemyPos;
					mainPath = this.evaluatePathAttack(this.chara.getPos(), mainTarget);
					mainTarget = mainPath.get(0);

					this.applyPath(mainPath, PM);
					this.attack(enemyPos);

					// S'il reste des PM apres s'etre deplace
					int mTargetDist = mainPath.size() - 1;
					int remainingPM = PM - mTargetDist;
					if (remainingPM > 0) {
						System.out.println("\t\tIl reste des PM");
						PM = remainingPM;
						if (this.bonusesInRange.size() > 0) {
							// S'il reste des bonus
							System.out.println("\t\t\tIl reste des bonus proches");
							secondPath = this.getPathToClosest(mainTarget, this.bonusesInRange);
							this.applyPath(secondPath, PM);
						} else {
							// Se rapprocher de l'ennemi le plus proche
							System.out.println("\t\t\tPlus de bonus, on se rapproche d'un ennemi");
							secondPath = this.getPathToClosest(mainTarget, this.enemies);
							secondTarget = secondPath.get(0);
							secondPath = this.evaluatePathAttack(mainTarget, secondTarget);
							this.applyPath(secondPath, PM);
						}
					}

					this.enemies.remove(enemyPos);
				}
			}
			// Sinon pas achevable
			if (!bestMove) {
				System.out.println("\tPas d'ennemi achevable");
				// Si bonus accessible
				if (this.bonusesInRange.size() > 0) {
					System.out.println("\t\tBonus accessible");
					mainPath = this.getPathToClosest(this.chara.getPos(), this.bonusesInRange);
					mainTarget = mainPath.get(0);
					this.applyPath(mainPath, PM);
					// S'il reste des PM
					int mTargetDist = mainPath.size() - 1;
					int remainingPM = PM - mTargetDist;
					if (remainingPM > 0) {
						System.out.println("\t\t\tIl reste des PM, on se rapproche");
						// Se rapprocher de l'ennemi le plus proche
						PM = remainingPM;
						secondPath = this.getPathToClosest(mainTarget, this.enemies);
						secondTarget = secondPath.get(0);
						secondPath = this.evaluatePathAttack(mainTarget, secondTarget);
						this.applyPath(secondPath, PM);
						Cell enemyPos = this.getClosest(secondTarget, this.enemies);
						this.attack(enemyPos);
					}
				}
				// Sinon se rapprocher de l'ennemi le plus proche
				else {
					System.out.println("\t\tPas de bonus accessible, on se rapproche");
					mainPath = this.getPathToClosest(this.chara.getPos(), this.enemiesInRange);
					mainTarget = mainPath.get(0);
					mainPath = this.evaluatePathAttack(this.chara.getPos(), mainTarget);
					this.applyPath(mainPath, PM);
					Cell enemyPos = this.getClosest(mainTarget, this.enemies);
					this.attack(enemyPos);
				}
			}
		}
		// Sinon pas d'ennemis dans la zone d'action
		else {
			System.out.println("Pas d'ennemis en zone d'action");
			// Si bonus accessible
			if (this.bonusesInRange.size() > 0) {
				System.out.println("\tBonus accessible");
				mainPath = this.getPathToClosest(this.chara.getPos(), this.bonusesInRange);
				mainTarget = mainPath.get(0);
				this.applyPath(mainPath, PM);
				// S'il reste des PM
				int mTargetDist = mainPath.size() - 1;
				int remainingPM = PM - mTargetDist;
				if (remainingPM > 0) {
					System.out.println("\t\tIl reste des PM, on se rapproche");
					// Se rapprocher de l'ennemi le plus proche
					PM = remainingPM;
					secondPath = this.getPathToClosest(mainTarget, this.enemies);
					this.applyPath(secondPath, PM);
				}
			} else {
				// Se rapprocher de l'ennemi le plus proche
				System.out.println("\tPas de bonus accessible, on se rapproche");
				mainPath = this.getPathToClosest(this.chara.getPos(), this.enemies);
				this.applyPath(mainPath, PM);
			}
		}

		this.clear();

		System.out.println("\n\n===== PATHS =====\n");
		System.out.println(mainPath + "\n");
		System.out.println(secondPath + "\n");
	}
}
