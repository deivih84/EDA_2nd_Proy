/*
 * Autores:
 * Carolina de las Heras Clavier
 * David de la Calle Azahares
 */

package EDA2;

import java.util.Arrays;
import java.util.Objects;

public class CeldaAvanzada implements Celda {
    private boolean[][] conductor;
    private boolean[][] visitado;
    private boolean hayCortoCircuito;
    private int iAnterior, jAnterior; //Variables para el toString
    private boolean filaSuperior, filaInferior; //Booleanos para comprobar si hay cortocircuito
    private final int[] vecinosX = {-1, -1, -1, 0, 0, 1, 1, 1, -2, 0, +2, 0}; //Arrays finales para recorrer las celdas vecinas a estudiar
    private final int[] vecinosY = {-1, 0, 1, -1, 1, -1, 0, 1, 0, +2, 0, -2};


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
            for (int j = 0; j < n; j++)
                conductor[i][j] = visitado[i][j] = false;
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


    @Override
    public int hashCode() {
        int result = Objects.hash(hayCortoCircuito, iAnterior, jAnterior, filaSuperior, filaInferior);
        result = 31 * result + Arrays.hashCode(conductor);
        result = 31 * result + Arrays.hashCode(visitado);
        result = 31 * result + Arrays.hashCode(vecinosX);
        result = 31 * result + Arrays.hashCode(vecinosY);
        return result;
    }
}//class


