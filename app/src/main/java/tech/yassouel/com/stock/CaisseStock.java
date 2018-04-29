package tech.yassouel.com.stock;

public class CaisseStock {

    private int montant;

    public CaisseStock() {
    }

    public CaisseStock(int montant) {
        this.montant = montant;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public void addMontant(int montant){ this.montant += montant; }
}
