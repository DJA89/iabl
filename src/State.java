import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.HashSet;


public class State {

    private HashSet<Short>[] conductor_pasajeros;  //Cada posicion del array corresponde a un conductor, y este contiene el conjunto de pasajeros a transportar
    private  int[] distancia_ruta_optima;   //distancia_ruta_optima[i] = distancia de la ruta mas corta resultante de las posibles combinaciones con los pasajeros a transportar

    private static int N;                   // total usuarios
    private static int M;                   // total conductores
    private static int[][] distancias;      // distancias(i,j), donde por ejemplo 'i' es el numero de pasajero si queremos su punto de recogida, o i+N para el punto de dejada
    private static Position[][] usersInfo;  // coordenadas de origen y destino de cada uno de los usuarios, usersInfo[0 - M-1] corresponde a los conductores

    /*
    private static int[] drivers;
    private static int[] passengers;
    private static int[] users;
    private static Usuarios usersList;
    */


    public void ImprimirDistancias() {
        for (int i = 0; i < distancias.length; i++) {
            for (int j = 0; j < distancias[i].length; j++) System.out.print(distancias[i][j] + "\t");
            System.out.print("\n");
        }
    }


    public State(Usuarios usuarios) {

        //inicializamos datos

        conductor_pasajeros = new HashSet[M];
        for(int i = 0; i < M; i++) {
            conductor_pasajeros[i] = new HashSet<Short>();
        }
        N = usuarios.size();

        //Contamos y colocamos primero para procesar los conductores
        ArrayList<Usuario> driversNUsers = new ArrayList<>();
        M = 0;
        for (Usuario user: usuarios) {
            if (user.isConductor()) {
                driversNUsers.add(0, user);
                M++;
            }
            else {
                driversNUsers.add(user);
            }

        }

        distancia_ruta_optima = new int[M];

        usersInfo = new Position[N][2];
        int i = 0;
        for (Usuario user: driversNUsers) {
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
        for(int i = 0; i < M; i++) {
            conductor_pasajeros[i] = new HashSet<Short>();
            conductor_pasajeros[i] = state.GetConductor_pasajeros()[i];
        }

        this.distancia_ruta_optima = new int[M];
        this.distancia_ruta_optima = state.GetDistancia_ruta_optima();
    }

    public int GetConductoresActuales() {
        int m = 0;
        for (HashSet s:conductor_pasajeros) {
            if(s.size() > 0) m++;
        }
        return m;
    }

    public int GetConductoresTotales() {
        return M;
    }

    public HashSet[] GetConductor_pasajeros() {
        return this.conductor_pasajeros;
    }

    public int[] GetDistancia_ruta_optima() {
        return this.distancia_ruta_optima;
    }

    private void ComputeAllDistances() {

        distancias = new int[2*N][2*N];
        for(int i = 0; i < N; i++) {

            Position pOrigen_i = usersInfo[i][0];
            Position pDestino_i = usersInfo[i][1];

            for(int j = i; j < N; j++) {
                Position pOrigen_j = usersInfo[j][0];
                Position pDestino_j = usersInfo[j][1];

                distancias[i][j] = distancias[j][i] = ComputeDistance(pOrigen_i, pOrigen_j);
                distancias[N+i][j] = distancias[j][N+i] = ComputeDistance(pDestino_i, pOrigen_j);
                distancias[i][N+j] = distancias[N+j][i] = ComputeDistance(pOrigen_i,pDestino_j);
                distancias[N+i][N+j] = distancias[N+j][N+i] = ComputeDistance(pDestino_i, pDestino_j);
           }
        }

        //Asignamos distancia 0 a la distancia de un punto consigo mismo
        for (int k = 0; k < N; k++) {
            distancias[k][k] = 0;
            distancias[k+N][k+N] = 0;
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
        if( (cOrigen == pasajero && pasajeros_cOrigen.size() > 1) ||
                pasajeros_cDestino.size() == 0 && cDestino != pasajero) return false;

        pasajeros_cOrigen.remove(pasajero);
        pasajeros_cDestino.add(pasajero);

        //recalcular distancia_ruta_optima[cOrigen]
        //recalcular distancia_ruta_optima[cDestino]

        return true;
    }

    public boolean SwapPasajeros(short c1, short c2, short pasajero1, short pasajero2) {

        HashSet<Short> pasajeros_c1 = conductor_pasajeros[c1];
        HashSet<Short> pasajeros_c2 = conductor_pasajeros[c2];

        if( (c1 == pasajero1 && pasajeros_c1.size() > 1) || (c2 == pasajero2 && pasajeros_c1.size() > 1) ||
                pasajeros_c2.size() == 1 || pasajeros_c1.size() == 1) return false;

        pasajeros_c1.remove(pasajero1);
        pasajeros_c2.remove(pasajero2);

        pasajeros_c1.add(pasajero2);
        pasajeros_c2.add(pasajero1);

        //recalcular distancia_ruta_optima[cOrigen]
        //recalcular distancia_ruta_optima[cDestino]

        return true;

    }

/*
      public State(Usuarios userList){
        setUserList(userList);
    }

    public void setUserList(Usuarios usersList) {
        Usuario tmp;
        ArrayList<Integer> tmpDrivers = new ArrayList<Integer>();
        ArrayList<Integer> tmpPassengers = new ArrayList<Integer>();
        ArrayList<Integer> tmpUsers = new ArrayList<Integer>();
        Iterator<Usuario> iterator = usersList.iterator();
        while (iterator.hasNext()) {
            tmp = iterator.next();
            if(tmp.isConductor()) {
                tmpDrivers.add(tmp.hashCode());
            }else{
                tmpPassengers.add(tmp.hashCode());
            }
            tmpUsers.add(tmp.hashCode());
        }
        conductor_pasajeros = Arrays.stream(tmpDrivers.toArray(new Integer[tmpDrivers.size()])).mapToInt(Integer::intValue).toArray();
        passengers = Arrays.stream(tmpPassengers.toArray(new Integer[tmpPassengers.size()])).mapToInt(Integer::intValue).toArray();
        users = Arrays.stream(tmpUsers.toArray(new Integer[tmpUsers.size()])).mapToInt(Integer::intValue).toArray();
        this.usersList = usersList;
        routes = new ArrayList[conductor_pasajeros.length];
        clearRoutes();
    }

    private void clearRoutes(){
        for(int i = 0; i < conductor_pasajeros.length; i++) routes[i] = new ArrayList<Integer>();
    }

    public void donkeyInit(){
        clearRoutes();
        pass(0, conductor_pasajeros[0]);
        for(int i=0;i<users.length;i++){
            if(users[i] != conductor_pasajeros[0]){
                pass(0,users[i]);
                pass(0,users[i]);
            }
        }
        pass(0, conductor_pasajeros[0]);
    }

    public void averageInit() {
        clearRoutes();
        int average = passengers.length / conductor_pasajeros.length;
        int passenger_pos = 0, j;
        for (int i = 0; i < conductor_pasajeros.length; i++) {
            pass(i, conductor_pasajeros[i]);
            for (j = passenger_pos; j < passenger_pos + average; j++) {
                pass(i, passengers[j]);
                pass(i, passengers[j]);
            }
            passenger_pos = j;
            if (i == conductor_pasajeros.length - 1) {
                for (j = passenger_pos; j < passengers.length; j++) {
                    pass(i, passengers[j]);
                    pass(i, passengers[j]);
                }
            }
            pass(i, conductor_pasajeros[i]);
        }
    }



    public void pass(int driver_pos, int passenger_id) {
        routes[driver_pos].add(passenger_id);
    }

    public String getRoute(int driver_pos) {
        String retVal = "Size: " + routes[driver_pos-1].size() + ": ";
        for(int i = 0; i < routes[driver_pos-1].size(); i++) {
            retVal += " " + routes[driver_pos-1].get(i);
        }
        retVal += "\n";
        return retVal;
    }

    public String toString() {
        String retVal = "";
        for(int i = 0; i < routes.length; i++) {
            retVal += getRoute(i + 1);
        }
        return retVal;
    }

    */


}
