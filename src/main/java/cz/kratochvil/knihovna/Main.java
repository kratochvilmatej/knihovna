package cz.kratochvil.knihovna;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main extends Application {
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml")));
        stage.setScene(new Scene(root, 1080, 720));
        stage.getIcons().add(new Image("file:src/main/resources/cz/kratochvil/knihovna/ikona.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();

        List<Kniha> users = new ArrayList<>();

        String jednaf = "file:src/main/resources/cz/kratochvil/knihovna/jedna.png";
        String dvaf = "file:src/main/resources/cz/kratochvil/knihovna/dva.png";
        String trif = "file:src/main/resources/cz/kratochvil/knihovna/tri.png";
        String ctyrif = "file:src/main/resources/cz/kratochvil/knihovna/ctyri.png";
        String petf = "file:src/main/resources/cz/kratochvil/knihovna//pet.png";
        String sestf = "file:src/main/resources/cz/kratochvil/knihovna/sest.png";
        String sedmf = "file:src/main/resources/cz/kratochvil/knihovna/sedm.png";
        String osmf = "file:src/main/resources/cz/kratochvil/knihovna/osm.png";
        String devetf = "file:src/main/resources/cz/kratochvil/knihovna/devet.png";

        Kniha jedna = new Kniha("Babička", "Božena Němcová", "1855", jednaf, "null", false);
        Kniha dva = new Kniha("Spalovač Mrtvol", "Ladislav Fuks", "1969", dvaf, "null", false);
        Kniha tri = new Kniha("Osudy dobrého vojáka Švejka za světové války", "Jaroslav Hašek", "1922", trif, "null", false);
        Kniha ctyri = new Kniha("Nesnesitelná Lehkost Bytí", "Milan Kundera", "1984", ctyrif, "null", false);
        Kniha pet = new Kniha("Lakomec", "Molière", "1668", petf, "null", false);
        Kniha sest = new Kniha("Král Lávra", "Karel Havlíček Borovský", "1836", sestf, "null", false);
        Kniha sedm = new Kniha("Ostře Sledované Vlaky", "Bohumil Hrabal", "1965", sedmf, "null", false);
        Kniha osm = new Kniha("Kytice", "Karel Jaromír Erben", "1853", osmf,"null", false);
        Kniha devet = new Kniha("Malý Princ", "Antoine de Saint-Exupéry", "1943", devetf, "null", false);

        users.add(jedna);
        users.add(dva);
        users.add(tri);
        users.add(ctyri);
        users.add(pet);
        users.add(sest);
        users.add(sedm);
        users.add(osm);
        users.add(devet);


        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("src/main/resources/cz/kratochvil/knihovna/knihy.dat"))) {
            out.writeObject(users);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
