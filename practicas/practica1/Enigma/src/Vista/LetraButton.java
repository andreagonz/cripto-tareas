package Vista;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public abstract class LetraButton extends JComponent {

    protected Character c;
    protected boolean pressed;

    public LetraButton(Character c) {
        this.c = c;
        pressed = false;
    }

    public void pressed() {
        pressed = true;
    }

    public void released() {
        pressed = false;
    }

    public Character getCharacter() {
        return c;
    }

    public abstract void draw(Graphics2D g);

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw((Graphics2D) g);
    }
}
