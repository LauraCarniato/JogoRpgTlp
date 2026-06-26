package com.mycompany.jogorpgtlp.controller;

import com.mycompany.jogorpgtlp.App;
import com.mycompany.jogorpgtlp.model.Personagem;
import com.mycompany.jogorpgtlp.model.PersonagemDAO;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TelaInicialController {

    @FXML
    private ImageView capa;

    @FXML
    public void initialize() {

        capa.setImage(
                new Image(
                        getClass().getResourceAsStream("/assets/capa.png")
                )
        );
    }

    @FXML
    private void irParaMenuPersonagens() throws IOException {
        App.setRoot("menuPersonagens");
    }

    @FXML
    private void encerrar() {
        Platform.exit();
    }

    @FXML
    private void mostrarCreditos() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Créditos");
        alert.setHeaderText("O Filho Perdido");

        alert.setContentText(
                "Apresentação\n\n"
                + "Jogo desenvolvido por \n" +
"Laura Carniato & Melissa Gauna\n" +
"\n" +
"\n" +
"Game Design\n" +
"Laura Carniato & Melissa Gauna\n" +
"\n" +
"\n" +
"Roteiro\n" +
"Laura Carniato & Melissa Gauna\n" +
"\n" +
"\n" +
"Ferramentas Utilizadas\n" +
"Ferramentas de IA (ChatGpt)\n" +
"Piskel\n" +
"\n" +
"\n" +
"Aviso sobre IA\n" +
"Este jogo utilizou ferramentas de IA (ChatGpt) como auxílio durante o processo de desenvolvimento e recursos visuais. Todo conteúdo foi revisado e integrado pelos desenvolvedores. \n" +
"\n" +
"\n" +
"Agradecimentos\n" +
"A todos que apoiaram este projeto e dedicaram seu tempo para explorá-lo.\n" +
"\n" +
"\n" +
"Copyright\n" +
"© 2026 Laura Carniato & Melissa Gauna. Todos os direitos reservados.\n\n"
                
        );

        alert.showAndWait();
    }

    @FXML
    private void mostrarRanking() {

        try {

            PersonagemDAO dao = new PersonagemDAO();

            ArrayList<Personagem> lista = dao.listarTodos();

            lista.sort((a, b) -> Integer.compare(
                    calcularPontuacao(b),
                    calcularPontuacao(a)
            ));

            StringBuilder texto = new StringBuilder();

            int posicao = 1;

            for (Personagem p : lista) {

                texto.append(posicao++)
                        .append("º - ")
                        .append(p.getNome())
                        .append(" (")
                        .append(calcularPontuacao(p))
                        .append(" pontos)\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Ranking");
            alert.setHeaderText("Ranking dos Heróis");

            if (texto.length() == 0) {
                texto.append("Nenhum personagem encontrado.");
            }

            alert.setContentText(texto.toString());

            alert.showAndWait();

        } catch (Exception e) {

            Alert erro = new Alert(Alert.AlertType.ERROR);

            erro.setTitle("Erro");
            erro.setHeaderText(null);
            erro.setContentText("Erro ao carregar ranking.");

            erro.showAndWait();

            e.printStackTrace();
        }
    }

    private int calcularPontuacao(Personagem p) {

        return p.getPontosDisponiveis()
                + p.getVida()
                + p.getEspada()
                + p.getCura()
                + p.getEscudo()
                + p.getDano()
                + p.getCongelamento();
    }
}