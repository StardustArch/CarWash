package com.example.carwash.model;

import com.example.carwash.dao.ProdutoDAO;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.List;

public class ProdutoGasto {
    private int id_Servico;
    private List<Produto> produtosUsados;

    public ProdutoGasto() {
        produtosUsados = new ArrayList<>();
    }

    public ProdutoGasto(int id_Servico) {
        this.id_Servico = id_Servico;
        produtosUsados = new ArrayList<>();
    }

    public int getId_Servico() {
        return id_Servico;
    }

    public void setId_Servico(int id_Servico) {
        this.id_Servico = id_Servico;
    }

    private List<Produto> obterQuantidadeGasta() {
        if (id_Servico == 1) {
            Produto produto = new Produto();
            produto.setTipoProduto(TipoProduto.valueOf("DETERGENTE"));
            produto.setQuantia(2);
            produtosUsados.add(produto);
        } else if (id_Servico == 2) {
            Produto produto = new Produto();
            produto.setTipoProduto(TipoProduto.valueOf("DETERGENTE"));
            produto.setQuantia(2);
            Produto spray = new Produto();
            spray.setTipoProduto(TipoProduto.valueOf("SPRAY"));
            spray.setQuantia(2); // Corrigido para adicionar quantia corretamente
            produtosUsados.add(produto);
            produtosUsados.add(spray);
        } else if (id_Servico == 3) {
            Produto cera = new Produto();
            cera.setTipoProduto(TipoProduto.valueOf("CERA"));
            cera.setQuantia(5);
            produtosUsados.add(cera);
        } else if (id_Servico == 4) {
            Produto produto = new Produto();
            produto.setTipoProduto(TipoProduto.valueOf("DETERGENTE"));
            produto.setQuantia(6);
            Produto spray = new Produto();
            spray.setTipoProduto(TipoProduto.valueOf("SPRAY"));
            spray.setQuantia(6);
            Produto cera = new Produto();
            cera.setTipoProduto(TipoProduto.valueOf("CERA"));
            cera.setQuantia(5);
            produtosUsados.add(produto);
            produtosUsados.add(spray);
            produtosUsados.add(cera);
        } else if (id_Servico == 5) {
            Produto produto = new Produto();
            produto.setTipoProduto(TipoProduto.valueOf("DETERGENTE"));
            produto.setQuantia(2);
            Produto spray = new Produto();
            spray.setTipoProduto(TipoProduto.valueOf("SPRAY"));
            spray.setQuantia(2);
            Produto cera = new Produto();
            cera.setTipoProduto(TipoProduto.valueOf("CERA"));
            cera.setQuantia(1);
            produtosUsados.add(produto);
            produtosUsados.add(spray);
            produtosUsados.add(cera);
        }
        return produtosUsados;
    }

    public void concluirAgendamento() {
        List<Produto> produtosUsados = obterQuantidadeGasta(); // Atualiza a lista de produtos

        // Cria uma tarefa para a execução assíncrona
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Para cada produto usado, atualiza a quantidade no banco de dados
                for (Produto produto : produtosUsados) {
                    try {
                        System.out.println("Tipo prod: " + produto.getTipoProduto() + ", Quantia: " + produto.getQuantia());

                        // Aqui você atualizaria o produto no banco de dados
                        ProdutoDAO produtoDAO = new ProdutoDAO();
                        produtoDAO.atualizarProdutos(produto.getTipoProduto(), produto.getQuantia());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                // Exibe uma mensagem de sucesso na interface do usuário
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Atualização de Produtos");
                alert.setHeaderText(null);
                alert.setContentText("Todos os produtos foram atualizados com sucesso!");
                alert.showAndWait();
            }

            @Override
            protected void failed() {
                super.failed();
                // Exibe uma mensagem de erro na interface do usuário
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro na Atualização");
                alert.setHeaderText(null);
                alert.setContentText("Houve um erro ao atualizar os produtos.");
                alert.showAndWait();
            }
        };

        // Inicia a tarefa em um novo thread
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
