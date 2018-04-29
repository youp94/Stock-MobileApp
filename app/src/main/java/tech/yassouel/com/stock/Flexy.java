package tech.yassouel.com.stock;

/**
 * Created by youpes on 31/03/18.
 */

public class Flexy {

    private int ID;
    private String nom;
    private int montant;

    public Flexy(){

    }

    public Flexy(int ID, String nom, int montant) {
        this.ID = ID;
        this.nom = nom;
        this.montant = montant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void reduireMontant(int montant){
        this.montant -= montant;

    }
}
