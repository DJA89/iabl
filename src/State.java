import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;
import java.util.ArrayList;
import java.util.HashSet;

public class State {

    private HashSet<Short>[] conductor_pasajeros;  //Cada posicion del array corresponde a un conductor, y este contiene el conjunto de pasajeros a transportar
    private int[] distancia_ruta_optima;   //distancia_ruta_optima[i] = distancia de la ruta mas corta resultante de las posibles combinaciones con los pasajeros a transportar
    private static int N;                   // total usuarios
    private static short M;                   // total conductores
    private static int[][] distancias;      // distancias(i,j), donde por ejemplo 'i' es el numero de pasajero si queremos su punto de recogida, o i+N para el punto de dejada
    private static Position[][] usersInfo;  // coordenadas de origen y destino de cada uno de los usuarios, usersInfo[0 - M-1] corresponde a los conductores
    private static int maxDistancia = 300;
    private short conductoresLibres;

    public void ImprimirDistancias() {
        for (int i = 0; i < distancias.length; i++) {
            for (int j = 0; j < distancias[i].length; j++) System.out.print(distancias[i][j] + "\t");
            System.out.print("\n");
        }
    }

    public void ImprimirPosiciones() {
        for (int i = 0; i < usersInfo.length; i++) {
            for (int j = 0; j < usersInfo[i].length; j++) System.out.print(usersInfo[i][j] + "\t");
            System.out.print("\n");
        }
    }

    public void ImprimirConductores() {
        System.out.print(this);
    }

    public String toString() {
        int i = 0;
        String str = "\n";
        for (HashSet<Short> pasajeros : conductor_pasajeros) {
            str += (i + ") ");
            for (Short pasajero : pasajeros) str += (pasajero + "\t");
            str += ("\n");
            i++;
        }
        return str;
    }

    public State(Usuarios usuarios) {

        //inicializamos datos

        //Contamos y colocamos primero para procesar los conductores
        ArrayList<Usuario> driversNUsers = new ArrayList<>();
        M = 0;
        conductoresLibres = 0;
        for (Usuario user : usuarios) {
            if (user.isConductor()) {
                driversNUsers.add(0, user);
                M++;
            } else {
                driversNUsers.add(user);
            }
        }
        conductor_pasajeros = new HashSet[M];
        for (int i = 0; i < M; i++) {
            conductor_pasajeros[i] = new HashSet<Short>();
        }
        N = usuarios.size();


        distancia_ruta_optima = new int[M];

        for(int i=0;i<M;i++) conductor_pasajeros[i] = new HashSet<Short>();
        usersInfo = new Position[N][2];
        int i = 0;
        for (Usuario user : driversNUsers) {
            Position coordOrigen = new Position(user.getCoordOrigenX(), user.getCoordOrigenY());
            Position coordDestino = new Position(user.getCoordDestinoX(), user.getCoordDestinoY());
            usersInfo[i][0] = coordOrigen;
            usersInfo[i][1] = coordDestino;
            i++;
        }
        ComputeAllDistances();
    }

    //Pre: Algun state previo debe haber sido creado con state(usuarios) , la creadora anterior, para tener inicializados los campos static
    public State(State state) {
        this.conductor_pasajeros = new HashSet[M];
        for (int i = 0; i < M; i++) {
            conductor_pasajeros[i] = new HashSet<Short>(state.GetConductor_pasajeros()[i]);
        }
        this.distancia_ruta_optima = new int[M];
        int[] distancia_vieja = state.GetDistancia_ruta_optima();
        for (int i = 0; i < M; i++) {
            distancia_ruta_optima[i] = distancia_vieja[i];
        }
    }

    public int GetConductoresActuales() {
        int m = 0;
        for (HashSet s : conductor_pasajeros) {
            if (s.size() > 0) m++;
        }
        return m;
    }

    public short GetConductoresTotales() {
        return M;
    }

    public HashSet[] GetConductor_pasajeros() {
        return this.conductor_pasajeros;
    }

    public int[] GetDistancia_ruta_optima() {
        return this.distancia_ruta_optima;
    }

    private void ComputeAllDistances() {
        distancias = new int[2 * N][2 * N];
        for (int i = 0; i < N; i++) {

            Position pOrigen_i = usersInfo[i][0];
            Position pDestino_i = usersInfo[i][1];

            for (int j = i; j < N; j++) {
                Position pOrigen_j = usersInfo[j][0];
                Position pDestino_j = usersInfo[j][1];

                distancias[i][j] = distancias[j][i] = ComputeDistance(pOrigen_i, pOrigen_j);
                distancias[N + i][j] = distancias[j][N + i] = ComputeDistance(pDestino_i, pOrigen_j);
                distancias[i][N + j] = distancias[N + j][i] = ComputeDistance(pOrigen_i, pDestino_j);
                distancias[N + i][N + j] = distancias[N + j][N + i] = ComputeDistance(pDestino_i, pDestino_j);
            }
        }

        //Asignamos distancia 0 a la distancia de un punto consigo mismo
        for (int k = 0; k < N; k++) {
            distancias[k][k] = 0;
            distancias[k + N][k + N] = 0;
        }
    }

