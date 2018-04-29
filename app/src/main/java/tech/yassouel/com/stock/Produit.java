package tech.yassouel.com.stock;

/**
 * Created by youpes on 19/03/18.
 */

public class Produit {

    private int ID;
    private String nom;
    private int prix_achat;
    private int prix_vente;
    private int quantite;
    private String imageURl;

    public Produit(){

    }

    public Produit(int ID, String nom, int prix_achat, int prix_vente, int quantite, String imageURl){
        this.ID = ID;
        this.nom = nom;
        this.prix_achat = prix_achat;
        this.prix_vente = prix_vente;
        this.quantite = quantite;
        this.imageURl = imageURl;
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

    public int getPrix_achat() {
        return prix_achat;
    }

    public void setPrix_achat(int prix_achat) {
        this.prix_achat = prix_achat;
    }

    public int getPrix_vente() {
        return prix_vente;
    }

    public void setPrix_vente(int prix_vente) {
        this.prix_vente = prix_vente;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
    }

    public void vendre(int quantite){
        this.quantite -= quantite;
    }

    public void reduire(int reduc){
        this.prix_vente -= reduc;
    }

}
