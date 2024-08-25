package com.example.carwash.model;

public class Servico {
    private int id;
    private String descricao;
    private String tipoServico; // Lavagem a seco, Polimento, etc.
    private double preco;

    public Servico(int id, String descricao, String tipoServico, double preco) {
        this.id = id;
        this.descricao = descricao;
        this.tipoServico = tipoServico;
        this.preco = preco;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
