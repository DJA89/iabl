import aima.search.framework.HeuristicFunction;

public class IHeuristicFunctionDistanceAndDrivers implements HeuristicFunction  {

//    public boolean equals(Object obj) {
//        boolean retValue;
//
//        retValue = super.equals(obj);
//        return retValue;
//    }

    public double getHeuristicValue(Object state) {
        State sState = (State) state;
        int[] routesDistance = sState.GetDistancia_ruta_optima();


        int retValue = 0;
        int currentLength;
        int conductoresTotales = sState.GetConductoresTotales();
        for (int i = 0; i < conductoresTotales; i++) {
            currentLength = routesDistance[i];
            if (currentLength <= 150) {
                retValue += (currentLength^2)/(-75.0) + currentLength * 50;
            } else {
                retValue += currentLength^2;
            }
        }

        int N = sState.GetTotalUsuarios();
        int M = sState.GetConductoresTotales();
        int k1 = 100;
        int hConductores = 0;
        for(int i = 0; i < conductoresTotales; i++) {
            int x = sState.GetPasajeros(i);
            hConductores += (4 * k1 / (N ^ 2)) * x^2 + (4*k1/N)*x;
        }
        return retValue + k1*hConductores;


    }
}
