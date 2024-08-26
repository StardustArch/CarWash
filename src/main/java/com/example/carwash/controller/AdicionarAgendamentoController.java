package com.example.carwash.controller;

import com.example.carwash.dao.AgendamentoDAO;
import com.example.carwash.dao.ServicoDAO;
import com.example.carwash.dao.DatabaseConnection;
import com.example.carwash.model.Servico;
import com.example.carwash.model.TipoServico;
import com.example.carwash.model.Usuario; // Adicione a importação para Usuario
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AdicionarAgendamentoController {

    @FXML
    private DatePicker dataPicker;

    @FXML
    private TextField descricaoTextField;

    @FXML
    private ComboBox<TipoServico> tipoServicoComboBox;

    @FXML
    private ComboBox<String> planoComboBox;

    private AgendamentoDAO agendamentoDAO;
    private ServicoDAO servicoDAO;
    private Connection connection;
    private Usuario usuario; // Adicione um campo para Usuario

    @FXML
    private void initialize() {
        try {
            connection = DatabaseConnection.getConnection(); // Obtém a conexão com o banco de dados
            agendamentoDAO = new AgendamentoDAO(connection);
            servicoDAO = new ServicoDAO(connection);

            carregarTipoServico();
            carregarPlano();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Erro de Conexão", "Não foi possível conectar ao banco de dados.");
        }
    }

    private void carregarTipoServico() {
        tipoServicoComboBox.getItems().setAll(TipoServico.values());
    }

    private void carregarPlano() {
        planoComboBox.getItems().setAll("LIGEIRO", "PESADO");
    }

    @FXML
    private void confirmar() {
        try {
            TipoServico tipoServico = tipoServicoComboBox.getValue();
            String plano = planoComboBox.getValue();
            if (tipoServico == null || plano == null || dataPicker.getValue() == null) {
                mostrarAlerta(AlertType.WARNING, "Dados Incompletos", "Preencha todos os campos.");
                return;
            }

            String tipoServicoStr = tipoServico.toString(); // Assumindo que TipoServico é um enum

            // Encontrar o serviço com base no tipo de serviço e plano
            Servico servico = agendamentoDAO.encontrarServicoPorTipoEPlano(tipoServicoStr, plano);
            if (servico == null) {
                mostrarAlerta(AlertType.WARNING, "Serviço Não Encontrado", "Nenhum serviço encontrado para o tipo e plano selecionados.");
                return;
            }

            // Supondo que você tenha o ID do usuário disponível
            int usuarioId = usuario.getId();

            agendamentoDAO.adicionarAgendamento(usuarioId, servico.getId(), dataPicker.getValue(), descricaoTextField.getText());

            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Agendamento adicionado com sucesso.");

            // Fecha a tela de adicionar após o sucesso
            fecharTela();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Erro", "Erro ao adicionar agendamento.");
        }
    }

    private void fecharTela() {
        // Obtém o stage atual e o fecha
        ((Stage) descricaoTextField.getScene().getWindow()).close();
    }

    private Servico encontrarServicoPorTipo(TipoServico tipoServico) throws SQLException {
        List<Servico> servicos = servicoDAO.obterServicosPorTipo(tipoServico);
        return servicos.isEmpty() ? null : servicos.get(0); // Retorna o primeiro serviço encontrado
    }

    @FXML
    private void cancelar() {
        // Fechar a janela ou realizar outra ação de cancelamento
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario; // Defina o usuário
    }
}
