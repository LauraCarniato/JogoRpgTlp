package com.mycompany.jogorpgtlp.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PersonagemDAO {

    public int salvar(Personagem p) throws Exception {
        String sql =
                "INSERT INTO personagem " +
                "(nome, classe, aparencia, pontos_disponiveis, vida, espada, cura, escudo, dano, congelamento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING id";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getClasse());
            stmt.setString(3, p.getAparencia());
            stmt.setInt(4, p.getPontosDisponiveis());
            stmt.setInt(5, p.getVida());
            stmt.setInt(6, p.getEspada());
            stmt.setInt(7, p.getCura());
            stmt.setInt(8, p.getEscudo());
            stmt.setInt(9, p.getDano());
            stmt.setInt(10, p.getCongelamento());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                p.setId(id);
                return id;
            }

            return 0;
        }
    }

    public void atualizar(Personagem p) throws Exception {
        String sql =
                "UPDATE personagem SET " +
                "nome = ?, " +
                "classe = ?, " +
                "aparencia = ?, " +
                "pontos_disponiveis = ?, " +
                "vida = ?, " +
                "espada = ?, " +
                "cura = ?, " +
                "escudo = ?, " +
                "dano = ?, " +
                "congelamento = ? " +
                "WHERE id = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getClasse());
            stmt.setString(3, p.getAparencia());
            stmt.setInt(4, p.getPontosDisponiveis());
            stmt.setInt(5, p.getVida());
            stmt.setInt(6, p.getEspada());
            stmt.setInt(7, p.getCura());
            stmt.setInt(8, p.getEscudo());
            stmt.setInt(9, p.getDano());
            stmt.setInt(10, p.getCongelamento());
            stmt.setInt(11, p.getId());

            stmt.executeUpdate();
        }
    }

    public Personagem buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM personagem WHERE id = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return montarPersonagem(rs);
            }

            return null;
        }
    }

    public Personagem buscarUltimo() throws Exception {
        String sql = "SELECT * FROM personagem ORDER BY id DESC LIMIT 1";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return montarPersonagem(rs);
            }

            return null;
        }
    }
    
    public java.util.ArrayList<Personagem> listarTodos() throws Exception {

    java.util.ArrayList<Personagem> lista = new java.util.ArrayList<>();

    String sql = "SELECT * FROM personagem ORDER BY id";

    try (Connection conn = ConexaoBD.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            lista.add(montarPersonagem(rs));
        }
    }

    return lista;
}
    
    public void excluir(int id) throws Exception {
    String sql = "DELETE FROM personagem WHERE id = ?";

    try (Connection conn = ConexaoBD.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}

    private Personagem montarPersonagem(ResultSet rs) throws Exception {
        Personagem p = new Personagem(
                rs.getString("nome"),
                rs.getString("classe"),
                rs.getString("aparencia")
        );

        p.setId(rs.getInt("id"));
        p.setPontosDisponiveis(rs.getInt("pontos_disponiveis"));
        p.setVida(rs.getInt("vida"));
        p.setEspada(rs.getInt("espada"));
        p.setCura(rs.getInt("cura"));
        p.setEscudo(rs.getInt("escudo"));
        p.setDano(rs.getInt("dano"));
        p.setCongelamento(rs.getInt("congelamento"));

        return p;
    }
}