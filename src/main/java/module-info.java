module cz.kratochvil.knihovna {
    requires javafx.controls;
    requires javafx.fxml;


    opens cz.kratochvil.knihovna to javafx.fxml;
    exports cz.kratochvil.knihovna;
}