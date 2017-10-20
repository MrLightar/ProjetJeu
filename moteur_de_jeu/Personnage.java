package moteur_de_jeu;


public class Personnage {

	protected int pv;
	protected int pv_max;
	protected int puissance;
	protected int defense;
	protected int resistance;
	protected int vitesse;
	protected int technique;
	protected String nom;
	protected int equipe;
	protected TypeCombat type_combat;

	

	public Personnage(String str) {

		this.pv = 20;
		this.pv_max=pv;
		this.puissance = 5;
		this.defense = 4;
		this.resistance = 4;
		this.vitesse = 5;
		this.technique = 5;
		this.nom = str;
	}
	
	public void afficher() {
		System.out.println("Fiche de " + nom + "\nPv : " + pv + " | PvMax : " + pv_max + " | Puissance : " + puissance + " | Defense : " + defense + " | Resistance : " + resistance + " | Vitesse : " + vitesse + " | Technique : " + technique);
	} 

}
