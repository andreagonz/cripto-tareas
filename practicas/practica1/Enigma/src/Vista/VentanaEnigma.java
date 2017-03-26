package Vista;

import Logica.MapFunction;
import Logica.PlugBoard;
import Logica.ReflectorB;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class VentanaEnigma extends JFrame {

    public static final int width = 700;
    public static final int height = 800;
    private final Background background;
    private final Display display;
    private final KeyBoard keyBoard;
    private final Rotores rotores;
    private final PlugBoard plugBoard;
    private MapFunction reflector;

    public VentanaEnigma() {
        background = new Background();
        plugBoard = new PlugBoard();
        display = new Display();
        keyBoard = new KeyBoard();
        rotores = new Rotores();
        reflector = new ReflectorB();
        initComponents();
    }

    private void setMenuBar() {
        JMenuBar mb = new JMenuBar();
        setJMenuBar(mb);
        JMenu menuSettings = new JMenu("Ajustes");
        mb.add(menuSettings);
        JMenuItem itemConfigurar = new JMenuItem("Configurar");

        itemConfigurar.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_C, ActionEvent.ALT_MASK));
        itemConfigurar.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
//        mi1.addActionListener(this);
        menuSettings.add(itemConfigurar);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setMinimumSize(new Dimension(VentanaEnigma.width, VentanaEnigma.height));
        setMaximumSize(new Dimension(VentanaEnigma.width, VentanaEnigma.height));
        setPreferredSize(new Dimension(VentanaEnigma.width, VentanaEnigma.height));
        setResizable(false);
        setMenuBar();

        getContentPane().add(rotores);
        rotores.setBounds(0, 0, 270, 175);

        getContentPane().add(display);
        display.setBounds(0, 200, VentanaEnigma.width, 200);

        getContentPane().add(keyBoard);
        keyBoard.setBounds(0, 400, VentanaEnigma.width, 230);

        getContentPane().add(background);
        background.setBounds(0, 0, VentanaEnigma.width, VentanaEnigma.height);

        pack();
    }

    public Display getDisplay() {
        return display;
    }

    public PlugBoard getPlugBoard() {
        return plugBoard;
    }

    public Rotores getRotores() {
        return rotores;
    }

    public Character transmit(Character c) {
        int a = c - 'A';
        a = plugBoard.map(a);
        a = rotores.transmitForward(a);
        a = reflector.map(a);
        a = rotores.transmitBackward(a);
        return (char)(a + 'A');
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaEnigma.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaEnigma().setVisible(true);
            }
        });
    }

}
