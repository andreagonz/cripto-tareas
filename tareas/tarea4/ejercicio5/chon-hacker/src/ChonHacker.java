import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;
import java.util.Random;
import java.util.Arrays;


class ChonHacker {

    private byte[] emojiA;
    private byte[] emojiB;
    
    public ChonHacker(int numHilos) {
        emojiA = DatatypeConverter.parseHexBinary("F09F9884");
        emojiB = DatatypeConverter.parseHexBinary("F09F988F");
        for (int x = 0; x < numHilos; x++)
            new Hilo(x).start();
    }

    class Hilo extends Thread {
    
        private MessageDigest md;
        private byte[] mensaje;
        private int id;
        
        public Hilo(int id) {
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch(NoSuchAlgorithmException nsea) {
                nsea.printStackTrace();
            }
            this.id = id;
            System.out.println("Iniciado hilo " + id );
            mensaje = new byte[id * 50 + 4];
            for(int i = 0; i < emojiA.length; i++)
                mensaje[i] = emojiA[i];
        }

        public byte[] hash() {
            md.update(mensaje);
            return md.digest();
        }

        private int getBit(int idx) {
            int i = idx / 8;
            int b = 1 << (8 - 1);
            b >>= (idx % 8);
            b &= mensaje[i];
            if(b == 0)
                return 0;
            return 1;
        }
       
        void setBit(int idx, int bit){
            if(getBit(idx) == bit)
                return;
            int i = idx / 8;
            int m = 1 << (8 - 1);
            m >>= (idx % 8);
            if(getBit(idx) == 0)
                mensaje[i] |= m;
            else
                mensaje[i] ^= m;
        }

        public void agregaUno() {
            if(mensaje.length * 8 - 1 < 32) {
                mensaje = new byte[mensaje.length + 1];
                Arrays.fill(mensaje, (byte) 0);
                for(int j = 0; j < emojiA.length; j++)
                    mensaje[j] = emojiA[j];
            }
            else {
                for (int i = mensaje.length * 8 - 1; i >= 32; i--) {
                    if (getBit(i) == 0) {
                        setBit(i, 1);
                        return;
                    }
                    setBit(i, 0);
                    if (i == 32) {
                        mensaje = new byte[mensaje.length + 1];
                        Arrays.fill(mensaje, (byte) 0);
                        for(int j = 0; j < emojiA.length; j++)
                            mensaje[j] = emojiA[j];
                        setBit(32, 1);
                    }                
                }
            }
        }
        
        public boolean mensajeAceptado() {
            if(mensaje == null)
                return false;
            byte[] h = hash();
            for(int i = 0; i < emojiB.length; i++)
                if(emojiB[i] != h[i])
                    return false;
            System.out.println("Mensaje encontrado: " + DatatypeConverter.printHexBinary(mensaje));
            System.out.println("Hash: " + DatatypeConverter.printHexBinary(h));
            System.exit(0);
            return true;
        }
    
        @Override
        public void run() {
            int x = 0;
            while(!mensajeAceptado()) {
                agregaUno();
                if(x % 1000000 == 0)
                    System.out.println("Hilo " + id + ": Iteración " + x);
                x++;
            }
        }
    }
}
