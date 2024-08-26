package com.example.carwash.controller;

import com.example.carwash.dao.AgendamentoDAO;
import com.example.carwash.dao.DatabaseConnection;
import com.example.carwash.dao.ServicoDAO;
import com.example.carwash.model.Agendamento;
import com.example.carwash.model.Servico;
import com.example.carwash.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.List;

public class SingularViewController {

    @FXML
    private ListView<AnchorPane> agendamentosListView;

    @FXML
    private Button editarAgendamentoButton;

    @FXML
    private Button removerAgendamentoButton;

    @FXML
    private Button adicionarAgendamentoButton;

    @FXML
    private Label nomeLabel;

    @FXML
    private Label descricaoLabel;

    @FXML
    private Label dataLabel;

    @FXML
    private Label tipoServicoLabel;

    @FXML
    private Label categoriaLabel;

    @FXML
    private Label precoLabel;

    private AgendamentoDAO agendamentoDAO;
    private ServicoDAO servicoDAO;
    private Connection connection;
    private Usuario usuarioLogado;
    private Agendamento agendamento;

    public void initialize() {
        try {
            connection = DatabaseConnection.getConnection();
            agendamentoDAO = new AgendamentoDAO(connection);
            servicoDAO = new ServicoDAO(connection);
        } catch (SQLException e) {
            exibirMensagem("Erro de Conexão", "Não foi possível conectar ao banco de dados.");
            e.printStackTrace();
        }

        // Listener para detectar clique nos itens da lista
        agendamentosListView.setOnMouseClicked(this::mostrarDetalhes);
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        nomeLabel.setText(usuario.getNome());
        carregarAgendamentos(usuario.getId());
    }

    private void carregarAgendamentos(int usuarioId) {
        try {
            List<Agendamento> agendamentosAtivos = agendamentoDAO.buscarAgendamentosAtivosPorUsuarioId(usuarioId);

            agendamentosListView.getItems().clear();

            for (Agendamento agendamento : agendamentosAtivos) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/agendamento-individual-item.fxml"));
                AnchorPane pane = loader.load();

                AgendamentoIndividualItemController itemController = loader.getController();
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
            AgendamentoIndividualItemController itemController = (AgendamentoIndividualItemController) selectedPane.getUserData();
            Agendamento agendamento = itemController.getAgendamento();

            try {
                Servico servico = servicoDAO.buscarServicoPorId(agendamento.getServicoId());

                descricaoLabel.setText("Descrição: " + servico.getDescricao());
                dataLabel.setText("Data: " + agendamento.getData().toString());
                tipoServicoLabel.setText("Tipo de Serviço: " + servico.getTipoServico());
                categoriaLabel.setText("Categoria: " + servico.getPlano());
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
    private void editarAgendamento() {
        AnchorPane selectedPane = agendamentosListView.getSelectionModel().getSelectedItem();

        if (selectedPane != null) {
            AgendamentoIndividualItemController itemController = (AgendamentoIndividualItemController) selectedPane.getUserData();
            Agendamento agendamento = itemController.getAgendamento();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/editar-agendamento-view.fxml"));
                AnchorPane editPane = loader.load();

                EditarAgendamentoController editarController = loader.getController();
                editarController.setAgendamento(agendamento);
                editarController.setConnection(connection);

                Stage stage = new Stage();
                stage.setTitle("Editar Agendamento");
                stage.setScene(new Scene(editPane));
                stage.show();
            } catch (IOException e) {
                exibirMensagem("Erro ao carregar tela de edição", "Não foi possível carregar a tela de edição do agendamento.");
                e.printStackTrace();
            }

        } else {
            exibirMensagem("Seleção inválida", "Nenhum agendamento selecionado.");
        }
    }

    @FXML
    private void removerAgendamento() {
        AnchorPane selectedPane = agendamentosListView.getSelectionModel().getSelectedItem();

        if (selectedPane != null) {
            try {
                AgendamentoIndividualItemController itemController = (AgendamentoIndividualItemController) selectedPane.getUserData();
                Agendamento agendamento = itemController.getAgendamento();

                boolean removido = agendamentoDAO.removerAgendamento(agendamento.getId());

                if (removido) {
                    exibirMensagem("Sucesso", "Agendamento removido com sucesso.");
                    carregarAgendamentos(usuarioLogado.getId());
                } else {
                    exibirMensagem("Erro", "Não foi possível remover o agendamento.");
                }
            } catch (SQLException e) {
                exibirMensagem("Erro ao remover agendamento", "Não foi possível remover o agendamento.");
                e.printStackTrace();
            }
        } else {
            exibirMensagem("Seleção inválida", "Nenhum agendamento selecionado.");
        }
    }

    @FXML
    private void adicionarAgendamento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/adicionar-agendamento-view.fxml"));
            AnchorPane addPane = loader.load();

            AdicionarAgendamentoController adicionarController = loader.getController();
            adicionarController.setUsuario(usuarioLogado);

            Stage stage = new Stage();
            stage.setTitle("Adicionar Agendamento");
            stage.setScene(new Scene(addPane));

            // Adicione um listener para quando a janela for fechada
            stage.setOnHiding(event -> carregarAgendamentos(usuarioLogado.getId()));

            stage.show();
        } catch (IOException e) {
            exibirMensagem("Erro ao carregar tela de adição", "Não foi possível carregar a tela de adicionar agendamento.");
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
}
