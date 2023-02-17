package cz.kratochvil.knihovna;

import javafx.scene.control.CheckBox;

import java.io.Serial;
import java.io.Serializable;

public class Kniha implements Serializable {
    String nazev;
    String autor;
    String vydani;
    String obrazek;
    String user;
    Boolean checked;


    public String getNazev() {
        return nazev;
    }

    public String getAutor() {
        return autor;
    }

    public String getVydani() {
        return vydani;
    }

    public String getObrazek() {
        return obrazek;
    }

    public String getUser() {
        return user;
    }

    public Kniha(String nazev, String autor, String vydani, String obrazek, String user, Boolean checked) {
        this.nazev = nazev;
        this.autor = autor;
        this.vydani = vydani;
        this.obrazek = obrazek;
        this.user = user;
        this.checked = checked;
    }

    @Override
    public String toString() {
        return nazev + " " + autor + " " + vydani + " " + obrazek + " " + user;
    }
    @Serial
    private static final long serialVersionUID = -4649616439668071342L;
}
