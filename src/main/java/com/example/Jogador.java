package com.example;

import java.util.StringTokenizer;

public class Jogador {
    private int id;
    private String nome;
    private String posicao;
    private String clube;
    private int camisa;

    public Jogador(String nome, String posicao, String clube, int camisa) {
        this.nome = nome;
        this.posicao = posicao;
        this.clube = clube;
        this.camisa = camisa;
    }

    public Jogador(int id, String nome, String posicao, String clube, int camisa) {
        this.id = id;
        this.nome = nome;
        this.posicao = posicao;
        this.clube = clube;
        this.camisa = camisa;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getPosicao() {
        return posicao;
    }

    public String getClube() {
        return clube;
    }

    public int getCamisa() {
        return camisa;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public void setClube(String clube) {
        this.clube = clube;
    }

    public void setCamisa(int camisa) {
        this.camisa = camisa;
    }

    @Override
    public String toString() {
        return id + "," + nome + "," + posicao + "," + clube + "," + camisa;
    }

    public static Jogador fromString(String data) {
        StringTokenizer tokenizer = new StringTokenizer(data, ",");

        String nome = tokenizer.nextToken();
        String posicao = tokenizer.nextToken();
        String clube = tokenizer.nextToken();
        int camisa = Integer.parseInt(tokenizer.nextToken());

        return new Jogador(nome, posicao, clube, camisa);
    }
}
