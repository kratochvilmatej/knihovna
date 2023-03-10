package cz.kratochvil.knihovna;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
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
    private TextField txSearch;

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
    private ScrollPane pneScroll;

    @FXML
    private VBox vbox;

    @FXML
    private ImageView imgNahled;

    public String loggedUser;

    public List<Kniha> knihy = new ArrayList<>();

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

    public void loadSeznam(int x, List<Kniha> kniz) {

        vbox.getChildren().clear();

        List<Kniha> tem = new ArrayList<>();

        int pos = 0;

        for (Kniha kniha : kniz) {

            HBox hbox = new HBox();
            hbox.setSpacing(50);

            Label label = new Label(kniha.getNazev() + " - " + kniha.getAutor() + " (" + kniha.getVydani() + ")");

            Image obrazek = new Image(kniha.getObrazek());
            ImageView image = new ImageView(obrazek);
            image.setFitHeight(50);
            image.setFitWidth(50);

            CheckBox check = new CheckBox();


            if (!kniha.user.equals("null")) {
                check.setDisable(true);
            }
            if (x == 1) { //Vraceni
                check.setDisable(true);
                if (kniha.user.equals(loggedUser)) {
                    check.setDisable(false);
                }
            } else if (x == 2) {
                if (kniha.checked) {
                    check.setSelected(true);
                }
                if (!kniha.user.equals("null")) {
                    check.setDisable(true);
                }
            } else {
                if (check.isSelected()) {
                    check.setSelected(false);
                }
            }

            check.selectedProperty().addListener(
                    (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {

                        int index = knihy.indexOf(kniha);

                        Kniha nova = new Kniha(kniha.getNazev(), kniha.getAutor(), kniha.getVydani(), kniha.getObrazek(), kniha.getUser(), new_val);

                        knihy.set(index, nova);

                    });

            hbox.getChildren().addAll(check, image, label);
            vbox.getChildren().add(hbox);

            pneScroll.setContent(vbox);
            pneScroll.setFitToHeight(true);
            vbox.setAlignment(Pos.CENTER);

            try {
                FileOutputStream fileOut = new FileOutputStream("src/main/resources/cz/kratochvil/knihovna/knihy.dat");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(knihy);
                out.close();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        txSearch.textProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {

            List<Kniha> temp = new ArrayList<>();

            for (Kniha kniha : knihy) {
                if (kniha.getNazev().toLowerCase().contains(new_val.toLowerCase()) || kniha.getAutor().toLowerCase().contains(new_val.toLowerCase())) {
                    temp.add(kniha);
                }
            }
            loadSeznam(0, temp);
        });

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
        for (Kniha kniha : knihy) {
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
            loadSeznam(0, loadKnizky());

        } else {
            lblStat.setTextFill(Color.rgb(255, 0, 0, 1));
            lblStat.setText("Nespr??vn?? U??ivatelsk?? Jm??no nebo Heslo");
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
                        lblStat.setText("Neplatn?? u??ivatelsk?? jm??no");
                    }
                } else {
                    lblStat.setTextFill(Color.rgb(255, 0, 0, 1));
                    lblStat.setText("U??ivatelsk?? Jm??no ani Heslo nesm?? b??t pr??zdn??");
                }
            } else {
                lblStat.setTextFill(Color.rgb(255, 0, 0, 1));
                lblStat.setText("U??ivatelsk?? Jm??no ani Heslo nesm?? obsahovat mezery!");
            }
        } else {
            lblStat.setTextFill(Color.rgb(255, 0, 0, 1));
            lblStat.setText("Tento u??ivatel ji?? existuje!");
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
        lblSiteInfo.setText("Registrace nov??ho u??ivatele");
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
        lblSiteInfo.setText("P??ihl????en?? U??ivatele");
        txUser.clear();
        txPass.clear();
        lblStat.setText("");
    }

    public void prepniVraceni(ActionEvent e) {

        loadSeznam(1, knihy);

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

        loadSeznam(0, knihy);

        if (lblVypujceni.getTextFill().toString().equals("0xff0000ff")) {
            lblVypujceni.setText("");
        }
    }

    public void prepniAdmin(ActionEvent e) {
        if (pneVyber.isVisible()) {
            btnAdmin.setText("Zp??t k v??b??ru");
            pneAdmin.setVisible(true);
            pneVyber.setVisible(false);
        } else {
            btnAdmin.setText("P??idat Knihu");
            pneAdmin.setVisible(false);
            pneVyber.setVisible(true);
        }
    }

    //-----------------------------Pujcovani
    public void pujcit(ActionEvent e) {

        if (!necoVybrane()) {
            lblVypujceni.setTextFill(Color.rgb(255, 0, 0, 1));
            lblVypujceni.setText("Vyberte knihy, kter?? chcete vyp??j??it!");
        } else {

            int pos = 0;

            List<Kniha> temp = new ArrayList<>();

            for (Kniha kniha1 : knihy) {
                temp.add(kniha1);
            }

            for (Kniha kniha : knihy) {
                if (kniha.checked) {
                    kniha.user = loggedUser;

                    temp.remove(kniha);
                    temp.add(pos, kniha);

                }
                pos++;
            }

            knihy.clear();

            for (Kniha kniha1 : temp) {
                knihy.add(kniha1);
            }

            lblVypujceni.setTextFill(Color.rgb(0, 255, 0, 1));
            lblVypujceni.setText("Vybran?? knihy ??sp????n?? vyp??j??eny!");
        }
        loadSeznam(3, knihy);
    }

    public void vratit(ActionEvent e) {

        if (necoVybrane()) {

            int pos = 0;

            List<Kniha> temp = new ArrayList<>();

            for (Kniha kniha1 : knihy) {
                temp.add(kniha1);
            }

            for (Kniha kniha : knihy) {
                if (kniha.checked) {

                    kniha.checked = false;

                    kniha.user = "null";

                    temp.remove(kniha);
                    temp.add(pos, kniha);

                }
                pos++;
            }

            knihy.clear();

            for (Kniha kniha1 : temp) {
                knihy.add(kniha1);
            }

            lblVypujceni.setTextFill(Color.rgb(0, 255, 0, 1));
            lblVypujceni.setText("Knihy ??sp????n?? vr??ceny!");

            loadSeznam(0, knihy);

            vypniVraceni(e);
        } else {
            lblVypujceni.setTextFill(Color.rgb(255, 0, 0, 1));
            lblVypujceni.setText("Vyberte knihy, kter?? chcete vr??tit nebo se navra??te k vyp??j??en??!");
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

        if (!txNewNazev.getText().isBlank() && !txNewAutor.getText().isBlank() && !txNewVydani.getText().isBlank()) {
            String obrazek = "file:src/main/resources/cz/kratochvil/knihovna/neznama.png";
            if (!txNewObrazek.getText().isBlank()) {
                obrazek = txNewObrazek.getText();
            }
            Kniha nova = new Kniha(txNewNazev.getText(), txNewAutor.getText(), txNewVydani.getText(), obrazek, "null", false);

            knihy.add(nova);

            btnPridat.setStyle("-fx-background-color: #00FF00;");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            btnPridat.setStyle("");

            pneAdmin.setVisible(false);
            pneVyber.setVisible(true);

            loadSeznam(0, knihy);

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
