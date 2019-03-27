import IA.Comparticion.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.awt.Color;

public class Main {

    public static void main(String[] args) throws IOException{
        adjustmentK();
        /*
        Plot plot = Plot.plot(Plot.plotOpts().
                title("TSPSimulatedAnnealingSearch").
                legend(Plot.LegendFormat.BOTTOM)).
                xAxis("Usuarios", Plot.axisOpts().
                        range(0, 200)).
                yAxis("Tiempo", Plot.axisOpts().
                        range(0, 200));
        Plot.Data datos = Plot.data();
        for(int i=1;i<=20;i++){
            State s = new State(new Usuarios(10*i, 5*i, 1234));
            s.averageInit();
            TSPSimulatedAnnealingSearch(s);
            datos.xy(10*i,elapsedTime);

        }
        plot.series("Data", datos,
                Plot.seriesOpts().
                        marker(Plot.Marker.DIAMOND).
                        markerColor(Color.GREEN).
                        color(Color.BLACK));
        plot.save("TSPSimulatedAnnealingSearch", "png");
        */
    }


    private static long elapsedTime;
    private static State goalState;
    private static SimulatedAnnealingSearch search;

    private static void TSPHillClimbingSearch(State myState) {
        System.out.println("\nTSP HillClimbing  -->" + "\n");
        try {
            long startTime = System.currentTimeMillis();

            Problem problem =  new Problem(myState,new ConductoresSuccessorFunctionExperiments(), new MyGoalTest(),new IHeuristicFunctionDistance());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);
            long stopTime = System.currentTimeMillis();
            elapsedTime = stopTime - startTime;
            System.out.println("Tiempo de ejecución: " + elapsedTime);
            printInstrumentation(agent.getInstrumentation());
            goalState = (State) search.getGoalState();
            System.out.println("Distancia total: " + goalState.distanciaTotal());
            System.out.println(search.getGoalState());
            printActions(agent.getActions());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void TSPSimulatedAnnealingSearch(State myState) {
        System.out.println("\nTSP Simulated Annealing  -->" + "\n");
        try {
            long startTime = System.currentTimeMillis();

            Problem problem =  new Problem(myState,new ConductoresSuccessorFunctionSA(), new MyGoalTest(),new IHeuristicFunctionDistance());
            //search.traceOn();
            SearchAgent agent = new SearchAgent(problem,search);
            long stopTime = System.currentTimeMillis();
            elapsedTime = stopTime - startTime;
            System.out.println("Tiempo de ejecución: " + elapsedTime);
            printInstrumentation(agent.getInstrumentation());
            goalState = (State) search.getGoalState();
            System.out.println("Distancia total: " + goalState.distanciaTotal());
            System.out.println();
            printActions(agent.getActions());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = actions.get(i).toString();
            System.out.println(action);
        }
    }

    private static void adjustmentK() throws IOException{
        State s = new State(new Usuarios(50, 25, 1234));
        s.minRouteInit();
        TSPHillClimbingSearch(s);
        double hillClimbingDistance = goalState.distanciaTotal();
        Plot plot = Plot.plot(Plot.plotOpts().
                title("TSPSimulatedAnnealingSearch").
                legend(Plot.LegendFormat.BOTTOM)).
                xAxis("k", Plot.axisOpts().
                        range(0, 30)).
                yAxis("Tiempo", Plot.axisOpts().
                        range(0, 200));
        Plot.Data datos = Plot.data();
        int bestK = 1;
        double bestDistance = Double.MAX_VALUE;
        for(int i=0;i<3;i++){
            int k = (int) Math.pow(5,i);
            System.out.println("K: " + k);
            search =  new SimulatedAnnealingSearch(500,10000,k,0.1);
            TSPSimulatedAnnealingSearch(s);
            datos.xy(k,elapsedTime);
            if(bestDistance > Math.abs(goalState.distanciaTotal() - hillClimbingDistance)){
                bestDistance = goalState.distanciaTotal();
                bestK = k;
            }

        }
        plot.series("Data", datos,
                Plot.seriesOpts().
                        marker(Plot.Marker.DIAMOND).
                        markerColor(Color.GREEN).
                        color(Color.BLACK));
        plot.save("Adjustment_K", "png");
        System.out.println("Mejor K: " + bestK);
    }
}
