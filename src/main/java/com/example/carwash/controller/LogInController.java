package com.example.carwash.controller;

import com.example.carwash.dao.DatabaseConnection;
import com.example.carwash.dao.UsuarioDAO;
import com.example.carwash.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LogInController {

    @FXML
    private TextField nomeUsuarioField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private Button logInButton;

    @FXML
    private Button CadastroButton;

    @FXML
    private Label nomeUsuarioLabel;

    @FXML
    private Label senhaLabel;

    private UsuarioDAO usuarioDAO;

    @FXML
    private void initialize() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            usuarioDAO = new UsuarioDAO(connection);
        } catch (SQLException e) {
            exibirMensagem("Erro de Conexão", "Não foi possível conectar ao banco de dados.");
            e.printStackTrace();
        }
    }

    @FXML
    private void logIn() {
        String nomeUsuario = nomeUsuarioField.getText();
        String senha = senhaField.getText();

        if (verificarCredenciais(nomeUsuario, senha)) {
            exibirMensagem("Login bem-sucedido!", "Você está logado.");
        } else {
            exibirMensagem("Erro de login", "Nome de usuário ou senha incorretos.");
        }
    }

    @FXML
    private void cadastro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/cadastroUsuario.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Cadastro de Usuário");
            stage.show();
        } catch (IOException e) {
            exibirMensagem("Erro ao abrir a tela de cadastro", "Não foi possível abrir a tela de cadastro.");
            e.printStackTrace();
        }
    }

    private boolean verificarCredenciais(String nomeUsuario, String senha) {
        try {
            Usuario usuario = usuarioDAO.buscarUsuarioPorEmailESenha(nomeUsuario, senha);
            return usuario != null;
        } catch (SQLException e) {
            exibirMensagem("Erro de Login", "Erro ao verificar credenciais.");
            e.printStackTrace();
            return false;
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
