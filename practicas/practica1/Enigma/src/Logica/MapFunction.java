package Logica;

public class MapFunction {

    private Character[] map;

    public MapFunction(Character[] f) {
        this.map = f;
    }
    /*La primera posición del arreglo corresponde al map de la letra A, B la segunda, 
      C la tercera y así sucesivamente*/
    public int map(int c) {
        return map[c] - 'A';
    }
}
