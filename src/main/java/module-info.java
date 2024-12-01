module antonchuvashov.familybudget {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires java.desktop;
    requires org.jfree.jfreechart;
    requires kernel;
    requires layout;
    requires io;
    requires bcrypt;

    opens antonchuvashov.familybudget to javafx.fxml;
    exports antonchuvashov.familybudget;
    exports antonchuvashov.utils;
    opens antonchuvashov.utils to javafx.fxml;
    exports antonchuvashov.model;
    opens antonchuvashov.model to javafx.fxml;
}