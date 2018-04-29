package tech.yassouel.com.stock;

public class Idoom {

    private int ID;
    private String nom;
    private int prix;
    private int quantite;

    public Idoom() {
    }

    public Idoom(int ID, String nom, int prix, int quantite) {
        this.ID = ID;
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void reduire(int quantite){
        this.quantite -= quantite;
    }

}
