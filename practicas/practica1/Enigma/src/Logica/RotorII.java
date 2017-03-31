package Logica;

public class RotorII extends MapRotor {

    public RotorII() {
        Character[] b = {'A', 'J', 'P', 'C', 'Z', 'W', 'R', 'L', 'F', 'B', 'D', 'K', 'O', 'T', 'Y', 'U', 'Q', 'G', 'E', 'N', 'H', 'X', 'M', 'I', 'V', 'S'};
        Character[] f = {'A', 'J', 'D', 'K', 'S', 'I', 'R', 'U', 'X', 'B', 'L', 'H', 'W', 'T', 'M', 'C', 'Q', 'G', 'Z', 'N', 'P', 'Y', 'F', 'V', 'O', 'E'};
        forward = new MapFunction(f);
        backward = new MapFunction(b);
    }
    
    @Override
    public Character getSalto() {
	return 'E';
    }
}
