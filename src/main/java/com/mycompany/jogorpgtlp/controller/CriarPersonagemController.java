package com.mycompany.jogorpgtlp.controller;

import com.mycompany.jogorpgtlp.App;
import com.mycompany.jogorpgtlp.model.GerenciadorPersonagens;
import com.mycompany.jogorpgtlp.model.Personagem;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CriarPersonagemController {

    @FXML
    private TextField campoNome;

    @FXML
    private ChoiceBox<String> escolhaClasse;

    @FXML
    private ImageView imagemAparencia1;

    @FXML
    private ImageView imagemAparencia2;

    private String aparenciaSelecionada;

    @FXML
    private void initialize() {
        escolhaClasse.getItems().addAll(
                "Druida",
                "Bardo",
                "Bárbaro",
                "Mago"
        );

        escolhaClasse.setOnAction(event -> mostrarAparencias());
    }

    private Image carregarImagem(String nomeArquivo) {
        return new Image(
                getClass().getResourceAsStream("/assets/" + nomeArquivo)
        );
    }

    private void mostrarAparencias() {
        String classe = escolhaClasse.getValue();

        if (classe == null) {
            return;
        }

        String nome1 = "";
        String nome2 = "";

        if (classe.equals("Bardo")) {
            nome1 = "bardo";
            nome2 = "bardo2";
        } else if (classe.equals("Mago")) {
            nome1 = "mago";
            nome2 = "mago2";
        } else if (classe.equals("Druida")) {
            nome1 = "druida";
            nome2 = "druida2";
        } else if (classe.equals("Bárbaro")) {
            nome1 = "barbaro";
            nome2 = "barbaro2";
        }

        imagemAparencia1.setImage(carregarImagem(nome1 + ".png"));
        imagemAparencia2.setImage(carregarImagem(nome2 + ".png"));

        aparenciaSelecionada = null;
        imagemAparencia1.setStyle("");
        imagemAparencia2.setStyle("");
    }

    @FXML
    private void selecionarAparencia1() {
        selecionarAparencia(false);
    }

    @FXML
    private void selecionarAparencia2() {
        selecionarAparencia(true);
    }

    private void selecionarAparencia(boolean segunda) {
        String classe = escolhaClasse.getValue();

        if (classe == null) {
            return;
        }

        String base = "";

        if (classe.equals("Bardo")) {
            base = "bardo";
        } else if (classe.equals("Mago")) {
            base = "mago";
        } else if (classe.equals("Druida")) {
            base = "druida";
        } else if (classe.equals("Bárbaro")) {
            base = "barbaro";
        }

        aparenciaSelecionada = segunda ? base + "2" : base;

        imagemAparencia1.setStyle("");
        imagemAparencia2.setStyle("");

        if (segunda) {
            imagemAparencia2.setStyle("-fx-effect: dropshadow(gaussian, #f0e68c, 15, 0.8, 0, 0);");
        } else {
            imagemAparencia1.setStyle("-fx-effect: dropshadow(gaussian, #f0e68c, 15, 0.8, 0, 0);");
        }
    }

    @FXML
    private void salvarPersonagem() throws IOException {
        String nome = campoNome.getText();
        String classe = escolhaClasse.getValue();

        if (nome.isEmpty() || classe == null || aparenciaSelecionada == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Preencha todos os campos e escolha uma aparência!");
            alert.showAndWait();
            return;
        }

        Personagem personagem = new Personagem(nome, classe, aparenciaSelecionada);

        GerenciadorPersonagens.adicionarPersonagem(personagem);

        App.setRoot("menuPersonagens");
    }

    @FXML
    private void voltar() throws IOException {
        App.setRoot("menuPersonagens");
    }
}
