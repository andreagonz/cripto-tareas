package Vista;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class Background extends JComponent {

    private BufferedImage img;

    public Background() {
        try {
            img = ImageIO.read(new File(getClass().getResource("fondo.png").toURI()));
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(VentanaEnigma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }

}
