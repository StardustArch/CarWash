package com.example.carwash.controller;

import com.example.carwash.dao.AgendamentoDAO;
import com.example.carwash.dao.DatabaseConnection;
import com.example.carwash.dao.ServicoDAO;
import com.example.carwash.model.Agendamento;
import com.example.carwash.model.Servico;
import com.example.carwash.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmpresarialView {

    @FXML
    private ListView<AnchorPane> agendamentosListView;

    @FXML
    private Button relatorioButao;

    @FXML
    private Button editarButton;

    @FXML
    private Button marcarButton;

    @FXML
    private Label nomeUsuarioLabel;

    @FXML
    private Label descricaoLabel;

    @FXML
    private Label tipoServicoLabel;

    @FXML
    private Label planoLabel;

    @FXML
    private Label dataLabel;

    @FXML
    private Label precoLabel;

    private AgendamentoDAO agendamentoDAO;
    private ServicoDAO servicoDAO;
    private Connection connection;
    private Usuario usuarioLogado; // Adicionando o usuário logado

    public void initialize() {
        try {
            connection = DatabaseConnection.getConnection();
            agendamentoDAO = new AgendamentoDAO(connection);
            servicoDAO = new ServicoDAO(connection);

            // Carregar agendamentos com base no tipo de usuário logado
            carregarAgendamentos();
        } catch (SQLException e) {
            exibirMensagem("Erro de Conexão", "Não foi possível conectar ao banco de dados.");
            e.printStackTrace();
        }
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        carregarAgendamentos();
    }

    private void carregarAgendamentos() {
        try {

            List<Agendamento> agendamentosAtivos = agendamentoDAO.buscarAgendamentosAtivos();

            agendamentosListView.getItems().clear(); // Limpa a lista antes de adicionar novos itens

            for (Agendamento agendamento : agendamentosAtivos) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/agendamento-item.fxml"));
                AnchorPane pane = loader.load();

                AgendamentoItemController itemController = loader.getController();
                itemController.setConnection(connection);
                itemController.setAgendamento(agendamento);

                pane.setUserData(itemController);

                agendamentosListView.getItems().add(pane);
            }
        } catch (SQLException | IOException e) {
            exibirMensagem("Erro ao carregar agendamentos", "Não foi possível carregar os agendamentos.");
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarDetalhes(MouseEvent event) {
        AnchorPane selectedPane = agendamentosListView.getSelectionModel().getSelectedItem();
        if (selectedPane != null) {
            AgendamentoItemController itemController = (AgendamentoItemController) selectedPane.getUserData();
            Agendamento agendamento = itemController.getAgendamento();

            try {
                Usuario usuario = agendamentoDAO.buscarUsuarioPorId(agendamento.getUsuarioId());
                Servico servico = servicoDAO.buscarServicoPorId(agendamento.getServicoId());

                nomeUsuarioLabel.setText("Nome do Usuário: " + usuario.getNome());
                descricaoLabel.setText("Descrição: " + servico.getDescricao());
                tipoServicoLabel.setText("Tipo de Serviço: " + servico.getTipoServico());
                planoLabel.setText("Plano: " + servico.getPlano());
                dataLabel.setText("Data: " + agendamento.getData().toString());
                precoLabel.setText("Preço: " + servico.getPreco().toString());
            } catch (SQLException e) {
                exibirMensagem("Erro ao carregar detalhes", "Não foi possível carregar os detalhes do agendamento.");
                e.printStackTrace();
            }
        } else {
            exibirMensagem("Seleção inválida", "Nenhum agendamento selecionado.");
        }
    }

    @FXML
    private void marcarConcluido() {
        AnchorPane selectedPane = agendamentosListView.getSelectionModel().getSelectedItem();

        if (selectedPane != null) {
            try {
                AgendamentoItemController itemController = (AgendamentoItemController) selectedPane.getUserData();
                Agendamento agendamento = itemController.getAgendamento();

                // Atualize o status para "CONFIRMADO" em vez de "CONCLUIDO"
                boolean atualizado = agendamentoDAO.atualizarStatusAgendamento(agendamento.getId(), "CONFIRMADO");

                if (atualizado) {
                    exibirMensagem("Sucesso", "Agendamento marcado como confirmado.");
                    carregarAgendamentos(); // Recarrega a lista de agendamentos
                } else {
                    exibirMensagem("Erro", "Não foi possível atualizar o status do agendamento.");
                }
            } catch (SQLException e) {
                exibirMensagem("Erro ao atualizar status", "Não foi possível atualizar o status do agendamento.");
                e.printStackTrace();
            }
        } else {
            exibirMensagem("Seleção inválida", "Nenhum agendamento selecionado.");
        }
    }

    @FXML
    public void abrirRelatorio(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/relatorioCarwash.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Relatorio");
            stage.show();
        } catch (IOException e) {
            exibirMensagem("Erro ao abrir a tela de cadastro", "Não foi possível abrir a tela de Relatorio.");
            e.printStackTrace();
        }
    }

    @FXML
    public void gerirFuncionarios(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/gestaoFuncionariosCarWash.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gerir funcionarios");
            stage.show();
        } catch (IOException e) {
            exibirMensagem("Erro ao abrir a tela de cadastro", "Não foi possível abrir a tela de gestao de funcionarios.");
            e.printStackTrace();
        }
    }

    private void exibirMensagem(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public void abrirProdutos(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/produtos.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Produtos");
            stage.show();
        } catch (IOException e) {
            exibirMensagem("Erro ao abrir a tela de produtos", "Não foi possível abrir a tela de produtos.");
            e.printStackTrace();
        }
    }
}
