package com.example.octanapp.model;

public class CombustivelPosto {
    String nome;
    double preco;
    int id_posto;
    int id_combustivel;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getId_posto() {
        return id_posto;
    }

    public void setId_posto(int id_posto) {
        this.id_posto = id_posto;
    }

    public int getId_combustivel() {
        return id_combustivel;
    }

    public void setId_combustivel(int id_combustivel) {
        this.id_combustivel = id_combustivel;
    }

    public CombustivelPosto() {
    }

    public CombustivelPosto(String nome, float preco, int id_posto, int id_combustivel) {
        this.nome = nome;
        this.preco = preco;
        this.id_posto = id_posto;
        this.id_combustivel = id_combustivel;
    }
}
