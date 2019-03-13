import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class State {

    private ArrayList[] routes;
    private static int[] routesDistance;
    private static int[][] distances;
    private static int[] drivers;
    private static int[] passengers;
    private static int[] users;
    private static Usuarios usersList;

    private void ComputeDistances(Usuarios usuarios) {
        Iterator<Usuario> iterator = usuarios.iterator();
        distances = new int[nApuntados][nApuntados];
        while (iterator.hasNext()) {

        }
    }

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

    public void averageInit(){
        clearRoutes();
        int average = passengers.length/drivers.length;
        int passenger_pos = 0, j;
        for(int i=0;i<drivers.length;i++){
            pass(i,drivers[i]);
            for(j = passenger_pos;j<passenger_pos+average;j++){
                pass(i,passengers[j]);
                pass(i,passengers[j]);
            }
            passenger_pos = j;
            if(i == drivers.length-1){
                for(j = passenger_pos;j < passengers.length;j++){
                    pass(i,passengers[j]);
                    pass(i,passengers[j]);
                }
            }
            pass(i,drivers[i]);
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
