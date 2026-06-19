package com.mycompany.jogorpgtlp.controller;

import com.mycompany.jogorpgtlp.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class TelaInicialController {

    @FXML
    private void irParaMenuPersonagens() throws IOException {
        App.setRoot("menuPersonagens");
    }
}