package moteur_de_jeu;

public class Guerrier extends Personnage {
	private int	bonus_pv=5;
	private int bonus_pv_max=5;
	private int bonus_puissance=3;
	private int bonus_defense=2;
	private int bonus_resistance=0;
	private int bonus_vitesse=1;
	private int bonus_technique=1;

	
	public Guerrier(String str) {
		super(str);
		pv = pv + bonus_pv;
		pv_max = pv_max + bonus_pv_max ;
		puissance= puissance + bonus_puissance;
		defense = defense + bonus_defense;
		resistance= resistance + bonus_resistance;
		vitesse = vitesse + bonus_vitesse;
		technique = technique + bonus_technique;
		type_combat=(Combat_physique) type_combat;
	}
	
	public static  void main(String args[]){
		//test création de guerrier
		Guerrier charles=new Guerrier("Charles");
		charles.afficher();
		
		
	}


}
