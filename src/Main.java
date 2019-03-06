import IA.Comparticion.*;
public class Main {

    public static void main(String[] args) {
        Usuario us = new Usuario(1,2,3,4,true);
        System.out.println(us.getCoordDestinoX());
        State s = new State(1,2);
        System.out.println(s.toString());
    }
}
