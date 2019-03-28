
import java.util.Arrays;
import java.util.Vector;
import java.util.PriorityQueue;


//Clase para gestionar el calculo de la distancia de la mejor ruta entre todas las posibles combinaciones dado un conductor
public class Util {

    private int[][] distancias;
    private int conductor;
    private int N;
    private int[] paradasPasajeros;
    private boolean[] paradasCompletadas;
    private int dmin = 999999;
    private PriorityQueue<Integer> resultados;

    private int[] rutaAux;
    private int[] ruta;

    public int backtracking(int N, int[] paradasPasajeros, int conductor, int[][] distancias) {
        //Inicializamos
        this.distancias = distancias;
        this.conductor = conductor;
        this.N = N;
        this.paradasPasajeros = paradasPasajeros;

        paradasCompletadas = new boolean[paradasPasajeros.length];
        resultados = new PriorityQueue<>();

        //computeMinDistancia();
        //Iniciamos backtracking con parada i = 1, distancia actual = 0, pasajero anterior = conductor, sentados = 1
        i_backtracking(1, 0, conductor, 1);
        return resultados.peek();
    }

    private void i_backtracking(int i, int d, int pAnterior, int sentados) {
        int n = paradasPasajeros.length;
        if (!resultados.isEmpty() && d + distancias[pAnterior][conductor + N] >= resultados.peek()) return;
        if (i == n - 1) {
            resultados.add(d + distancias[pAnterior][conductor + N]);
            return;
        }

        for (int j = 1; j < paradasPasajeros.length - 1; j++) {
            int p = paradasPasajeros[j];

            if (!paradasCompletadas[j] && (p < N || paradasCompletadas[j - 1]) && (p >= N || sentados < 3)) {

                int i_Sentados = sentados;
                if (p < N) i_Sentados++;
                else i_Sentados--;

                paradasCompletadas[j] = true;
                i_backtracking(i + 1, d + distancias[pAnterior][p], p, i_Sentados);
                paradasCompletadas[j] = false;
            }
        }
    }

    public int[] GetRutaOptima(int N, int[] paradasPasajeros, int conductor, int[][] distancias) {
        //Inicializamos
        this.distancias = distancias;
        this.conductor = conductor;
        this.N = N;
        this.paradasPasajeros = paradasPasajeros;

        paradasCompletadas = new boolean[paradasPasajeros.length];
        resultados = new PriorityQueue<>();

        rutaAux = new int[paradasPasajeros.length];
        ruta = new int[paradasPasajeros.length];

        //Iniciamos backtracking con parada i = 1, distancia actual = 0, pasajero anterior = conductor, sentados = 1
        i_backtrackingCamino(1, 0, conductor, 1);
        return ruta;
    }

    private void i_backtrackingCamino(int i, int d, int pAnterior, int sentados) {

        rutaAux[i-1] = pAnterior;

        int n = paradasPasajeros.length;
        if (!resultados.isEmpty() && d + distancias[pAnterior][conductor + N] >= resultados.peek()) return;
        if (i == n - 1) {
            int dAux = d + distancias[pAnterior][conductor + N];
            resultados.add(dAux);
            if(!resultados.isEmpty() && dAux <= resultados.peek()) {
                rutaAux[i] = conductor + N;
                ruta = Arrays.copyOf(rutaAux, rutaAux.length);
            }
            return;
        }

        for (int j = 1; j < paradasPasajeros.length - 1; j++) {
            int p = paradasPasajeros[j];

            if (!paradasCompletadas[j] && (p < N || paradasCompletadas[j - 1]) && (p >= N || sentados < 3)) {

                int i_Sentados = sentados;
                if (p < N) i_Sentados++;
                else i_Sentados--;

                paradasCompletadas[j] = true;
                i_backtrackingCamino(i + 1, d + distancias[pAnterior][p], p, i_Sentados);
                paradasCompletadas[j] = false;
            }
        }
    }

    private void computeMinDistancia() {
        for(int i = 0; i < paradasPasajeros.length; i++) {
            for(int j = i+1; j < paradasPasajeros.length; j++) {
                int d = distancias[i][j];
                if(d < dmin) dmin = d;
            }
        }
    }
}

