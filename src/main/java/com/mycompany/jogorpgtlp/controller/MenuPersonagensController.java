package com.mycompany.jogorpgtlp.controller;

import com.mycompany.jogorpgtlp.App;
import com.mycompany.jogorpgtlp.model.GerenciadorPersonagens;
import com.mycompany.jogorpgtlp.model.Personagem;
import com.mycompany.jogorpgtlp.model.SessaoJogo;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class MenuPersonagensController {

    @FXML
    private ListView<Personagem> listaPersonagens;

    @FXML
    private void initialize() {
        listaPersonagens.setItems(GerenciadorPersonagens.getPersonagens());

        listaPersonagens.setStyle(
                "-fx-control-inner-background: #A52A2A;" +
                "-fx-background-color: #A52A2A;"
        );

        listaPersonagens.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                try {
                    entrarNoJogo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        listaPersonagens.setCellFactory(param -> new ListCell<Personagem>() {
            @Override
            protected void updateItem(Personagem p, boolean empty) {
                super.updateItem(p, empty);

                if (empty || p == null) {
                    setGraphic(null);
                    setStyle("");
                    return;
                }

                Label nome = new Label(
                        p.getNome() + " - Nv " + p.getNivel() + " (" + p.getClasse() + ")"
                );

                nome.setStyle(
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;"
                );

                Button excluir = new Button("X");

                excluir.setStyle(
                        "-fx-background-color: #8B0000;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;"
                );

                excluir.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Excluir");
                    alert.setHeaderText(null);
                    alert.setContentText("Tem certeza que deseja excluir esse mundo?");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            GerenciadorPersonagens.removerPersonagem(p);
                        }
                    });
                });

                Region espaco = new Region();
                HBox.setHgrow(espaco, Priority.ALWAYS);

                HBox linha = new HBox(10, nome, espaco, excluir);
                linha.setAlignment(Pos.CENTER_LEFT);

                if (isSelected()) {
                    linha.setStyle(
                            "-fx-background-color: #404040;" +
                            "-fx-border-color: #f0e68c;" +
                            "-fx-border-width: 3;" +
                            "-fx-padding: 10;"
                    );
                } else if (getIndex() % 2 == 0) {
                    linha.setStyle(
                            "-fx-background-color: #696969;" +
                            "-fx-padding: 10;"
                    );
                } else {
                    linha.setStyle(
                            "-fx-background-color: #404040;" +
                            "-fx-padding: 10;"
                    );
                }

                setGraphic(linha);
                setStyle("-fx-background-color: transparent;");
            }
        });
    }

    @FXML
    private void criarPersonagem() throws IOException {
        App.setRoot("criarPersonagem");
    }

    @FXML
    private void entrarNoJogo() throws IOException {
        Personagem selecionado = listaPersonagens
                .getSelectionModel()
                .getSelectedItem();

        if (selecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Selecione um personagem primeiro!");
            alert.showAndWait();
            return;
        }

        SessaoJogo.setPersonagemAtual(selecionado);
        App.setRoot("game");
    }

    @FXML
    private void voltar() throws IOException {
        App.setRoot("telaInicial");
    }
}