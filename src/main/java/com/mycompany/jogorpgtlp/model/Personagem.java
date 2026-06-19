package com.mycompany.jogorpgtlp.model;

public class Personagem {

    private String nome;
    private String classe;
    private int nivel;
    private String aparencia;

    private int pontosDisponiveis;

    private int vida;
    private int espada;
    private int cura;
    private int escudo;
    private int dano;
    private int congelamento;

    public Personagem(String nome, String classe, String aparencia) {
        this.nome = nome;
        this.classe = classe;
        this.aparencia = aparencia;
        this.nivel = 0;

        this.pontosDisponiveis = 0;

        this.vida = 0;
        this.espada = 0;
        this.cura = 0;
        this.escudo = 0;
        this.dano = 0;
        this.congelamento = 0;
    }

    public String getNome() {
        return nome;
    }

    public String getClasse() {
        return classe;
    }

    public int getNivel() {
        return nivel;
    }

    public String getAparencia() {
        return aparencia;
    }

    public void subirNivel() {
        nivel++;
    }

    public int getPontosDisponiveis() {
        return pontosDisponiveis;
    }

    public void ganharPontoEvolucao() {
        pontosDisponiveis++;
    }

    public boolean gastarPonto(String atributo) {
        if (pontosDisponiveis <= 0) {
            return false;
        }

        if (atributo.equals("vida") && vida < 10) {
            vida++;
        } else if (atributo.equals("espada") && espada < 10) {
            espada++;
        } else if (atributo.equals("cura") && cura < 10) {
            cura++;
        } else if (atributo.equals("escudo") && escudo < 10) {
            escudo++;
        } else if (atributo.equals("dano") && dano < 10) {
            dano++;
        } else if (atributo.equals("congelamento") && congelamento < 10) {
            congelamento++;
        } else {
            return false;
        }

        pontosDisponiveis--;
        return true;
    }

    public int getVida() {
        return vida;
    }

    public int getEspada() {
        return espada;
    }

    public int getCura() {
        return cura;
    }

    public int getEscudo() {
        return escudo;
    }

    public int getDano() {
        return dano;
    }

    public int getCongelamento() {
        return congelamento;
    }

    public int getVidaMaxima() {
        return 30 + (vida * 5);
    }

    public int getDanoEspada() {
        return 5 + espada;
    }

    public int getValorCura() {
        return 5 + cura;
    }

    public int getValorEscudo() {
        return 3 + escudo;
    }

    public int getDanoPocao() {
        return 8 + dano;
    }

    public int getTurnosCongelamento() {
        if (congelamento >= 10) {
            return 3;
        }

        if (congelamento >= 5) {
            return 2;
        }

        return 1;
    }
}