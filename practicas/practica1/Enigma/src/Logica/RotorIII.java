package Logica;

public class RotorIII extends MapRotor {

    public RotorIII() {
        Character[] b = {'T', 'A', 'G', 'B', 'P', 'C', 'S', 'D', 'Q', 'E', 'U', 'F', 'V', 'N', 'Z', 'H', 'Y', 'I', 'X', 'J', 'W', 'L', 'R', 'K', 'O', 'M'};
        Character[] f = {'B', 'D', 'F', 'H', 'J', 'L', 'C', 'P', 'R', 'T', 'X', 'V', 'Z', 'N', 'Y', 'E', 'I', 'W', 'G', 'A', 'K', 'M', 'U', 'S', 'Q', 'O'};
        forward = new MapFunction(f);
        backward = new MapFunction(b);
    }
    
    @Override
    public Character getSalto() {
	return 'V';
    }
}
