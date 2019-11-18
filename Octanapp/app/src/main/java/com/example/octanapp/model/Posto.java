package com.example.octanapp.model;

public class Posto {
    int id_posto;
    String nomeFantasia;
    String bandeira;
    int numAvaliacoes;
    double notaMedia;
    String coordenadas;

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public double getNotaMedia() {
        return notaMedia;
    }

    public void setNotaMedia(double notaMedia) {
        this.notaMedia = notaMedia;
    }

    public int getId_posto() {
        return id_posto;
    }

    public void setId_posto(int id_posto) {
        this.id_posto = id_posto;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public int getNumAvaliacoes() {
        return numAvaliacoes;
    }

    public void setNumAvaliacoes(int numAvaliacoes) {
        this.numAvaliacoes = numAvaliacoes;
    }


}
