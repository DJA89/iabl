import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;








public class State {

    private ArrayList[] routes;
    private  int[] routesDistance;

    private static int N;
    private static int[][] distances;  // distances[i,N+j] = d(punto recogida pasajero i - punto dejada j)
    private static Position[][] usersInfo;

    private static int[] drivers;
    private static int[] passengers;
    private static int[] users;
    private static Usuarios usersList;


    public class InfoParada {
        public int passenger;
        public boolean dejada;
    }



    public State(int mDrivers, Usuarios usuarios) {

        //m: numero de personas que pueden conducir
        routes = new ArrayList[mDrivers];
        for(int i = 0; i < mDrivers; i++) {
            routes[i] = new ArrayList<Integer>();

        }

        N = usuarios.size();
        usersInfo = new Position[N][2];
        int i = 0;
        for (Usuario user: usuarios) {
            Position coordOrigen = new Position(user.getCoordOrigenX(), user.getCoordOrigenY());
            Position coordDestino = new Position(user.getCoordDestinoX(), user.getCoordDestinoY());
            usersInfo[i][0] = coordOrigen;
            usersInfo[i][1] = coordDestino;
            i++;
        }

        ComputeAllDistances();
    }

    public State(Usuarios userList){
        setUserList(userList);
    }

    private void ComputeAllDistances() {

        distances = new int[2*N][2*N];
        for(int i = 0; i < N; i++) {

            Position pOrigen_i = usersInfo[i][0];
            Position pDestino_i = usersInfo[i][1];

            for(int j = i; j < N; j++) {
                Position pOrigen_j = usersInfo[j][0];
                Position pDestino_j = usersInfo[j][1];

                distances[i][j] = distances[j][i] = ComputeDistance(pOrigen_i, pOrigen_j);
                distances[N+i][j] = distances[j][N+i] = ComputeDistance(pDestino_i, pOrigen_j);
                distances[i][N+j] = distances[N+j][i] = ComputeDistance(pOrigen_i,pDestino_j);
                distances[N+i][N+j] = distances[N+j][N+i] = ComputeDistance(pDestino_i, pDestino_j);
           }
        }
    }

