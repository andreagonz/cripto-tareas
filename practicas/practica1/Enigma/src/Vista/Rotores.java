package Vista;

import Logica.*;
import java.awt.Container;

public class Rotores extends Container {

    private final Rotor[] rotor;
    private final MapRotor[] mf;

    public Rotores() {
        /*El rotor 0  es el de la izquierda*/
        rotor = new Rotor[]{new Rotor('Q'), new Rotor('E'), new Rotor('V')};
        mf = new MapRotor[]{new RotorI(), new RotorII(), new RotorIII()};

        for (int i = 0; i < 3; i++) {
            rotor[i].setBounds(26 + i * 86, 55, 90, 120);
            add(rotor[i]);
        }
        for (int i = 2; i > 0; i--) {
            rotor[i].setNext(rotor[i - 1]);
        }
    }

    public int transmitForward(int c) {
        for (int i = 2; i >= 0; i--) {
            int a = Math.floorMod(c + (rotor[i].getCharacter() - 'A'), 26);
            int m = mf[i].mapForward(a);
            int res = Math.abs(a - m);
            if(a < m)
                c = Math.floorMod(c + res, 26);
            else
                c = Math.floorMod(c - res, 26);
        }
        return c; //Deberás regresar el caracter que obtienes después de pasar por el rotor cero
    }

    public int transmitBackward(int c) {
        for (int i = 0; i < 3; i++) {
            int a = Math.floorMod(c + (rotor[i].getCharacter() - 'A'), 26);
            int m = mf[i].mapBackward(a);
            int res = Math.abs(a - m);
            if(a < m)
                c = Math.floorMod(c + res, 26);
            else
                c = Math.floorMod(c - res, 26);  
        }
        return c;//Deberás regresar el caracter que obtienes después de pasar por el rotor dos
    }

    public void increase() {
        rotor[2].down();
    }
}
