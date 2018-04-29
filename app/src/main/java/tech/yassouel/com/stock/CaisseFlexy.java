package tech.yassouel.com.stock;

public class CaisseFlexy {

    private int montant;

    public CaisseFlexy() {
    }

    public CaisseFlexy(int montant) {
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
