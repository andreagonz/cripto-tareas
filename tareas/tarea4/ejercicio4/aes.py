import os,sys
from Crypto.Cipher import AES
#from operator import xor

iv = os.urandom(16)

def xor(a, b):    
    bout = bytes(0)
    for i in range(0,16):
        bout += bytes([a[i] ^ b[i]])
    return bout

def padding(entrada):
    print(len(entrada))
    dif = 16-(len(entrada)%16)
    if dif > 0:        
        entrada += bytearray([1])
        for x in range(1,dif):
            entrada += bytearray([0])            
    else:
        entrada += bytearray([1])
        for x in range(1,16):
            entrada += bytearray([0])
    print(len(entrada))
    return entrada

def unpadding(salida):
    while(salida[len(salida)-1] == 0):
        salida = salida[:len(salida)-1]
    salida = salida[:len(salida)-1]
    return salida

class Cifrado:
    '''
    Constructor de la clase
    '''
    def __init__(self, clave, entrada):
        self.clave = clave
        self.entrada = entrada        
        self.encryptor = AES.new(clave, AES.MODE_ECB, IV=iv)

    '''
    Función que cifra el mensaje de acuerdo con
    el esquema indicado
    '''
    def cifra(self, esquema):
        if esquema == "cbc":
            return self.cifra_cbc()
        if esquema == "ofb":
            return self.cifra_ofb()
        else:
            print("Esquema de cifrado no reconocido")
            sys.exit(1)
    '''
    Función que cifra el mensaje de acuerdo con
    el esquema indicado
    '''
    def descifra(self, esquema):
        if esquema == "cbc":
            return self.descifra_cbc()
        if esquema == "ofb":
            return self.descifra_ofb()
        else:
            print("Esquema de cifrado no reconocido")
            sys.exit(1)

    def cifra_cbc(self):
        e = padding(self.entrada)
        out = bytearray(0)
        v_actual = iv[0:16]
        for x in range(0,int(len(e)/16)):
            idx = x*16
            v_actual = self.encryptor.encrypt(xor(v_actual,e[idx:idx+16]))
            out += v_actual
        print(iv)
        return iv+out

    def descifra_cbc(self):
        out = bytearray(0)
        v_actual = self.entrada[0:16]
        print(v_actual)
        self.entrada = self.entrada[16:]
        for x in range(0,int(len(self.entrada)/16)):
            idx = x*16                       
            out += xor(v_actual,self.encryptor.decrypt(self.entrada[idx:idx+16]))
            v_actual = self.entrada[idx:idx+16]
        return unpadding(out)

    def cifra_ofb(self):
        e = padding(self.entrada)
        out = bytearray(0)
        v_actual = iv[0:16]
        for x in range(0,int(len(e)/16)):
            idx = x*16
            v_actual = self.encryptor.encrypt(v_actual)
            out += xor(v_actual,e[idx:idx+16])
        print(iv)
        return iv+out
    
    def descifra_ofb(self):
        out = bytearray(0)
        v_actual = self.entrada[0:16]
        self.entrada = self.entrada[16:]
        print(v_actual)
        for x in range(0,int(len(self.entrada)/16)):
            idx = x*16
            v_actual = self.encryptor.encrypt(v_actual)
            out += xor(v_actual, self.entrada[idx:idx+16])
        return unpadding(out)
    

def escribe_archivo(nom, arch):
    try:
        na = open(nom, "wb")    
        na.write(arch)
        na.close()
    except Error as e:
        print(e)
        print("Error al crear archivo " + nom)
        
if len(sys.argv) != 5:
    print("Uso del programa:\npython3 aes.py [c|d] [cbc|ofb] <archivoEntrada> <archivoClave>")
    
else:
    cd = sys.argv[1]
    esquema = sys.argv[2]
    entrada = sys.argv[3]
    clave = sys.argv[4]
    try:
        archk = open(clave, "rb")
        clave = archk.read()
        archk.close()
        arche = open(entrada, "rb")
        entrada = arche.read()
        #print(len(entrada))
        arche.close()
    except:
        print("Archivo(s) inválido(s)")
    cifrado = Cifrado(clave, entrada)
    if cd == "c":
        res = cifrado.cifra(esquema)
        escribe_archivo("cifrado.aes", res)
    elif cd == "d":
        res = cifrado.descifra(esquema)
        escribe_archivo("descifrado.aes", res)
    else:
        print("Argumento '" + cd + "' no reconocido")

