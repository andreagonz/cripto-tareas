package Vista;

import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class Rotor extends JComponent {

    private final JLabel displayLetra;
    private final Engrane engraneTop, engraneButtom;
    private Rotor next;
    private Character c;
    private final Character zero;

    public Rotor(Character zero) {
        super();
        this.zero = zero;
        c = zero;
        next = null;
        engraneTop = new Engrane(Engrane.Top);
        engraneButtom = new Engrane(Engrane.Buttom);
        engraneTop.setBounds(58, 0, 13, 60);
        engraneButtom.setBounds(58, 60, 13, 60);
        this.add(engraneTop);
        this.add(engraneButtom);

        displayLetra = new JLabel(zero.toString());
        displayLetra.setFont(new Font("TimesRoman", Font.BOLD, 20));
        displayLetra.setBounds(26, 55, 20, 20);

        this.add(displayLetra);
    }

    public void setNext(Rotor n) {
        this.next = n;
    }

    public void up() {
        c--;
        if (c < 'A') {
            c = 'Z';
        }
        displayLetra.setText(c.toString());
    }

    public void down() {
        c++;
        if (c > 'Z') {
            c = 'A';
        }
        if (c.equals(zero) && next != null) {
            next.down();
        }
        displayLetra.setText(c.toString());
    }

    public Character getCharacter() {
        return c;
    }
}
