package com.example.carwash.dao;

import com.example.carwash.model.Agendamento;
import com.example.carwash.model.Servico;
import com.example.carwash.model.StatusAgendamento;
import com.example.carwash.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AgendamentoDAO {

    private final Connection connection;

    public AgendamentoDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean adicionarAgendamento(Agendamento agendamento) throws SQLException {
        String sql = "INSERT INTO agendamentos (usuario_id, servico_id, data, descricao, tipo_servico, plano, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, agendamento.getUsuarioId());
            stmt.setInt(2, agendamento.getServicoId());
            stmt.setDate(3, java.sql.Date.valueOf(agendamento.getData())); // Converte LocalDate para java.sql.Date
            stmt.setString(4, agendamento.getDescricao());
            stmt.setString(5, agendamento.getTipoServico().name());
            stmt.setString(6, agendamento.getPlano().name());
            stmt.setString(7, "PENDENTE"); // Defina um status padrão ou ajuste conforme necessário
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        }
    }

    public void adicionarAgendamento(int usuarioId, int servicoId, LocalDate data, String descricao) throws SQLException {
        String sql = "INSERT INTO agendamentos (usuario_id, servico_id, data, descricao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, servicoId);
            stmt.setDate(3, java.sql.Date.valueOf(data));
            stmt.setString(4, descricao);
            stmt.executeUpdate();
        }
    }

    public boolean atualizarAgendamento(Agendamento agendamento) throws SQLException {
        String sql = "UPDATE agendamentos SET servico_id = ?, data = ?, descricao = ?, tipo_servico = ?, plano = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, agendamento.getServicoId());
            stmt.setDate(2, java.sql.Date.valueOf(agendamento.getData())); // Converte LocalDate para java.sql.Date
            stmt.setString(3, agendamento.getDescricao());
            stmt.setString(4, agendamento.getTipoServico().name());
            stmt.setString(5, agendamento.getPlano().name());
            stmt.setInt(6, agendamento.getId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    public boolean atualizarAgendamentoInterface(Agendamento agendamento) throws SQLException {
        String sql = "UPDATE agendamentos SET servico_id = ?, data = ?, descricao = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, agendamento.getServicoId());
            stmt.setDate(2, java.sql.Date.valueOf(agendamento.getData())); // Converte LocalDate para java.sql.Date
            stmt.setString(3, agendamento.getDescricao());
            stmt.setInt(4, agendamento.getId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    public void adicionarAgendamento(int servicoId, LocalDate data, String descricao) throws SQLException {
        String sql = "INSERT INTO agendamentos (servico_id, data, descricao, status) VALUES (?, ?, ?, 'PENDENTE')";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, servicoId);
            stmt.setDate(2, java.sql.Date.valueOf(data));
            stmt.setString(3, descricao);
            stmt.executeUpdate();
        }
    }

    public List<Agendamento> buscarAgendamentosAtivosPorUsuarioId(int usuarioId) throws SQLException {
        List<Agendamento> agendamentosAtivos = new ArrayList<>();
        String sql = "SELECT * FROM agendamentos WHERE usuario_id = ? AND status <> 'CONFIRMADO'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Agendamento agendamento = mapResultSetToAgendamento(rs);
                    agendamentosAtivos.add(agendamento);
                }
            }
        }
        return agendamentosAtivos;
    }

    public Servico encontrarServicoPorTipoEPlano(String tipoServico, String plano) throws SQLException {
        String sql = "SELECT * FROM servicos WHERE tipo_servico = ? AND plano = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoServico);
            stmt.setString(2, plano);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Supondo que você tenha um construtor em Servico que aceita os parâmetros abaixo
                    return new Servico(
                            rs.getInt("id"),
                            rs.getString("descricao"),
                            rs.getString("tipo_servico"),
                            rs.getString("plano"),
                            rs.getBigDecimal("preco")
                    );
                } else {
                    return null; // Serviço não encontrado
                }
            }
        }
    }

    public List<Agendamento> buscarTodosAgendamentos() throws SQLException {
        String sql = "SELECT * FROM agendamentos";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            List<Agendamento> agendamentos = new ArrayList<>();
            while (resultSet.next()) {
                Agendamento agendamento = mapearAgendamento(resultSet);
                agendamentos.add(agendamento);
            }

            return agendamentos;
        }
    }

    public List<Agendamento>   buscarAgendamentosAtivos() throws SQLException {
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
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, usuarioId);
            try (ResultSet resultSet = statement.executeQuery()) {

                List<Agendamento> agendamentos = new ArrayList<>();
                while (resultSet.next()) {
                    Agendamento agendamento = mapearAgendamento(resultSet);
                    agendamentos.add(agendamento);
                }

                return agendamentos;
            }
        }
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
    public Agendamento obterAgendamentoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM agendamentos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Agendamento agendamento = new Agendamento();
                    agendamento.setId(rs.getInt("id"));
                    agendamento.setUsuarioId(rs.getInt("usuario_id"));
                    agendamento.setServicoId(rs.getInt("servico_id"));
                    agendamento.setData(rs.getDate("data").toLocalDate());
                    agendamento.setStatus(StatusAgendamento.valueOf(rs.getString("status")));

                    return agendamento;
                } else {
                    return null; // Agendamento não encontrado
                }
            }
        }
    }

    public boolean removerAgendamento(int id) throws SQLException {
        String sql = "DELETE FROM agendamentos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Agendamento> listarAgendamentosConcluidos() throws SQLException {
        return buscarTodosAgendamentos().stream()
                .filter(agendamento -> agendamento.getStatus() == StatusAgendamento.CONFIRMADO)
                .collect(Collectors.toList());
    }

    public List<Agendamento> filtrarAgendamentosConcluidosPorData(LocalDate inicio, LocalDate fim) throws SQLException {
        return listarAgendamentosConcluidos().stream()
                .filter(agendamento -> !agendamento.getData().isBefore(inicio) && !agendamento.getData().isAfter(fim))
                .collect(Collectors.toList());
    }
}
