/*
 * Autores:
 * Carolina de las Heras Clavier
 * David de la Calle Azahares
 */

package EDA2;



public class CeldaAvanzada implements Celda {
    private boolean[][] conductor;
    private boolean[][] visitado;
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
        conductor = new boolean[n][n];
        visitado = new boolean[n][n];
        filaSuperior = false;
        filaInferior = false;
        hayCortoCircuito = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                //conductor[i][j] = null;
                //for j
            }//for i
        }//Inicializar
    }

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
        iAnterior = fil;
        jAnterior = col;
        hayCortoCircuito = false;
        int sigCeldaX, sigCeldaY;

        //resetVisitados();

        DisjointSet disjointSet = new DisjointSet(conductor.length * conductor.length);
        conductor[fil][col] = true; //Al caer el rayo la celda pasa a ser conductora
        for (int i = 0; i < vecinosX.length; i++) { //Registrar a todos los vecinos llamando dde forma recursiva.
            sigCeldaX = fil + vecinosX[i];
            sigCeldaY = col + vecinosY[i];
            if (!(sigCeldaX < 0 || sigCeldaY < 0 || sigCeldaX >= conductor.length || sigCeldaY >= conductor[0].length)) { // || visitado[sigCeldaX][sigCeldaY]
                if (conductor[fil + vecinosX[i]] [col + vecinosY[i]])
                    disjointSet.union(fil * conductor.length + col, (fil + vecinosX[i]) * conductor.length + (col + vecinosY[i]));
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


//    /**
//     * Una función void muy sencilla, simplemente resetea todos los valores de la matriz visitados a falso. Es una
//     * función porque se necesita en varios puntos del código.
//     */
//    public void resetVisitados() {
//        for (int i = 0; i < visitado.length; i++) {
//            for (int j = 0; j < visitado.length; j++)
//                visitado[i][j] = false;
//            //for j
//        }//for i
//    }
}//class

class RangosMinimosYMaximos {
    private int rango;
    private int max;
    private int min;

    public RangosMinimosYMaximos(int rango, int max, int min) {
        this.rango = rango;
        this.max = max;
        this.min = min;
    }

    public int getRango() {
        return rango;
    }
    public int getMax() {
        return max;
    }
    public int getMin() {
        return min;
    }

    public void incrementarRango() {
        this.rango++;
    }
    public void setRango(int rango) {
        this.rango = rango;
    }
    public void setMax(int max) {
        this.max = max;
    }
    public void setMin(int min) {
        this.min = min;
    }
}


class DisjointSet {
    private int[] parent;
    private RangosMinimosYMaximos[] rangosMinsMax;
    private int max;
    private int min;
    private int size;

    public DisjointSet(int size) {
        parent = new int[size];
        rangosMinsMax = new RangosMinimosYMaximos[size];
        this.size = size;
        inicializarRangos();
        makeSet();
    }

    private void makeSet() {
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
            rangosMinsMax[i].setRango(0);
        }
    }

    private void inicializarRangos() {
        for (int i = 0; i < rangosMinsMax.length; i++) {
            rangosMinsMax[i].setMax(i / (int) (Math.sqrt(rangosMinsMax.length)));
            rangosMinsMax[i].setMin(i / (int) (Math.sqrt(rangosMinsMax.length)));
        }
    }

    // fil * conductor.length + col (Posición por fila y columna)

    public int findUParent(int x) { //Se usa para encontrar al padre de cada disjoint set.
        if (x != parent[x]) {
            parent[x] = findUParent(parent[x]); // APLICAR PATH COMPRESSION parent[i] = find(i);
        }
        return parent[x];
    }

    public void union(int nuevaCelda, int celdaYaExistente) { ///////////////////////////////////////
        int rootX = findUParent(nuevaCelda);
        int rootY = findUParent(celdaYaExistente);

        if (rootX != rootY) {
            if (rangosMinsMax[rootX].getRango() > rangosMinsMax[rootY].getRango()) {
                parent[rootY] = rootX;
            } else if (rangosMinsMax[rootX].getRango() < rangosMinsMax[rootY].getRango()) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rangosMinsMax[rootX].incrementarRango();
            }
        }
    }

    public void actualizarMinMax(int hijo, int padre) {
        //rangosMinsMax[padre].setMax() = (rangosMinsMax[hijo].getMax() > rangosMinsMax[rootX].getMax());

    }
}



