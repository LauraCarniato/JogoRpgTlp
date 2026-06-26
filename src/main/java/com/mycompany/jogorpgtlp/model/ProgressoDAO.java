package com.mycompany.jogorpgtlp.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProgressoDAO {

    public void salvarOuAtualizar(Personagem personagem, double x, double y) throws Exception {
        String sql =
                "INSERT INTO progresso (personagem_id, mapa_atual, inimigo_atual, posicao_x, posicao_y) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT (personagem_id) DO UPDATE SET " +
                "mapa_atual = EXCLUDED.mapa_atual, " +
                "inimigo_atual = EXCLUDED.inimigo_atual, " +
                "posicao_x = EXCLUDED.posicao_x, " +
                "posicao_y = EXCLUDED.posicao_y";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personagem.getId());
            stmt.setString(2, SessaoJogo.getMapaAtual());
            stmt.setString(3, SessaoJogo.getInimigoAtual());
            stmt.setDouble(4, x);
            stmt.setDouble(5, y);

            stmt.executeUpdate();
        }
    }

    public void carregarProgresso(Personagem personagem) throws Exception {
        String sql =
                "SELECT mapa_atual, inimigo_atual, posicao_x, posicao_y " +
                "FROM progresso " +
                "WHERE personagem_id = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personagem.getId());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                SessaoJogo.setMapaAtual(rs.getString("mapa_atual"));
                SessaoJogo.setInimigoAtual(rs.getString("inimigo_atual"));
                SessaoJogo.setPosicaoX(rs.getDouble("posicao_x"));
                SessaoJogo.setPosicaoY(rs.getDouble("posicao_y"));
            }
        }
    }
}