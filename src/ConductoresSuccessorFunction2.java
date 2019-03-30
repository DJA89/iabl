import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;

public class ConductoresSuccessorFunction2 implements SuccessorFunction {
    @SuppressWarnings("unchecked")


    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        State currentState = (State) aState;
        //System.out.println("Estado actual" + currentState);
        IHeuristicFunctionDistanceAndDrivers ihf = new IHeuristicFunctionDistanceAndDrivers();
        double heuristicValue;
        short conductoresTotales = currentState.GetConductoresTotales();
        //System.out.println("distancia optima actual: " + ihf.getHeuristicValue(currentState));

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
                                    ". Nuevo valor: " + heuristicValue + "; Número total de conductores libres: " + newState.numeroDeConductoresActivos() + "; distancia total: " + newState.distanciaTotal();
                            //System.out.println("posible nueva distancia óptima: " + heuristicValue);
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
                    if (conductor2 != conductor1 && newState.MoverPasajero(conductor1, conductor2, pasajero)) {
                        heuristicValue = ihf.getHeuristicValue(newState);
                        String S = "Movemos al pasajero " + pasajero + " del conductor " + conductor1 +
                                " al conductor " + conductor2 + ". Nuevo valor: " + heuristicValue + "; Número total de conductores libres: " + newState.numeroDeConductoresActivos() + "; distancia total: " + newState.distanciaTotal();
                        //System.out.println("posible nueva distancia óptima: " + heuristicValue);
                        retVal.add(new Successor(S, newState));
                    }
                }
            }
        }

        for (short conductor1 = 0; conductor1 < conductoresTotales; conductor1++) {
            for (short conductor2 = (short) (conductor1 + 1); conductor2 < conductoresTotales; conductor2++) {
                State newState = new State(currentState);
                if (newState.SwapConductor(conductor1, conductor2)) {
                    heuristicValue = ihf.getHeuristicValue(newState);
                    String S = "Intercambiar todos los pasajeros de " + conductor1 +
                            " y " + conductor2 + ". Nuevo valor: " + heuristicValue + "; Número total de conductores libres: " + newState.numeroDeConductoresActivos() + "; distancia total: " + newState.distanciaTotal();
                    retVal.add(new Successor(S, newState));
                }
            }
        }
        return retVal;
    }
}


