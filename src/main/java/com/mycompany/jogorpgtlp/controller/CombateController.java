package com.mycompany.jogorpgtlp.controller;

import com.mycompany.jogorpgtlp.model.Personagem;
import com.mycompany.jogorpgtlp.model.SessaoJogo;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
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

    @FXML private Rectangle cartaGoblin1;
    @FXML private Rectangle cartaGoblin2;
    @FXML private Rectangle cartaGoblin3;
    @FXML private Rectangle cartaGoblin4;
    @FXML private Rectangle cartaGoblin5;

    @FXML private Rectangle cartaEspada;
    @FXML private Rectangle cartaCura;
    @FXML private Rectangle cartaEscudo;
    @FXML private Rectangle cartaDano;
    @FXML private Rectangle cartaCongelamento;

    private Image goblinFechado;
    private Image goblinAberto;

    private boolean trocar = false;

    private Personagem personagemAtual;

    private int vidaMaximaJogador;
    private int vidaJogador;
    private int vidaGoblin = 25;

    private int escudoJogador = 0;
    private int escudoGoblin = 0;

    private int turnosGoblinCongelado = 0;
    private int venenoJogador = 0;

    private boolean recompensaEntregue = false;

    private Random random = new Random();

    @FXML
    private void initialize() {
        personagemAtual = SessaoJogo.getPersonagemAtual();

        if (personagemAtual != null) {
            vidaMaximaJogador = personagemAtual.getVidaMaxima();
        } else {
            vidaMaximaJogador = 30;
        }

        vidaJogador = vidaMaximaJogador;

        goblinFechado = carregarImagem("goblinFechado.png");
        goblinAberto = carregarImagem("goblinAberto.png");

        goblin.setImage(goblinFechado);
        dialogo.setText("Goblin: Você entrou na minha mina...");

        atualizarVidas();

        Platform.runLater(() -> iniciarFala());
    }

    private void iniciarFala() {
        Timeline fala = new Timeline(
                new KeyFrame(
                        Duration.millis(277.5),
                        event -> {
                            if (trocar) {
                                goblin.setImage(goblinFechado);
                            } else {
                                goblin.setImage(goblinAberto);
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

        mapaCombate.setImage(carregarImagem("nivel1_sala2.png"));

        if (personagemAtual != null) {
            String aparencia = personagemAtual.getAparencia();
            playerCombate.setImage(carregarImagem(aparencia + "Costas.png"));
        }

        goblinCombate.setImage(carregarImagem("goblinFechado.png"));

        playerCombate.setFitWidth(goblinCombate.getFitWidth());
        playerCombate.setFitHeight(goblinCombate.getFitHeight());

        mapaCombate.setVisible(true);
        playerCombate.setVisible(true);
        goblinCombate.setVisible(true);

        vidaJogadorLabel.setVisible(true);
        vidaGoblinLabel.setVisible(true);

        mostrarCartas();
    }

    private void mostrarCartas() {
        cartaGoblin1.setVisible(true);
        cartaGoblin2.setVisible(true);
        cartaGoblin3.setVisible(true);
        cartaGoblin4.setVisible(true);
        cartaGoblin5.setVisible(true);

        cartaEspada.setVisible(true);
        cartaCura.setVisible(true);
        cartaEscudo.setVisible(true);
        cartaDano.setVisible(true);
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
        cartaDano.setVisible(false);
        cartaCongelamento.setVisible(false);
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
        cartaDano.setDisable(bloquear);
        cartaCongelamento.setDisable(bloquear);
    }

    private void esperarTurnoGoblin() {
        bloquearCartas(true);

        PauseTransition pausa = new PauseTransition(Duration.seconds(2));

        pausa.setOnFinished(event -> {
            turnoGoblin();

            PauseTransition pausaDepoisGoblin = new PauseTransition(Duration.seconds(2));
            pausaDepoisGoblin.setOnFinished(e -> {
                if (vidaJogador > 0 && vidaGoblin > 0) {
                    bloquearCartas(false);
                }
            });

            pausaDepoisGoblin.play();
        });

        pausa.play();
    }

    @FXML
    private void usarEspada() {
        mostrarMensagem("Você usou Espada!");

        int dano = 5;

        if (personagemAtual != null) {
            dano = personagemAtual.getDanoEspada();
        }

        causarDanoNoGoblin(dano);
        esperarTurnoGoblin();
    }

    @FXML
    private void usarCura() {
        mostrarMensagem("Você usou Cura!");

        int cura = 5;

        if (personagemAtual != null) {
            cura = personagemAtual.getValorCura();
        }

        vidaJogador += cura;

        if (vidaJogador > vidaMaximaJogador) {
            vidaJogador = vidaMaximaJogador;
        }

        atualizarVidas();
        verificarFim();
        esperarTurnoGoblin();
    }

    @FXML
    private void usarEscudo() {
        mostrarMensagem("Você usou Escudo!");

        if (personagemAtual != null) {
            escudoJogador = personagemAtual.getValorEscudo();
        } else {
            escudoJogador = 3;
        }

        esperarTurnoGoblin();
    }

    @FXML
    private void usarDano() {
        mostrarMensagem("Você usou Poção de Dano!");

        int dano = 8;

        if (personagemAtual != null) {
            dano = personagemAtual.getDanoPocao();
        }

        causarDanoNoGoblin(dano);
        esperarTurnoGoblin();
    }

    @FXML
    private void usarCongelamento() {
        mostrarMensagem("Você usou Congelamento!");

        if (personagemAtual != null) {
            turnosGoblinCongelado = personagemAtual.getTurnosCongelamento();
        } else {
            turnosGoblinCongelado = 1;
        }

        esperarTurnoGoblin();
    }

    private void causarDanoNoGoblin(int dano) {
        if (escudoGoblin > 0) {
            dano -= escudoGoblin;
            escudoGoblin = 0;

            if (dano < 0) {
                dano = 0;
            }
        }

        vidaGoblin -= dano;

        if (vidaGoblin < 0) {
            vidaGoblin = 0;
        }

        atualizarVidas();
        verificarFim();
    }

    private void causarDanoNoJogador(int dano) {
        if (escudoJogador > 0) {
            dano -= escudoJogador;
            escudoJogador = 0;

            if (dano < 0) {
                dano = 0;
            }
        }

        vidaJogador -= dano;

        if (vidaJogador < 0) {
            vidaJogador = 0;
        }

        atualizarVidas();
        verificarFim();
    }

    private void turnoGoblin() {
        if (vidaGoblin <= 0 || vidaJogador <= 0) {
            return;
        }

        aplicarVeneno();

        if (vidaJogador <= 0) {
            return;
        }

        if (turnosGoblinCongelado > 0) {
            mostrarMensagem("Goblin está congelado!");
            turnosGoblinCongelado--;
            return;
        }

        int sorteio = random.nextInt(100) + 1;

        if (sorteio <= 25) {
            mostrarMensagem("Goblin usou Flecha!");
            causarDanoNoJogador(4);

        } else if (sorteio <= 45) {
            mostrarMensagem("Goblin usou Cura!");

            vidaGoblin += 5;

            if (vidaGoblin > 25) {
                vidaGoblin = 25;
            }

            atualizarVidas();

        } else if (sorteio <= 65) {
            mostrarMensagem("Goblin usou Escudo!");
            escudoGoblin = 3;

        } else if (sorteio <= 85) {
            mostrarMensagem("Goblin usou Veneno!");
            venenoJogador = 3;

        } else {
            mostrarMensagem("Goblin usou Ataque Forte!");
            causarDanoNoJogador(7);
        }

        verificarFim();
    }

    private void aplicarVeneno() {
        if (venenoJogador > 0) {
            vidaJogador -= 2;
            venenoJogador--;

            mostrarMensagem("Veneno causou 2 de dano!");

            if (vidaJogador < 0) {
                vidaJogador = 0;
            }

            atualizarVidas();
            verificarFim();
        }
    }

    private void atualizarVidas() {
        vidaJogadorLabel.setText("Jogador: " + vidaJogador + "/" + vidaMaximaJogador);
        vidaGoblinLabel.setText("Goblin: " + vidaGoblin + "/25");
    }

    private void verificarFim() {
        if (vidaJogador <= 0) {
            esconderCartas();

            goblinCombate.setVisible(false);
            mensagemCombate.setVisible(false);
            premiosLabel.setVisible(false);

            dialogo.setText("Você perdeu!");
            dialogo.setVisible(true);

            bloquearCartas(true);
        }

        if (vidaGoblin <= 0) {
            if (!recompensaEntregue && personagemAtual != null) {
                personagemAtual.ganharPontoEvolucao();
                recompensaEntregue = true;
            }

            esconderCartas();

            goblinCombate.setVisible(false);
            mensagemCombate.setVisible(false);

            dialogo.setText("Você venceu!");
            dialogo.setVisible(true);

            premiosLabel.setText("+1 ponto de evolução");
            premiosLabel.setVisible(true);

            bloquearCartas(true);
        }
    }

    private Image carregarImagem(String nomeArquivo) {
        return new Image(
                getClass().getResourceAsStream("/assets/" + nomeArquivo)
        );
    }
}