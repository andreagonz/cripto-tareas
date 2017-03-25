package Vista;

import java.awt.Container;

public class Display extends Container {

    private final Foco[] foco;

    public Display() {
        super();
        foco = new Foco[27];
        for (char c = 'A'; c <= 'Z'; c++) {
            foco[c - 'A'] = new Foco(c);
        }
        for (int i = 0; i < 9; i++) {
            int aux = i * 66 + 60;
            foco[i].setBounds(aux, 0, 54, 54);
            foco[i + 17].setBounds(aux, 120, 54, 54);

            this.add(foco[i]);
            this.add(foco[i + 17]);
        }

        for (int i = 0; i < 8; i++) {
            foco[i + 9].setBounds(i * 66 + 100, 60, 54, 54);
            this.add(foco[i + 9]);
        }
    }

    public void keyPressed(Character c) {
        foco[c - 'A'].pressed();
    }

    public void keyReleased(Character c) {
        foco[c - 'A'].released();
    }
}
