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
    CheckBox check;


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


    public Kniha(String nazev, String autor, String vydani, String obrazek, String user) {
        this.nazev = nazev;
        this.autor = autor;
        this.vydani = vydani;
        this.obrazek = obrazek;
        this.user = user;
    }

    @Override
    public String toString() {
        return nazev + " " + autor + " " + vydani + " " + obrazek + " " + user;
    }
    @Serial
    private static final long serialVersionUID = -4649616439668071342L;
}
