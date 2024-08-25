package com.example.carwash.model;

import java.time.LocalDate;

public class Agendamento {
    private int id;
    private int usuarioId;
    private int servicoId;
    private LocalDate data;
    private StatusAgendamento status;
    private Servico servico;  // Opcional: para obter detalhes do serviço

    public Agendamento() {}

    public Agendamento(int id, int usuarioId, int servicoId, LocalDate data, String status) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.servicoId = servicoId;
        this.data = data;
        this.status = StatusAgendamento.valueOf(status);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getServicoId() {
        return servicoId;
    }

    public void setServicoId(int servicoId) {
        this.servicoId = servicoId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    @Override
    public String toString() {
        return "Agendamento{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", servicoId=" + servicoId +
                ", data=" + data +
                ", status=" + status +
                ", servico=" + servico +
                '}';
    }
}
