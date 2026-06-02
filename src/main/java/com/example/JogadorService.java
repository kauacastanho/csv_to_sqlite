package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JogadorService {
    private String nomeArquivo = "jogadores.csv";
    private String arquivoErros = "jogadores_erros.csv";

    public JogadorService() {
    }

    public JogadorService(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public List<Jogador> carregarJogadores() {
        int totalImportados = 0;
        int totalLinhas = 0;

        List<Jogador> jogadores = new ArrayList<>();
        List<String> linhasComErro = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            // Ignorar a primeira linha (cabeçalho) se existir
            br.readLine();
            while ((linha = br.readLine()) != null) {
                totalLinhas++;
                String[] campos = linha.split(",", -1);

                try {
                    String erro = "";

                    if (campos.length == 5) {

                        erro += validaId(campos[0]);
                        erro += validaNome(campos[1]);
                        erro += validaPosicao(campos[2]);
                        erro += validaClube(campos[3]);
                        erro += validaCamisa(campos[4]);

                        if (erro.isEmpty()) {

                            int id = Integer.parseInt(campos[0].trim());
                            String nome = campos[1].trim();
                            String posicao = campos[2].trim();
                            String clube = campos[3].trim();
                            int camisa = Integer.parseInt(campos[4].trim());

                            jogadores.add(new Jogador(id, nome, posicao, clube, camisa));
                            totalImportados++;

                        } else {
                            linhasComErro.add(linha + ", linha " + totalLinhas + ", " + erro);
                        }

                    } else if (campos.length == 4) {

                        erro += validaNome(campos[0]);
                        erro += validaPosicao(campos[1]);
                        erro += validaClube(campos[2]);
                        erro += validaCamisa(campos[3]);

                        if (erro.isEmpty()) {

                            String nome = campos[0].trim();
                            String posicao = campos[1].trim();
                            String clube = campos[2].trim();
                            int camisa = Integer.parseInt(campos[3].trim());

                            jogadores.add(new Jogador(nome, posicao, clube, camisa));
                            totalImportados++;
                        } else {
                            linhasComErro.add(linha + ", linha " + totalLinhas + ", " + erro);
                        }

                    } else {
                        linhasComErro.add(linha + ", linha " + totalLinhas + ", Quantidade de colunas inválida");
                    }
                } catch (Exception e) {
                    linhasComErro.add(linha + ", linha " + totalLinhas + " , Erro inesperado" + e.getMessage());
                }
            }

            System.out.println("Total de linhas: " + totalLinhas);
            System.out.println("Jogadores importados: " + totalImportados);
            System.out.println("Linhas com erro: " + linhasComErro.size());

            salvarErrosEmArquivo(linhasComErro);
        } catch (IOException e) {
            System.err.println("Erro ao ler os jogadores do arquivo: " + e.getMessage());
        }
        return jogadores;
    }

    private String validaNome(String nome) {
        nome = nome.trim();

        if (nome.isEmpty()) {
            return "Nome vazio; ";
        }

        if (!nome.matches("^[A-Za-zÀ-ÿ ]{2,}$")) {
            return "Nome inválido; ";
        }

        return "";
    }

    private String validaPosicao(String posicao) {
        posicao = posicao.trim();

        if (posicao.isEmpty()) {
            return "Posição vazia; ";
        }

        if (!posicao.matches("^[A-Za-zÀ-ÿ\\- ]{2,}$")) {
            return "Posição inválida; ";
        }

        return "";
    }

    private String validaClube(String clube) {
        clube = clube.trim();

        if (clube.isEmpty()) {
            return "Clube vazio; ";
        }

        // Permite números porque existem clubes como "PSG", "Schalke 04"
        if (!clube.matches("^[A-Za-zÀ-ÿ0-9 ]{2,}$")) {
            return "Clube inválido; ";
        }

        return "";
    }

    private String validaCamisa(String camisaTexto) {
        camisaTexto = camisaTexto.trim();

        if (camisaTexto.isEmpty()) {
            return "Camisa vazia; ";
        }

        try {
            int camisa = Integer.parseInt(camisaTexto);

            if (camisa < 1 || camisa > 99) {
                return "Camisa fora do intervalo permitido; ";
            }

        } catch (NumberFormatException e) {
            return "Camisa inválida; ";
        }

        return "";
    }

    private String validaId(String idTexto) {
        idTexto = idTexto.trim();

        if (idTexto.isEmpty()) {
            return "ID vazio; ";
        }

        try {
            int id = Integer.parseInt(idTexto);

            if (id <= 0) {
                return "ID inválido; ";
            }

        } catch (NumberFormatException e) {
            return "ID inválido; ";
        }

        return "";
    }

    public void salvarErrosEmArquivo(List<String> erros) {
        if (erros.isEmpty())
            return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoErros))) {
            for (String linha : erros) {
                bw.write(linha);
                bw.newLine();
            }
            System.out.println("Arquivo de erros salvo em: " + arquivoErros);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo de erros: " + e.getMessage());
        }
    }

    public void gravarJogadores() {
        try {
            List<Jogador> jogadores = carregarJogadores();
            JogadorDAO dao = new JogadorDAO("jdbc:sqlite:jogadores.db");

            // Carregar os jogadores do CSV para o banco de dados
            for (Jogador j : jogadores) {
                dao.inserir(j);
            }
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Arquivo CSV já foi gravado no banco: " + e.getMessage());
        }
    }

}