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
        Scanner reader = new Scanner(System.in);
        do{
            int program;
            do{
                System.out.print("Eliga opción: Terminar programa (0) / Iniciar programa (1): ");
                program = reader.nextInt();
            }while(program != 0 && program != 1);
            if(program == 0) break;
            System.out.print("Escriba número de usuarios: ");
            int n = reader.nextInt();
            System.out.print("Escriba número de conductores: ");
            int m = reader.nextInt();
            System.out.print("Escriba seed: ");
            int seed = reader.nextInt();
            State s = new State(new Usuarios(n, m, seed));
            int init;
            do{
                System.out.print("Elige inicializador: Average Init (0) / Greedy Init (1): ");
                init = reader.nextInt();
            }while(init != 0 && init != 1);
            if(init == 0) s.averageInit();
            else s.minRouteInit();
            int typeSearch;
            do{
                System.out.print("Elige tipo de búsqueda: Hill Climbing (0) / Simulated Annealing (1): ");
                typeSearch = reader.nextInt();
            }while(typeSearch != 0 && typeSearch != 1);
            if(typeSearch == 0) TSPHillClimbingSearch(s);
            else{
                System.out.print("Escriba número de iteraciones totales: ");
                int iter = reader.nextInt();
                System.out.print("Escriba número de iteraciones por cambio de temperatura: ");
                int steps = reader.nextInt();
                System.out.print("Escriba k (recomendación: 1): ");
                int k = reader.nextInt();
                System.out.print("Escriba lambda (recomendación: 0.01): ");
                double l = reader.nextDouble();
                search = new SimulatedAnnealingSearch(steps,iter,k,l);
                TSPSimulatedAnnealingSearch(s);
            }
        } while (true);
        experimentSA();
    }


    private static long elapsedTime;
    private static State goalState;
    private static SimulatedAnnealingSearch search;

    private static void TSPHillClimbingSearch(State myState) {
        System.out.println("\nTSP HillClimbing  -->" + "\n");
        try {
            long startTime = System.currentTimeMillis();

            Problem problem =  new Problem(myState,new ConductoresSuccessorFunction2(), new MyGoalTest(),new IHeuristicFunctionDistanceAndDrivers());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);
            elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Tiempo de ejecución: " + time(elapsedTime));
            printInstrumentation(agent.getInstrumentation());
            goalState = (State) search.getGoalState();
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

            Problem problem =  new Problem(myState,new ConductoresSuccessorFunctionSA(), new MyGoalTest(),new IHeuristicFunctionDistanceAndDrivers());
            SearchAgent agent = new SearchAgent(problem,search);
            elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Tiempo de ejecución: " + time(elapsedTime));
            printInstrumentation(agent.getInstrumentation());
            goalState = (State) search.getGoalState();
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
        State s = new State(new Usuarios(200, 100, 1234));
        int iter = 4;
        double distance;
        s.averageInit();
        ArrayList<String> res = new ArrayList<String>();
        for(int i=0; i<iter;i++){
            for(int j=0; j<iter; j++){
                int k = (int) Math.pow(5,i);
                double l = Math.pow(10,-j);
                search = new SimulatedAnnealingSearch(500,10000,k,l);
                TSPSimulatedAnnealingSearch(s);
                distance = goalState.distanciaTotal();
                res.add("k: " + k + " l: " + l + " Costo: " + distance + " Tiempo: " + time(elapsedTime));
            }
        }
        for(String r: res){
            System.out.println(r);
        }
    }

    private static void plotting() throws IOException{
        Plot plot = Plot.plot(Plot.plotOpts().
                title("Graphic").
                legend(Plot.LegendFormat.BOTTOM));
        Plot.Data line = Plot.data();
        for(int i=0;i<5;i++){
            line.xy(0,1);
        }
        plot.series("Data", line, Plot.seriesOpts().
                        marker(Plot.Marker.DIAMOND).
                        markerColor(Color.GREEN).
                        color(Color.BLACK)).
                xAxis("k", Plot.axisOpts().range(0, 5)).
                yAxis("Distancia", Plot.axisOpts().range(0, 5*1.05)).
                save("Adjustment", "png");
    }

    private static String time(double ms){
        return (int) ms/60000 + " m " + (int) (ms/1000)%60 + " s";

    }
}
