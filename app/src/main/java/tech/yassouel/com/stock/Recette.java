package tech.yassouel.com.stock;

/**
 * Created by youpes on 31/03/18.
 */

public class Recette {

    private String date;
    private int recette;

    public Recette(){

    }

    public Recette(String date, int recette) {

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
