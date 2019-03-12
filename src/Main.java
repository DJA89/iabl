import IA.Comparticion.*;
public class Main {

    public static void main(String[] args) {
        Demo d = new Demo(new Usuarios(10, 4,1));
        System.out.println("Donkey Init\n" + d.donkeyInit().toString());
        System.out.println("Average Init\n" + d.averageInit().toString());
    }
}
