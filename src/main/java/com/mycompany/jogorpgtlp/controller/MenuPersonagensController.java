package com.mycompany.jogorpgtlp.controller;

import com.mycompany.jogorpgtlp.App;
import com.mycompany.jogorpgtlp.model.Personagem;
import com.mycompany.jogorpgtlp.model.PersonagemDAO;
import com.mycompany.jogorpgtlp.model.ProgressoDAO;
import com.mycompany.jogorpgtlp.model.SessaoJogo;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private ObservableList<Personagem> personagens =
            FXCollections.observableArrayList();

    private PersonagemDAO personagemDAO = new PersonagemDAO();
    private ProgressoDAO progressoDAO = new ProgressoDAO();

    @FXML
    private void initialize() {
        carregarPersonagensBanco();

        listaPersonagens.setItems(personagens);

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

        configurarCelulas();
    }

    private void carregarPersonagensBanco() {
        try {
            ArrayList<Personagem> lista = personagemDAO.listarTodos();

            personagens.clear();
            personagens.addAll(lista);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar personagens do banco.");
            alert.showAndWait();

            e.printStackTrace();
        }
    }

    private void configurarCelulas() {
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
    try {
        personagemDAO.excluir(p.getId());
        personagens.remove(p);
    } catch (Exception ex) {
        Alert erro = new Alert(Alert.AlertType.ERROR);
        erro.setTitle("Erro");
        erro.setHeaderText(null);
        erro.setContentText("Erro ao excluir personagem do banco.");
        erro.showAndWait();

        ex.printStackTrace();
    }
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

        try {
            SessaoJogo.setPersonagemAtual(selecionado);

            progressoDAO.carregarProgresso(selecionado);

            App.setRoot("game");

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar progresso do personagem.");
            alert.showAndWait();

            e.printStackTrace();
        }
    }

    @FXML
    private void voltar() throws IOException {
        App.setRoot("telaInicial");
    }
}