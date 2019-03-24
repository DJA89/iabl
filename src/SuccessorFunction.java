import aima.search.framework.Successor;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;

public class SuccessorFunction implements aima.search.framework.SuccessorFunction {
    @SuppressWarnings("unchecked")


    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        State currentState = (State) aState;
        IHeuristicFunctionDistance ihf = new IHeuristicFunctionDistance();
        double heuristicValue;
        short conductoresTotales = currentState.GetConductoresTotales();

        for (short conductor1 = 0; conductor1 < conductoresTotales - 1; conductor1++) {
            for(short conductor2 = (short) (conductor1 + 1); conductor2 < conductoresTotales; conductor2++) {
                HashSet<Short> pasajerosDeConductor1 = currentState.PasajerosDeConductor(conductor1);
                HashSet<Short> pasajerosDeConductor2 = currentState.PasajerosDeConductor(conductor2);
                Iterator it1 = pasajerosDeConductor1.iterator();
                Iterator it2 = pasajerosDeConductor2.iterator();
                while (it1.hasNext()) {
                    short pasajero1 = (short) it1.next();
                    while (it2.hasNext()) {
                        short pasajero2 = (short) it2.next();
                        State newState = new State(currentState);
                        if (newState.SwapPasajeros(conductor1, conductor2, pasajero1, pasajero2)) {
                            heuristicValue = ihf.getHeuristicValue(newState);
                            String S = "Intercambiamos al pasajero " + pasajero1 + " del conductor " + conductor1 +
                                    " con el pasajero " + pasajero2 + " del conductor " + conductor2 +
                                    ". Nuevo valor: " + heuristicValue;
                            retVal.add(new Successor(S, newState));
                        }
                    }
                }
            }
        }
        //conductor1, conductor2, pasajero1, pasajero2, recogida1, dejada1, recogida2, dejada2;
        for (short conductor1 = 0; conductor1 < conductoresTotales; conductor1++) {
            HashSet<Short> pasajerosDeConductor1 = currentState.PasajerosDeConductor(conductor1);
            //Iterator it1 = pasajerosDeConductor1.iterator();
            //while (it1.hasNext()) {
            for(short pasajero : pasajerosDeConductor1) {
                //short pasajero = (short) it1.next();
                for (short conductor2 = 0; conductor2 < conductoresTotales; conductor2++) {
                    State newState = new State(currentState);
                    if (newState.MoverPasajero(conductor1, conductor2, pasajero)) {
                        heuristicValue = ihf.getHeuristicValue(newState);
                        String S = "Movemos al pasajero " + pasajero + " del conductor " + conductor1 +
                                " al conductor " + conductor2 + ". Nuevo valor: " + heuristicValue;
                        retVal.add(new Successor(S, newState));
                    }
                }
            }
        }

        return retVal;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