    private int ComputeDistance(Position p1, Position p2) {
        // d(i,j) = /ix - jx/ + /iy - jy/
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    //Recibe como parametro x el numero de pasajero, 2*x si se corresponde a la posicion de dejada
    private int GetDistance(int x1, int x2) {

        return distances[x1][x2];
    }

    private int GetDistance(InfoParada p1, InfoParada p2) {

        int x1 = p1.passenger;
        if (p1.dejada) x1 = x1 + N;

        int x2 = p2.passenger;
        if (p2.dejada) x2 = x2 + N;

        return distances[x1][x2];
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
        drivers = Arrays.stream(tmpDrivers.toArray(new Integer[tmpDrivers.size()])).mapToInt(Integer::intValue).toArray();
        passengers = Arrays.stream(tmpPassengers.toArray(new Integer[tmpPassengers.size()])).mapToInt(Integer::intValue).toArray();
        users = Arrays.stream(tmpUsers.toArray(new Integer[tmpUsers.size()])).mapToInt(Integer::intValue).toArray();
        this.usersList = usersList;
        routes = new ArrayList[drivers.length];
        clearRoutes();
    }

    private void clearRoutes(){
        for(int i = 0; i < drivers.length; i++) routes[i] = new ArrayList<Integer>();
    }

    public void donkeyInit(){
        clearRoutes();
        pass(0, drivers[0]);
        for(int i=0;i<users.length;i++){
            if(users[i] != drivers[0]){
                pass(0,users[i]);
                pass(0,users[i]);
            }
        }
        pass(0, drivers[0]);
    }

    public void averageInit() {
        clearRoutes();
        int average = passengers.length / drivers.length;
        int passenger_pos = 0, j;
        for (int i = 0; i < drivers.length; i++) {
            pass(i, drivers[i]);
            for (j = passenger_pos; j < passenger_pos + average; j++) {
                pass(i, passengers[j]);
                pass(i, passengers[j]);
            }
            passenger_pos = j;
            if (i == drivers.length - 1) {
                for (j = passenger_pos; j < passengers.length; j++) {
                    pass(i, passengers[j]);
                    pass(i, passengers[j]);
                }
            }
            pass(i, drivers[i]);
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

    public void SwapPassenger(int c1, int c2, int p1, int p2, int posip1, int posjp1, int posip2, int posjp2 ) {}

    public void SwapPassenger(int c1, int c2, int p1, int p2) {
      /*
        //get posiciones
        MovePassenger(c1,c2,p1, posRecogida_p1, posDejada_p1 );   //mirar tema posiciones ruta de insercion, puede dar errores
        MovePassenger(c2,c1,p2, posRecogida_p1, posDejada_p1);
        */
    }


    //Intercambia la parada numero pos1 de la ruta con la pos2
    public void SwapRouteOrder(int c, int pos1, int pos2) {
        ArrayList<Integer> ruta = routes[c];

        int a1 = ruta.get(pos1 -1);
        int x1 = ruta.get(pos1);
        int b1 = ruta.get(pos1 + 1);

        int a2 = ruta.get(pos2 -1);
        int x2 = ruta.get(pos2);
        int b2 = ruta.get(pos2 + 1);

        ruta.set(pos1, x2);
        int d_to_remove = distances[a1][x1] + distances[x1][b1];
        int d_to_add = distances[a1][x2] + distances[x2][b1];


        ruta.set(pos2, x1);
        int d_to_remove2 = distances[a2][x2] + distances[x2][b2];
        int d_to_add2 = distances[a1][x2] + distances[x2][b1];

        routesDistance[c] = - d_to_remove - d_to_remove2 + d_to_add + d_to_add2;
    }

    public void MovePassenger(int cOrigen, int cDestino, int passenger, int posRecogida, int posDejada) {

        RemovePassenger(cOrigen, passenger);
        PutPassenger(cDestino, passenger, posRecogida, posDejada);
    }

    //elimina el punto de recogida y de dejada de un pasajero y actualiza la distancia de la ruta del conductor resultante
    private void RemovePassenger( int conductor, int passenger) {

        ArrayList<Integer> ruta = routes[conductor];

        int i = ruta.indexOf(passenger); //posicion de recogida pasajero
        UpdateDistanceRemoved(ruta, conductor,i-1,i,i+1);
        ruta.remove(i);

        i = ruta.indexOf(passenger + N); //posicion de dejada pasajero
        UpdateDistanceRemoved(ruta, conductor,i-1,i,i+1);
        ruta.remove(i);
    }



    //añade el punto de recogida y de dejada de un pasajero en las posiciones de ruta especificadas
    // y actualiza la distancia de la ruta del conductor resultante
    private void PutPassenger(int conductor, int passenger, int pos1, int pos2) {

        ArrayList<Integer> ruta = routes[conductor];

        ruta.add(pos1, passenger);
        UpdateDistanceAdded(ruta, conductor, pos1 -1, pos1, pos1 +1 );
        ruta.add(pos2, N + passenger);  //N +  passenger para indicar que es lo posicion de dejada
        UpdateDistanceAdded(ruta, conductor, pos2 -1, pos2, pos2 +1 );
    }

    //actualiza la distancia de la ruta al eliminar la parada numero j con paradas adjacentes i  k
    private void UpdateDistanceRemoved(ArrayList<Integer> ruta, int conductor,int i,int j,int k) {

        int a = ruta.get(i);
        int b = ruta.get(i);
        int c = ruta.get(i);

        routesDistance[conductor] = routesDistance[conductor] -  GetDistance(a,b) - GetDistance(b,c) +  GetDistance(a,c);

    }

    //actualiza la distancia de la ruta al añadir la parada numero j con paradas adjacentes i  k
    private void UpdateDistanceAdded(ArrayList<Integer> ruta, int conductor,int i,int j,int k) {

        int a = ruta.get(i);
        int b = ruta.get(i);
        int c = ruta.get(i);

        routesDistance[conductor] = routesDistance[conductor] +  GetDistance(a,b) + GetDistance(b,c) -  GetDistance(a,c);

    }

    /*private void UpdateDistanceRemoved(ArrayList<InfoParada> ruta, int conductor,int i,int j,int k) {


        InfoParada a = ruta.get(i);
        InfoParada b = ruta.get(i);
        InfoParada c = ruta.get(i);

        routesDistance[conductor] = routesDistance[conductor] -  GetDistance(a,b) - GetDistance(b,c) +  GetDistance(a,c);

    }*/

}
