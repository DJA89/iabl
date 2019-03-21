/*import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;

import java.util.ArrayList;
import java.util.Iterator;


public class Demo {
    private Integer[] drivers;
    private Integer[] passengers;
    private Integer[] users;
    private Usuarios usersList;

    public Integer[] getDrivers() {
        return drivers;
    }

    public Integer[] getPassengers() {
        return passengers;
    }

    public Integer[] getUsers() {
        return users;
    }

    public Demo(Usuarios usersList){
        setUserList(usersList);
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
        drivers = tmpDrivers.toArray(new Integer[tmpDrivers.size()]);
        passengers = tmpPassengers.toArray(new Integer[tmpPassengers.size()]);
        users = tmpUsers.toArray(new Integer[tmpUsers.size()]);
        this.usersList = usersList;
    }

    public State donkeyInit(){
        State s = new State(drivers.length);
        s.pass(0, drivers[0]);
        for(int i=0;i<users.length;i++){
            if(users[i].intValue() != drivers[0].intValue()){
                s.pass(0,users[i]);
                s.pass(0,users[i]);
            }
        }
        s.pass(0, drivers[0]);
        return s;
    }

    public State averageInit(){
        State s = new State(drivers.length);
        int average = passengers.length/drivers.length;
        int passenger_pos = 0, j;
        for(int i=0;i<drivers.length;i++){
            s.pass(i,drivers[i]);
            for(j = passenger_pos;j<passenger_pos+average;j++){
                s.pass(i,passengers[j]);
                s.pass(i,passengers[j]);
            }
            passenger_pos = j;
            if(i == drivers.length-1){
                for(j = passenger_pos;j < passengers.length;j++){
                    s.pass(i,passengers[j]);
                    s.pass(i,passengers[j]);
                }
            }
            s.pass(i,drivers[i]);
        }
        return s;
    }

    public State minInit(){
        State s = new State(drivers.length,this.usersList);

    }
}

*/