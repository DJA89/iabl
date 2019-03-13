import java.util.ArrayList;

public class State {

    private int[] routesDistance;
    private ArrayList[] routes;
    private static int[][] distances;

    private void ComputeDistances(Usuarios usuarios) {
        Iterator<Usuario> iterator = usuarios.iterator();
        while (iterator.hasNext()) {
        }

    }

    public State(int mDrivers) {
        //m: numero de personas que pueden conducir
        routes = new ArrayList[mDrivers];
        for(int i = 0; i < mDrivers; i++) {
            routes[i] = new ArrayList<Integer>();
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
