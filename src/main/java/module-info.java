module antonchuvashov.familybudget {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens antonchuvashov.familybudget to javafx.fxml;
    exports antonchuvashov.familybudget;
}