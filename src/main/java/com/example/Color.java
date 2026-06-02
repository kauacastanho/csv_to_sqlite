package com.example;

public class Color {

    public String vermelho(String frase) {
        String frase_colorida = ("\u001B[31m" + frase + "\u001B[0m");
        return frase_colorida;
    }

    public String verde(String frase) {
        String frase_colorida = ("\u001B[32m" + frase + "\u001B[0m");
        return frase_colorida;
    }

    public String amarelo(String frase) {
        String frase_colorida = ("\u001B[33m" + frase + "\u001B[0m");
        return frase_colorida;
    }
}
