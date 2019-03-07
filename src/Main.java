import IA.Comparticion.*;
public class Main {

    public static void main(String[] args) {
        Usuario us = new Usuario(1,2,3,4,true);
        Usuarios usuarios = new Usuarios(5, 3,1);
        State s1 = new State(3,2);
        State s2 = new State(usuarios);
        s2.pass(usuarios.get(1),usuarios.get(2));
        System.out.println(s1.toString());
        System.out.println(s2.toString());
        System.out.println(s2.getRoute(2));
        System.out.println(s2.getRoute(usuarios.get(1)));
    }
}
