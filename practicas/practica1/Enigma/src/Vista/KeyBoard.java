package Vista;

import java.awt.Container;

public class KeyBoard extends Container {

    private final Tecla[] tecla;

    public KeyBoard() {
        super();
        tecla = new Tecla[27];
        for (char c = 'A'; c <= 'Z'; c++) {
            tecla[c - 'A'] = new Tecla(c);
        }
        for (int i = 0; i < 9; i++) {
            int aux = i * 66 + 60;
            tecla[i].setBounds(aux, 0, 54, 54);
            tecla[i + 17].setBounds(aux, 150, 54, 54);

            this.add(tecla[i]);
            this.add(tecla[i + 17]);
        }

        for (int i = 0; i < 8; i++) {
            tecla[i + 9].setBounds(i * 66 + 100, 75, 54, 54);
            this.add(tecla[i + 9]);
        }
    }

}
