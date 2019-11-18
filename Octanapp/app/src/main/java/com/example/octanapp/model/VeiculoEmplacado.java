package com.example.octanapp.model;

public class VeiculoEmplacado {

    String placa;
    String marca;
    String modelo;
    int ano;
    int kmTotal;
    long id_usuario;
    int id_veiculo;
    int ativo;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getKmTotal() {
        return kmTotal;
    }

    public void setKmTotal(int kmTotal) {
        this.kmTotal = kmTotal;
    }

    public long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_veiculo() {
        return id_veiculo;
    }

    public void setId_veiculo(int id_veiculo) {
        this.id_veiculo = id_veiculo;
    }

    public int getAtivo() {
        return ativo;
    }

    public void setAtivo(int ativo) {
        this.ativo = ativo;
    }

    public VeiculoEmplacado() {

    }

    public VeiculoEmplacado(String placa, String marca, String modelo, int ano, int kmTotal, long id_usuario, int id_veiculo) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.kmTotal = kmTotal;
        this.id_usuario = id_usuario;
        this.id_veiculo = id_veiculo;
    }
}
