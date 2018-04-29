package tech.yassouel.com.stock;

/**
 * Created by youpes on 30/03/18.
 */

public class Vendu {

    private String date;
    private Produit produit;

    public Vendu(){

    }

    public Vendu(String date, Produit produit) {
        this.date = date;
        this.produit = produit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }
}
