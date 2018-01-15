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
		int mTargetDist;
		int sTDist;

		// S'il y a des ennemis
		if (this.enemiesPos.size() >= 0) {
			// Pour chaque ennemi
			for (Cell enemyPos : this.enemiesPos) {
				Character enemy = enemyPos.getChara();
				ArrayList<Cell> sightView = this.evaluateSightView(this.chara.getPos(), enemyPos);
				// S'il est en ligne de vue
				if (this.isInSightView(sightView)) {
					// S'il est achevable
					if (enemy.isKillable(this.chara.getAtt())) {
						mainTarget = enemyPos;
						mTargetDist = this.chara.getPos().distanceFrom(enemyPos);
						/*===============================*/
						int remainingPM = mTargetDist - 1 - this.chara.getPM();
						// S'il reste des PM apres s'être déplacé
						if () {

						}
						// Sinon pas achevable
					} else {
						// Trouver un ennemi adapté (le plus proche)
					}
				}
			}
		}
	}

}
