import IA.Comparticion.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        State s = new State(new Usuarios(50, 25, 1234));
        //s.ImprimirDistancias();
        //s.ImprimirPosiciones();
        //s.donkeyInit();
        //System.out.println("Donkey Init" + s);
        s.averageInit();
        System.out.println("Average Init" + s);
        //s.minRouteInit();
        //System.out.println("Min Route Init" + s);

        TSPSimulatedAnnealingSearch(s);
        TSPHillClimbingSearch(s);
    }


    private static void TSPHillClimbingSearch(State myState) {
        System.out.println("\nTSP HillClimbing  -->" + "\n");
        try {
            long startTime = System.currentTimeMillis();

            Problem problem =  new Problem(myState,new ConductoresSuccessorFunctionExperiments(), new MyGoalTest(),new IHeuristicFunctionDistance());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println("Tiempo de ejecución: " + elapsedTime);
            printInstrumentation(agent.getInstrumentation());
            System.out.println("Distancia total: " + (((State) search.getGoalState()).distanciaTotal()));
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
            SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(100,5,5,0.001);
            //search.traceOn();
            SearchAgent agent = new SearchAgent(problem,search);
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println("Tiempo de ejecución: " + elapsedTime);
            printInstrumentation(agent.getInstrumentation());
            System.out.println("Distancia total: " + (((State) search.getGoalState()).distanciaTotal()));
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
}
