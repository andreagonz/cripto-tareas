import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Base64;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

class NoHash {
    private String[] pass;
    private HashMap<String, Integer> pass_salt;
    private String[] pass_res;
    private String[] salt;
    private int numHilos;
    
    public NoHash(int hilos) {
        pass_salt = new HashMap<>();
        pass_res = new String[2000];
        pass = new String[14344391];
        salt = new String[2000];
        numHilos = hilos;
    }
    
    public void leePassSalt(String a) {
        try (BufferedReader br = new BufferedReader(new FileReader(a))) {
            String linea;
            int i = 0;
            while ((linea = br.readLine()) != null) {
                linea = linea.replace("\n", "");
                salt[i] = linea.substring(1, 11);
                pass_salt.put(linea.substring(12, linea.length()), i++);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void leePass(String a) {
        try (BufferedReader br = new BufferedReader(new FileReader(a))) {
            String linea;
            int x = 0;
            while ((linea = br.readLine()) != null) {
                linea = linea.replace("\n", "");
                pass[x] = linea;
                x++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void escribePass() {
        int j = 0;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("passwords-salt-RESPUESTA.txt"))) {
            for(int i = 0; i < pass_res.length; i++) {
                if(pass_res[i] != null) {
                    bw.write(pass_res[i] + "\n");
                    j++;
                }
            }
            System.out.println(j);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenContrasenas(String ps, String cp) {
        leePassSalt(ps);
        leePass(cp);
        
        Hilo[] hilos = new Hilo[numHilos];
        int elemPorHilo = pass.length / numHilos;
        for (int x = 0; x < numHilos; x++) {
            int from = x * elemPorHilo;
            int to = (x + 1) * elemPorHilo;
            if(x == numHilos - 1)
                hilos[x] = new Hilo(x, from, pass.length);
            else hilos[x] = new Hilo(x, from, to);
        }

        for (Hilo c : hilos)
            c.start();

        for (int x = 0; x < hilos.length; x++) {
            try {
                hilos[x].join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        escribePass();
    }

    class Hilo extends Thread {
    
        private final int id;
        private final int from;
        private final int to;
        private MessageDigest md;
        
        public Hilo(int id, int from, int to) {
            this.id = id;
            this.from = from;
            this.to = to;
            System.out.println("Hilo " + id + " creado. Con índices " + from + " a " + to);
            try {
                md = MessageDigest.getInstance("SHA-1");
            } catch(NoSuchAlgorithmException nsea) {
                nsea.printStackTrace();
            }
        }
        
        public String hash(String s) {
            md.reset();
            byte[] b = s.getBytes(StandardCharsets.UTF_8);
            md.update(b);
            byte[] digest = md.digest();
            return Base64.getEncoder().encodeToString(digest);
        }

        @Override
        public void run() {
            int encontrados = 0;
            for (int x = from; x < to; x++) {
                for(int j = 0; j < salt.length; j++) {
                    String h = hash(pass[x] + salt[j]);
                    Integer i = pass_salt.get(h);
                    if(i != null) {
                        pass_res[i] = pass[x];
                        encontrados++;
                        System.out.println("Encontrado #" + encontrados + " por hilo " + id + ". Contraseña: " + pass[x] + ", Id: " + i);
                    }
                }
                if(x % 100000 == 0)
                    System.out.println("Hilo " + id + " va en contraseña " + x);
            }
            System.out.println("Hilo " + id + " ha terminado");
        }   
    }
}
