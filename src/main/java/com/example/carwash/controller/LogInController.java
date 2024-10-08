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
    private void logIn() throws SQLException {
        String email = nomeUsuarioField.getText();
        String senha = senhaField.getText();

        Usuario usuario = usuarioDAO.buscarUsuarioPorEmailESenha(email, senha);

        if (usuario != null) {
            try {
                FXMLLoader loader;
                Parent root;

                if (usuario.getTipoUsuario().equals("EMPRESARIAL")) {
                    loader = new FXMLLoader(getClass().getResource("/com/example/carwash/empresarial-view.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("/com/example/carwash/singular-view.fxml"));
                }

                root = loader.load();

                // Obter o controlador da próxima tela
                if (usuario.getTipoUsuario().equals("EMPRESARIAL")) {
                    EmpresarialView empresarialView = loader.getController();
                    empresarialView.setUsuarioLogado(usuario);
                } else {
                    SingularViewController singularViewController = loader.getController();
                    singularViewController.setUsuarioLogado(usuario);
                }

                // Mudar de tela
                Stage stage = (Stage) logInButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                exibirMensagem("Erro", "Não foi possível carregar a tela.");
                e.printStackTrace();
            }
        } else {
            exibirMensagem("Erro de login", "Usuário ou senha incorretos");
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

    private boolean eEmpresarial() {
        try {
            Usuario usuario = usuarioDAO.buscarUsuarioPorEmailESenha(nomeUsuarioField.getText(), senhaField.getText());
            return usuario != null && "EMPRESARIAL".equals(usuario.getTipoUsuario());
        } catch (SQLException e) {
            exibirMensagem("Erro ao verificar tipo de usuário", "Erro ao verificar o tipo de usuário.");
            e.printStackTrace();
            return false;
        }
    }

    private void abreJanelaEmpresarial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/empresarial-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Área Empresarial");
            stage.show();

            // Fechar a janela de login
            Stage loginStage = (Stage) logInButton.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            exibirMensagem("Erro ao abrir a janela empresarial", "Não foi possível abrir a janela empresarial.");
            e.printStackTrace();
        }
    }

    private void abreJanelaSingular() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/carwash/singular-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Área Singular");
            stage.show();

            // Fechar a janela de login
            Stage loginStage = (Stage) logInButton.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            exibirMensagem("Erro ao abrir a janela singular", "Não foi possível abrir a janela singular.");
            e.printStackTrace();
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
