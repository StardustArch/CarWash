package com.example.carwash.dao;

import com.example.carwash.model.Agendamento;
import com.example.carwash.model.Usuario;
import com.example.carwash.model.Servico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO {

    private Connection conexao;

    public AgendamentoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void adicionarAgendamento(Agendamento agendamento) throws SQLException {
        String sql = "INSERT INTO agendamentos (usuario_id, servico_id, data, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, agendamento.getUsuario().getId());
            stmt.setInt(2, agendamento.getServico().getId());
            stmt.setDate(3, new java.sql.Date(agendamento.getData().getTime()));
            stmt.setString(4, agendamento.getStatus());
            stmt.executeUpdate();
        }
    }

    public List<Agendamento> listarAgendamentos() throws SQLException {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = "SELECT * FROM agendamentos";

        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usuario = buscarUsuarioPorId(rs.getInt("usuario_id"));
                Servico servico = buscarServicoPorId(rs.getInt("servico_id"));

                Agendamento agendamento = new Agendamento(
                        rs.getInt("id"),
                        usuario,
                        servico,
                        rs.getDate("data"),
                        rs.getString("status")
                );
                agendamentos.add(agendamento);
            }
        }
        return agendamentos;
    }

    public Agendamento buscarAgendamentoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM agendamentos WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = buscarUsuarioPorId(rs.getInt("usuario_id"));
                    Servico servico = buscarServicoPorId(rs.getInt("servico_id"));

                    return new Agendamento(
                            rs.getInt("id"),
                            usuario,
                            servico,
                            rs.getDate("data"),
                            rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    private Usuario buscarUsuarioPorId(int id) throws SQLException {
        // Implemente a busca do usuário pelo ID
        // Por exemplo: SELECT * FROM usuarios WHERE id = ?
        return null; // Retorne o objeto Usuario correspondente
    }

    private Servico buscarServicoPorId(int id) throws SQLException {
        // Implemente a busca do serviço pelo ID
        // Por exemplo: SELECT * FROM servicos WHERE id = ?
        return null; // Retorne o objeto Servico correspondente
    }
}
