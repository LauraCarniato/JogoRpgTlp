package com.mycompany.jogorpgtlp.controller;

import com.mycompany.jogorpgtlp.App;
import com.mycompany.jogorpgtlp.model.Combate;
import com.mycompany.jogorpgtlp.model.Personagem;
import com.mycompany.jogorpgtlp.model.SessaoJogo;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class CombateController {

    @FXML private ImageView goblin;
    @FXML private Label dialogo;

    @FXML private ImageView mapaCombate;
    @FXML private ImageView playerCombate;
    @FXML private ImageView goblinCombate;

    @FXML private Label vidaJogadorLabel;
    @FXML private Label vidaGoblinLabel;
    @FXML private Label mensagemCombate;
    @FXML private Label premiosLabel;

    @FXML private ImageView cartaGoblin1;
    @FXML private ImageView cartaGoblin2;
    @FXML private ImageView cartaGoblin3;
    @FXML private ImageView cartaGoblin4;
    @FXML private ImageView cartaGoblin5;

    @FXML private ImageView cartaEspada;
    @FXML private ImageView cartaCura;
    @FXML private ImageView cartaEscudo;
    @FXML private ImageView cartaFogo;
    @FXML private ImageView cartaCongelamento;

    private Image inimigoFechado;
    private Image inimigoAberto;

    private String nomeInimigo;
    private String imagemFechada;
    private String imagemAberta;
    private String falaInimigo;
    private String proximoMapa;

    private boolean trocar = false;
    private boolean esperandoY = false;

    private Personagem personagemAtual;
    private Combate combate;

    @FXML
    private void initialize() {
        personagemAtual = SessaoJogo.getPersonagemAtual();
        combate = new Combate(personagemAtual, SessaoJogo.getInimigoAtual());

        configurarInimigo();

        inimigoFechado = carregarImagem(imagemFechada);
        inimigoAberto = carregarImagem(imagemAberta);

        goblin.setImage(inimigoFechado);
        dialogo.setText(falaInimigo);

        atualizarVidas();

        Platform.runLater(() -> iniciarFala());
    }

    private void configurarInimigo() {
        String inimigo = SessaoJogo.getInimigoAtual();

        if (inimigo.equals("esqueleto")) {
            nomeInimigo = "Esqueleto";
            imagemFechada = "esqueletoFechado.png";
            imagemAberta = "esqueletoAberto.png";
            falaInimigo = "Esqueleto: Você é realmente forte...";
            proximoMapa = "nivel2_sala2.png";

        } else if (inimigo.equals("boss")) {
            nomeInimigo = "Boss";
            imagemFechada = "bossFechado.png";
            imagemAberta = "bossAberto.png";
            falaInimigo = "Boss: Você chegou longe demais...";
            proximoMapa = "nivel3_sala2.png";

        } else {
            nomeInimigo = "Goblin";
            imagemFechada = "goblinFechado.png";
            imagemAberta = "goblinAberto.png";
            falaInimigo = "Goblin: Você entrou na minha mina...";
            proximoMapa = "nivel1_sala2.png";
        }
    }

    private void iniciarFala() {
        Timeline fala = new Timeline(
                new KeyFrame(
                        Duration.millis(277.5),
                        event -> {
                            if (trocar) {
                                goblin.setImage(inimigoFechado);
                            } else {
                                goblin.setImage(inimigoAberto);
                            }

                            trocar = !trocar;
                        }
                )
        );

        fala.setCycleCount(8);
        fala.setOnFinished(event -> mostrarCenaCombate());
        fala.play();
    }

    private void mostrarCenaCombate() {
        goblin.setVisible(false);
        dialogo.setVisible(false);

        mapaCombate.setImage(carregarImagem(proximoMapa));

        if (personagemAtual != null) {
            String aparencia = personagemAtual.getAparencia();
            playerCombate.setImage(carregarImagem(aparencia + "Costas.png"));
        }

        goblinCombate.setImage(carregarImagem(imagemFechada));

        playerCombate.setFitWidth(goblinCombate.getFitWidth());
        playerCombate.setFitHeight(goblinCombate.getFitHeight());

        mapaCombate.setVisible(true);
        playerCombate.setVisible(true);
        goblinCombate.setVisible(true);

        vidaJogadorLabel.setVisible(true);
        vidaGoblinLabel.setVisible(true);

        mostrarCartas();
        configurarTeclaY();
    }

    private void mostrarCartas() {
        Image versoGoblin = carregarImagem("cartaCosta.png");

        cartaGoblin1.setImage(versoGoblin);
        cartaGoblin2.setImage(versoGoblin);
        cartaGoblin3.setImage(versoGoblin);
        cartaGoblin4.setImage(versoGoblin);
        cartaGoblin5.setImage(versoGoblin);

        cartaEspada.setImage(carregarImagem("cartaEspada.png"));
        cartaCura.setImage(carregarImagem("cartaCura.png"));
        cartaEscudo.setImage(carregarImagem("cartaEscudo.png"));
        cartaFogo.setImage(carregarImagem("cartaFogo.png"));
        cartaCongelamento.setImage(carregarImagem("cartaCongelamento.png"));

        cartaGoblin1.setVisible(true);
        cartaGoblin2.setVisible(true);
        cartaGoblin3.setVisible(true);
        cartaGoblin4.setVisible(true);
        cartaGoblin5.setVisible(true);

        cartaEspada.setVisible(true);
        cartaCura.setVisible(true);
        cartaEscudo.setVisible(true);
        cartaFogo.setVisible(true);
        cartaCongelamento.setVisible(true);
    }

    private void esconderCartas() {
        cartaGoblin1.setVisible(false);
        cartaGoblin2.setVisible(false);
        cartaGoblin3.setVisible(false);
        cartaGoblin4.setVisible(false);
        cartaGoblin5.setVisible(false);

        cartaEspada.setVisible(false);
        cartaCura.setVisible(false);
        cartaEscudo.setVisible(false);
        cartaFogo.setVisible(false);
        cartaCongelamento.setVisible(false);
    }

    private void atualizarVidas() {
        vidaJogadorLabel.setText(
                "Jogador: "
                + combate.getVidaJogador()
                + "/"
                + combate.getVidaMaximaJogador()
        );

        vidaGoblinLabel.setText(
                nomeInimigo
                + ": "
                + combate.getVidaGoblin()
                + "/"
                + combate.getVidaMaximaGoblin()
        );
    }

    private void mostrarMensagem(String texto) {
        mensagemCombate.setText(texto);
        mensagemCombate.setVisible(true);

        PauseTransition pausa = new PauseTransition(Duration.seconds(2));
        pausa.setOnFinished(event -> mensagemCombate.setVisible(false));
        pausa.play();
    }

    private void bloquearCartas(boolean bloquear) {
        cartaEspada.setDisable(bloquear);
        cartaCura.setDisable(bloquear);
        cartaEscudo.setDisable(bloquear);
        cartaFogo.setDisable(bloquear);
        cartaCongelamento.setDisable(bloquear);
    }

    private void esperarTurnoInimigo() {
        bloquearCartas(true);

        PauseTransition pausa = new PauseTransition(Duration.seconds(2));

        pausa.setOnFinished(event -> {
            combate.turnoGoblin();

            atualizarVidas();
            mostrarMensagem(combate.getUltimaMensagem());
            verificarFim();

            PauseTransition pausaDepois = new PauseTransition(Duration.seconds(2));

            pausaDepois.setOnFinished(e -> {
                if (!combate.acabou()) {
                    bloquearCartas(false);
                }
            });

            pausaDepois.play();
        });

        pausa.play();
    }

    @FXML
    private void usarEspada() {
        combate.usarEspada();

        atualizarVidas();
        mostrarMensagem(combate.getUltimaMensagem());
        verificarFim();

        if (!combate.acabou()) {
            esperarTurnoInimigo();
        }
    }

    @FXML
    private void usarCura() {
        combate.usarCura();

        atualizarVidas();
        mostrarMensagem(combate.getUltimaMensagem());
        verificarFim();

        if (!combate.acabou()) {
            esperarTurnoInimigo();
        }
    }

    @FXML
    private void usarEscudo() {
        combate.usarEscudo();

        atualizarVidas();
        mostrarMensagem(combate.getUltimaMensagem());
        verificarFim();

        if (!combate.acabou()) {
            esperarTurnoInimigo();
        }
    }

    @FXML
    private void usarFogo() {
        combate.usarFogo();

        atualizarVidas();
        mostrarMensagem(combate.getUltimaMensagem());
        verificarFim();

        if (!combate.acabou()) {
            esperarTurnoInimigo();
        }
    }

    @FXML
    private void usarCongelamento() {
        combate.usarCongelamento();

        atualizarVidas();
        mostrarMensagem(combate.getUltimaMensagem());
        verificarFim();

        if (!combate.acabou()) {
            esperarTurnoInimigo();
        }
    }

    private void verificarFim() {
        if (combate.jogadorPerdeu()) {
            esconderCartas();

            goblinCombate.setVisible(false);
            mensagemCombate.setVisible(false);
            premiosLabel.setVisible(false);

            dialogo.setText("Você perdeu!");
            dialogo.setVisible(true);
            dialogo.toFront();

            bloquearCartas(true);

            return;
        }

        if (combate.jogadorVenceu()) {
            combate.entregarRecompensa();

            esconderCartas();

            goblinCombate.setVisible(false);
            mensagemCombate.setVisible(false);

            dialogo.setText("Você venceu!");
            dialogo.setVisible(true);
            dialogo.toFront();

            premiosLabel.setText(
                    "+1 ponto de evolução\n\n"
                    + "Y para continuar e abrir/fechar menu"
            );

            premiosLabel.setVisible(true);
            premiosLabel.toFront();

            esperandoY = true;

            bloquearCartas(true);
            configurarTeclaY();
        }
    }

    private void configurarTeclaY() {
        Platform.runLater(() -> {
            Scene scene = mapaCombate.getScene();

            if (scene == null) {
                return;
            }

            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.Y && esperandoY) {
                    esperandoY = false;

                    try {
                        SessaoJogo.setMapaAtual(proximoMapa);
                        SessaoJogo.setPosicaoX(340);
                        SessaoJogo.setPosicaoY(320);

                        App.setRoot("game");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    event.consume();
                }
            });
        });
    }

    private Image carregarImagem(String nomeArquivo) {
        var input = getClass().getResourceAsStream("/assets/" + nomeArquivo);

        if (input == null) {
            throw new RuntimeException(
                    "Imagem não encontrada: /assets/" + nomeArquivo
            );
        }

        return new Image(input);
    }
}