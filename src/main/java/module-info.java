module com.example.networkfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires javafx.base;

    opens com.example.networkfx.domain to javafx.fxml;

    opens com.example.networkfx to javafx.fxml, javafx.base;
    exports com.example.networkfx;
    exports com.example.networkfx.domain;
}