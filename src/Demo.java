import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;
import aima.search.framework.Problem;

import java.util.Iterator;


public class Demo {

    public State donkeyInit(Usuarios usuarios){
        State s = new State(usuarios);
        Usuario tmp;
        int id = s.getDrivers()[0];
        s.pass(0,id);
        Iterator<Usuario> iterator = usuarios.iterator();
        while (iterator.hasNext()) {
            tmp = iterator.next();
            if(tmp.hashCode() != id) {
                s.pass(0, tmp);
                s.pass(0, tmp);
            }
        }
        s.pass(0,id);
        return s;
    }
}
