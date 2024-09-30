package com.example.carwash.controller;

import com.example.carwash.dao.AgendamentoDAO;
import com.example.carwash.dao.DatabaseConnection;
import com.example.carwash.dao.ProdutoDAO;
import com.example.carwash.dao.ServicoDAO;
import com.example.carwash.model.Produto;
import com.example.carwash.model.TipoProduto;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProdutosController {
    public Button relatorioButao;
    public Button produtosButton;
    public Button GeralButton;
    public Button adicionarButton;
    public Button removerButton;
    public Label detergenteLabel;
    public Label ceraLabel;
    public Label sparyLabel;

    private Connection connection;
    private Produto produto;
    private ProdutoDAO produtoDAO;

    public void initialize() {
        try {
            connection = DatabaseConnection.getConnection();
            produtoDAO = new ProdutoDAO();
            atualizarProdutos();
        } catch (SQLException e) {
            exibirMensagem("Erro de Conexão", "Não foi possível conectar ao banco de dados.");
            e.printStackTrace();
        }
    }

    public void abrirRelatorio(ActionEvent actionEvent) {
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

    public void abrirGeral(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/empresarialView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestor");
            stage.show();
        } catch (IOException e) {
            exibirMensagem("Erro ao abrir a tela geral", "Não foi possível abrir a tela geral.");
            e.printStackTrace();
        }
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

    public void adicionarProduto(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/adicionarProduto.fxml"));
            Pane root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Adicionar Produto");
            stage.showAndWait();

            atualizarProdutos();

        } catch (IOException e) {
            exibirMensagem("Erro ao abrir a tela de adicionar produto.", "Erro ao abrir a tela de adicionar produto.");
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removerProduto(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/removerProduto.fxml"));
            Pane root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Remover Produto");
            stage.showAndWait();

            atualizarProdutos();

        } catch (IOException e) {
            exibirMensagem("Erro ao abrir a tela de remover produto.", "Erro ao abrir a tela de remover produto.");
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void atualizarProdutos() throws SQLException {
        List<Produto> produtos = produtoDAO.obterTodos();
        for (Produto produto : produtos) {
            if (produto.getTipoProduto() == TipoProduto.DETERGENTE) {
                detergenteLabel.setText("Detergente: " + produto.getQuantia());
            } else if (produto.getTipoProduto() == TipoProduto.SPRAY) {
                sparyLabel.setText("Spray: " + produto.getQuantia());
            } else if (produto.getTipoProduto() == TipoProduto.CERA) {
                ceraLabel.setText("Cera: " + produto.getQuantia());
            }
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
