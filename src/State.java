import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;

import java.util.ArrayList;
import java.util.Iterator;

public class State {

    private ArrayList[] routes;
    private int[] drivers;
    private int nApuntados;
    private int mConducir;
    private String ERROR_DRIVER = "ERROR: CONDUCTOR NO ENCONTRADO";
    private String ERROR_PASSENGER = "ERROR: PASAJERO NO ENCONTRADO";

    public State(int n, int m) {
        //n: numero de personas apuntadas
        //m: numero de personas que pueden conducir
        nApuntados = n;
        mConducir = m;
        drivers = new int[m];
        routes = new ArrayList[mConducir];
        for(int i = 0; i < mConducir; i++) {
            routes[i] = new ArrayList<Integer>();
        }
    }

    public State(Usuarios usuarios) {
        Usuario tmp;
        mConducir = 0;
        nApuntados = usuarios.size();
        int[] tmpDrivers = new int[nApuntados];
        Iterator<Usuario> iterator = usuarios.iterator();
        while (iterator.hasNext()) {
            tmp = iterator.next();
            if(tmp.isConductor()) {
                tmpDrivers[mConducir] = tmp.hashCode();
                mConducir ++;
            }
        }
        drivers = new int[mConducir];
        routes = new ArrayList[mConducir];
        for(int i = 0; i < mConducir; i++) {
            routes[i] = new ArrayList<Integer>();
            drivers[i] = tmpDrivers[i];
        }
    }

    public void pass(Usuario driver, Usuario passenger) {
        int pos = -1;
        for(int i = 0; i < mConducir; i++) {
            if(drivers[i] == driver.hashCode()) {
                pos = i;
            }
        }
        if(pos != -1) {
            routes[pos].add(passenger.hashCode());
        }
    }

    public String getRoute(int n) {
        String retVal = "id: " + drivers[n-1]+ ", route size: " + routes[n-1].size();
        for(int j = 0; j < routes[n-1].size(); j++) {
            retVal += " " + routes[n-1].get(j);
        }
        return retVal;
    }

    public String getRoute(Usuario driver) {
        int pos = -1;
        for(int i = 0; i < mConducir; i++) {
            if(drivers[i] == driver.hashCode()) {
                pos = i;
            }
        }
        if(pos != -1){
            String retVal = "id: " + drivers[pos]+ ", route size: " + routes[pos].size();
            for(int j = 0; j < routes[pos].size(); j++) {
                retVal += " " + routes[pos].get(j);
            }
            return retVal;
        } else {
            return ERROR_DRIVER;
        }
    }

    public String toString() {
        String retVal = "";
        for(int i = 0; i < mConducir; i++) {
            retVal += "id: " + drivers[i]+ ", route size: " + routes[i].size();
            for(int j = 0; j < routes[i].size(); j++) {
                retVal += " " + routes[i].get(j);
            }
            retVal += "\n";
        }
        return retVal;
    }
}
