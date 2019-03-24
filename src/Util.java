import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.*;

public class Util {

    public class Aux {
        int vertice;
        int d_recorrida;
        int pasajeros_actuales;
        HashSet<Integer> pasajeros_recojidos;

        Aux(int v, int d, int pasajeros_actuales, HashSet pasajeros_recojidos) {
            this.vertice = v;
            this.d_recorrida = d;
            this.pasajeros_actuales = pasajeros_actuales;

            this.pasajeros_recojidos = new HashSet<>();
            this.pasajeros_recojidos = pasajeros_recojidos;
        }

        Aux(int d) {
            d_recorrida = d;
        }

    }

    class AuxComparator implements Comparator<Aux>{

        public int compare(Aux a1, Aux a2) {
            if (a1.d_recorrida < a2.d_recorrida) return -1;
            else if (a1.d_recorrida == a2.d_recorrida) return 0;
            else return 1;
        }
    }


    //paradasPasajeros = [conductor ... conductorDejada]
    int dijkstra(int N, int[] paradasPasajeros, int conductor, int[][] d_PreComputed ) {

        PriorityQueue<Aux> pq = new PriorityQueue<Aux>(10, new AuxComparator());

        boolean[] visited = new boolean[paradasPasajeros.length];
        visited[0] = true;

        //inicializamos las distancias a 'infinito'
        int[] d = new int[paradasPasajeros.length];
        for (int i = 0; i < paradasPasajeros.length; i++){
            d[i] = 999999;
        }
        d[0] = 0;

        HashSet<Integer> pasajeros_recojidos = new HashSet<>();
        pasajeros_recojidos.add(conductor); //consideramos al conductor recojido

        pq.add(new Aux(conductor, 0, 1, pasajeros_recojidos));
        while (!pq.isEmpty()) {
            Aux u_aux = pq.poll();

            int u = u_aux.vertice;
            int parada_u = paradasPasajeros[u];
            int u_pasajeros_actuales = u_aux.pasajeros_actuales;
            HashSet<Integer> u_recojidos = u_aux.pasajeros_recojidos;

            if (!visited[u]) {
                visited[u] = true;
                for (int v = 0; v < paradasPasajeros.length; v++) {
                    int parada_v = paradasPasajeros[v];
                    //punto pasajero no visitado y punto de recojida o pasajero previamente recojido si es de dejada o si es punto de recojida hay menos de 3 plazas ocupadas
                    if (!visited[v] && (parada_v < N || u_recojidos.contains(parada_v-N) ) && (parada_v > N || u_pasajeros_actuales < 3 )
                            && (parada_v != conductor + N || AllParadasVisitadas(visited) ) ) {

                        int c = d_PreComputed[parada_u][parada_v];

                        if (d[v] > d[u] + c) {
                            d[v] = d[u] + c;
                            //p[v] = u;  para recuperar camino
                            Aux v_aux = new Aux(v, d[v],u_pasajeros_actuales, u_recojidos);
                            if (v<N) {  //si se recoje pasajero
                                v_aux.pasajeros_actuales++;
                                v_aux.pasajeros_recojidos.add(parada_v);
                            }
                            //si se deja pasajero
                            else v_aux.pasajeros_actuales--;

                            pq.add(v_aux);
                        }
                    }
                }
            }
        }
        return d[paradasPasajeros.length -1];
    }

    //Comprueba si todas las paradas han sido visitadas, excepto la final del conductor
    private boolean AllParadasVisitadas(boolean[] visited) {
        for(int i = 0; i < visited.length -1; i++) {
            if(!visited[i]) return false;
        }
        return true;
    }
}
