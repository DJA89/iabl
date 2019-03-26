
import java.util.Vector;
import java.util.PriorityQueue;

public class Util {

    int[][] distancias;
    int conductor;
    int N;
    int[] paradasPasajeros;
    boolean[] paradasCompletadas;

    PriorityQueue<Integer> resultados;


    public int backtracking(int N, int[] paradasPasajeros, int conductor, int[][] distancias) {
        this.distancias = distancias;
        this.conductor = conductor;
        this.N = N;
        this.paradasPasajeros = paradasPasajeros;

        paradasCompletadas = new boolean[paradasPasajeros.length];
        resultados = new PriorityQueue<>();


        i_backtracking(1, 0, conductor, 1);
        return resultados.peek();
    }

    private void i_backtracking(int i, int d, int pAnterior, int sentados) {

        if (!resultados.isEmpty() && d + distancias[pAnterior][conductor + N] >= resultados.peek()) return;
        if (i == paradasPasajeros.length - 1) {
            resultados.add(d + distancias[pAnterior][conductor + N]);
            return;
        }

        for (int j = 1; j < paradasPasajeros.length - 1; j++) {
            int p = paradasPasajeros[j];

            if (!paradasCompletadas[j] && (p < N || paradasCompletadas[j - 1]) && (p > N || sentados < 3)) {


                int i_Sentados = sentados;
                if (p < N) i_Sentados++;
                else i_Sentados--;

                paradasCompletadas[j] = true;
                i_backtracking(i + 1, d + distancias[pAnterior][p], p, i_Sentados);
                paradasCompletadas[j] = false;
            }
        }
    }
}

