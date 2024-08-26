package com.example.carwash.controller;

import com.example.carwash.dao.DatabaseConnection;
import com.example.carwash.dao.ServicoDAO;
import com.example.carwash.model.Agendamento;
import com.example.carwash.model.Servico;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.SQLException;

public class AgendamentoIndividualItemController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label descricaoLabel;

    private Agendamento agendamento;

    private Connection connection;

    @FXML
    public void initialize() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;

        try {
            ServicoDAO servicoDAO = new ServicoDAO(connection);
            Servico servico = servicoDAO.buscarServicoPorId(agendamento.getServicoId());

            // Define a descrição na Label
            descricaoLabel.setText(servico.getDescricao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
