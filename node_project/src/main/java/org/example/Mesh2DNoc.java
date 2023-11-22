package org.example;

public class Mesh2DNoc {

    private final int tamanhoX;
    private final int tamanhoY;
    private final Nodo[][] nodos;

    public Mesh2DNoc(int tamanhoX, int tamanhoY) {
        this.tamanhoX = tamanhoX;
        this.tamanhoY = tamanhoY;
        this.nodos = new Nodo[tamanhoX][tamanhoY];

        // Inicializar nodos
        for (int x = 0; x < tamanhoX; x++) {
            for (int y = 0; y < tamanhoY; y++) {
                nodos[x][y] = new Nodo(x, y);
            }
        }
    }

    public Nodo getNodo(int x, int y) {
         if (x >= 0 && x < tamanhoX && y >= 0 && y < tamanhoY) {
            return nodos[x][y];
        } else {
            System.out.println("Índices fora dos limites da matriz.");
            return null;
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < tamanhoX; x++) {
            for (int y = 0; y < tamanhoY; y++) {
                sb.append(nodos[x][y]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
