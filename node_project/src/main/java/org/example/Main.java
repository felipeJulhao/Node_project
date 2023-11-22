package org.example;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int topology = 0;

        while (topology != 1 && topology != 2) {

            System.out.println("Informe a topologia da rede: ");
            System.out.print("(1)Ring  \n(2)Mesh-2D ");
            topology = sc.nextInt();

            if (topology != 1 && topology != 2) {
                System.out.println("Topologia invalida, por favor digite novamente");
            }
            if (topology == 1) {
                ringSession();
            }
            if (topology == 2) {
                mesh2dSession();
            }

        }

    }

    private static void ringSession() {

        int node = 0;
        int source = 0;
        int target = 0;
        int distanceLeft = 0;
        int distanceRight = 0;
        List<Integer> positionsList = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        System.out.print("Informe a quantidade de Nodos: ");
        node = sc.nextInt();
        validateNodes(node);

        while (true) {
            distanceLeft = 0;
            distanceRight = 0;
            System.out.print("Quem é o Source? ");
            source = sc.nextInt();
            System.out.print("Quem é o Target? ");
            target = sc.nextInt();

            if (validateNodeValues(source, target, node)) {
                System.out.println("Nao existem nodes SUFICIENTES");
                break;
            }
            System.out.println("Proc[" + source + "] criou a mensagem");
            positionsList.addAll(sortPositionList(node, positionsList));

            System.out.println("positions: " + positionsList);

            for (int positionRight = source; positionRight < positionsList.size(); positionRight++) {
                distanceRight++;
                if (positionsList.get(positionRight) == target) {
                    break;
                }
            }

            for (int positionLeft = source; positionLeft < positionsList.size(); positionLeft--) {
                distanceLeft++;
                if (positionLeft == 0) {
                    //valor negativo
                    break;
                }
                if (positionsList.get(positionLeft) == target) {
                    break;
                }
            }

            if (source == target) {
                targetMessage(source);
            }

            if (distanceRight <= distanceLeft) {
                System.out.println("moved to right");
                for (int i = source + 1; i < positionsList.size(); i++) {
                    int previuosSource = i - 1;
                    if (i - 1 == source) {
                        System.out.println("Proc[" + positionsList.get(previuosSource) + "] enviou a mensagem para Proc[" + positionsList.get(i) + "]");
                        System.out.println("Proc[" + positionsList.get(i) + "] recebeu a mensagem de Proc[" + positionsList.get(previuosSource) + "]");
                    }

                    if (positionsList.get(i) == target) {
                        targetMessage(positionsList.get(i));
                        break;
                    } else {
                        System.out.println("Proc[" + positionsList.get(i) + "] Nao eh o destino");
                        int nextPosition = positionsList.get(i) + 1;
                        System.out.println("Proc[" + positionsList.get(i) + "] Enviou a mensagem para Proc[" + nextPosition + "]");
                    }

                }

            } else {
                System.out.println("moved to left");
                int previousSource = source - 1;
                System.out.println("Proc[" + source + "] enviou a mensagem para Proc[" + previousSource + "]");
                for (int i = previousSource; i >= target; i--) {
                    int previousPosition = i + 1;
                    System.out.println("Proc[" + i + "] recebeu a mensagem de Proc[" + previousPosition + "]");

                    if (i == target) {
                        targetMessage(i);
                        break;
                    } else {
                        System.out.println("Proc[" + i + "] Nao eh o destino");
                        int nextPosition = i - 1;
                        System.out.println("Proc[" + i + "] Enviou a mensagem para Proc[" + nextPosition + "]");
                    }
                }
            }
        }
    }

    private static void mesh2dSession() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Informe o tamanho da NoC para X:");
        int tamanhoX = scanner.nextInt();

        System.out.println("Informe o tamanho da NoC para Y:");
        int tamanhoY = scanner.nextInt();

        while (validarTamanhoNocXY(tamanhoX, tamanhoY)) {
            System.out.println("Tamanho da matriz máximo 9x9");

            System.out.println("Informe o tamanho da NoC para X:");
            tamanhoX = scanner.nextInt();

            System.out.println("Informe o tamanho da NoC para Y:");
            tamanhoY = scanner.nextInt();
        }

        System.out.println("Construção de pacotes:");

        System.out.println("Informe origem X: ");
        int origemX = scanner.nextInt();

        while (origemX > tamanhoX) {
            System.out.println("Origem X INVALIDA");
            System.out.println("Informe origem X: ");
            origemX = scanner.nextInt();
        }

        System.out.println("Informe origem Y: ");
        int origemY = scanner.nextInt();

        while (origemY > tamanhoY) {
            System.out.println("Origem Y INVALIDA");
            System.out.println("Informe origem Y: ");
            origemY = scanner.nextInt();
        }

        System.out.println("Informe destino X: ");
        int destinoX = scanner.nextInt();

        while (destinoX > tamanhoX) {
            System.out.println("Destino X INVALIDO");
            System.out.println("Informe destino X: ");
            destinoX = scanner.nextInt();
        }

        System.out.println("Informe destino Y: ");
        int destinoY = scanner.nextInt();

        while (destinoY > tamanhoY) {
            System.out.println("Destino Y INVALIDO");
            System.out.println("Informe destino Y: ");
            destinoY = scanner.nextInt();
        }

        Mesh2DNoc redeNoC = new Mesh2DNoc(tamanhoX, tamanhoY);

        Nodo origem = redeNoC.getNodo(origemX, origemY);
        Nodo destino = redeNoC.getNodo(destinoX, destinoY);

        System.out.println("Coordenadas da origem (x,y): " + origem);
        System.out.println("Coordenadas do destino (x,y): " + destino);
        System.out.println("==============================");

        printTableNoc(tamanhoX, tamanhoY, redeNoC);
        System.out.println("==============================");

        int y = origemY;
        int x = origemX;

        Nodo nodoAnterior = origem;
        Nodo proximo = redeNoC.getNodo(destinoX + 1, destinoY + 1);
        Nodo anterior = redeNoC.getNodo(destinoX - 1, destinoY - 1);

        if (origem.hashCode() <= destino.hashCode()) {

            while (redeNoC.getNodo(x, y) != proximo) {
                if (origem == redeNoC.getNodo(x, y)) {

                    String msgInicial = "Proc[" + redeNoC.getNodo(x, y) + "] criou a mensagem";
                    System.out.println(msgInicial);
                } else {

                    System.out.println("Proc[" + nodoAnterior + "] enviou a mensagem para o Proc[" + redeNoC.getNodo(x, y) + "]");
                    System.out.println("Proc[" + redeNoC.getNodo(x, y) + "] recebeu a mensagem de Proc[" + nodoAnterior + "]");
                    nodoAnterior = redeNoC.getNodo(x, y);

                    if (destino == redeNoC.getNodo(x, y)) {
                        System.out.println("Proc[" + redeNoC.getNodo(x, y) + "] é o destino");
                        System.out.println("Proc[" + redeNoC.getNodo(x, y) + "] consumiu a mensagem");
                        return;
                    } else {
                        System.out.println("Proc[" + redeNoC.getNodo(x, y) + "] NÃO é o destino");
                    }

                }

                if (destino.getX() != x) {
                    x++;
                } else {
                    y++;
                }
            }

        } else {
            while (redeNoC.getNodo(x, y) != anterior) {
                if (origem == redeNoC.getNodo(x, y)) {

                    String msgInicial = "Proc[" + redeNoC.getNodo(x, y) + "] criou a mensagem";
                    System.out.println(msgInicial);
                } else {

                    System.out.println("Proc[" + nodoAnterior + "] enviou a mensagem para o Proc[" + redeNoC.getNodo(x, y) + "]");
                    System.out.println("Proc[" + redeNoC.getNodo(x, y) + "] recebeu a mensagem de Proc[" + nodoAnterior + "]");
                    nodoAnterior = redeNoC.getNodo(x, y);

                    if (destino == redeNoC.getNodo(x, y)) {
                        System.out.println("Proc[" + redeNoC.getNodo(x, y) + "] é o destino");
                        System.out.println("Proc[" + redeNoC.getNodo(x, y) + "] consumiu a mensagem");
                        return;
                    } else {
                        System.out.println("Proc[" + redeNoC.getNodo(x, y) + "] NÃO é o destino");
                    }

                }

                if (destino.getX() != x) {
                    x--;
                } else {
                    y--;
                }
            }
        }
    }

    private static boolean validarTamanhoNocXY(int x, int y) {
        return x > 9 || y > 9 || x < 0 || y < 0;
    }

    private static void printTableNoc(int tamanhoX, int tamanhoY, Mesh2DNoc redeNoC) {
        System.out.println("Imprime Rede NoC: ");
        for (int x = 0; x < tamanhoX; x++) {
            for (int y = 0; y < tamanhoY; y++) {
                System.out.print(redeNoC.getNodo(x, y) + " ");
            }
            System.out.println();
        }
    }

    private static void validateNodes(int node) {
        if (node < 2 || node > 10) {
            System.out.println("Informe um NODE valido entre 2 e 10");
        }
    }

    private static boolean validateNodeValues(int source, int target, int node) {
        boolean sourceValueUpper = source >= node;
        boolean targetValueUpper = target >= node;
        return sourceValueUpper || targetValueUpper;
    }

    private static List<Integer> sortPositionList(int node, List<Integer> positions) {
        positions.clear();
        for (int i = 0; i < node; i++) {
            positions.add(i);
        }
        List<Integer> sortedPositions = new ArrayList<>();
        sortedPositions.addAll(positions);
        sortedPositions.addAll(positions);

        positions.clear();
        return sortedPositions;
    }

    private static void targetMessage(int actualPosition) {
        System.out.println("Proc[" + actualPosition + "] Eh o destino");
        System.out.println("Proc[" + actualPosition + "] consumiu a mensagem");
    }

    private static boolean validateMatrizLenght(int x, int y) {
        return x > 9 || y > 9;
    }

}