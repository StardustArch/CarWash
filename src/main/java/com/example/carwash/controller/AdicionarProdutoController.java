package com.example.carwash.controller;

import com.example.carwash.dao.DatabaseConnection;
import com.example.carwash.dao.ProdutoDAO;
import com.example.carwash.model.Produto;
import com.example.carwash.model.TipoProduto;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

public class AdicionarProdutoController {
    public ChoiceBox tipoProdutoCB;
    public TextField quantiaTF;
    public Button adicionarProdutoButton;
    private Produto produto;

    private Connection connection;
    private ProdutoDAO produtoDAO;

    public void initialize() {
        try {
            connection = DatabaseConnection.getConnection();
            produtoDAO = new ProdutoDAO();
            tipoProdutoCB.getItems().addAll("Detergente", "Cera", "Spray");
            tipoProdutoCB.setValue("Detergente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void adicionarProduto(ActionEvent actionEvent) throws SQLException {
        String selecao = (String) tipoProdutoCB.getValue();
        int quantia = Integer.parseInt(quantiaTF.getText());
        produto = new Produto();

        if(selecao.equals("Cera")) {
            produto.setTipoProduto(TipoProduto.CERA);
        } else if(selecao.equals("Spray")) {
            produto.setTipoProduto(TipoProduto.SPRAY);
        } else if(selecao.equals("Detergente")) {
            produto.setTipoProduto(TipoProduto.DETERGENTE);
        }

        produto.setQuantia(quantia);

        boolean resultado = produtoDAO.atualizarProduto(produto.getTipoProduto(), produto.getQuantia());

        if (resultado) {
            adicionarProdutoButton.getScene().getWindow().hide();
        }
    }
}
