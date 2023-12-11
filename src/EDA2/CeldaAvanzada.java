/*
 * 2º Ingeniería Informática UVA - Estructura de Datos y Algoritmos
 * Autores:
 * Carolina de las Heras Clavier
 * David de la Calle Azahares
 * Grupo de laboratorio:K7
 */

package EDA2;


public class CeldaAvanzada implements Celda {
    private boolean[][] conductor;
    // Se ha omitido la creación de un array de visitados, pues quedaba inutilizado con Disjoint sets
    private boolean hayCortoCircuito;
    private int iAnterior, jAnterior; //Variables para el toString
    private final int[] vecinosX = {-1, -1, -1, 1, 1, 1, 0, 0}; //Arrays finales para recorrer las celdas vecinas a estudiar
    private final int[] vecinosY = {-1, 0, 1, -1, 0, 1, -1, 1};
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
        conductor = new boolean[n][n];  ////////////////////// n * m
        hayCortoCircuito = false;
        disjointSet = new DisjointSet(n * n);  ////////////////////// n * m
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
            for (int i = 0; i < vecinosX.length; i++) { //Registrar a todos los vecinos llamando de forma recursiva.
                sigCeldaX = fil + vecinosX[i];
                sigCeldaY = col + vecinosY[i];
                if (!(sigCeldaX < 0 || sigCeldaY < 0 || sigCeldaX >= conductor.length || sigCeldaY >= conductor[0].length)) { // || visitado[sigCeldaX][sigCeldaY]
                    if (conductor[fil + vecinosX[i]][col + vecinosY[i]]) {
                        disjointSet.union(fil * conductor.length + col, (fil + vecinosX[i]) * conductor.length + (col + vecinosY[i]));
                        hayCortoCircuito = disjointSet.isCortocircuito();
                        if (hayCortoCircuito) {break;}
                    }
                }
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
            for (int j = 0; j < conductor[0].length; j++)
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

    /**
     * Constructor de la clase RangosMinimosYMaximos, recibe un rango un mínimo y un máximo y los establece para la
     * celda que haya sido destinado
     *
     * @param rango El nivel de jerarquía en el que está el nodo en su grupo del DisjointSet
     * @param max La máxima fila a la que puede llegar el grupo al que pertenece este nodo
     * @param min La mínima fila a la que puede llegar el grupo al que pertenece este nodo
     */
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

    /**
     * Como cada rango ya se inicializa a 0 y solo puede incrementar en 1 cada iteración es más util un incrementador
     * de rango
     */
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
    private final int[] padres;
    private final RangosMinimosYMaximos[] rangosMinsMax;
    private boolean cortocircuito;
    private int nFil;
    private int nCol;

    /**
     * Constructor de la clase DisjointSet, llama a inicializarRangos.
     * Recibe nFil y nCol, el número de elementos de la matriz
     *
     * @param nFil Nº de filas del DisjointSet
     * @param nCol Nº de columnas del DisjointSet
     */
    public DisjointSet(int nFil, int nCol) {
        this.nFil = nFil;
        this.nCol = nCol;
        padres = new int[nFil * nCol];
        rangosMinsMax = new RangosMinimosYMaximos[nFil * nCol];
        inicializarRangos();
    }

    /**
     * Ayuda al constructor creando todas las celdas con el tipo de objeto RangosMinimosYMaximos, los inicializa a
     * rango 0, y mínimo y máximo a su misma celda, pues en un principio cada celda conductora no está unida a
     * ninguna otra y, por lo tanto, su mínimo y máximo son ella misma.
     */
    private void inicializarRangos() {
        for (int i = 0; i < rangosMinsMax.length; i++) {
            rangosMinsMax[i] = new RangosMinimosYMaximos(0, i / nCol, i / nCol);
            padres[i] = i;
        }
    }

    /**
     * Se encarga de buscar recursivamente al padre de un nodo en concreto dada su posición (identificador) en el array
     * para devolver finalmente el padre de todos los padres, el que ocupa la cúspide de la relación de jerarquía.
     * Algo muy importante es que se aplica pathCompression cuando se encuentra a un padre que estaba por encima del
     * actual en el grupo. Es tan importante porque ahorra incontables iteraciones de esta función, haciendo que el
     * último padre siempre esté a una iteración como máximo
     *
     * @param x Identificador del nodo del que se quiere encontrar el padre.
     * @return padres[x] Se devuelve al padre de dicha celda como valor en el array de padres.
     */
    public int encontrarUPadre(int x) { //Se usa para encontrar al padre "definitivo" de cada celda.
        if (x != padres[x]) {
            padres[x] = encontrarUPadre(padres[x]); // APLICAR PATH COMPRESSION i = encontrarUPadre(i);
        }
        return padres[x];
    }

    /**
     * La función más importante del código, se encarga de unir dos nodos (celdas) que son
     * vecinas y conductoras (comprobado previamente). Cada celda conductora tiene un grupo y el identificador de cada
     * grupo es el padre de sí mismo, el que ocupa la posición más alta en la relación de jerarquía (el lider).
     * Si el nuevo nodo tiene un minimo o máximo que es menor o mayor respectivamente que el del grupo al que se está
     * uniendo, el mínimo o máximo del grupo se actualiza.
     *
     * @param nuevaCelda Celda a añadir al grupo, es decir la que acaba de caer
     * @param celdaYaExistente Celda que ya formaba parte del tablero como conductora
     */
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

    /**
     * Función auxiliar de union, se encarga de actualizar los valores de los mínimos o máximos en el grupo.
     * Al final verifica si tras los cambios se ha producido un cortocircuito.
     *
     * @param hijo El nuevo hijo a añadir a la jerarquía
     * @param padre El padre sobre el que se van a actualizar los mínimos y máximos
     */
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
