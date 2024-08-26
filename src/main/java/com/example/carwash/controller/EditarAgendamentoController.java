package com.example.carwash.controller;

import com.example.carwash.dao.AgendamentoDAO;
import com.example.carwash.model.Agendamento;
import com.example.carwash.model.TipoServico;
import com.example.carwash.model.Plano;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class EditarAgendamentoController {

    @FXML
    private DatePicker dataPicker;

    @FXML
    private TextField descricaoTextField;


    private Connection connection;
    private Agendamento agendamento;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
        dataPicker.setValue(agendamento.getData());
        descricaoTextField.setText(agendamento.getDescricao());
    }

    @FXML
    private void initialize() {

    }

    @FXML
    private void confirmar() {
        try {
            AgendamentoDAO agendamentoDAO = new AgendamentoDAO(connection);

            agendamento.setData(dataPicker.getValue());
            agendamento.setDescricao(descricaoTextField.getText());

            agendamentoDAO.atualizarAgendamentoInterface(agendamento);

            exibirMensagem("Sucesso", "Agendamento atualizado com sucesso.");
            fecharJanela();
        } catch (SQLException e) {
            exibirMensagem("Erro", "Erro ao atualizar agendamento.");
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) dataPicker.getScene().getWindow();
        stage.close();
    }

    private void exibirMensagem(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
