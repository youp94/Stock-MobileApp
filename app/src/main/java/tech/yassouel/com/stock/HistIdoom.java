package tech.yassouel.com.stock;

public class HistIdoom {

    private String date;
    private Idoom idoom;

    public HistIdoom() {
    }

    public HistIdoom(String date, Idoom idoom) {
        this.date = date;
        this.idoom = idoom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Idoom getIdoom() {
        return idoom;
    }

    public void setIdoom(Idoom idoom) {
        this.idoom = idoom;
    }
}
