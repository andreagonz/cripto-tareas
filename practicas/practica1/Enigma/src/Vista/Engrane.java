package Vista;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class Engrane extends JComponent {

    public static final int Top = 0;
    public static final int Buttom = 1;

    private BufferedImage img;

    public Engrane(final int tipo) {
        super();
        String url = (tipo == Engrane.Top) ? "rotorTop.png" : "rotorBottom.png";

        try {
            img = ImageIO.read(new File(getClass().getResource(url).toURI()));
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(VentanaEnigma.class.getName()).log(Level.SEVERE, null, ex);
        }

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Rotor r = (Rotor) ((Engrane) e.getSource()).getParent();
                if (tipo == Engrane.Top) {
                    r.up();
                } else {
                    r.down();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
}
