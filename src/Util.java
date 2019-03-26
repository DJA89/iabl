
import java.util.Vector;

public class Util {

    int[][] distancias;
    int conductor;
    int N;
    int[] paradasPasajeros;
    boolean[] paradasCompletadas;

    Vector<Integer> resultados;


    public int backtracking(int N, int[] paradasPasajeros, int conductor, int[][] distancias ) {
        this.distancias = distancias;
        this.conductor = conductor;
        this.N = N;
        this.paradasPasajeros = paradasPasajeros;

        paradasCompletadas = new boolean[paradasPasajeros.length];
        resultados = new Vector<>();

        int i = 1;
        int d = 0;
        int pAnterior = conductor;
        int sentados = 1;
        i_backtracking( i, d, pAnterior, sentados);

        return minimo(resultados);
    }

    private void i_backtracking(int i, int d, int pAnterior, int sentados) {

        if (i == paradasPasajeros.length -1) {
            resultados.add(d + distancias[pAnterior][conductor+N]);
            return;
        }

        for (int j = 1; j < paradasPasajeros.length -1; j++) {
            int p = paradasPasajeros[j];

            if(!paradasCompletadas[j] && (p<N || paradasCompletadas[j-1]) && (p > N || sentados < 3) ) {


                int i_Sentados = sentados;
                if (p < N) i_Sentados++;
                else i_Sentados--;

                paradasCompletadas[j] = true;
                i_backtracking(i+1, d + distancias[pAnterior][p], p, i_Sentados);
                paradasCompletadas[j] = false;
            }
        }
    }

    private int minimo(Vector<Integer> V) {
        int r = 999999;
        for (Integer x: V) {
            if (x < r) r = x;
        }
        return r;

    }



}
/*
if (!visited[v] && (parada_v < N || u_recojidos.contains(parada_v-N) ) && (parada_v > N || u_pasajeros_actuales < 3 )
        && (parada_v != conductor + N || AllParadasVisitadas(visited) ) ) {

        int c = d_PreComputed[parada_u][parada_v];

        if (d[v] > d[u] + c) {
        d[v] = d[u] + c;
        //p[v] = u;  para recuperar camino
        Aux v_aux = new Aux(v, d[v],u_pasajeros_actuales, u_recojidos);
        if (parada_v<N) {  //si se recoje pasajero
        v_aux.pasajeros_actuales++;
        v_aux.pasajeros_recojidos.add(parada_v);
        }
        //si se deja pasajero
        else v_aux.pasajeros_actuales--;
*/