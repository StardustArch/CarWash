package com.example.carwash.controller;

import com.example.carwash.dao.AgendamentoDAO;
import com.example.carwash.dao.DatabaseConnection;
import com.example.carwash.dao.ServicoDAO;
import com.example.carwash.model.Agendamento;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

public class RelatorioCarwashController {
    @FXML
    private ChoiceBox<String> filtroChoiceBox;

    @FXML
    private Label labelPreco;

    @FXML
    private VBox vboxAgendamentos;

    private AgendamentoDAO agendamentoDAO;
    private Connection connection;
    private ServicoDAO servicoDAO;

    @FXML
    public void initialize() throws SQLException {
        // Inicializar opções do filtro
        connection = DatabaseConnection.getConnection();
        filtroChoiceBox.getItems().addAll("Dia", "Semana", "Mês");
        filtroChoiceBox.setValue("Dia");

        // Listener para aplicar o filtro quando o valor do ChoiceBox mudar
        filtroChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            try {
                filtrarAgendamentos();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Inicializar DAO
        agendamentoDAO = new AgendamentoDAO(connection);
        servicoDAO = new ServicoDAO(connection);

        try {
            // Carregar agendamentos inicialmente
            filtrarAgendamentos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filtrarAgendamentos() throws SQLException {
        String filtro = filtroChoiceBox.getValue();
        List<Agendamento> agendamentosConcluidos;

        // Definir o intervalo de datas com base no filtro selecionado
        LocalDate hoje = LocalDate.now();
        LocalDate inicio = null;
        LocalDate fim = hoje;

        switch (filtro) {
            case "Dia":
                inicio = fim;
                break;
            case "Semana":
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                inicio = hoje.with(weekFields.getFirstDayOfWeek());
                break;
            case "Mês":
                inicio = hoje.withDayOfMonth(1);
                break;
        }

        // Buscar e filtrar agendamentos concluídos com base no intervalo
        agendamentosConcluidos = agendamentoDAO.filtrarAgendamentosConcluidosPorData(inicio, fim);

        // Atualizar a interface com os agendamentos filtrados
        atualizarVboxAgendamentos(agendamentosConcluidos);
        atualizarPrecoTotal(agendamentosConcluidos);
    }

    private void atualizarVboxAgendamentos(List<Agendamento> agendamentos) {
        vboxAgendamentos.getChildren().clear(); // Limpar a VBox antes de adicionar novos agendamentos

        for (Agendamento agendamento : agendamentos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/relatorio-itemWash.fxml"));
                AnchorPane agendamentoItem = loader.load();


                RelatorioItemController itemController = loader.getController();
                itemController.setAgendamentoData(agendamento);


                vboxAgendamentos.getChildren().add(agendamentoItem);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void atualizarPrecoTotal(List<Agendamento> agendamentos) {
        double total = agendamentos.stream()
                .mapToDouble(agendamento -> {
                    try {
                        return servicoDAO.buscarServicoPorId(agendamento.getServicoId()).getPreco().doubleValue();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .sum();

        labelPreco.setText(String.valueOf(total));
    }

}
