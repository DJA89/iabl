import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;

import java.util.Iterator;

public class State {

    private int [][] routes;
    public int [][] getRoutes(){return routes;}
    private int nApuntados;
    private int mConducir;
    public State(int n, int m) {
        //n: numero de personas apuntadas
        //m: numero de personas que pueden conducir
        nApuntados = n;
        mConducir = m;
        routes = new int[m][2*n+1];
    }
    public State(Usuarios usuarios) {
        Usuario tmp;
        mConducir = 0;
        nApuntados = usuarios.size();
        int[] tmpDrivers = new int[nApuntados];
        Iterator<Usuario> iterator = usuarios.iterator();
        while (iterator.hasNext()) {
            tmp = iterator.next();
            if(tmp.isConductor()){
                tmpDrivers[mConducir] = tmp.hashCode();
                mConducir ++;
            }
        }
        routes = new int[mConducir][2*nApuntados + 1];
        for(int i=0;i<mConducir;i++){
            routes[i][0] = tmpDrivers[i];
        }
    }
    public String getRoute(int n){
        String retVal = "";
        for(int j=0;j<=2*nApuntados;j++){
            retVal += routes[n-1][j] + " ";
        }
        return retVal;
    }
    public String toString() {
        String retVal = "";
        for(int i=0;i<mConducir;i++){
            for(int j=0;j<=2*nApuntados;j++){
                retVal += routes[i][j] + " ";
            }
            retVal += "\n";
        }
        return retVal;
    }
}
