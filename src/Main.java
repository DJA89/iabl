import IA.Comparticion.*;
public class Main {

    public static void main(String[] args) {
        Usuario us = new Usuario(1,2,3,4,true);
        Usuarios usuarios = new Usuarios(5, 3,1);
        State s1 = new State(1,2);
        State s2 = new State(usuarios);
        System.out.println(s1.toString());
        System.out.println(s2.toString());
        System.out.println(s2.getRoute(2));
    }
}
