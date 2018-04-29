package tech.yassouel.com.stock;

public class RecetteIdoom {

    private String date;
    private int recette;

    public RecetteIdoom() {
    }

    public RecetteIdoom(String date, int recette) {
        this.date = date;
        this.recette = recette;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRecette() {
        return recette;
    }

    public void setRecette(int recette) {
        this.recette = recette;
    }

}
