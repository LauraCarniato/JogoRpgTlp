module com.mycompany.jogorpgtlp {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.jogorpgtlp.controller to javafx.fxml;

    exports com.mycompany.jogorpgtlp;
    exports com.mycompany.jogorpgtlp.model;
}