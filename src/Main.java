import IA.Comparticion.*;
public class Main {

    public static void main(String[] args) {
        State s = new State(new Usuarios(10, 4,1));
        s.donkeyInit();
        System.out.println("Donkey Init\n" + s.toString());
        s.averageInit();
        System.out.println("Average Init\n" + s.toString());
    }
}
