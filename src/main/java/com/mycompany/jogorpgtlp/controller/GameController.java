package com.mycompany.jogorpgtlp.controller;

import com.mycompany.jogorpgtlp.App;
import com.mycompany.jogorpgtlp.model.Personagem;
import com.mycompany.jogorpgtlp.model.SessaoJogo;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameController {

    @FXML private AnchorPane paneJogo;
    @FXML private ImageView mapaFundo;
    @FXML private ImageView player;
    @FXML private Label mensagemAcao;

    @FXML private Rectangle pedra1;
    @FXML private Rectangle pedra2;
    @FXML private Rectangle pedra3;
    @FXML private Rectangle bau1;
    @FXML private Rectangle bau2;
    @FXML private Rectangle passagemPorta;

    @FXML private Rectangle paredeCima;
    @FXML private Rectangle paredeBaixo;
    @FXML private Rectangle paredeEsquerda;
    @FXML private Rectangle paredeDireita;

    @FXML private AnchorPane menuEvolucao;
    @FXML private Label labelPontos;
    @FXML private Button btnVida;
    @FXML private Button btnEspada;
    @FXML private Button btnCura;
    @FXML private Button btnEscudo;
    @FXML private Button btnDano;
    @FXML private Button btnCongelamento;

    private ArrayList<Rectangle> colisoes = new ArrayList<>();
    private AnimationTimer timer;

    private double velocidade = 3;

    private boolean cima;
    private boolean baixo;
    private boolean esquerda;
    private boolean direita;

    private boolean bau1Aberto = false;
    private boolean bau2Aberto = false;
    private boolean mensagemTemporariaAtiva = false;

    private Image imagemParado;
    private Image imagemAndando;

    private boolean alternarImagem = false;
    private long ultimoTempoAnimacao = 0;

    private Personagem personagemAtual;

    @FXML
    private void initialize() {
        mapaFundo.setImage(carregarImagem("nivel1_sala1.png"));

        colisoes.add(pedra1);
        colisoes.add(pedra2);
        colisoes.add(pedra3);
        colisoes.add(bau1);
        colisoes.add(bau2);
        colisoes.add(paredeCima);
        colisoes.add(paredeBaixo);
        colisoes.add(paredeEsquerda);
        colisoes.add(paredeDireita);

        personagemAtual = SessaoJogo.getPersonagemAtual();

        if (personagemAtual != null) {
            String aparencia = personagemAtual.getAparencia();

            imagemParado = carregarImagem(aparencia + ".png");

            try {
                imagemAndando = carregarImagem(aparencia + "Andando.png");
            } catch (Exception e) {
                imagemAndando = imagemParado;
            }

            player.setImage(imagemParado);
        }

        atualizarMenuEvolucao();
        configurarTeclado();
        iniciarLoopJogo();
    }

    private Image carregarImagem(String nomeArquivo) {
        return new Image(
                getClass().getResourceAsStream("/assets/" + nomeArquivo)
        );
    }

    private void configurarTeclado() {
        paneJogo.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Scene scene = newScene;

                scene.setOnKeyPressed(event -> {

                    if (event.getCode() == KeyCode.Y) {
                        abrirFecharMenuEvolucao();
                        event.consume();
                        return;
                    }

                    if (menuEvolucao.isVisible()) {
                        return;
                    }

                    if (event.getCode() == KeyCode.TAB) {
                        tentarAbrirBau();
                        event.consume();
                        return;
                    }

                    if (event.getCode() == KeyCode.W) cima = true;
                    if (event.getCode() == KeyCode.S) baixo = true;

                    if (event.getCode() == KeyCode.A) {
                        esquerda = true;
                        player.setScaleX(-1);
                    }

                    if (event.getCode() == KeyCode.D) {
                        direita = true;
                        player.setScaleX(1);
                    }
                });

                scene.setOnKeyReleased(event -> {
                    if (event.getCode() == KeyCode.W) cima = false;
                    if (event.getCode() == KeyCode.S) baixo = false;
                    if (event.getCode() == KeyCode.A) esquerda = false;
                    if (event.getCode() == KeyCode.D) direita = false;
                });
            }
        });
    }

    private void tentarAbrirBau() {
        Rectangle areaInteracao = new Rectangle(
                player.getLayoutX() + 5,
                player.getLayoutY() + 20,
                70,
                70
        );

        if (!bau1Aberto && areaInteracao.getBoundsInParent().intersects(bau1.getBoundsInParent())) {
            bau1Aberto = true;
            ganharPontoBau();
            return;
        }

        if (!bau2Aberto && areaInteracao.getBoundsInParent().intersects(bau2.getBoundsInParent())) {
            bau2Aberto = true;
            ganharPontoBau();
        }
    }

    private void ganharPontoBau() {
        if (personagemAtual != null) {
            personagemAtual.ganharPontoEvolucao();
        }

        atualizarMenuEvolucao();

        mostrarMensagemTemporaria(
                "Você encontrou um baú!\n+1 ponto de evolução"
        );
    }

    private void mostrarMensagemTemporaria(String texto) {
        mensagemTemporariaAtiva = true;

        mensagemAcao.setText(texto);
        mensagemAcao.setVisible(true);
        mensagemAcao.setLayoutX(player.getLayoutX() + 95);
        mensagemAcao.setLayoutY(player.getLayoutY() - 45);

        PauseTransition pausa = new PauseTransition(Duration.seconds(2));
        pausa.setOnFinished(event -> {
            mensagemTemporariaAtiva = false;
            mensagemAcao.setText("TAB — Abrir");
            mensagemAcao.setVisible(false);
        });

        pausa.play();
    }

    private void abrirFecharMenuEvolucao() {
        boolean aberto = menuEvolucao.isVisible();

        menuEvolucao.setVisible(!aberto);

        cima = false;
        baixo = false;
        esquerda = false;
        direita = false;

        atualizarMenuEvolucao();
    }

    private void atualizarMenuEvolucao() {
        if (personagemAtual == null) {
            return;
        }

        labelPontos.setText("Pontos: " + personagemAtual.getPontosDisponiveis());

        btnVida.setText("Vida " + personagemAtual.getVida() + "/10");
        btnEspada.setText("Espada " + personagemAtual.getEspada() + "/10");
        btnCura.setText("Cura " + personagemAtual.getCura() + "/10");
        btnEscudo.setText("Escudo " + personagemAtual.getEscudo() + "/10");
        btnDano.setText("Dano " + personagemAtual.getDano() + "/10");
        btnCongelamento.setText("Congelamento " + personagemAtual.getCongelamento() + "/10");
    }

    @FXML private void uparVida() { gastarPonto("vida"); }
    @FXML private void uparEspada() { gastarPonto("espada"); }
    @FXML private void uparCura() { gastarPonto("cura"); }
    @FXML private void uparEscudo() { gastarPonto("escudo"); }
    @FXML private void uparDano() { gastarPonto("dano"); }
    @FXML private void uparCongelamento() { gastarPonto("congelamento"); }

    private void gastarPonto(String atributo) {
        if (personagemAtual == null) {
            return;
        }

        personagemAtual.gastarPonto(atributo);
        atualizarMenuEvolucao();
    }

    private void iniciarLoopJogo() {
        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                if (menuEvolucao.isVisible()) {
                    animarPlayer(false, now);
                    return;
                }

                double novoX = player.getLayoutX();
                double novoY = player.getLayoutY();

                boolean andando = false;

                if (cima) {
                    novoY -= velocidade;
                    andando = true;
                }

                if (baixo) {
                    novoY += velocidade;
                    andando = true;
                }

                if (esquerda) {
                    novoX -= velocidade;
                    andando = true;
                }

                if (direita) {
                    novoX += velocidade;
                    andando = true;
                }

                Rectangle hitboxPlayer = new Rectangle(
                        novoX + 20,
                        novoY + 50,
                        35,
                        25
                );

                boolean colidiu = false;

                for (Rectangle colisao : colisoes) {
                    if (hitboxPlayer.getBoundsInParent().intersects(colisao.getBoundsInParent())) {
                        colidiu = true;
                        break;
                    }
                }

                if (!colidiu) {
                    player.setLayoutX(novoX);
                    player.setLayoutY(novoY);
                }

                atualizarMensagemBau();

                boolean pertoPorta =
                        hitboxPlayer.getBoundsInParent()
                                .intersects(passagemPorta.getBoundsInParent());

                if (pertoPorta) {
                    cima = false;
                    baixo = false;
                    esquerda = false;
                    direita = false;

                    if (timer != null) {
                        timer.stop();
                    }

                    try {
                        App.setRoot("combate");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return;
                }

                animarPlayer(andando, now);
            }
        };

        timer.start();
    }

    private void atualizarMensagemBau() {
        if (mensagemTemporariaAtiva) {
            return;
        }

        Rectangle areaInteracao = new Rectangle(
                player.getLayoutX() + 5,
                player.getLayoutY() + 20,
                70,
                70
        );

        boolean pertoBauFechado =
                (!bau1Aberto && areaInteracao.getBoundsInParent().intersects(bau1.getBoundsInParent()))
                ||
                (!bau2Aberto && areaInteracao.getBoundsInParent().intersects(bau2.getBoundsInParent()));

        mensagemAcao.setVisible(pertoBauFechado);
        mensagemAcao.setText("TAB — Abrir");

        if (pertoBauFechado) {
            mensagemAcao.setLayoutX(player.getLayoutX() + 95);
            mensagemAcao.setLayoutY(player.getLayoutY() - 35);
        }
    }

    private void animarPlayer(boolean andando, long now) {
        if (andando) {
            if (now - ultimoTempoAnimacao > 200_000_000) {
                alternarImagem = !alternarImagem;

                if (alternarImagem) {
                    player.setImage(imagemAndando);
                } else {
                    player.setImage(imagemParado);
                }

                ultimoTempoAnimacao = now;
            }
        } else {
            player.setImage(imagemParado);
        }
    }
}