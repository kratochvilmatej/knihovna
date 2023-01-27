package cz.kratochvil.knihovna;

import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class Kniha implements Serializable {
    String nazev;
    String autor;
    String vydani;
    ImageView obrazek;
    CheckBox check;
    String user;

    public String getUser() {
        return user;
    }

    public Kniha(String nazev, String autor, String vydani, ImageView obrazek, CheckBox check, String user) {
        this.nazev = nazev;
        this.autor = autor;
        this.vydani = vydani;
        this.obrazek = obrazek;
        this.check = check;
        this.user = user;
    }
}
