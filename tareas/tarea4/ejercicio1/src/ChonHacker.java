import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;
import java.util.Random;

class ChonHacker {

    private MessageDigest md;
    private byte[] mensaje;
    private int longitud;
    private Random r;
    private byte[] emoji;
    
    public ChonHacker(int seed) {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch(NoSuchAlgorithmException nsea) {
            nsea.printStackTrace();
        }
        r = new Random(seed);
        emoji = DatatypeConverter.parseHexBinary("F09F988F");
    }
    
    public byte[] hash() {
        md.update(mensaje);
        return md.digest();
    }

    public void mensajeAleatorio() {
        mensaje = new byte[r.nextInt(100000)];
        r.nextBytes(mensaje);
    }

    public boolean mensajeAceptado() {
        if(mensaje == null)
            return false;
        byte[] h = hash();
        for(int i = 0; i < emoji.length; i++)
            if(emoji[i] != h[i])
                return false;
        System.out.println("Mensaje: " + DatatypeConverter.printHexBinary(mensaje));
        System.out.println("Hash: " + DatatypeConverter.printHexBinary(h));
        return true;
    }
    
    public void buscarMensaje() {
        int x = 0;
        while(!mensajeAceptado()) {
            mensajeAleatorio();
            if(x % 10000 == 0)
                System.out.println("Iteración: " + x);
            x++;
        }
    }
}
