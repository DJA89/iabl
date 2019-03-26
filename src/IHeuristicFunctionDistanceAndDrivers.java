import aima.search.framework.HeuristicFunction;

public class IHeuristicFunctionDistanceAndDrivers implements HeuristicFunction  {

    public double getHeuristicValue(Object state) {
        State sState = (State) state;
        int[] routesDistance = sState.GetDistancia_ruta_optima();
        double retValue = 0;
        int currentLength;
        int conductoresTotales = sState.GetConductoresTotales();
        float promedioPasPerCond = sState.getPasajerosTotales() / (float) conductoresTotales;
        boolean esSolucion = true;
        for (int i = 0; i < conductoresTotales; i++) {
            currentLength = routesDistance[i];
            if (currentLength <= 300) {
                int cantidadPasajeros = sState.PasajerosDeConductor((short) i).size();
                if (cantidadPasajeros < promedioPasPerCond) {
                    retValue += currentLength * cantidadPasajeros/promedioPasPerCond;
                } else {
                    retValue += currentLength;
                }
            } else {
                esSolucion = false;
                retValue += currentLength^2;
            }
        }
        if (esSolucion) {
            int conductoresActivos = sState.numeroDeConductoresActivos();
            retValue *= ((float) (conductoresTotales - conductoresActivos))/(float) (conductoresTotales * 1.5);
        }
        return (retValue);
    }
}
