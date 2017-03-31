package Logica;

public class RotorIV extends MapRotor {

    public RotorIV() {
        Character[] b = {'H', 'Z', 'W', 'V', 'A', 'R', 'T', 'N', 'L', 'G', 'U', 'P', 'X', 'Q', 'C', 'E', 'J', 'M', 'B', 'S', 'K', 'D', 'Y', 'O', 'I', 'F'};
        Character[] f = {'E', 'S', 'O', 'V', 'P', 'Z', 'J', 'A', 'Y', 'Q', 'U', 'I', 'R', 'H', 'X', 'L', 'N', 'F', 'T', 'G', 'K', 'D', 'C', 'M', 'W', 'B'};
        forward = new MapFunction(f);
        backward = new MapFunction(b);
    }
    
    @Override
    public Character getSalto() {
	return 'J';
    }
}
