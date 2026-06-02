package com.example;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Método que limpa a tela do terminal
    public final static void clear() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO()
                        .start()
                        .waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (final Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            JogadorDAO dao = new JogadorDAO("jdbc:sqlite:jogadores.db");
            JogadorService fileDAO = new JogadorService("csv_to_sqlite/jogadores.csv");
            Color c = new Color();

            while (true) {
                System.out.println(c.verde("=".repeat(16) + " Menu " + "=".repeat(17)));
                System.out.printf(c.verde("|") + " %-2s " + c.verde(" |") + " %-30s" + c.verde(" |") + "%n",
                        c.amarelo("1"), "Cadastrar jogador de teste");
                System.out.printf(c.verde("|") + " %-2s " + c.verde(" |") + " %-30s" + c.verde(" |") + "%n",
                        c.amarelo("2"), "Cadastrar jogador manualmente");
                System.out.printf(c.verde("|") + " %-2s " + c.verde(" |") + " %-30s" + c.verde(" |") + "%n",
                        c.amarelo("3"), "Listar jogadores");
                System.out.printf(c.verde("|") + " %-2s " + c.verde(" |") + " %-30s" + c.verde(" |") + "%n",
                        c.amarelo("4"), "Buscar por nome");
                System.out.printf(c.verde("|") + " %-2s " + c.verde(" |") + " %-30s" + c.verde(" |") + "%n",
                        c.amarelo("5"), "Buscar por clube");
                System.out.printf(c.verde("|") + " %-2s " + c.verde(" |") + " %-30s" + c.verde(" |") + "%n",
                        c.amarelo("6"), "Atualizar jogador");
                System.out.printf(c.verde("|") + " %-2s " + c.verde(" |") + " %-30s" + c.verde(" |") + "%n",
                        c.amarelo("7"), "Deletar jogador");
                System.out.printf(c.verde("|") + " %-2s " + c.verde(" |") + " %-30s" + c.verde(" |") + "%n",
                        c.amarelo("8"), "Gravar jogadores do CSV");
                System.out.printf(c.verde("|") + " %-2s " + c.verde(" |") + " %-30s" + c.verde(" |") + "%n",
                        c.amarelo("9"), "Limpar tabela");
                System.out.printf(c.verde("|") + " %-2s " + c.verde("|") + " %-30s" + c.verde(" |") + "%n",
                        c.amarelo("10"), "Listar por ordem alfabética");
                System.out.printf(c.verde("|") + " %-2s " + c.verde("|") + " %-30s" + c.verde(" |") + "%n",
                        c.amarelo("11"), "Listar por ordem de camisa");
                System.out.printf(c.verde("|") + " %-2s " + c.verde(" |") + " %-30s" + c.verde(" |") + "%n",
                        c.amarelo("0"), "Sair");
                System.out.println(c.verde("=".repeat(39)));
                System.out.print("Escolha: ");

                String entrada = scanner.nextLine();
                int op;

                try {
                    clear();
                    op = Integer.parseInt(entrada);
                } catch (NumberFormatException e) {
                    System.out.println("Erro: digite apenas números.");
                    continue;
                }

                switch (op) {
                    case 1: {
                        try {
                            clear();
                            if (!dao.verificaSeJaExiste("Neymar")) {
                                dao.inserir(new Jogador("Neymar", "Atacante", "Santos", 10));
                            } else {
                                System.out.println("Jogador de teste já foi adicionado");
                            }

                        } catch (SQLException e) {
                            System.out.println("Erro: A tabela não existe" + e.getMessage());
                        }
                    }
                        break;

                    case 2: {
                        try {
                            clear();
                            System.out.print("Nome: ");
                            String nome = scanner.nextLine().toUpperCase();
                            System.out.print("Posição: ");
                            String posicao = scanner.nextLine();
                            System.out.print("Clube: ");
                            String clube = scanner.nextLine();
                            System.out.println("Camisa: ");
                            int camisa = scanner.nextInt();
                            Jogador jogador = new Jogador(nome, posicao, clube, camisa);

                            if (!dao.verificaSeJaExiste(nome)) {

                                dao.inserir(jogador);
                            } else {
                                System.out.println(nome + " já está na lista");
                            }

                        } catch (SQLException e) {
                            System.out.println("Erro: " + e.getMessage());
                        }
                    }
                        break;

                    case 3: {
                        try {
                            clear();
                            List<Jogador> lista = dao.listar();
                            System.out.println(c.vermelho("-".repeat(77)));
                            System.out.printf(c.vermelho("|") + "%-5s | %-20s | %-10s | %-20s | %-8s" + c.vermelho("|"),
                                    "ID", "Nome", "Posicao", "Clube", "Camisa");
                            System.out.println();
                            System.out.println(c.vermelho("-".repeat(77)));

                            for (Jogador j : lista) {
                                System.out.printf(
                                        c.vermelho("|") + "%-5d | %-20s | %-10s | %-20s | %-8s" + c.vermelho("|"),
                                        j.getId(),
                                        j.getNome(),
                                        j.getPosicao(),
                                        j.getClube(),
                                        j.getCamisa());
                                System.out.println();
                            }
                            System.out.println(c.vermelho("-".repeat(77)));

                        } catch (SQLException e) {
                            System.out.println("Erro: A tabela não existe");
                        }
                    }
                        break;
                    case 4: {
                        try {
                            clear();
                            System.out.print("Jogador a buscar: ");
                            String nomeBusca = scanner.nextLine();

                            Jogador j = dao.buscarPorNome(nomeBusca);

                            if (j != null) {
                                System.out.println("Encontrado:");
                                System.out.printf("ID %d | %s | %s | %s |%d\n", j.getId(), j.getNome(), j.getPosicao(),
                                        j.getClube(), j.getCamisa());

                            } else
                                System.out.println("Não encontrado.");

                        } catch (SQLException e) {
                            System.out.println("Erro: A tabela não existe");
                        }

                    }
                        break;
                    case 5: {
                        try {
                            clear();
                            System.out.print("Clube a buscar: ");
                            String clubeBusca = scanner.nextLine();

                            Jogador j = dao.buscarClube(clubeBusca);

                            if (j != null) {
                                System.out.println("Encontrado:");
                                System.out.printf("ID %d | %s | %s | %s |%d\n", j.getId(), j.getNome(), j.getPosicao(),
                                        j.getClube(), j.getCamisa());

                            } else
                                System.out.println("Não encontrado.");

                        } catch (SQLException e) {
                            System.out.println("Erro: A tabela não existe");
                        }

                    }
                        break;
                    case 6: {
                        try {
                            clear();
                            System.out.print("ID do jogador a atualizar: ");
                            int id = scanner.nextInt();
                            scanner.nextLine();

                            if (dao.verificaID(id)) {

                                System.out.print("Novo jogador: ");
                                String nome = scanner.nextLine();

                                System.out.print("Nova posicao: ");
                                String posicao = scanner.nextLine();

                                System.out.print("Novo clube: ");
                                String clube = scanner.nextLine();

                                System.out.print("Nova camisa: ");
                                int camisa = scanner.nextInt();

                                dao.atualizar(new Jogador(id, nome, posicao, clube, camisa));
                            } else {
                                System.out.println("Este ID não existe");
                            }

                        } catch (SQLException e) {
                            System.out.println("Erro: A tabela não existe");
                        }
                    }
                        break;

                    case 7: {
                        try {
                            clear();

                            System.out.print("ID do jogador a deletar: ");
                            int id = scanner.nextInt();
                            scanner.nextLine();

                            if (dao.verificaID(id)) {

                                dao.deletar(id);
                                System.out.println("Jogador deletado");

                            } else {
                                System.out.println("Este ID não existe");
                            }
                        } catch (SQLException e) {
                            System.out.println("Erro: A tabela não existe");
                        }
                    }
                        break;

                    case 8: {
                        try {
                            clear();
                            fileDAO.gravarJogadores();
                        } catch (Exception e) {
                            System.out.println("Erro: A tabela não existe");
                        }
                    }
                        break;

                    case 9: {
                        try {
                            clear();
                            dao.limparTabela();
                        } catch (SQLException e) {
                            System.out.println(e + "Erro: A tabela não existe");
                        }
                    }
                        break;

                    case 10: {
                        try {
                            clear();
                            List<Jogador> lista = dao.listarOrdemAlfabetica();
                            System.out.println(c.vermelho("-".repeat(77)));
                            System.out.printf(c.vermelho("|") + "%-5s | %-20s | %-10s | %-20s | %-8s" + c.vermelho("|"),
                                    "ID", "Nome", "Posicao", "Clube", "Camisa");
                            System.out.println();
                            System.out.println(c.vermelho("-".repeat(77)));

                            for (Jogador j : lista) {
                                System.out.printf(
                                        c.vermelho("|") + "%-5d | %-20s | %-10s | %-20s | %-8s" + c.vermelho("|"),
                                        j.getId(),
                                        j.getNome(),
                                        j.getPosicao(),
                                        j.getClube(),
                                        j.getCamisa());
                                System.out.println();
                            }
                            System.out.println(c.vermelho("-".repeat(77)));

                        } catch (SQLException e) {
                            System.out.println("Erro: A tabela não existe");
                        }
                    }
                        break;

                    case 11: {
                        try {
                            clear();
                            List<Jogador> lista = dao.listarOrdemCamisa();
                            System.out.println(c.vermelho("-".repeat(77)));
                            System.out.printf(c.vermelho("|") + "%-5s | %-20s | %-10s | %-20s | %-8s" + c.vermelho("|"),
                                    "ID", "Nome", "Posicao", "Clube", "Camisa");
                            System.out.println();
                            System.out.println(c.vermelho("-".repeat(77)));

                            for (Jogador j : lista) {
                                System.out.printf(
                                        c.vermelho("|") + "%-5d | %-20s | %-10s | %-20s | %-8s" + c.vermelho("|"),
                                        j.getId(),
                                        j.getNome(),
                                        j.getPosicao(),
                                        j.getClube(),
                                        j.getCamisa());
                                System.out.println();
                            }
                            System.out.println(c.vermelho("-".repeat(77)));

                        } catch (SQLException e) {
                            System.out.println("Erro: A tabela não existe");
                        }
                    }
                        break;

                    case 0: {
                        scanner.close();
                        return;
                    }
                    default:
                        System.out.println("Opção inválida.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}
