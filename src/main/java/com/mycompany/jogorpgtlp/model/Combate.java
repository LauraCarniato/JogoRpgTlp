package com.mycompany.jogorpgtlp.model;

import java.util.Random;

public class Combate {

    private Personagem jogador;
    private String inimigo;

    private int vidaMaximaJogador;
    private int vidaJogador;

    private int vidaMaximaInimigo;
    private int vidaInimigo;

    private int escudoJogador;
    private int escudoInimigo;

    private int turnosInimigoCongelado;
    private int venenoJogador;

    private boolean recompensaEntregue;
    private String ultimaMensagem;

    private Random random = new Random();

    public Combate(Personagem jogador, String inimigo) {
        this.jogador = jogador;
        this.inimigo = inimigo;

        vidaMaximaJogador = jogador != null ? jogador.getVidaMaxima() : 30;
        vidaJogador = vidaMaximaJogador;

        if (inimigo.equals("esqueleto")) {
            vidaMaximaInimigo = 35;
        } else {
            vidaMaximaInimigo = 25;
        }

        vidaInimigo = vidaMaximaInimigo;
    }

    public void usarEspada() {
        int dano = jogador != null ? jogador.getDanoEspada() : 5;
        ultimaMensagem = "Você usou Espada!";
        causarDanoNoInimigo(dano);
    }

    public void usarCura() {
        int cura = jogador != null ? jogador.getValorCura() : 5;

        vidaJogador += cura;
        if (vidaJogador > vidaMaximaJogador) {
            vidaJogador = vidaMaximaJogador;
        }

        ultimaMensagem = "Você usou Cura!";
    }

    public void usarEscudo() {
        escudoJogador = jogador != null ? jogador.getValorEscudo() : 3;
        ultimaMensagem = "Você usou Escudo!";
    }

    public void usarFogo() {
        int dano = jogador != null ? jogador.getDanoPocao() : 8;
        ultimaMensagem = "Você usou Bola de Fogo!";
        causarDanoNoInimigo(dano);
    }

    public void usarCongelamento() {
        turnosInimigoCongelado = jogador != null ? jogador.getTurnosCongelamento() : 1;
        ultimaMensagem = "Você usou Congelamento!";
    }

    public void turnoGoblin() {
        if (acabou()) return;

        aplicarVeneno();

        if (acabou()) return;

        if (turnosInimigoCongelado > 0) {
            turnosInimigoCongelado--;

            if (inimigo.equals("esqueleto")) {
                ultimaMensagem = "Esqueleto está congelado!";
            } else {
                ultimaMensagem = "Goblin está congelado!";
            }

            return;
        }

        if (inimigo.equals("esqueleto")) {
            turnoEsqueleto();
        } else {
            turnoGoblinNormal();
        }
    }

    private void turnoGoblinNormal() {
        int sorteio = random.nextInt(100) + 1;

        if (sorteio <= 25) {
            ultimaMensagem = "Goblin usou Flecha!";
            causarDanoNoJogador(4);

        } else if (sorteio <= 45) {
            ultimaMensagem = "Goblin usou Cura!";
            curarInimigo(5);

        } else if (sorteio <= 65) {
            ultimaMensagem = "Goblin usou Escudo!";
            escudoInimigo = 3;

        } else if (sorteio <= 85) {
            ultimaMensagem = "Goblin usou Veneno!";
            venenoJogador = 3;

        } else {
            ultimaMensagem = "Goblin usou Ataque Forte!";
            causarDanoNoJogador(7);
        }
    }

    private void turnoEsqueleto() {
        int sorteio = random.nextInt(100) + 1;

        if (sorteio <= 25) {
            ultimaMensagem = "Esqueleto usou Corte Enferrujado!";
            causarDanoNoJogador(6);

        } else if (sorteio <= 45) {
            ultimaMensagem = "Esqueleto usou Escudo de Ossos!";
            escudoInimigo = 5;

        } else if (sorteio <= 65) {
            ultimaMensagem = "Esqueleto usou Osso Arremessado!";
            causarDanoNoJogador(5);

        } else if (sorteio <= 85) {
            ultimaMensagem = "Esqueleto usou Maldição!";
            venenoJogador = 4;

        } else {
            ultimaMensagem = "Esqueleto usou Golpe Sombrio!";
            causarDanoNoJogador(9);
        }
    }

    private void aplicarVeneno() {
        if (venenoJogador > 0) {
            vidaJogador -= 2;
            venenoJogador--;

            if (vidaJogador < 0) {
                vidaJogador = 0;
            }

            if (inimigo.equals("esqueleto")) {
                ultimaMensagem = "Maldição causou 2 de dano!";
            } else {
                ultimaMensagem = "Veneno causou 2 de dano!";
            }
        }
    }

    private void causarDanoNoInimigo(int dano) {
        if (escudoInimigo > 0) {
            dano -= escudoInimigo;
            escudoInimigo = 0;

            if (dano < 0) {
                dano = 0;
            }
        }

        vidaInimigo -= dano;

        if (vidaInimigo < 0) {
            vidaInimigo = 0;
        }
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
    }

    private void curarInimigo(int cura) {
        vidaInimigo += cura;

        if (vidaInimigo > vidaMaximaInimigo) {
            vidaInimigo = vidaMaximaInimigo;
        }
    }

    public void entregarRecompensa() {
        if (!recompensaEntregue && jogador != null) {
            jogador.ganharPontoEvolucao();
            recompensaEntregue = true;
        }
    }

    public boolean jogadorVenceu() {
        return vidaInimigo <= 0;
    }

    public boolean jogadorPerdeu() {
        return vidaJogador <= 0;
    }

    public boolean acabou() {
        return jogadorVenceu() || jogadorPerdeu();
    }

    public int getVidaJogador() {
        return vidaJogador;
    }

    public int getVidaMaximaJogador() {
        return vidaMaximaJogador;
    }

    public int getVidaGoblin() {
        return vidaInimigo;
    }

    public int getVidaMaximaGoblin() {
        return vidaMaximaInimigo;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }
}
