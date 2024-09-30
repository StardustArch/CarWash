package com.example.carwash.controller;

import com.example.carwash.dao.AgendamentoDAO;
import com.example.carwash.dao.DatabaseConnection;
import com.example.carwash.dao.ServicoDAO;
import com.example.carwash.dao.UsuarioDAO;
import com.example.carwash.model.Agendamento;
import com.example.carwash.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.SQLException;

public class RelatorioItemController {
    @FXML
    private Label nomeAgendanteLabel;

    @FXML
    private Label precoLabel;

    private String nome;
    private Usuario usuario;
    private Connection conexao;
    private AgendamentoDAO agendamentoDAO;
    private UsuarioDAO usuarioDAO;
    private ServicoDAO servicoDAO;

    @FXML
    public void initialize() throws SQLException {
        conexao = DatabaseConnection.getConnection();
        agendamentoDAO = new AgendamentoDAO(conexao);
        usuarioDAO = new UsuarioDAO(conexao);
        servicoDAO = new ServicoDAO(conexao);
    }

    public void setAgendamentoData(Agendamento agendamento) throws SQLException {

        nome = usuarioDAO.buscarUsuarioPorId(agendamento.getUsuarioId()).getNome();
        nomeAgendanteLabel.setText(nome);
        int id = agendamento.getServicoId();
        precoLabel.setText(servicoDAO.buscarServicoPorId(id).getPreco().toString());
    }
}
