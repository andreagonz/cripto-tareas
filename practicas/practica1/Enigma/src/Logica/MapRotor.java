package Logica;

public abstract class MapRotor {

    protected MapFunction forward;
    protected MapFunction backward;

    public int mapForward(int c) {
        return forward.map(c);
    }

    public int mapBackward(int c) {
        return backward.map(c);
    }

}
