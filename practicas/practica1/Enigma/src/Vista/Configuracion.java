package Vista;

import Logica.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Configuracion extends JDialog implements ActionListener{
    
    VentanaEnigma p;
    JComboBox cbReflector;
    JComboBox cbRotorI;
    JComboBox cbRotorD;
    JComboBox cbRotorC;
    int reflectorIndice;
    int rotorIIndice;
    int rotorDIndice;
    int rotorCIndice;
    Character[] map;
    JTextField map1, map2, map3, map4, map5, to1, to2, to3, to4, to5;//mapeos
    Character rotorIC, rotorDC, rotorCC; //caracteres de inicio en los rotores       
    JTextField charIniI, charIniD, charIniC; //para captura de los chars de inicio
    
    public Configuracion(VentanaEnigma parent, String title, String message) {
        if (parent != null) {
            Dimension parentSize = parent.getSize(); 
            Point pp = parent.getLocation(); 
            setLocation(pp.x + parentSize.width / 4, pp.y + parentSize.height / 4);
        }
        p = parent;
        JPanel panel = new JPanel();
        initChars('Q','E','V');
        initCBReflector(panel);
        initCBRotorI(panel);
        initCBRotorC(panel);
        initCBRotorD(panel);
        initMaps();
        initPlugboardConf(panel);
        getContentPane().add(panel);
        
        JPanel buttonPane = new JPanel();
        JButton button = new JButton("Aceptar"); 
        buttonPane.add(button); 
        button.addActionListener(this);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        JButton botonCancelar = new JButton("Cancelar"); 
        buttonPane.add(botonCancelar); 
        botonCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                setVisible(false); 
                dispose(); 
            }
        });
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }
               
    private void initChars(char i, char c, char d){
        rotorIC = i;
        rotorCC = c;
        rotorDC = d;
        //más sucio todavía
        charIniC = new JTextField(""+c,1);
        charIniI = new JTextField(""+i,1);
        charIniD = new JTextField(""+d,1);
        
        charIniI.addKeyListener(delimiter(charIniI));
        charIniD.addKeyListener(delimiter(charIniD));
        charIniC.addKeyListener(delimiter(charIniC));                
    }
    
    private void initMaps(){
        map1 = new JTextField("A",1);
        map2 = new JTextField("B",1);
        map3 = new JTextField("C",1);
        map4 = new JTextField("D",1);
        map5 = new JTextField("E",1);
        
        map1.addKeyListener(delimiter(map1));
        map2.addKeyListener(delimiter(map2));
        map3.addKeyListener(delimiter(map3));
        map4.addKeyListener(delimiter(map4));
        map5.addKeyListener(delimiter(map5));        
        
        to1 = new JTextField("A",1);
        to2 = new JTextField("B",1);
        to3 = new JTextField("C",1);
        to4 = new JTextField("D",1);
        to5 = new JTextField("E",1);        
        
        to1.addKeyListener(delimiter(to1));
        to2.addKeyListener(delimiter(to2));
        to3.addKeyListener(delimiter(to3));
        to4.addKeyListener(delimiter(to4));
        to5.addKeyListener(delimiter(to5));        
        
    }
    
    //Algo sucio
    private KeyAdapter delimiter(final JTextField jtf){
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { 
                if (jtf.getText().length() >= 1)
                    e.consume(); 
            }};
    }
    
    /**
     * Nos dice si la entrada de los caracteres de inicio para 
     * los rotores es válida.    
     */
    private boolean validStartChars(){
        rotorIC = charIniI.getText().charAt(0);
        rotorDC = charIniD.getText().charAt(0);
        rotorCC = charIniC.getText().charAt(0);
        return (charAceptado(rotorIC) && charAceptado(rotorDC) && charAceptado(rotorCC));                
    }
    
    // Dice si c es un carácter válido según nuestro alfabeto
    private boolean charAceptado(char c) {
        return c >= 'A' && c <= 'Z';            
    }
    
    private boolean aceptadoA(char[] a){        
        for(int i = 0; i < a.length; i++)
            if(!charAceptado(a[i]))
                return false;
        return true;
    }
    
    // Nos regresa un arreglo de caracteres con las configuraciones de la entrada
    private void getNuevoPlugBoardMap(char[] s, char[] d) {
        map = new Character[26];
        for(int i = 0; i < 5; i++)
            if(s[i] != '\u0000' && d[i] != '\u0000'){
                map[s[i] - 'A'] = d[i];
                map[d[i] - 'A'] = s[i];
            }
        for(int i = 0; i < 26; i++)
            if(map[i] == null)
                map[i] = (char)(i + 'A');        
    }
    
    // Nos dice si el arreglo de caracteres es válido, i.e. si no se repite ningún carácter
    private boolean plugBoardMapFactible() {
        //this bs is for quitar bichitos :v
        for(int i = 0; i < 26; i++)            
            System.out.print(map[i]+",");                
        System.out.print('\n');
                
        int[] dicc = new int[26];
        for(int i = 0; i < 26; i++)
            if(dicc[map[i] - 'A'] > 0)
                return false;
            else
                dicc[map[i] - 'A' ]++;        
        return true;
    }
    
    private void initCBReflector(JPanel jp) {
        String[] labels = {"ReflectorB", "ReflectorC"};
        cbReflector = new JComboBox(labels);
        cbReflector.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                reflectorIndice = cbReflector.getSelectedIndex();
            }
        });
        jp.add(new JLabel("Reflector:"));
        jp.add(cbReflector);
    }
    
    private void initCBRotorI(JPanel jp) {
        String[] labels = {"Rotor I", "Rotor II", "Rotor III", "Rotor IV", "Rotor V"};
        cbRotorI = new JComboBox(labels);
        cbRotorI.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                rotorIIndice = cbRotorI.getSelectedIndex();
            }
        });
        jp.add(new JLabel("Rotor Izquierdo:"));
        jp.add(cbRotorI);    
        jp.add(charIniI);
    }       
    
    private void initCBRotorD(JPanel jp) {
        String[] labels = {"Rotor I", "Rotor II", "Rotor III", "Rotor IV", "Rotor V"};
        cbRotorD = new JComboBox(labels);
        cbRotorD.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                rotorDIndice = cbRotorD.getSelectedIndex();
            }
        });
        jp.add(new JLabel("Rotor Derecho:"));
        jp.add(cbRotorD);       
        jp.add(charIniD);
    }
    
    private void initCBRotorC(JPanel jp) {
        String[] labels = {"Rotor I", "Rotor II", "Rotor III", "Rotor IV", "Rotor V"};
        cbRotorC = new JComboBox(labels);
        cbRotorC.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                rotorCIndice = cbRotorC.getSelectedIndex();
            }
        });
        jp.add(new JLabel("Rotor Centro:"));
        jp.add(cbRotorC);       
        jp.add(charIniC);
    }
    
    private void initPlugboardConf(JPanel jp){
        
        jp.add(new JLabel("Configuración del plugboard\n"));
        
        jp.add(map1);
        jp.add(map2);
        jp.add(map3);
        jp.add(map4);
        jp.add(map5);
        
        jp.add(to1);       
        jp.add(to2);
        jp.add(to3);
        jp.add(to4);
        jp.add(to5);
    }       

    private boolean setMap(){            
        char[] s = new char[]{map1.getText().charAt(0),map2.getText().charAt(0),map3.getText().charAt(0),map4.getText().charAt(0),map5.getText().charAt(0)};
        char[] d = new char[]{to1.getText().charAt(0),to2.getText().charAt(0),to3.getText().charAt(0),to4.getText().charAt(0),to5.getText().charAt(0)};
        getNuevoPlugBoardMap(s,d);
        return aceptadoA(s) && aceptadoA(d);               
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {        
        if(!validStartChars()){
            JOptionPane.showMessageDialog(new Frame(), "Letras de inicio en rotores inválidas.");
            return;
        }
        if(!setMap()){
            JOptionPane.showMessageDialog(new Frame(), "Error en caracteres de plugboard.");
            return;
        }
        
        if(!plugBoardMapFactible()){
            JOptionPane.showMessageDialog(new Frame(), "No repetir letras para el plugboard.");
            return;
        }
                                    
        MapRotor[] mr = {new RotorI(), new RotorII(), new RotorIII(), new RotorIV(), new RotorV()};
        if(reflectorIndice == 0)
            p.setReflector(new ReflectorB());
        if(reflectorIndice == 1)
            p.setReflector(new ReflectorC());
        p.setRotores(new Rotores(mr[rotorIIndice], mr[rotorCIndice], mr[rotorDIndice], rotorIC, rotorCC, rotorDC));
        p.setPlugBoard(new PlugBoard(map));
                        
        p.initComponents();
        setVisible(false); 
        dispose(); 
    }
}
