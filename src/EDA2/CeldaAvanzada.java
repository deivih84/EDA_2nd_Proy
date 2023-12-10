/*
 * 2ºIngeniería Informática UVA - Estructura de Datos y Algoritmos
 * Autores:
 * Carolina de las Heras Clavier
 * David de la Calle Azahares
 * Grupo de laboratorio:K7
 */

package EDA2;


public class CeldaAvanzada implements Celda {
    private boolean[][] conductor;
    private boolean hayCortoCircuito;
    private int iAnterior, jAnterior; //Variables para el toString
    private final int[] vecinosX = {-1, -1, -1, 0, 0, 1, 1, 1}; //Arrays finales para recorrer las celdas vecinas a estudiar
    private final int[] vecinosY = {-1, 0, 1, -1, 1, -1, 0, 1};
    private DisjointSet disjointSet;

    public CeldaAvanzada() {
    }//CeldaAvanzada

    /**
     * Metodo que recibe el tamaño de las matrices conductor y visitado y las limpia o inicializa en todos sus valores
     * a falso.
     *
     * @param n Tamaño de las matrices
     */
    public void Inicializar(int n) {
        conductor = new boolean[n][n];
        hayCortoCircuito = false;
        disjointSet = new DisjointSet(n * n);
    }

    /**
     * Metodo implementado para devolver un booleano.
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
        int sigCeldaX, sigCeldaY;

        if (!conductor[fil][col]) {
            conductor[fil][col] = true; //Al caer el rayo la celda pasa a ser conductora
            for (int i = 0; i < vecinosX.length; i++) { //Registrar a todos los vecinos llamando dde forma recursiva.
                sigCeldaX = fil + vecinosX[i];
                sigCeldaY = col + vecinosY[i];
                if (!(sigCeldaX < 0 || sigCeldaY < 0 || sigCeldaX >= conductor.length || sigCeldaY >= conductor[0].length)) { // || visitado[sigCeldaX][sigCeldaY]
                    if (conductor[fil + vecinosX[i]][col + vecinosY[i]])
                        disjointSet.union(fil * conductor.length + col, (fil + vecinosX[i]) * conductor.length + (col + vecinosY[i]));
                }
            }
        }
        hayCortoCircuito = disjointSet.isCortocircuito();
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
}

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

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }
}


class DisjointSet {
    private int[] padres;
    private RangosMinimosYMaximos[] rangosMinsMax;
    private boolean cortocircuito;

    public DisjointSet(int size) {
        padres = new int[size];
        rangosMinsMax = new RangosMinimosYMaximos[size];
        inicializarRangos();
    }

    private void inicializarRangos() {
        for (int i = 0; i < rangosMinsMax.length; i++) {
            rangosMinsMax[i] = new RangosMinimosYMaximos(0, i / (int) (Math.sqrt(rangosMinsMax.length)), i / (int) (Math.sqrt(rangosMinsMax.length)));
            padres[i] = i;
        }
    }

    // fil * conductor.length + col (Posición por fila y columna)

    public int encontrarUPadre(int x) { //Se usa para encontrar al padre "definitivo" de cada celda.
        if (x != padres[x]) {
            padres[x] = encontrarUPadre(padres[x]); // APLICAR PATH COMPRESSION parent[i] = find(i);
        }
        return padres[x];
    }

    public void union(int nuevaCelda, int celdaYaExistente) {
        int nCelda = encontrarUPadre(nuevaCelda); //nCelda <- Nueva celda
        int aCelda = encontrarUPadre(celdaYaExistente); //aCelda <- Antigua celda

        if (nCelda != aCelda) {
            if (rangosMinsMax[nCelda].getRango() > rangosMinsMax[aCelda].getRango()) {
                padres[aCelda] = nCelda;
                actualizarMinMax(aCelda, nCelda);
                rangosMinsMax[nCelda].incrementarRango();
            } else if (rangosMinsMax[nCelda].getRango() < rangosMinsMax[aCelda].getRango()) {
                padres[nCelda] = aCelda;
                actualizarMinMax(nCelda, aCelda);
                rangosMinsMax[aCelda].incrementarRango();
            } else {
                padres[aCelda] = nCelda;
                rangosMinsMax[nCelda].incrementarRango();
                actualizarMinMax(aCelda, nCelda);
            }
        }
    }

    public void actualizarMinMax(int hijo, int padre) {
        //Actualiza los valores de los mínimos y máximos si es necesario
        if (rangosMinsMax[hijo].getMax() > rangosMinsMax[padre].getMax()) {
            rangosMinsMax[padre].setMax(rangosMinsMax[hijo].getMax());
        }
        if (rangosMinsMax[hijo].getMin() < rangosMinsMax[padre].getMin()) {
            rangosMinsMax[padre].setMin(rangosMinsMax[hijo].getMin());
        }
        //Verificar si se ha producido el cortocircuito
        cortocircuito = rangosMinsMax[padre].getMin() == 0 && rangosMinsMax[padre].getMax() == (Math.sqrt(rangosMinsMax.length) - 1);
    }

    public boolean isCortocircuito() {
        return cortocircuito;
    }
}
