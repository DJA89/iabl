public class State {

    private int [][] routes;
    public int [][] getRoutes(){return routes;}
    private int nApuntados;
    private int mConducir;
    public State(int n, int m) {
        //n: numero de personas apuntadas
        //m: numero de personas que pueden conducir
        this.nApuntados = n;
        this.mConducir = m;
        this.routes = new int[m][n];
    }
    public String toString() {
        String retVal = "";
        for(int i=0;i<mConducir;i++){
            for(int j=0;j<nApuntados;j++){
                retVal += routes[i][j] + " ";
            }
            retVal += "\n";
        }
        return retVal;
    }
}
