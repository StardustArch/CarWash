package com.example.carwash.controller;

import com.example.carwash.model.Agendamento;
import com.example.carwash.model.Usuario;
import com.example.carwash.dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.SQLException;

public class AgendamentoItemController {

    @FXML
    private Label nomeAgendanteLabel;

    private Agendamento agendamento;
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
        // Buscar o nome do usuário relacionado ao agendamento
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            Usuario usuario = usuarioDAO.buscarUsuarioPorId(agendamento.getUsuarioId());
            nomeAgendanteLabel.setText(usuario.getNome());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }
}
