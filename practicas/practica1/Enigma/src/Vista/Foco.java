package Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Foco extends LetraButton {

    public Foco(Character c) {
        super(c);
    }

    @Override
    public void draw(Graphics2D g) {
        if (!pressed) {
            g.setColor(Color.BLACK);
            g.fillOval(0, 0, 54, 54);
            g.setColor(new Color(51, 56, 81));
            g.fillOval(2, 2, 50, 50);
            g.setColor(Color.WHITE);
        } else {
            g.setColor(new Color(252, 255, 155));
            g.fillOval(0, 0, 54, 54);
            g.setColor(new Color(133, 139, 92));
            g.fillOval(2, 2, 50, 50);
            g.setColor(new Color(251, 255, 189));
        }
        g.setFont(new Font("TimesRoman", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(c.toString(), (56 - fm.charWidth(c)) / 2, 38);
    }
}
