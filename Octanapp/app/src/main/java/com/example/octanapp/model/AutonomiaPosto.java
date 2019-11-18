package com.example.octanapp.model;

public class AutonomiaPosto {
    int id_posto;
    int id_veiculo;
    double autonomia;
    String nome;
    double precoKm;
    int numAvaliacoes;

    public AutonomiaPosto(int id_posto, int id_veiculo, double autonomia, String nome, double precoKm, int numAvaliacoes) {
        this.id_posto = id_posto;
        this.id_veiculo = id_veiculo;
        this.autonomia = autonomia;
        this.nome = nome;
        this.precoKm = precoKm;
        this.numAvaliacoes = numAvaliacoes;
    }

    public AutonomiaPosto() {
    }

    public int getId_posto() {
        return id_posto;
    }

    public void setId_posto(int id_posto) {
        this.id_posto = id_posto;
    }

    public int getId_veiculo() {
        return id_veiculo;
    }

    public void setId_veiculo(int id_veiculo) {
        this.id_veiculo = id_veiculo;
    }

    public double getAutonomia() {
        return autonomia;
    }

    public void setAutonomia(double autonomia) {
        this.autonomia = autonomia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrecoKm() {
        return precoKm;
    }

    public void setPrecoKm(double precoKm) {
        this.precoKm = precoKm;
    }

    public int getNumAvaliacoes() {
        return numAvaliacoes;
    }

    public void setNumAvaliacoes(int numAvaliacoes) {
        this.numAvaliacoes = numAvaliacoes;
    }
}
