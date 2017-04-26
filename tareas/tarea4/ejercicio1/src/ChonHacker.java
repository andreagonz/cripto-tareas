import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

class ChonHacker {

    private MessageDigest md;
    private byte[] mensaje;
    private int longitud;
    
    public ChonHacker() {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch(NoSuchAlgorithmException nsea) {
            nsea.printStackTrace();
        }
        // mensaje = new ByteBuffer();
    }
    
    public String hash() {
        md.update(mensaje);
        return DatatypeConverter.printHexBinary(md.digest());
    }

    public void algo() {
        System.out.println(hash());
    }
    
}