    private int ComputeDistance(Position p1, Position p2) {
        // d(i,j) = /ix - jx/ + /iy - jy/
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    //Recibe como parametro x el numero de pasajero, 2*x si se corresponde a la posicion de dejada
    private int GetDistance(int x1, int x2) {
        return distancias[x1][x2];
    }

    public boolean MoverPasajero(short cOrigen, short cDestino, short pasajero) {

        HashSet<Short> pasajeros_cOrigen = conductor_pasajeros[cOrigen];
        HashSet<Short> pasajeros_cDestino = conductor_pasajeros[cDestino];

        //Comprobamos que el pasajero a mover no sea el conductor si el coche lleva pasajeros y que si el pasajero tiene que conducir un coche este sea el suyo
        if ((cOrigen == pasajero && pasajeros_cOrigen.size() > 1) ||
                pasajeros_cDestino.size() == 0 && cDestino != pasajero) return false;

        pasajeros_cOrigen.remove(pasajero);
        pasajeros_cDestino.add(pasajero);

        if (conductor_pasajeros[cOrigen].size() > 0) {
            searchOptimalRouteAlternativo(cOrigen);
        } else {
            distancia_ruta_optima[cOrigen] = 0;
            conductoresLibres++;
        }
        searchOptimalRouteAlternativo(cDestino);
        if (conductor_pasajeros[cDestino].size() == 1) {
            conductoresLibres--;
        }

        return true;
    }

    public boolean SwapPasajeros(short c1, short c2, short pasajero1, short pasajero2) {

        HashSet<Short> pasajeros_c1 = conductor_pasajeros[c1];
        HashSet<Short> pasajeros_c2 = conductor_pasajeros[c2];

        if ((c1 == pasajero1 && pasajeros_c1.size() > 1) || (c2 == pasajero2 && pasajeros_c1.size() > 1) ||
                pasajeros_c2.size() == 1 || pasajeros_c1.size() == 1) return false;

        pasajeros_c1.remove(pasajero1);
        pasajeros_c2.remove(pasajero2);

        pasajeros_c1.add(pasajero2);
        pasajeros_c2.add(pasajero1);

        searchOptimalRouteAlternativo(c1);
        searchOptimalRouteAlternativo(c2);
        return true;

    }

    //intercambia los coches/conductores que llevan a los pasajeros,  (cada conductor transportara al conjunto de pasajeros que llevaba el otro)
    public boolean SwapConductor(short c1, short c2) {

        HashSet<Short> pasajeros_c1 = conductor_pasajeros[c1];
        HashSet<Short> pasajeros_c2 = conductor_pasajeros[c2];

        if(pasajeros_c1.isEmpty() || pasajeros_c2.isEmpty())
            return false;

        //swap de el conjunto de pasajeros entero(incluido el propio conductor)
        HashSet<Short> aux = pasajeros_c1;
        pasajeros_c1 = pasajeros_c2;
        pasajeros_c2 = aux;

        //colocamos de nuevo los conductores en sus coches
        pasajeros_c1.remove(c2);
        pasajeros_c1.add(c1);

        pasajeros_c2.remove(c1);
        pasajeros_c2.add(c2);

        conductor_pasajeros[c1] = pasajeros_c1;
        conductor_pasajeros[c2] = pasajeros_c2;

        searchOptimalRouteAlternativo(c1);
        searchOptimalRouteAlternativo(c2);
        return true;

    }

    public boolean AnadirPasajero(short chofer, short pasajero){
        conductor_pasajeros[chofer].add(pasajero);
        return true;
    }

    private void inicioPasajeros(){
        conductor_pasajeros = new HashSet[M];
        for(int i=0;i<M;i++) conductor_pasajeros[i] = new HashSet<Short>();
    }

    public void donkeyInit(){
        inicioPasajeros();
        for(short i=0;i<N;i++) AnadirPasajero((short) 0,i);
        for(int i = 0; i < M; i++) {
            searchOptimalRouteAlternativo(i);
        }
        conductoresLibres = 1;
    }

    public void averageInit() {
        inicioPasajeros();
        int average = N/M - 1;
        int passenger_pos = M, j;
        for (int i = 0; i < M; i++) {
            AnadirPasajero((short)i,(short)i);
            for (j = passenger_pos; j < passenger_pos + average; j++) AnadirPasajero((short)i,(short)j);
            passenger_pos = j;
            if (i == M - 1)
                for (int k = 0; k < M; k++) {
                    if(passenger_pos == N) break;
                    AnadirPasajero((short)k,(short)passenger_pos);
                    passenger_pos ++;
                }
        }
        for(int i = 0; i < M; i++) {
            searchOptimalRouteAlternativo(i);
        }
        conductoresLibres = 0;
    }


    public HashSet PasajerosDeConductor(short conductor) {
        return conductor_pasajeros[conductor];
    }

    private void  searchOptimalRouteAlternativo(int c) {
        if (conductor_pasajeros[c].size() != 0) {
            Util u = new Util();
            int[] paradasPasajeros = new int[conductor_pasajeros[c].size() * 2];
            paradasPasajeros[0] = c;
            paradasPasajeros[paradasPasajeros.length - 1] = c + N;
            int i = 1;
            for (Short x : conductor_pasajeros[c]) {
                if (x != c) {
                    paradasPasajeros[i] = x;
                    paradasPasajeros[i + 1] = x + N;
                    i = i + 2;
                }
            }
            distancia_ruta_optima[c] = u.backtracking(N, paradasPasajeros, c, distancias);
        }
        else distancia_ruta_optima[c] = 0;

    }

    private void searchOptimalRoute(int conductor) {
        int cantidadDePasajeros = conductor_pasajeros[conductor].size() - 1;
        int[] pasajeros = new int[cantidadDePasajeros];
        int[] ruta = new int[(cantidadDePasajeros+1)*2];
        ruta[0] = conductor;
        ruta[(cantidadDePasajeros+1)*2-1] = conductor + N;
        int[] pasajerosActuales = new int[2];
        pasajerosActuales[0] = pasajerosActuales[1] = -1;
        int lugarActual = 0;
        boolean[] yaRecogidos = new boolean[cantidadDePasajeros];
        boolean[] yaDejados = new boolean[cantidadDePasajeros];
        Short iterator = 0;
        for (Short pasajero: conductor_pasajeros[conductor]) {
            if (pasajero != conductor) {
                pasajeros[iterator] = pasajero;
                yaRecogidos[iterator] = false;
                yaDejados[iterator] = false;
                iterator++;
            }
        }
        distancia_ruta_optima[conductor] = searchOptimalRouteAux(ruta, pasajeros, pasajerosActuales, yaRecogidos, yaDejados, lugarActual);
    }

    private int searchOptimalRouteAux(int[] ruta, int[] pasajeros, int[] pasajerosActuales, boolean[] yaRecogidos, boolean[] yaDejados, int lugarActual) {
        lugarActual++; //avancemos en la ruta
        int bestLength = 50000;
        if (lugarActual == ruta.length - 1) { // Si llegamos al final de una ruta
            bestLength = measureLength(ruta); //Calculemos la distancia del recorrido total y devolvamos eso
        } else { //Si estamos construyendo la ruta (No es el último nodo)
            int possibleBestLength; //variable para guardar candidatos a mejor distancia
            int lugarVacio = -1; //variable para saber si hay lugar libre o no
            if (pasajerosActuales[0] == -1) { //si no hay nadie en el primer lugar
                lugarVacio = 0; //el primer lugar está vacío
            } else if (pasajerosActuales[1] == -1) { //si no hay nadie en el segundo lugar
                lugarVacio = 1; //el segundo lugar está vacío
            }
            if (lugarVacio != -1) { //si hay algún lugar vacío
                for(int i = 0; i < yaRecogidos.length; i++) { //recorramos a ver quién todavía no fue recogido
                    if (!yaRecogidos[i]) { //si el pasajero i no ha sido recogido aún
                        ruta[lugarActual] = pasajeros[i]; //ponemos al pasajero i en el siguiente lugar de la ruta
                        pasajerosActuales[lugarVacio] = i; //pongamos que el pasajero i ocupa uno de los lugares de pasajero
                        yaRecogidos[i] = true; //marquemos que el pasajero i fue recogido
                        possibleBestLength = searchOptimalRouteAux(ruta, pasajeros, pasajerosActuales, yaRecogidos, yaDejados, lugarActual); //llamemos a la función recursiva para llenar los siguientes lugares, nos devolverá el largo de la mejor ruta que encuentre
                        if (possibleBestLength < bestLength) { //si el largo recogiendo al pasajero i siguiente en la ruta es el mejor que hemos encontrado hasta ahora
                            bestLength = possibleBestLength; //marcarlo como el mejor largo que hemos encontrado hasta ahora
                        }
                        yaRecogidos[i] = false; //ya probamos recogiendo al pasajero i, marquemos que no fue recogido y terminemos para probar a recoger a otro pasajero
                    }
                }
                pasajerosActuales[lugarVacio] = -1; //Marquemos que el lugar que estaba libre antes de inicar el for sigue libre
            }
            for (int i = 0; i < 2; i++) { //Miremos si podemos dejar a alguno de los pasajeros que estamos llevando
                int pasajeroActualAux = pasajerosActuales[i]; //este es uno de los pasajeros que estamos llevando
                if (pasajeroActualAux != -1) { //si hay un pasajero en el asiento (no está vacío)
                    ruta[lugarActual] = pasajeros[pasajeroActualAux] + N; //marquemos en la ruta que dejamos a este pasajero
                    pasajerosActuales[i] = -1; //marquemos que el asiento quedó vacío
                    yaDejados[pasajeroActualAux] = true; //marquemos que dejamos al pasajero i
                    possibleBestLength = searchOptimalRouteAux(ruta, pasajeros, pasajerosActuales, yaRecogidos, yaDejados, lugarActual); //llamemos a la función recursiva para llenar los siguientes lugares, nos devolverá el largo de la mejor ruta que encuentre
                    if (possibleBestLength < bestLength) { //si el largo dejando al pasajero i siguiente en la ruta es el mejor que hemos encontrado hasta ahora
                        bestLength = possibleBestLength; //marcarlo como el mejor largo que hemos encontrado hasta ahora
                    }
                    yaDejados[pasajeroActualAux] = false; //ya probamos dejando al pasajero i, marquemos que no fue dejado aún y terminemos para probar dejar a otro pasajero que estemos llevando ahora
                }
                pasajerosActuales[i] = pasajeroActualAux; //Marquemos que i vuelve a estar ocupando el asiento que había dejado libre cuando lo dejamos
            }
        }
        lugarActual--; //volvamos un paso atrás en la ruta
        return bestLength; //devolvamos la mejor ruta que hemos encontrado hasta ahora
    }

    private int measureLength(int[] ruta) {
        int length = 0;
        for(int i = 0; i < ruta.length -1; i++) {
            length += distancias[ruta[i]][ruta[i+1]];
        }
        return length;
    }

    public void minRouteInit() {
        inicioPasajeros();
        int tmpDistancia, restDistancia, minDistancia;
        short pasajero = 0, ultimoPasajero;
        ArrayList<Short> pasajerosDisponibles = new ArrayList<Short>();
        for (int i = M; i < N; i++) pasajerosDisponibles.add((short) i);

        for (int i = 0; i < M; i++) {
            AnadirPasajero((short)i,(short)i);
            if (pasajerosDisponibles.isEmpty()) break;
            restDistancia = 0;
            minDistancia = distancias[i][N + i];
            ultimoPasajero = (short) (N + i);
            while (minDistancia <= maxDistancia) {
                minDistancia = Integer.MAX_VALUE;
                for (Short j : pasajerosDisponibles) {
                    tmpDistancia = distancias[j][i] + distancias[N + j][j] + distancias[N + j][ultimoPasajero] + restDistancia;
                    if (tmpDistancia < minDistancia) {
                        minDistancia = tmpDistancia;
                        pasajero = j;
                    }
                }
                if (minDistancia <= maxDistancia) {
                    AnadirPasajero((short) i, pasajero);
                    ultimoPasajero = pasajero;
                    pasajerosDisponibles.remove((Short) pasajero);
                    restDistancia = minDistancia - distancias[pasajero][i];
                }
            }
            conductoresLibres = 0;
        }
        //Cabe la posibilidad de no encontrar una solucion optima, por lo que, si es que han sobrado
        //pasajeros disponibles, se los distribuye equitativamente entre los conductores
        if (!pasajerosDisponibles.isEmpty()){
            int average = pasajerosDisponibles.size()/M;
            for (int i = 0; i < M; i++) {
                for (Short j : pasajerosDisponibles) {
                    for(int k = 0; k < average; k++){
                        AnadirPasajero((short)i,(short)j);
                        pasajerosDisponibles.remove(j);
                    }
                }
                if (i == M - 1){
                    int k = 0;
                    for (Short j : pasajerosDisponibles){
                        AnadirPasajero((short)k,j);
                        pasajerosDisponibles.remove(j);
                        if(pasajerosDisponibles.isEmpty()) break;
                        k++;
                    }
                }
            }
        }
        for(int i = 0; i < M; i++) {
            searchOptimalRouteAlternativo(i);
        }
      }

      public short numeroDeConductoresActivos() {
        return conductoresLibres;
      }

    public double distanciaTotal() {
        int currentLength = 0;
        for (int i = 0; i < M; i++) {
            currentLength += distancia_ruta_optima[i];
        }

        return (currentLength);
    }

}
