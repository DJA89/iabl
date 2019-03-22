import IA.Comparticion.*;
public class Main {

    public static void main(String[] args) {
        State s = new State(new Usuarios(10, 4, 1));
        s.ImprimirDistancias();
        s.ImprimirPosiciones();
        s.donkeyInit();
        System.out.println("Donkey Init");
        s.ImprimirConductores();
        s.averageInit();
        System.out.println("Average Init");
        s.ImprimirConductores();
        s.minRouteInit();
        System.out.println("Min Route Init");
        s.ImprimirConductores();
    }
}
