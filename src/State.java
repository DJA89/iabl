import java.util.ArrayList;
import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;
import java.util.Iterator;



public class State {

    private int[] routesDistance;
    private ArrayList[] routes;
    private static int[][] distances;  // distances[i,2*j] = d(punto recogida pasajero i - punto dejada j)
    private static Position[][] usersInfo;


    private void ComputeAllDistances() {
        int n = usersInfo.length;
        distances = new int[2*n][2*n];
        for(int i = 0; i < n; i++) {

            Position pOrigen_i = usersInfo[i][0];
            Position pDestino_i = usersInfo[i][1];

            for(int j = i; j < n; j++) {
                Position pOrigen_j = usersInfo[j][0];
                Position pDestino_j = usersInfo[j][1];

                distances[i][j] = distances[j][i] = ComputeDistance(pOrigen_i, pOrigen_j);
                distances[2*i][j] = distances[j][i] = ComputeDistance(pDestino_i, pOrigen_j);
                distances[i][2*j] = distances[j][i] = ComputeDistance(pOrigen_i,pDestino_j);
                distances[2*i][2*j] = distances[j][i] = ComputeDistance(pDestino_i, pDestino_j);
           }
        }
    }

    private int ComputeDistance(Position p1, Position p2) {
        // d(i,j) = /ix - jx/ + /iy - jy/
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }



    public State(int mDrivers, Usuarios usuarios) {

        //m: numero de personas que pueden conducir
        routes = new ArrayList[mDrivers];
        for(int i = 0; i < mDrivers; i++) {
            routes[i] = new ArrayList<Integer>();
        }



        int n = usuarios.size();
        usersInfo = new Position[n][2];
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

    public void SwapPassenger(int c1, int c2, int p1, int p2) {}

    public void SwapRouteOrder(int c, int pos1, int pos2) {}

    public void MovePassenger(int cOrigen, int cDestino, int passenger, int posRecogida, int posDejada) {
        RemovePassenger(routes[cOrigen], passenger);
        PutPassenger(routes[cDestino], passenger, posRecogida, posDejada);
    }

    private void RemovePassenger(ArrayList<Integer> ruta, int passenger) {
        ruta.remove(passenger); //removes first occurrence of the passenger
        ruta.remove(passenger); //removes second occurrence of the passenger
    }

    private void PutPassenger(ArrayList<Integer> ruta, int passenger, int pos1, int pos2) {
        ruta.add(pos1, passenger);
        ruta.add(pos2, passenger);
    }

}
