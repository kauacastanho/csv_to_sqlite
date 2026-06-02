package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogadorDAO {
    private Connection conexao;

    public JogadorDAO(String url) throws SQLException {
        conexao = DriverManager.getConnection(url);
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS jogador (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome VARCHAR(80) UNIQUE NOT NULL," +
                "posicao VARCHAR(80) NOT NULL," +
                "clube VARCHAR(80) NOT NULL," +
                "camisa INTEGER UNIQUE NOT NULL)";
        try (Statement stmt = conexao.createStatement()) {
            stmt.execute(sql);
        }
    }

    public boolean verificaSeJaExiste(String nome) throws SQLException {
        String sql = "SELECT 1 FROM jogador WHERE nome = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, nome.toUpperCase());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void inserir(Jogador j) throws SQLException {
        String sql = "INSERT INTO jogador (nome, posicao, clube, camisa) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, j.getNome().toUpperCase());
            stmt.setString(2, j.getPosicao().toUpperCase());
            stmt.setString(3, j.getClube().toUpperCase());
            stmt.setInt(4, j.getCamisa());

            stmt.executeUpdate();
        }
    }

    public List<Jogador> listar() throws SQLException {
        List<Jogador> lista = new ArrayList<Jogador>();

        String sql = "SELECT * FROM jogador";

        try (
                Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                lista.add(new Jogador(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("posicao"),
                        rs.getString("clube"),
                        rs.getInt("camisa")));
            }
        }
        return lista;
    }

    public List<Jogador> listarOrdemAlfabetica() throws SQLException {
        List<Jogador> lista = new ArrayList<Jogador>();

        String sql = "SELECT * FROM jogador ORDER BY nome";

        try (
                Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                lista.add(new Jogador(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("posicao"),
                        rs.getString("clube"),
                        rs.getInt("camisa")));
            }
        }
        return lista;
    }

    public List<Jogador> listarOrdemCamisa() throws SQLException {
        List<Jogador> lista = new ArrayList<Jogador>();

        String sql = "SELECT * FROM jogador ORDER BY camisa";

        try (
                Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                lista.add(new Jogador(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("posicao"),
                        rs.getString("clube"),
                        rs.getInt("camisa")));
            }
        }
        return lista;
    }

    public Jogador buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM jogador WHERE nome = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, nome.toUpperCase());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return new Jogador(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("posicao"),
                            rs.getString("clube"),
                            rs.getInt("camisa"));
                }
            }
        }
        return null;
    }

    public Jogador buscarClube(String clube) throws SQLException {
        String sql = "SELECT * FROM jogador WHERE clube = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, clube.toUpperCase());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return new Jogador(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("posicao"),
                            rs.getString("clube"),
                            rs.getInt("camisa"));
                }
            }
        }
        return null;
    }

    public void atualizar(Jogador j) throws SQLException {
        String sql = "UPDATE jogador SET nome=?, posicao=?, clube=?, camisa=? WHERE id=?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, j.getNome().toUpperCase());
            stmt.setString(2, j.getPosicao().toUpperCase());
            stmt.setString(3, j.getClube().toUpperCase());
            stmt.setInt(4, j.getCamisa());
            stmt.setInt(5, j.getId());

            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM jogador WHERE id=?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    public boolean verificaID(int id) throws SQLException {
        String sql = "SELECT 1 FROM jogador WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void limparTabela() throws SQLException {
        String sql = "DELETE FROM jogador";

        try (Statement stmt = conexao.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void fecharConexao() throws SQLException {
        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }
}
