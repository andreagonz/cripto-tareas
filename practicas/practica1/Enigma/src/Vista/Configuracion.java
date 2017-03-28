package Vista;

import Logica.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Configuracion extends JDialog implements ActionListener{
    
    VentanaEnigma p;
    Rotores rotores;
    PlugBoard plugboard;
    MapFunction reflector;
    
    public Configuracion(VentanaEnigma parent, String title, String message) {
        if (parent != null) {
            Dimension parentSize = parent.getSize(); 
            Point p = parent.getLocation(); 
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }
        p = parent;
        rotores = parent.getRotores();
        reflector = parent.getReflector();
        plugboard = parent.getPlugBoard();
        JPanel messagePane = new JPanel();
        messagePane.add(new JLabel(message));
        getContentPane().add(messagePane);
        JPanel buttonPane = new JPanel();
        JButton button = new JButton("Aceptar"); 
        buttonPane.add(button); 
        button.addActionListener(this);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //p.initComponents();
        p.setReflector(new ReflectorC());
        setVisible(false); 
        dispose(); 
    }
}
