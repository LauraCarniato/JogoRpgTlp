package com.mycompany.jogorpgtlp.model;

public class SessaoJogo {

    private static Personagem personagemAtual;

    private static String mapaAtual = "nivel1_sala1.png";
    private static String inimigoAtual = "goblin";

    private static double posicaoX = 360;
    private static double posicaoY = 260;
    private static boolean temPosicaoSalva = false;

    public static void setPersonagemAtual(Personagem personagem) {
        personagemAtual = personagem;
    }

    public static Personagem getPersonagemAtual() {
        return personagemAtual;
    }

    public static String getMapaAtual() {
        return mapaAtual;
    }

    public static void setMapaAtual(String mapa) {
        mapaAtual = mapa;
    }

    public static String getInimigoAtual() {
        return inimigoAtual;
    }

    public static void setInimigoAtual(String inimigo) {
        inimigoAtual = inimigo;
    }

    public static double getPosicaoX() {
        return posicaoX;
    }

    public static void setPosicaoX(double x) {
        posicaoX = x;
        temPosicaoSalva = true;
    }

    public static double getPosicaoY() {
        return posicaoY;
    }

    public static void setPosicaoY(double y) {
        posicaoY = y;
        temPosicaoSalva = true;
    }

    public static boolean temPosicaoSalva() {
        return temPosicaoSalva;
    }

    public static void limparPosicaoSalva() {
        temPosicaoSalva = false;
    }
}