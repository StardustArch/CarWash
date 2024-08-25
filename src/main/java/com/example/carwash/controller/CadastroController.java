package com.example.carwash.controller;

import com.example.carwash.dao.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CadastroController {

    @FXML
    private TextField nomeField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private Button CadastroButton;

    @FXML
    private Label messageLabel;

    @FXML
    private void cadastrar() {
        String nome = nomeField.getText().trim();
        String email = emailField.getText().trim();
        String senha = senhaField.getText().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            exibirMensagem("Cadastro inválido", "Todos os campos devem ser preenchidos!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO usuarios (nome, email, senha, tipo_usuario) VALUES (?, ?, ?, 'SINGULAR')";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nome);
                stmt.setString(2, email);
                stmt.setString(3, senha);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    exibirMensagem("Cadastro realizado com sucesso!", "Usuário cadastrado com sucesso.");
                    mudarParaLogIn();
                } else {
                    exibirMensagem("Erro ao cadastrar", "Não foi possível realizar o cadastro.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                exibirMensagem("Erro ao cadastrar", "Erro ao realizar o cadastro.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            exibirMensagem("Erro de Conexão", "Erro na conexão com o banco de dados.");
        }
    }

    private void mudarParaLogIn() {
        try {
            // Carregar a tela de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/logIn-view.fxml")); // Substitua pelo caminho correto do arquivo FXML
            Parent loginRoot = loader.load();

            // Obter o stage atual
            Stage currentStage = (Stage) CadastroButton.getScene().getWindow();
            currentStage.setScene(new Scene(loginRoot));

            // Mostrar o novo stage
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            exibirMensagem("Erro ao carregar tela", "Não foi possível carregar a tela de login.");
        }
    }

    private void exibirMensagem(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
