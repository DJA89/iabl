import IA.Comparticion.*;
public class Main {

    public static void main(String[] args) {
        Usuarios usuarios = new Usuarios(5, 3,1);
        Demo d = new Demo();
        State s2 = d.donkeyInit(usuarios);
        System.out.println(s2.toString());

    }
}
