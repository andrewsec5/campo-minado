import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static Random random = new Random();
    public static Scanner scanner = new Scanner(System.in);

    public static char[][] gerarCampoMinado(byte escala) {
        char[][] matriz = new char[escala][escala];
        //GERA O CAMPO MINADO EXIBIDO AO JOGADOR
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j] = '#';
            }
        }
        return matriz;
    }

    public static char[][] gerarCampoBombas(byte escala, int bombas) {
        char[][] matriz = new char[escala][escala];
        int bombasProntas = 0;
        //GERA A POSIÇÃO DAS BOMBAS
        for (int i = 0; i < escala; i++) {
            for (int j = 0; j < escala; j++) {
                matriz[i][j] = 'O';
            }
        }
        while (bombasProntas < bombas) {
            int iAleatorio = random.nextInt(escala);
            int jAleatorio = random.nextInt(escala);

            if (matriz[iAleatorio][jAleatorio] != 'X') {
                matriz[iAleatorio][jAleatorio] = 'X';
                bombasProntas++;
            }
        }
        return matriz;
    }

    public static void exibirCampoMinado(char[][] campoMinado) {
        //EXIBE O CAMPO MINADO COM AS POSIÇÕES CONHECIDAS PELO JOGADOR
        for (int i = 0; i < campoMinado[0].length; i++) {
            if (i == 0) System.out.print("    01");
            else if ((i + 1) < 10) System.out.print(" 0" + (i + 1));
            else System.out.print(" " + (i + 1));
        }
        System.out.println();
        for (int i = 0; i < campoMinado.length; i++) {
            if ((i + 1) < 10) System.out.printf("0%d: ", (i + 1));
            else System.out.printf("%d: ", (i + 1));
            for (int j = 0; j < campoMinado[i].length; j++) {
                System.out.printf("%2s ", campoMinado[i][j]);
            }
            System.out.println();
        }
    }

    public static byte selecionarEscala() {
        byte escolha;
        boolean validacao = false;
        byte escala = 0;
        //MENU INICIAL DE ESCOLHA DE DIFICULDADE/ESCALA DO CAMPO MINADO
        while (!validacao) {
            try {
                System.out.println("\nEscolha o nivel de dificuldade: ");
                System.out.println("1 - Fácil (9x9 - 10 bombas)");
                System.out.println("2 - Médio (16x16 - 40 bombas)");
                System.out.println("3 - Díficil (22x22 - 99 bombas)");
                System.out.println("4 - Impossível (50x50 - 500 bombas)");

                escolha = scanner.nextByte();
                if (escolha == 1) escala = 9;
                if (escolha == 2) escala = 16;
                if (escolha == 3) escala = 22;
                if (escolha == 4) escala = 50;
                if (escala != 0) validacao = true;
                else System.out.println("Opção inválida! Digite 1, 2, 3 ou 4.");
            } catch (InputMismatchException e) {
                System.out.println("Opção inválida! Digite 1, 2, 3 ou 4.");
                scanner.nextLine();
            }
        }
        return escala;
    }

    public static int validarEscolha(String msg, int escala) {
        int escolha = 0;
        boolean validacao = false;
        //TRATA A ENTRADA DE DADOS
        while (!validacao) {
            try {
                System.out.print(msg);
                escolha = scanner.nextInt();
                if ((escolha - 1) >= 0 && (escolha - 1) < escala) {
                    validacao = true;
                } else System.out.println("Opção inválida!");
            } catch (InputMismatchException e) {
                System.out.println("Opção inválida!");
            }
        }
        return (escolha - 1);
    }

    public static int bombasAoRedor(int linhaSelecionada, int colunaSelecionada, char[][] campoBombas, char[][] campoMinado, int escala) {
        int bombas = 0;
        int pontuacao = 1;
        //RETORNA PARA EVITAR RECURSÃO INFINITA
        if (campoMinado[linhaSelecionada][colunaSelecionada] != '#') return 0;

        //TESTAR LINHA DE CIMA
        if ((linhaSelecionada) > 0) {
            if (colunaSelecionada > 0 && campoBombas[linhaSelecionada - 1][colunaSelecionada - 1] == 'X') {
                bombas++;
            }
            if (campoBombas[linhaSelecionada - 1][colunaSelecionada] == 'X') {
                bombas++;
            }
            if (colunaSelecionada < (escala - 1) && campoBombas[linhaSelecionada - 1][colunaSelecionada + 1] == 'X') {
                bombas++;
            }
        }
        //TESTAR LINHA
        if (colunaSelecionada > 0 && campoBombas[linhaSelecionada][colunaSelecionada - 1] == 'X') {
            bombas++;
        }
        if (colunaSelecionada < (escala - 1) && campoBombas[linhaSelecionada][colunaSelecionada + 1] == 'X') {
            bombas++;
        }
        //TESTAR LINHA DE BAIXO
        if ((linhaSelecionada) < (escala - 1)) {
            if (colunaSelecionada > 0 && campoBombas[linhaSelecionada + 1][colunaSelecionada - 1] == 'X') {
                bombas++;
            }
            if (campoBombas[linhaSelecionada + 1][colunaSelecionada] == 'X') {
                bombas++;
            }
            if (colunaSelecionada < (escala - 1) && campoBombas[linhaSelecionada + 1][colunaSelecionada + 1] == 'X') {
                bombas++;
            }
        }
        //MUDA A POSIÇÃO SELECIONADA DE # PARA O TOTAL DE BOMBAS EM VOLTA
        campoMinado[linhaSelecionada][colunaSelecionada] = (char) (bombas + '0');
        //SE SEM BOMBAS EM VOLTA, REVELA AUTOMATICAMENTE OS CAMPOS AO REDOR
        if (bombas == 0) {
            if (linhaSelecionada > 0) {
                pontuacao += bombasAoRedor(linhaSelecionada - 1, colunaSelecionada, campoBombas, campoMinado, escala);
                if (colunaSelecionada > 0) {
                    pontuacao += bombasAoRedor(linhaSelecionada - 1, colunaSelecionada - 1, campoBombas, campoMinado, escala);
                }
                if (colunaSelecionada < (escala - 1)) {
                    pontuacao += bombasAoRedor(linhaSelecionada - 1, colunaSelecionada + 1, campoBombas, campoMinado, escala);
                }
            }
            if (colunaSelecionada > 0) {
                pontuacao += bombasAoRedor(linhaSelecionada, colunaSelecionada - 1, campoBombas, campoMinado, escala);
            }
            if (colunaSelecionada < (escala - 1)) {
                pontuacao += bombasAoRedor(linhaSelecionada, colunaSelecionada + 1, campoBombas, campoMinado, escala);
            }
            if (linhaSelecionada < (escala - 1)) {
                pontuacao += bombasAoRedor(linhaSelecionada + 1, colunaSelecionada, campoBombas, campoMinado, escala);
                if (colunaSelecionada > 0) {
                    pontuacao += bombasAoRedor(linhaSelecionada + 1, colunaSelecionada - 1, campoBombas, campoMinado, escala);
                }
                if (colunaSelecionada < (escala - 1)) {
                    pontuacao += bombasAoRedor(linhaSelecionada + 1, colunaSelecionada + 1, campoBombas, campoMinado, escala);
                }
            }
        }
        return pontuacao;
    }

    public static void fim(char[][] campoMinado, char[][] campoBombas) {
        //REVELA A POSIÇÃO DE TODAS AS BOMBAS
        for (int i = 0; i < campoMinado.length; i++) {
            for (int j = 0; j < campoMinado[0].length; j++) {
                if (campoBombas[i][j] == 'X') campoMinado[i][j] = 'X';
            }
        }
    }

    public static int quantidadeBombas(byte escala) {
        int bombas = 0;
        //DEFINE O TOTAL DE BOMBAS DE ACORDO COM A ESCALA
        switch (escala) {
            case 9 -> bombas = 10;
            case 16 -> bombas = 40;
            case 22 -> bombas = 99;
            case 50 -> bombas = 500;
        }
        return bombas;
    }

    public static void jogo(byte escala, int bombas, char[][] campoMinado, char[][] campoBombas) {
        boolean fim = false;
        int linhaSelecionada = 0;
        int colunaSelecionada = 0;
        int pontuacao = 0;
        
        while (!fim) {
            exibirCampoMinado(campoMinado);
            System.out.println();
            linhaSelecionada = validarEscolha("Selecione uma linha: ", escala);
            colunaSelecionada = validarEscolha("Selecione uma coluna: ", escala);
            //CASO HAJA BOMBA NO LOCAL, JOGO ENCERRA
            if (campoBombas[linhaSelecionada][colunaSelecionada] == 'X') {
                System.out.println();
                fim(campoMinado, campoBombas);
                exibirCampoMinado(campoMinado);
                System.out.println("BOOM!!!");
                System.out.println("Você perdeu...\nPontuação: " + pontuacao);
                fim = true;
                fim(campoMinado, campoBombas);
            }//SE NÃO HOUVER BOMBA, REVELA A QUANTIDADE DE BOMBAS AO REDOR
            else if (campoMinado[linhaSelecionada][colunaSelecionada] == '#') {
                pontuacao += bombasAoRedor(linhaSelecionada, colunaSelecionada, campoBombas, campoMinado, escala);
            }//CASO NÃO HAJA MAIS BOMBAS, ENCERRA O JOGO
            if (pontuacao == (escala * escala) - bombas) {
                System.out.println();
                fim(campoMinado, campoBombas);
                exibirCampoMinado(campoMinado);
                System.out.println("Você venceu!\nPontuação: " + pontuacao);
                fim = true;
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        char[][] campoMinado;
        char[][] campoBombas;
        int bombas;
        byte escala;

        escala = selecionarEscala();

        bombas = quantidadeBombas(escala);

        campoMinado = gerarCampoMinado(escala);

        campoBombas = gerarCampoBombas(escala, bombas);

        jogo(escala, bombas, campoMinado, campoBombas);

    }
}
