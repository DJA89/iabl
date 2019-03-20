/*package IA.probTSP;

import aima.search.framework.HeuristicFunction;

public class IHeuristicFunctionDistanceAndDrivers implements HeuristicFunction  {

//    public boolean equals(Object obj) {
//        boolean retValue;
//
//        retValue = super.equals(obj);
//        return retValue;
//    }

    public double getHeuristicValue(Object state) {
        State state = (State) state;
        int[] routesDistance = state.getRoutesDistance();
        int retValue = 0;
        int currentLength;
        for (i = 0; i < routesDistance.length(); i++) {
            currentLength = routesDistance[i];
            if (currentLength <= 300) {
                retValue += (currentLength^2)/(-75.0) + currentLength * 50;
            } else {
                retValue += currentLength^2;
            }
        }

        return (retValue);
    }
}
*/