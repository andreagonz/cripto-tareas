package Vista;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;

public class Tecla extends LetraButton {

    public Tecla(Character c) {
        super(c);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Tecla t = (Tecla) e.getSource();
                VentanaEnigma ve = (VentanaEnigma) SwingUtilities.getWindowAncestor(t);
                t.pressed();

                ve.getRotores().increase();
                ve.getDisplay().keyPressed(ve.transmit(t.getCharacter()));
                ve.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Tecla t = (Tecla) e.getSource();
                VentanaEnigma ve = (VentanaEnigma) SwingUtilities.getWindowAncestor(t);

                t.released();
                ve.getDisplay().keyReleased(ve.transmit(t.getCharacter()));
                ve.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    @Override
    public void draw(Graphics2D g) {
        if (!pressed) {
            g.setColor(new Color(173, 173, 181));
            g.fillOval(0, 0, 54, 54);
            g.setColor(new Color(83, 88, 108));
            g.fillOval(2, 2, 50, 50);
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.BOLD, 40));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(c.toString(), (56 - fm.charWidth(c)) / 2, 38);
        } else {
            g.setColor(Color.WHITE);
            g.fillOval(5, 5, 44, 44);
            g.setColor(Color.BLACK);
            g.fillOval(7, 7, 40, 40);
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.BOLD, 30));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(c.toString(), (56 - fm.charWidth(c)) / 2, 35);
        }
    }

}
