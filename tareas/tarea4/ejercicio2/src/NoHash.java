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
    private HashMap<String, Integer> pass_salt;
    private String[] pass;
    private MessageDigest md;
    private String[] salt;
    
    public NoHash() {
        pass_salt = new HashMap<>();
        pass = new String[2000];
        salt = new String[2000];
        try {
        md = MessageDigest.getInstance("SHA-1");
        } catch(NoSuchAlgorithmException nsea) {
            nsea.printStackTrace();
        }
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

    public String hash(String s) {
        md.reset();
        byte[] b = s.getBytes(StandardCharsets.UTF_8);
        md.update(b);
        byte[] digest = md.digest();        
        return Base64.getEncoder().encodeToString(digest);
    }
    
    public void leePass(String a) {
        try (BufferedReader br = new BufferedReader(new FileReader(a))) {
            String linea;
            int x = 0;
            int encontrados = 0;
            while ((linea = br.readLine()) != null && encontrados < 2000) {
                linea = linea.replace("\n", "");
                for(int j = 0; j < salt.length && encontrados < 2000; j++) {
                    String h = hash(linea + salt[j]);
                    Integer i = pass_salt.get(h);
                    if(i != null) {
                        pass[i] = linea;
                        encontrados++;
                        System.out.println("Encontrado: " + encontrados + " " + linea);
                    }
                }
                if(x % 100000 == 0) {
                    System.out.println(x);
                }
                x++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void escribePass() {
        int j = 0;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("passwords-salt-RESPUESTA.txt"))) {
            for(int i = 0; i < pass.length; i++) {
                if(pass[i] != null) {
                    bw.write(pass[i]);
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
        escribePass();
    }
        
}
