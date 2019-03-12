import aima.search.framework.HeuristicFunction;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;


public class SuccessorFunction implements aima.search.framework.SuccessorFunction {
    @SuppressWarnings("unchecked")

    private void movePassengers(ArrayList retVal, State currentState, int conductor1, int conductor2) {
        for (int pasajero = 0; pasajero < currentState.pasajerosDeConductor(conductor1); pasajero++) {
            for (int recogida = 0; currentState.pasajerosDeConductor(conductor1); recogida++) {
                for (int dejada = recogida + 1; currentState.pasajerosDeConductor(conductor2) + 1; dejada++) {
                    State newState = new State(currentState);
                    if (newState.movePassenger(conductor2, conductor1, pasajero, recogida, dejada) {
                        heuristicValue = ihf.getHeuristicValue(newState);
                        String s = "Mover pasajero " + conductor2 + " -> " + conductor1 + "; " + pasajero + " -> ";
                        s += (recogida + ", " + dejada + "; Coste: " + heuristicValue);
                        retVal.add(new Successor(s, newState));
                    }
                }
            }
        }
    }


    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        State currentState = (State) aState;
        IablHeuristicFuncion ihf = new IablHeuristicFunction();

        double heuristicValue;

        for (int conductor1 = 0; conductor1 < aState.getmConducir() - 1; conductor1++) {
            for(int conductor2 = conductor1 + 1; conductor2 < aState.getmConducir(); conductor2++) {
                movePassengers(retVal, currentState, conductor1, conductor2);
                movePassengers(retVal, currentState, conductor2, conductor1);
            }
        }
        //conductor1, conductor2, pasajero1, pasajero2, recogida1, dejada1, recogida2, dejada2;

        for (int conductor1 = 0; conductor1 < currentState.getmConducir - 1; conductor1++) {
            for (int conductor2 = conductor1 + 1; conductor2 < currentState.getmConducir; conductor2++) {
                for (int pasajero1 = 0; pasajero1 < currentState.pasajerosDeConductor(conductor1)< pasajero1++) {
                    for (int pasajero2 = 0; pasajero2 < currentState.pasajerosDeConductor(conductor2)< pasajero2++) {
                        for (int recogida1 = 0; recogida1 < currentState.pasajerosDeConductor(conductor2) - 1; recogida1++) {
                            for(int dejada1 = recogida1 + 1; dejada1 < currentState.pasajerosDeConductor(conductor2); dejada1++) {

                            }
                        }
                    }
                }
            }
        }
//        // No permitimos intercambiar la primera ciudad
//        for (int i = 0; i < board.getNCities(); i++) {
//            for (int j = i + 1; j < board.getNCities(); j++) {
//                ProbTSPBoard newBoard = new ProbTSPBoard(board.getNCities(), board.getPath(), board.getDists());
//
//                newBoard.swapCities(i, j);
//
//                double    v = TSPHF.getHeuristicValue(newBoard);
//                String S = ProbTSPBoard.INTERCAMBIO + " " + i + " " + j + " Coste(" + v + ") ---> " + newBoard.toString();
//
//                retVal.add(new Successor(S, newBoard));
//            }
        }

        return retVal;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
