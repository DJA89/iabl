import aima.search.framework.HeuristicFunction;

public class IHeuristicFunctionDistanceAndDrivers implements HeuristicFunction  {

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

        return (retValue);
    }
}
