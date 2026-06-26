package com.mycompany.jogorpgtlp.controller;

import com.mycompany.jogorpgtlp.App;
import com.mycompany.jogorpgtlp.model.Personagem;
import com.mycompany.jogorpgtlp.model.PersonagemDAO;
import com.mycompany.jogorpgtlp.model.ProgressoDAO;
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
    @FXML private Rectangle pedra4;
    @FXML private Rectangle pedra5;
    @FXML private Rectangle pedra6;
    @FXML private Rectangle pedra7;

    @FXML private Rectangle bau1;
    @FXML private Rectangle bau2;
    @FXML private Rectangle bau3;

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

    private Personagem personagemAtual;

    private PersonagemDAO personagemDAO = new PersonagemDAO();
    private ProgressoDAO progressoDAO = new ProgressoDAO();

    private Image imagemParado;
    private Image imagemAndando;

    private boolean cima;
    private boolean baixo;
    private boolean esquerda;
    private boolean direita;

    private boolean alternarImagem = false;
    private long ultimoTempoAnimacao = 0;

    private boolean bau1Aberto = false;
    private boolean bau2Aberto = false;
    private boolean bau3Aberto = false;

    private boolean mensagemTemporariaAtiva = false;

    private double velocidade = 3;

    @FXML
    private void initialize() {
        personagemAtual = SessaoJogo.getPersonagemAtual();

mapaFundo.setImage(
        carregarImagem(SessaoJogo.getMapaAtual())
);

posicionarObjetosMapa();
configurarColisoes();

if (SessaoJogo.temPosicaoSalva()) {
    player.setLayoutX(SessaoJogo.getPosicaoX());
    player.setLayoutY(SessaoJogo.getPosicaoY());
}

carregarImagemPersonagem();

atualizarMenuEvolucao();
configurarTeclado();
iniciarLoopJogo();
    }

    private void salvarProgresso() {
        if (personagemAtual == null) {
            return;
        }

        try {
            SessaoJogo.setPosicaoX(player.getLayoutX());
SessaoJogo.setPosicaoY(player.getLayoutY());

progressoDAO.salvarOuAtualizar(
        personagemAtual,
        player.getLayoutX(),
        player.getLayoutY()
);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarImagemPersonagem() {
        if (personagemAtual == null) {
            return;
        }

        String aparencia = personagemAtual.getAparencia();

        imagemParado = carregarImagem(aparencia + ".png");

        try {
            imagemAndando = carregarImagem(aparencia + "Andando.png");
        } catch (Exception e) {
            imagemAndando = imagemParado;
        }

        player.setImage(imagemParado);
    }

    private Image carregarImagem(String nomeArquivo) {
        return new Image(
                getClass().getResourceAsStream("/assets/" + nomeArquivo)
        );
    }

    private void posicionarObjetosMapa() {
        esconderObjetos();

        String mapa = SessaoJogo.getMapaAtual();

        if (mapa.equals("nivel1_sala1.png")) {
            configurarNivel1Sala1();

        } else if (mapa.equals("nivel1_sala2.png")) {
            configurarNivel1Sala2();

        } else if (mapa.equals("nivel2_sala1.png")) {
            configurarNivel2Sala1();

        } else if (mapa.equals("nivel2_sala2.png")) {
            configurarNivel2Sala2();
        }

        passagemPorta.setVisible(true);
    }
    
    private void esconderObjetos() {
        pedra1.setVisible(false);
        pedra2.setVisible(false);
        pedra3.setVisible(false);
        pedra4.setVisible(false);
        pedra5.setVisible(false);
        pedra6.setVisible(false);
        pedra7.setVisible(false);

        bau1.setVisible(false);
        bau2.setVisible(false);
        bau3.setVisible(false);
    }

    private void configurarNivel1Sala1() {
        mostrarPedra(pedra1, 200, 150, 65, 40);
        mostrarPedra(pedra2, 295, 213, 55, 35);
        mostrarPedra(pedra3, 475, 340, 30, 25);

        mostrarBau(bau1, 133, 100, 37, 33);
        mostrarBau(bau2, 435, 275, 40, 30);

        player.setLayoutX(360);
        player.setLayoutY(260);
    }

    private void configurarNivel1Sala2() {
        player.setLayoutX(340);
        player.setLayoutY(320);
    }

    private void configurarNivel2Sala1() {
        mostrarPedra(pedra1, 135, 125, 45, 45);
        mostrarPedra(pedra2, 150, 230, 45, 45);
        mostrarPedra(pedra3, 240, 180, 45, 45);
        mostrarPedra(pedra4, 430, 180, 45, 45);
        mostrarPedra(pedra5, 500, 90, 45, 45);
        mostrarPedra(pedra6, 410, 270, 45, 45);
        mostrarPedra(pedra7, 300, 350, 45, 45);

        mostrarBau(bau1, 175, 77, 37, 33);
        mostrarBau(bau2, 537, 220, 37, 33);
        mostrarBau(bau3, 160, 350, 37, 33);

        player.setLayoutX(330);
        player.setLayoutY(250);
    }

    private void configurarNivel2Sala2() {
        player.setLayoutX(340);
        player.setLayoutY(320);
    }

    private void mostrarPedra(
            Rectangle pedra,
            double x,
            double y,
            double largura,
            double altura
    ) {
        pedra.setVisible(true);
        pedra.setLayoutX(x);
        pedra.setLayoutY(y);
        pedra.setWidth(largura);
        pedra.setHeight(altura);
    }

    private void mostrarBau(
            Rectangle bau,
            double x,
            double y,
            double largura,
            double altura
    ) {
        bau.setVisible(true);
        bau.setLayoutX(x);
        bau.setLayoutY(y);
        bau.setWidth(largura);
        bau.setHeight(altura);
    }

    private void configurarColisoes() {
        colisoes.clear();

        colisoes.add(paredeCima);
        colisoes.add(paredeBaixo);
        colisoes.add(paredeEsquerda);
        colisoes.add(paredeDireita);

        if (pedra1.isVisible()) colisoes.add(pedra1);
        if (pedra2.isVisible()) colisoes.add(pedra2);
        if (pedra3.isVisible()) colisoes.add(pedra3);
        if (pedra4.isVisible()) colisoes.add(pedra4);
        if (pedra5.isVisible()) colisoes.add(pedra5);
        if (pedra6.isVisible()) colisoes.add(pedra6);
        if (pedra7.isVisible()) colisoes.add(pedra7);

        if (bau1.isVisible()) colisoes.add(bau1);
        if (bau2.isVisible()) colisoes.add(bau2);
        if (bau3.isVisible()) colisoes.add(bau3);
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

        if (bau1.isVisible()
                && !bau1Aberto
                && areaInteracao.getBoundsInParent().intersects(bau1.getBoundsInParent())) {
            bau1Aberto = true;
            ganharPontoBau();
            return;
        }

        if (bau2.isVisible()
                && !bau2Aberto
                && areaInteracao.getBoundsInParent().intersects(bau2.getBoundsInParent())) {
            bau2Aberto = true;
            ganharPontoBau();
            return;
        }

        if (bau3.isVisible()
                && !bau3Aberto
                && areaInteracao.getBoundsInParent().intersects(bau3.getBoundsInParent())) {
            bau3Aberto = true;
            ganharPontoBau();
        }
    }

    private void ganharPontoBau() {
        if (personagemAtual != null) {
            personagemAtual.ganharPontoEvolucao();
        }

        atualizarMenuEvolucao();
        salvarProgresso();

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

    @FXML
    private void uparVida() {
        gastarPonto("vida");
    }

    @FXML
    private void uparEspada() {
        gastarPonto("espada");
    }

    @FXML
    private void uparCura() {
        gastarPonto("cura");
    }

    @FXML
    private void uparEscudo() {
        gastarPonto("escudo");
    }

    @FXML
    private void uparDano() {
        gastarPonto("dano");
    }

    @FXML
    private void uparCongelamento() {
        gastarPonto("congelamento");
    }

    private void gastarPonto(String atributo) {
        if (personagemAtual == null) {
            return;
        }

        boolean gastou = personagemAtual.gastarPonto(atributo);

        if (gastou) {
            atualizarMenuEvolucao();
            salvarProgresso();
        }
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

                Rectangle hitbox = new Rectangle(
                        novoX + 20,
                        novoY + 50,
                        35,
                        25
                );

                boolean colidiu = false;

                for (Rectangle r : colisoes) {
                    if (hitbox.getBoundsInParent().intersects(r.getBoundsInParent())) {
                        colidiu = true;
                        break;
                    }
                }

                if (!colidiu) {
                    player.setLayoutX(novoX);
                    player.setLayoutY(novoY);
                }

                atualizarMensagemBau();

                if (hitbox.getBoundsInParent().intersects(passagemPorta.getBoundsInParent())) {
                    tratarPassagemPorta();
                    return;
                }

                animarPlayer(andando, now);
            }
        };

        timer.start();
    }

    private void tratarPassagemPorta() {
        String mapa = SessaoJogo.getMapaAtual();

        if (mapa.equals("nivel1_sala1.png")) {
            SessaoJogo.setInimigoAtual("goblin");
            salvarProgresso();
            irPara("combate");
            return;
        }

        if (mapa.equals("nivel1_sala2.png")) {
    SessaoJogo.setMapaAtual("nivel2_sala1.png");

    player.setLayoutX(330);
    player.setLayoutY(250);

    SessaoJogo.setPosicaoX(330);
    SessaoJogo.setPosicaoY(250);

    salvarProgresso();
    irPara("game");
    return;
}

        if (mapa.equals("nivel2_sala1.png")) {
            SessaoJogo.setInimigoAtual("esqueleto");
            salvarProgresso();
            irPara("combate");
            return;
        }

        if (mapa.equals("nivel2_sala2.png")) {
    System.out.println("Fim temporário do jogo");
    return;
}

        if (mapa.equals("nivel3_sala1.png")) {
            SessaoJogo.setInimigoAtual("boss");
            salvarProgresso();
            irPara("combate");
        }
    }

    private void irPara(String tela) {
        cima = false;
        baixo = false;
        esquerda = false;
        direita = false;

        if (timer != null) {
            timer.stop();
        }

        try {
            App.setRoot(tela);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void atualizarMensagemBau() {
        if (mensagemTemporariaAtiva) {
            return;
        }

        Rectangle area = new Rectangle(
                player.getLayoutX() + 5,
                player.getLayoutY() + 20,
                70,
                70
        );

        boolean perto = false;

        if (bau1.isVisible()
                && !bau1Aberto
                && area.getBoundsInParent().intersects(bau1.getBoundsInParent())) {
            perto = true;
        }

        if (bau2.isVisible()
                && !bau2Aberto
                && area.getBoundsInParent().intersects(bau2.getBoundsInParent())) {
            perto = true;
        }

        if (bau3.isVisible()
                && !bau3Aberto
                && area.getBoundsInParent().intersects(bau3.getBoundsInParent())) {
            perto = true;
        }

        mensagemAcao.setVisible(perto);

        if (perto) {
            mensagemAcao.setText("TAB — Abrir");
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