package tech.yassouel.com.stock;

/**
 * Created by youpes on 01/04/18.
 */

public class HistFlexy {

    private String date;
    private Flexy flexy;

    public HistFlexy(){

    }

    public HistFlexy(String date, Flexy flexy) {
        this.date = date;
        this.flexy = flexy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Flexy getFlexy() {
        return flexy;
    }

    public void setFlexy(Flexy flexy) {
        this.flexy = flexy;
    }

}
