package br.pro.ramon.conecta4;

import java.util.Scanner;

public class Program {

    public static final Scanner CONSOLE = new Scanner(System.in);

    public static final String VAZIO = " ";
    public static final String JOGADOR_1 = "O";
    public static final String JOGADOR_2 = "X";
    public static final String EMPATE = "EMPATE";

    public static void main(String[] args) {
        String[][] tabuleiro = criaTabuleiro();
        String jogador = JOGADOR_1;
        String ganhador = VAZIO;

        boolean terminou = false;
        do {
            mostraTabuleiro(tabuleiro);
            String jogada = pedeJogada(jogador, tabuleiro);
            int i = fazJogada(jogador, jogada, tabuleiro);
            ganhador = verificaSeGanhou(jogador, jogada, i, tabuleiro);
            if (!ganhador.equals(VAZIO)) {
                break;
            }
            jogador = trocaJogador(jogador);
        } while (!terminou);

        mostraTabuleiro(tabuleiro);
        System.out.println("Ganhador: " + ganhador);
    }

    public static String[][] criaTabuleiro() {
        String[][] tabuleiro = new String[6][7];

        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro[i].length; j++) {
                tabuleiro[i][j] = VAZIO;
            }
        }

        return tabuleiro;
    }

    public static void mostraTabuleiro(String[][] tabuleiro) {
        System.out.println();
        System.out.println("+---+---+---+---+---+---+---+");
        for (int i = 0; i < tabuleiro.length; i++) {
            System.out.print("|");
            for (int j = 0; j < tabuleiro[i].length; j++) {
                System.out.print(" " + tabuleiro[i][j] + " |");
            }
            System.out.println();
            System.out.println("+---+---+---+---+---+---+---+");
        }
        System.out.println("  A | B | C | D | E | F | G");
    }

    public static String pedeJogada(String jogador, String[][] tabuleiro) {
        String jogada;

        System.out.println();
        System.out.println("É a vez do " + jogador + "!");

        boolean valida = false;
        do {
            System.out.print("Onde você quer jogar? ");
            jogada = CONSOLE.nextLine();

            jogada = jogada.toUpperCase();

            if (jogadaValida(jogada, tabuleiro)) {
                valida = true;
            } else {
                System.out.println("Jogada inválida! Tente novamente...");
            }
        } while (!valida);

        return jogada;
    }

    public static boolean jogadaValida(String jogada, String[][] tabuleiro) {
        boolean colunaValida = jogada.equals("A") || jogada.equals("B") || jogada.equals("C") || jogada.equals("D") || jogada.equals("E") || jogada.equals("F") || jogada.equals("G");
        int j = traduzJogadaParaColuna(jogada);
        return colunaValida && !colunaCheia(j, tabuleiro);
    }

    public static boolean colunaCheia(int j, String[][] tabuleiro) {
        return !tabuleiro[0][j].equals(VAZIO);
    }

    public static int fazJogada(String jogador, String jogada, String[][] tabuleiro) {
        int j = traduzJogadaParaColuna(jogada);
        int i = achaLinhaVazia(jogada, tabuleiro);
        tabuleiro[i][j] = jogador;
        return i;
    }

    public static int traduzJogadaParaColuna(String jogada) {
        switch (jogada) {
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
            case "E":
                return 4;
            case "F":
                return 5;
            case "G":
                return 6;
            default:
                return -1;
        }
    }

    public static int achaLinhaVazia(String jogada, String[][] tabuleiro) {
        int j = traduzJogadaParaColuna(jogada);

        for (int i = tabuleiro.length - 1; i >= 0; i--) {
            if (tabuleiro[i][j].equals(VAZIO)) {
                return i;
            }
        }

        return -1;
    }

    public static String verificaSeGanhou(String jogador, String jogada, int i, String[][] tabuleiro) {
        String ganhador = VAZIO;

        if (verificaEmpate(tabuleiro)) {
            ganhador = EMPATE;
        } else {
            int j = traduzJogadaParaColuna(jogada);
            if (verifica(jogador, tabuleiro, i, j)) {
                ganhador = jogador;
            }
        }

        return ganhador;
    }

    public static boolean verificaEmpate(String[][] tabuleiro) {
        for (int j = 0; j < tabuleiro[0].length; j++) {
            if (!colunaCheia(j, tabuleiro)) {
                return false;
            }
        }

        return true;
    }

    public static boolean verifica(String jogador, String[][] tabuleiro, int i, int j) {
        boolean ganhouNaLinha = verificaLinha(jogador, tabuleiro, i, j);
        boolean ganhouNaColuna = verificaColuna(jogador, tabuleiro, i, j);
        boolean ganhouNaDiagonalPrincipal = verificaDiagonalPrincipal(jogador, tabuleiro, i, j);
        boolean ganhouNaDiagonalSecundaria = verificaDiagonalSecundaria(jogador, tabuleiro, i, j);

        return ganhouNaLinha || ganhouNaColuna || ganhouNaDiagonalPrincipal || ganhouNaDiagonalSecundaria;
    }

    public static boolean verificaLinha(String jogador, String[][] tabuleiro, int i, int j) {
        boolean ganhou = false;

        int esquerda = j - 3;
        int direita = j + 3;

        int contador = 0;
        for (int c = esquerda; c <= direita; c++) {
            if (dentroTabuleiro(tabuleiro, i, c)) {
                if (tabuleiro[i][c].equals(jogador)) {
                    contador++;
                    if (contador == 4) {
                        ganhou = true;
                        break;
                    }
                } else {
                    contador = 0;
                }
            }
        }

        return ganhou;
    }

    public static boolean verificaColuna(String jogador, String[][] tabuleiro, int i, int j) {
        int contador = 0;

        int cima = i - 3;
        int baixo = i + 3;

        for (int l = cima; l <= baixo; l++) {
            if (dentroTabuleiro(tabuleiro, l, j)) {
                if (tabuleiro[l][j].equals(jogador)) {
                    contador++;
                    if (contador >= 4) {
                        break;
                    }
                } else {
                    contador = 0;
                }
            }
        }

        return contador >= 4;
    }

    public static boolean verificaDiagonalPrincipal(String jogador, String[][] tabuleiro, int i, int j) {
        boolean ganhou = false;

        int esquerda = j - 3;
        int direita = j + 3;
        int cima = i - 3;
        int baixo = i + 3;

        int contador = 0;
        for (int l = cima, c = esquerda; l <= baixo && c <= direita; l++, c++) {
            if (dentroTabuleiro(tabuleiro, l, c)) {
                if (tabuleiro[l][c].equals(jogador)) {
                    contador++;
                    if (contador == 4) {
                        ganhou = true;
                        break;
                    }
                } else {
                    contador = 0;
                }
            }
        }

        return ganhou;
    }

    public static boolean verificaDiagonalSecundaria(String jogador, String[][] tabuleiro, int i, int j) {
        boolean ganhou = false;

        int esquerda = j - 3;
        int direita = j + 3;
        int cima = i - 3;
        int baixo = i + 3;

        int contador = 0;
        for (int l = cima, c = direita; l <= baixo && c >= esquerda; l++, c--) {
            if (dentroTabuleiro(tabuleiro, l, c)) {
                if (tabuleiro[l][c].equals(jogador)) {
                    contador++;
                    if (contador == 4) {
                        ganhou = true;
                        break;
                    }
                } else {
                    contador = 0;
                }
            }
        }

        return ganhou;
    }

    public static boolean dentroTabuleiro(String[][] tabuleiro, int i, int j) {
        return i >= 0 && j >= 0 && i < tabuleiro.length && j < tabuleiro[i].length;
    }

    public static String trocaJogador(String jogador) {
        if (jogador.equals(JOGADOR_1)) {
            return JOGADOR_2;
        } else {
            return JOGADOR_1;
        }
    }

}
