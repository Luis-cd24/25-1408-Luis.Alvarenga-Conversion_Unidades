module com.programacion.conversion_unidades {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.programacion.conversion_unidades to javafx.fxml;
    exports com.programacion.conversion_unidades;
}