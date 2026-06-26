package com.mycompany.jogorpgtlp.model;

import java.sql.Connection;

public class TesteConexao {

    public static void main(String[] args) {

        try (Connection conexao = ConexaoBD.conectar()) {

            System.out.println("================================");
            System.out.println(" CONEXÃO REALIZADA COM SUCESSO ");
            System.out.println("================================");

        } catch (Exception e) {

            System.out.println("Erro ao conectar:");
            e.printStackTrace();

        }

    }

}
