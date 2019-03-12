import java.util.ArrayList;

public class State {

    private ArrayList[] routes;

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
}
