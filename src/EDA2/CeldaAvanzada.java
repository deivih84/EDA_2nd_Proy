/*
 * Autores:
 * Carolina de las Heras Clavier
 * David de la Calle Azahares
 */

package EDA2;



public class CeldaAvanzada implements Celda {
    private DisjointSet[][] conductor;
    //private boolean[][] visitado;
    private boolean hayCortoCircuito;
    private int iAnterior, jAnterior; //Variables para el toString
    private boolean filaSuperior, filaInferior; //Booleanos para comprobar si hay cortocircuito
    private final int[] vecinosX = {-1, -1, -1, 0, 0, 1, 1, 1}; //Arrays finales para recorrer las celdas vecinas a estudiar
    private final int[] vecinosY = {-1, 0, 1, -1, 1, -1, 0, 1};


    public CeldaAvanzada() {
    }//CeldaSimple

    /**
     * Este método recibe el tamaño de las matrices conductor y visitado y las limpia o inicializa en todos sus valores
     * a falso.
     *
     * @param n Tamaño de las matrices
     */
    public void Inicializar(int n) {
        conductor = new DisjointSet[n][n];
        //visitado = new boolean[n][n];
        filaSuperior = false;
        filaInferior = false;
        hayCortoCircuito = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                conductor[i][j] = null;
            //for j
        }//for i
    }//Inicializar

    /**
     * Simplemente implementado para devolver un booleano.
     *
     * @return hayCortoCircuito True si se ha encontrado camino superior e inferior
     */
    public boolean Cortocircuito() {
        return hayCortoCircuito;
    }//Cortocircuito

    /**
     * Método casi principal que se encarga de reunir los resultados de caminoRecursivoSuperior y
     * caminoRecursivoInferior. Además, se llama a resetVisitados para limpiar en dos ocasiones la lista de visitados.
     *
     * @param fil Position actual en el eje x de la celda en la matriz de conductores.
     * @param col Position actual en el eje y de la celda en la matriz de conductores.
     */
    public void RayoCosmico(int fil, int col) {
        filaSuperior = false;
        filaInferior = false;
        iAnterior = fil;
        jAnterior = col;
        hayCortoCircuito = false;

        resetVisitados();

        DisjointSet disjointSet = new DisjointSet(conductor.length * conductor.length, fil, col);
        for (int i = 0; i < vecinosX.length; i++) {
            if (conductor[vecinosX[i]][vecinosY[i]] != null){

            }
        }
    }

    /**
     * Una sobrescritura del método toString de nuestra clase CeldaSimple. Se encarga de imprimir por pantalla una
     * representation de la matriz de conductores.
     *
     * @return s.toString(); La cadena de caracteres que representa la matriz de conductores.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < conductor.length; i++) {
            for (int j = 0; j < conductor.length; j++)
                if (i == iAnterior && j == jAnterior)
                    s.append("*");
                else
                    s.append(conductor[i][j] ? "X" : ".");
            s.append("\n");
        }//for i

        return s.toString();
    }//toString

    /**
     * Una función void muy sencilla, simplemente resetea todos los valores de la matriz visitados a falso. Es una
     * función porque se necesita en varios puntos del código.
     */
    public void resetVisitados() {
        for (int i = 0; i < visitado.length; i++) {
            for (int j = 0; j < visitado.length; j++)
                visitado[i][j] = false;
            //for j
        }//for i
    }
}//class

class DisjointSet {
    private int[] parent;
    private int[] rank;

    private int[] posicion;
    private int size;

    public DisjointSet(int size, int x, int y) {
        parent = new int[size];
        rank = new int[size];
        posicion = new int[2];
        posicion[0] = x;
        posicion[1] = y;
        this.size = size;
        makeSet();
    }

    private void makeSet() {
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) { //Se usa para encontrar al padre de cada disjoint set.
        if (x != parent[x]) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX != rootY) {
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }
}



