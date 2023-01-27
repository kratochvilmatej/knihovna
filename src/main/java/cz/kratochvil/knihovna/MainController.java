package cz.kratochvil.knihovna;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.*;
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
    private TextField txUser;

    @FXML
    private TextField txPass;

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
    private AnchorPane pneVyber;

    @FXML
    private AnchorPane pneLogin;

    @FXML
    private AnchorPane pneMain;

    public String loggedUser;

    //-----------------------------Loadery
    public List<User> load() {
        List<User> users = null;
        try {
            FileInputStream fileIn = new FileInputStream("data.dat");
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
            FileInputStream fileIn = new FileInputStream("knihy.dat");
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

    public void loadSeznam() {

    }

    //-----------------------------Checkery
    public boolean check(String regUser, String regPass) {
        List<User> users = load();
        for (User user : users) {
            if (regUser.equals(user.username) && regPass.equals(user.password)) {
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
            if (kniha.check.isSelected()) {
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
            loadKnizky();
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

                        try {
                            FileOutputStream fileOut = new FileOutputStream("data.dat");
                            ObjectOutputStream out = new ObjectOutputStream(fileOut);
                            out.writeObject(user);
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
        for (Kniha kniha : loadKnizky()) {
            kniha.check.setDisable(true);
            if (kniha.getUser().equals(loggedUser)) {
                kniha.check.setDisable(false);
            }
        }
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
        loadKnizky();
        if (lblVypujceni.getTextFill().toString().equals("0xff0000ff")) {
            lblVypujceni.setText("");
        }
    }

    //-----------------------------Pujcovani
    public void pujcit(ActionEvent e) {
        if (necoVybrane()) {
            for (Kniha kniha : loadKnizky()) {
                if (kniha.check.isSelected()) {
                    kniha.user = loggedUser;
                }
            }
            lblVypujceni.setTextFill(Color.rgb(0, 255, 0, 1));
            lblVypujceni.setText("Vybrané knihy úspěšně vypůjčeny!");
        } else {
            lblVypujceni.setTextFill(Color.rgb(255, 0, 0, 1));
            lblVypujceni.setText("Vyberte knihy, které chcete vypůjčit!");
        }

    }

    public void vratit(ActionEvent e) {
        if (necoVybrane()) {
            for (Kniha kniha : loadKnizky()) {
                if (kniha.check.isSelected()) {
                    kniha.user = "null";
                    kniha.check.setSelected(false);
                }
            }
            lblVypujceni.setTextFill(Color.rgb(0, 255, 0, 1));
            lblVypujceni.setText("Knihy úspěšně vráceny!");
            vypniVraceni(e);
        } else {
            lblVypujceni.setTextFill(Color.rgb(255, 0, 0, 1));
            lblVypujceni.setText("Vyberte knihy, které chcete vrátit nebo se navraťte k vypůjčení!");
        }
    }
}
