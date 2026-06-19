package com.mycompany.jogorpgtlp.model;

public class SessaoJogo {

    private static Personagem personagemAtual;

    public static void setPersonagemAtual(
            Personagem p
    ) {

        personagemAtual = p;
    }

    public static Personagem getPersonagemAtual() {

        return personagemAtual;
    }
}
