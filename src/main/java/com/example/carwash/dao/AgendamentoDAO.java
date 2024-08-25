package com.example.carwash.dao;

import com.example.carwash.model.Agendamento;
import com.example.carwash.model.StatusAgendamento;
import com.example.carwash.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO {

    private Connection connection;

    public AgendamentoDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Agendamento> buscarTodosAgendamentos() throws SQLException {
        String sql = "SELECT * FROM agendamentos";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        List<Agendamento> agendamentos = new ArrayList<>();
        while (resultSet.next()) {
            Agendamento agendamento = mapearAgendamento(resultSet);
            agendamentos.add(agendamento);
        }

        return agendamentos;
    }

    // Novo método para buscar agendamentos com status diferente de "CONFIRMADO"
    public List<Agendamento> buscarAgendamentosAtivos() throws SQLException {
        List<Agendamento> agendamentosAtivos = new ArrayList<>();
        String sql = "SELECT * FROM agendamentos WHERE status <> 'CONFIRMADO'";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Agendamento agendamento = mapResultSetToAgendamento(rs);
                agendamentosAtivos.add(agendamento);
            }
        }
        return agendamentosAtivos;
    }

    private Agendamento mapResultSetToAgendamento(ResultSet rs) throws SQLException {
        // Mapeia o ResultSet para um objeto Agendamento
        Agendamento agendamento = new Agendamento();
        agendamento.setId(rs.getInt("id"));
        agendamento.setUsuarioId(rs.getInt("usuario_id"));
        agendamento.setServicoId(rs.getInt("servico_id"));
        agendamento.setData(rs.getDate("data").toLocalDate());
        agendamento.setStatus(StatusAgendamento.valueOf(rs.getString("status")));
        // Adicione outros mapeamentos conforme necessário
        return agendamento;
    }

    public List<Agendamento> buscarAgendamentosPorUsuarioId(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM agendamentos WHERE usuario_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, usuarioId);
        ResultSet resultSet = statement.executeQuery();

        List<Agendamento> agendamentos = new ArrayList<>();
        while (resultSet.next()) {
            Agendamento agendamento = mapearAgendamento(resultSet);
            agendamentos.add(agendamento);
        }

        return agendamentos;
    }

    private Agendamento mapearAgendamento(ResultSet resultSet) throws SQLException {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(resultSet.getInt("id"));
        agendamento.setUsuarioId(resultSet.getInt("usuario_id"));
        agendamento.setServicoId(resultSet.getInt("servico_id"));
        agendamento.setData(resultSet.getDate("data").toLocalDate());
        agendamento.setStatus(StatusAgendamento.valueOf(resultSet.getString("status")));
        return agendamento;
    }

    public Usuario buscarUsuarioPorId(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("tipo_usuario")
                    );
                }
            }
        }
        return null;
    }

    public boolean atualizarStatusAgendamento(int id, String status) throws SQLException {
        String sql = "UPDATE agendamentos SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }
}
