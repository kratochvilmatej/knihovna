package cz.kratochvil.knihovna;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainController {

    @FXML
    private Label lblStat;

    @FXML
    private Label lblLogged;

    @FXML
    private Label lblSiteInfo;


    @FXML
    private Label lblVypujceni;

    @FXML
    private Label lblVraceni;

    @FXML
    private Label lblNahledNazev;

    @FXML
    private Label lblNahledAutor;

    @FXML
    private Label lblNahledVydani;

    @FXML
    private TextField txUser;

    @FXML
    private TextField txPass;

    @FXML
    private TextField txNewNazev;

    @FXML
    private TextField txNewAutor;

    @FXML
    private TextField txNewVydani;

    @FXML
    private TextField txNewObrazek;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnReg;

    @FXML
    private Button btnBackLogin;

    @FXML
    private Button btnToReg;

    @FXML
    private Button btnPujcit;

    @FXML
    private Button btnNaVraceni;

    @FXML
    private Button btnVraceni;

    @FXML
    private Button btnBackVypujceni;

    @FXML
    private Button btnAdmin;

    @FXML
    private Button btnPridat;

    @FXML
    private AnchorPane pneVyber;

    @FXML
    private AnchorPane pneLogin;

    @FXML
    private AnchorPane pneMain;

    @FXML
    private AnchorPane pneAdmin;

    @FXML
    private VBox vbox;

    @FXML
    private ImageView imgNahled;

    public String loggedUser;


    //-----------------------------Loadery

    public List<User> load() {
        List<User> users = null;
        try {
            FileInputStream fileIn = new FileInputStream("src/main/resources/cz/kratochvil/knihovna/data.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            users = (List<User>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        return users;
    }

    public List<Kniha> loadKnizky() {
        List<Kniha> knihy = null;
        try {
            FileInputStream fileIn = new FileInputStream("src/main/resources/cz/kratochvil/knihovna/knihy.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            knihy = (List<Kniha>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        return knihy;
    }

    public void loadSeznam(Boolean d) {

        List<Kniha> list = new ArrayList<>();

        final boolean[] novy = {false};

        for (Kniha kniha : loadKnizky()) {

            HBox hbox = new HBox();
            hbox.setSpacing(50);

            Label label = new Label(kniha.getNazev() + " - " + kniha.getAutor() + " (" + kniha.getVydani() + ")");

            Image obrazek = new Image(kniha.getObrazek());
            ImageView image = new ImageView(obrazek);
            image.setFitHeight(50);
            image.setFitWidth(50);

            CheckBox check = new CheckBox();

            check.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) ->{
                kniha.checked = check.isSelected();
                list.add(kniha);

                novy[0] = true;
            });

            if(d=false) {
                check.setDisable(true);

                if(loggedUser.equals(kniha.getUser())) {
                    check.setDisable(false);
                }
            }

            list.add(kniha);

            hbox.getChildren().addAll(check, image, label);
            vbox.getChildren().add(hbox);
        }
        if(novy[0]) {
            try {
                FileWriter writer = new FileWriter("src/main/resources/cz/kratochvil/knihovna/knihy.dat");
                writer.write("");
                writer.close();
                FileOutputStream fileOut = new FileOutputStream("src/main/resources/cz/kratochvil/knihovna/knihy.dat");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(list);
                out.close();
                fileOut.close();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }

    //-----------------------------Checkery
    public boolean check(String regUser, String regPass) {
        List<User> users = load();
        for (User user : users) {
            if (regUser.contains(user.username) && regPass.contains(user.password)) {
                return true;
            }
        }
        return false;
    }

    public boolean alreadyExists() {
        List<User> users = load();
        for (User user : users) {
            if (txUser.getText().equals(user.username)) {
                return true;
            }
        }
        return false;
    }

    public boolean necoVybrane() {
        for (Kniha kniha : loadKnizky()) {
            if (kniha.checked) {
                return true;
            }
        }
        return false;
    }

    //-----------------------------Prihlasovani
    public void login(ActionEvent e) {
        if (check(txUser.getText(), txPass.getText())) {
            loggedUser = txUser.getText();
            prepniMain(e);
            lblLogged.setText(loggedUser);
            loadSeznam(false);
        } else {
            lblStat.setTextFill(Color.rgb(255, 0, 0, 1));
            lblStat.setText("Nesprávné Uživatelské Jméno nebo Heslo");
        }
    }

    public void register(ActionEvent e) {
        if (!alreadyExists()) {
            if (!txUser.getText().contains("\\s+") || !txPass.getText().contains("\\s+")) {
                if (!txUser.getText().isBlank() && !txPass.getText().isBlank()) {
                    if (!txUser.getText().equals("null")) {

                        User user = new User(txUser.getText(), txPass.getText());

                        List<User> reg = load();
                        reg.add(user);


                        try {
                            FileWriter writer = new FileWriter("src/main/resources/cz/kratochvil/knihovna/data.dat");
                            writer.write("");
                            writer.close();
                            FileOutputStream fileOut = new FileOutputStream("src/main/resources/cz/kratochvil/knihovna/data.dat");
                            ObjectOutputStream out = new ObjectOutputStream(fileOut);
                            out.writeObject(reg);
                            out.close();
                            fileOut.close();
                        } catch (IOException i) {
                            i.printStackTrace();
                        }
                        prepniLogin(e);
                    } else {
                        lblStat.setTextFill(Color.rgb(255, 0, 0, 1));
                        lblStat.setText("Neplatné uživatelské jméno");
                    }
                } else {
                    lblStat.setTextFill(Color.rgb(255, 0, 0, 1));
                    lblStat.setText("Uživatelské Jméno ani Heslo nesmí být prázdné");
                }
            } else {
                lblStat.setTextFill(Color.rgb(255, 0, 0, 1));
                lblStat.setText("Uživatelské Jméno ani Heslo nesmí obsahovat mezery!");
            }
        } else {
            lblStat.setTextFill(Color.rgb(255, 0, 0, 1));
            lblStat.setText("Tento uživatel již existuje!");
        }
    }

    public void logOut(ActionEvent e) {
        loggedUser = "";
        prepniLogin(e);
    }

    public void reset(ActionEvent e) {
        txPass.setText("");
        txUser.setText("");
        lblStat.setText("");
    }

    //-----------------------------Prepinaci Metody
    public void prepniMain(ActionEvent e) {
        pneLogin.setVisible(false);
        pneMain.setVisible(true);
        pneVyber.setVisible(true);
        if (loggedUser.contains("admin")) {
            btnAdmin.setVisible(true);
        }
    }

    public void prepniRegister(ActionEvent e) {
        lblSiteInfo.setText("Registrace nového uživatele");
        btnToReg.setVisible(false);
        btnLogin.setVisible(false);
        btnReg.setVisible(true);
        btnBackLogin.setVisible(true);
        lblStat.setText("");
    }

    public void prepniLogin(ActionEvent e) {
        pneLogin.setVisible(true);
        pneMain.setVisible(false);
        pneAdmin.setVisible(false);
        pneVyber.setVisible(false);
        btnLogin.setVisible(true);
        btnToReg.setVisible(true);
        btnReg.setVisible(false);
        btnBackLogin.setVisible(false);
        lblSiteInfo.setText("Přihlášení Uživatele");
        txUser.clear();
        txPass.clear();
        lblStat.setText("");
    }

    public void prepniVraceni(ActionEvent e) {
        vbox.getChildren().clear();
        loadSeznam(true);

        lblVypujceni.setText("");
        lblVraceni.setVisible(true);
        btnNaVraceni.setVisible(true);
        btnBackVypujceni.setVisible(true);
        btnPujcit.setVisible(false);
        btnVraceni.setVisible(false);
    }

    public void vypniVraceni(ActionEvent e) {
        btnBackVypujceni.setVisible(false);
        btnNaVraceni.setVisible(false);
        lblVraceni.setVisible(false);
        btnPujcit.setVisible(true);
        btnVraceni.setVisible(true);

        vbox.getChildren().clear();
        loadSeznam(false);

        if (lblVypujceni.getTextFill().toString().equals("0xff0000ff")) {
            lblVypujceni.setText("");
        }
    }

    public void prepniAdmin(ActionEvent e) {
        if (pneVyber.isVisible()) {
            btnAdmin.setText("Zpět k výběru");
            pneAdmin.setVisible(true);
            pneVyber.setVisible(false);
        } else {
            btnAdmin.setText("Přidat Knihu");
            pneAdmin.setVisible(false);
            pneVyber.setVisible(true);
        }
    }

    //-----------------------------Pujcovani
    public void pujcit(ActionEvent e) {

        List<Kniha> pujc = new ArrayList<>();



        for( Kniha kniha : loadKnizky()) {
            System.out.println(kniha.checked + " " + kniha.vydani);
        }

        for (Kniha kniha : loadKnizky()) {
            if (kniha.checked=true && kniha.user.equals("null")) {
                kniha.user = loggedUser;
                pujc.add(kniha);
                System.out.println("je checkla" + kniha.vydani);
            } else {
                pujc.add(kniha);
                System.out.println("neni checkla" + kniha.vydani);
            }
            try {
                FileWriter writer = new FileWriter("src/main/resources/cz/kratochvil/knihovna/knihy.dat");
                writer.write("");
                writer.close();
                FileOutputStream fileOut = new FileOutputStream("src/main/resources/cz/kratochvil/knihovna/knihy.dat");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(pujc);
                out.close();
                fileOut.close();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
        if (!necoVybrane()) {
            lblVypujceni.setTextFill(Color.rgb(255, 0, 0, 1));
            lblVypujceni.setText("Vyberte knihy, které chcete vypůjčit!");
        } else {
            lblVypujceni.setTextFill(Color.rgb(0, 255, 0, 1));
            lblVypujceni.setText("Vybrané knihy úspěšně vypůjčeny!");
        }
    }

    public void vratit(ActionEvent e) {
        List<Kniha> vrat = new ArrayList<>();

        for(Kniha kniha : loadKnizky()) {
            if(kniha.checked) {
                kniha.user = "null";
                vrat.add(kniha);
            } else {
                vrat.add(kniha);
            }
        }

        if (necoVybrane()) {
            lblVypujceni.setTextFill(Color.rgb(0, 255, 0, 1));
            lblVypujceni.setText("Knihy úspěšně vráceny!");
            vypniVraceni(e);
        } else {
            lblVypujceni.setTextFill(Color.rgb(255, 0, 0, 1));
            lblVypujceni.setText("Vyberte knihy, které chcete vrátit nebo se navraťte k vypůjčení!");
        }

        try {
            FileWriter writer = new FileWriter("src/main/resources/cz/kratochvil/knihovna/knihy.dat");
            writer.write("");
            writer.close();
            FileOutputStream fileOut = new FileOutputStream("src/main/resources/cz/kratochvil/knihovna/knihy.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(vrat);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    //-----------------------------Admin
    public void nahled(ActionEvent e) {
        lblNahledAutor.setText(txNewAutor.getText());
        lblNahledNazev.setText(txNewNazev.getText());
        lblNahledVydani.setText(txNewVydani.getText());

        if (!txNewObrazek.getText().isBlank() && txNewObrazek.getText().contains(".png") || txNewObrazek.getText().contains(".jpg") || txNewObrazek.getText().contains(".jpeg")) {
            Image nahled = new Image(txNewObrazek.getText());

            imgNahled.setImage(nahled);
        }
    }

    public void pridat(ActionEvent e) {

        List<Kniha> list = loadKnizky();

        if (!txNewNazev.getText().isBlank() && !txNewAutor.getText().isBlank() && !txNewVydani.getText().isBlank()) {
            String obrazek = "file:src/main/resources/cz/kratochvil/knihovna/neznama.png";
            if (!txNewObrazek.getText().isBlank()) {
                obrazek = txNewObrazek.getText();
            }
            Kniha nova = new Kniha(txNewNazev.getText(), txNewAutor.getText(), txNewVydani.getText(), obrazek, "null", false);

            list.add(nova);

            try {
                FileWriter writer = new FileWriter("src/main/resources/cz/kratochvil/knihovna/knihy.dat");
                writer.write("");
                writer.close();
                FileOutputStream fileOut = new FileOutputStream("src/main/resources/cz/kratochvil/knihovna/knihy.dat");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(list);
                out.close();
                fileOut.close();
            } catch (IOException i) {
                i.printStackTrace();
            }

            btnPridat.setStyle("-fx-background-color: #00FF00;");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            btnPridat.setStyle("");

            prepniAdmin(e);

            vbox.getChildren().clear();

            loadSeznam(false);

        } else {
            btnPridat.setStyle("-fx-background-color: #ff0000;");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            btnPridat.setStyle("");
        }
    }

}
