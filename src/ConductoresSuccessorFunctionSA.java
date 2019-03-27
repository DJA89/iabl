
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.*;

public class ConductoresSuccessorFunctionSA implements SuccessorFunction {
    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        State currentState = (State) aState;
        IHeuristicFunctionDistance ihf = new IHeuristicFunctionDistance();
        double heuristicValue;
        short conductoresTotales = currentState.GetConductoresTotales(), conductor1, conductor2, pasajero1, pasajero2, pasajero;
        HashSet<Short> pasajerosDeConductor1, pasajerosDeConductor2;
        Iterator it1, it2;
        Random myRandom = new Random();
        State newState;
        int index;

        do{
            conductor1 = (short)myRandom.nextInt(conductoresTotales);
            pasajerosDeConductor1 = currentState.PasajerosDeConductor(conductor1);
        } while (pasajerosDeConductor1.size()<=0);
        index = myRandom.nextInt(pasajerosDeConductor1.size());
        it1 = pasajerosDeConductor1.iterator();
        for (int i = 0; i < index; i++) {
            it1.next();
        }
        pasajero1 = (short) it1.next();

        do{
            conductor2 = (short)myRandom.nextInt(conductoresTotales);
            pasajerosDeConductor2 = currentState.PasajerosDeConductor(conductor2);
        } while (pasajerosDeConductor2.size()<=0);
        index = myRandom.nextInt(pasajerosDeConductor2.size());
        it2 = pasajerosDeConductor2.iterator();
        for (int i = 0; i < index; i++) {
            it2.next();
        }
        pasajero2 = (short) it2.next();

        newState = new State(currentState);
        if (newState.SwapPasajeros(conductor1, conductor2, pasajero1, pasajero2)) {
            heuristicValue = ihf.getHeuristicValue(newState);
            String S = "Intercambiamos al pasajero " + pasajero1 + " del conductor " + conductor1 +
                    " con el pasajero " + pasajero2 + " del conductor " + conductor2 +
                    ". Nuevo valor: " + heuristicValue + "; Número total de conductores: " + newState.numeroDeConductoresActivos() + "; distancia total: " + newState.distanciaTotal();
            //System.out.println("posible nueva distancia óptima: " + heuristicValue);
            retVal.add(new Successor(S, newState));
        }

        do{
            conductor1 = (short)myRandom.nextInt(conductoresTotales);
            pasajerosDeConductor1 = currentState.PasajerosDeConductor(conductor1);
        } while (pasajerosDeConductor1.size()<=0);
        index = myRandom.nextInt(pasajerosDeConductor1.size());
        it1 = pasajerosDeConductor1.iterator();
        for (int i = 0; i < index; i++) {
            it1.next();
        }
        pasajero = (short) it1.next();
        conductor2 = (short)myRandom.nextInt(conductoresTotales);


        newState = new State(currentState);
        if (conductor2 != conductor1 && newState.MoverPasajero(conductor1, conductor2, pasajero)) {
            heuristicValue = ihf.getHeuristicValue(newState);
            String S = "Movemos al pasajero " + pasajero + " del conductor " + conductor1 +
                    " al conductor " + conductor2 + ". Nuevo valor: " + heuristicValue + "; Número total de conductores: " + newState.numeroDeConductoresActivos() + "; distancia total: " + newState.distanciaTotal();
            retVal.add(new Successor(S, newState));
        }

        conductor1 = (short)myRandom.nextInt(conductoresTotales);
        conductor2 = (short)myRandom.nextInt(conductoresTotales);

        newState = new State(currentState);
        if (newState.SwapConductor(conductor1, conductor2)) {
            heuristicValue = ihf.getHeuristicValue(newState);
            String S = "Intercambiar todos los pasajeros de " + conductor1 +
                    " y " + conductor2 + ". Nuevo valor: " + heuristicValue + "; Número total de conductores: " + newState.numeroDeConductoresActivos() + "; distancia total: " + newState.distanciaTotal();
            retVal.add(new Successor(S, newState));
        }

        return retVal;
    }
}