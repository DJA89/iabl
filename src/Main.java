import IA.Comparticion.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        State s = new State(new Usuarios(10, 4, 1));
        s.ImprimirDistancias();
        s.ImprimirPosiciones();
        //s.donkeyInit();
        System.out.println("Donkey Init" + s);
        s.averageInit();
        System.out.println("Average Init" + s);
        //s.minRouteInit();
        System.out.println("Min Route Init" + s);

        TSPHillClimbingSearch(s);
    }


    private static void TSPHillClimbingSearch(State myState) {
        System.out.println("\nTSP HillClimbing  -->");
        try {
            Problem problem =  new Problem(myState,new ConductoresSuccessorFunction(), new MyGoalTest(),new IHeuristicFunctionDistance());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
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
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }
}
