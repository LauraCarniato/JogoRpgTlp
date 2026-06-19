package com.mycompany.jogorpgtlp.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GerenciadorPersonagens {

    private static ObservableList<Personagem>
            personagens =
            FXCollections.observableArrayList();

    public static ObservableList<Personagem>
            getPersonagens() {

        return personagens;
    }

    public static void adicionarPersonagem(
            Personagem personagem
    ) {

        personagens.add(personagem);
    }

    public static void removerPersonagem(
            Personagem personagem
    ) {

        personagens.remove(personagem);
    }
}
