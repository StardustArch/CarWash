package com.example.carwash.dao;

import com.example.carwash.model.Servico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {

    private Connection conexao;

    public ServicoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void adicionarServico(Servico servico) throws SQLException {
        String sql = "INSERT INTO servicos (descricao, tipo_servico, preco) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, servico.getDescricao());
            stmt.setString(2, servico.getTipoServico());
            stmt.setDouble(3, servico.getPreco());
            stmt.executeUpdate();
        }
    }

    public List<Servico> listarServicos() throws SQLException {
        List<Servico> servicos = new ArrayList<>();
        String sql = "SELECT * FROM servicos";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                servicos.add(new Servico(
                        rs.getInt("id"),
                        rs.getString("descricao"),
                        rs.getString("tipo_servico"),
                        rs.getDouble("preco")
                ));
            }
        }
        return servicos;
    }

    public Servico buscarServicoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM servicos WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Servico(
                            rs.getInt("id"),
                            rs.getString("descricao"),
                            rs.getString("tipo_servico"),
                            rs.getDouble("preco")
                    );
                }
            }
        }
        return null;
    }
}
