import IA.Comparticion.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import java.io.IOException;
import java.util.*;
import java.awt.Color;

public class Main {

    public static void main(String[] args) {
        State s = new State(new Usuarios(200, 100, 1234));
        s.ImprimirDistancias();
        //s.ImprimirPosiciones();
        //s.donkeyInit();
        s.averageInit();
        //s.minRouteInit();
        //s.randomInit();
        System.out.println("Initial State" + s);

        //TSPSimulatedAnnealingSearch(s);
        TSPHillClimbingSearch(s);
    }


    private static long elapsedTime;
    private static State goalState;
    private static SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(500,10000,1 ,0.1);;

    private static void TSPHillClimbingSearch(State myState) {
        System.out.println("\nTSP HillClimbing  -->" + "\n");
        try {
            long startTime = System.currentTimeMillis();

            Problem problem =  new Problem(myState,new ConductoresSuccessorFunction2(), new MyGoalTest(),new IHeuristicFunctionDistanceAndDrivers());
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
            System.out.println("Tiempo de ejecución: " + elapsedTime);
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void TSPSimulatedAnnealingSearch(State myState) {
        System.out.println("\nTSP Simulated Annealing  -->" + "\n");
        try {
            long startTime = System.currentTimeMillis();

            Problem problem =  new Problem(myState,new ConductoresSuccessorFunction2(), new MyGoalTest(),new IHeuristicFunctionDistanceAndDrivers());
            SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(1000,10000,15,0.1);
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

    private static void experimentSA(){
        State s = new State(new Usuarios(50, 25, 1234));
        int iter = 4;
        double distance;
        s.minRouteInit();
        TSPHillClimbingSearch(s);
        ArrayList<String> res = new ArrayList<String>();
        for(int i=0; i<iter-1;i++){
            for(int j=0; j<iter; j++){
                int k = (int) Math.pow(5,i);
                double l = Math.pow(10,-j);
                search = new SimulatedAnnealingSearch(500,10000,k,l);
                TSPSimulatedAnnealingSearch(s);
                distance = goalState.distanciaTotal();
                res.add("k: " + k + " l: " + l + " Costo: " + distance + " Tiempo: " + elapsedTime);
            }
        }
        for(String r: res){
            System.out.println(r);
        }
    }
    private static void adjustmentK() throws IOException{
        State s = new State(new Usuarios(50, 25, 1234));
        s.minRouteInit();
        TSPHillClimbingSearch(s);
        double hillClimbingDistance = goalState.distanciaTotal();
        Plot plot = Plot.plot(Plot.plotOpts().
                title("TSPSimulatedAnnealingSearch").
                legend(Plot.LegendFormat.BOTTOM));
        Plot.Data line = Plot.data();
        int bestK = 1, iter = 2;
        double maxX, maxY, differ, maxDistance = 0, distance;
        double bestDistance = Double.MAX_VALUE;
        for(int i=0;i<iter;i++){
            int k = (int) Math.pow(5,i);
            System.out.println("K: " + k);
            search =  new SimulatedAnnealingSearch(500,10000,k,0.1);
            TSPSimulatedAnnealingSearch(s);
            distance = goalState.distanciaTotal();
            line.xy(k,distance);
            differ = Math.abs(distance - hillClimbingDistance);
            if(bestDistance > differ){
                bestDistance = differ;
                bestK = k;
            }
            if(maxDistance < distance) maxDistance = distance;
            if(i == iter - 1){
                plot.xAxis("k", Plot.axisOpts().
                        range(0, k)).
                        yAxis("Distancia", Plot.axisOpts().
                                range(0, maxDistance*1.05));
            }
        }
        plot.series("Data", line,
                Plot.seriesOpts().
                        marker(Plot.Marker.DIAMOND).
                        markerColor(Color.GREEN).
                        color(Color.BLACK));
        plot.save("Adjustment_K", "png");
        System.out.println("Mejor K: " + bestK);
    }
}
