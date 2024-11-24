module antonchuvashov.familybudget {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires java.desktop;

    opens antonchuvashov.familybudget to javafx.fxml;
    exports antonchuvashov.familybudget;
}