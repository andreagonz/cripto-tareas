import sys
from binascii import unhexlify

class Bytes:

    def __init__(self, arch1, arch2):
        try:
            A = open(arch1, "rb")
            self.bytesA = bytearray(A.read())
            A.close()
            B = open(arch2, "rb")
            self.bytesB = bytearray(B.read())
            B.close()
        except:
            print("Archivo(s) inválido(s)")
        self.pol = int("100011011", 2)

    def beetlejuice(self, arch, dif):
        if dif != 0:
            relleno = bytearray("beetlejuice", "utf-8")
            i = 1
            while i < dif / len(relleno):
                i += 1
                arch += relleno
            arch += relleno[0:dif%len(relleno)]
        return arch

    def xor_aux(self, bA, bB):
        res = bytearray()
        for i in range(len(bA)):
            if bA[i] != bB[i]:
                res.append(bA[i] ^ bB[i])
            else:
                res.append(bA[i])
        return res
        
    def xor(self):
        res = None
        if len(self.bytesA) > len(self.bytesB):
            beetB = self.beetlejuice(self.bytesB, len(self.bytesA) - len(self.bytesB))
            res = self.xor_aux(self.bytesA, beetB)
            #self.imprime(beetB, self.bytesA, res)
        else:
            beetA = self.beetlejuice(self.bytesA, len(self.bytesB) - len(self.bytesA))
            res = self.xor_aux(self.bytesB, beetA)
            #self.imprime(beetA, self.bytesB, res)
        self.escribe_archivo("xor.out", res)

    '''
    def imprime(self, a, b, res):
        for i in range(len(res)):
            print(bin(a[i])[2:].zfill(8) + "\n" + bin(b[i])[2:].zfill(8) + "\n"
                  + bin(res[i])[2:].zfill(8) + "\n")
    '''
    
    def escribe_archivo(self, nom, ba):
        try:
            na = open(nom, "wb")
            na.write(ba)
            na.close()
        except:
            print("Error al crear archivo " + nom)

    def get_grado(self, i):
        g = 0
        while i > 1:
            i >>= 1
            g += 1
        return g
    
    def redu(self, a):
        k = 0
        g = self.get_grado(a)
        while g >= 8:
            aux = 0
            if a & (1 << g):
                aux = 1 << g - 8
            aux = self.mult_aux(self.pol, aux)
            a ^= aux
            g = self.get_grado(a)
        return a
    
    def mult_aux(self, bmult, b):
        res = 0
        i = 1
        for x in range(self.get_grado(bmult) + 1):
            if bmult & i:
                res ^= b << x
            i <<= 1
        return res
    
    def mult(self):
        bmult = int("AA", 16)
        res = bytearray()
        for b in self.bytesA:
            res.append(self.redu(self.mult_aux(bmult, b)))
        self.escribe_archivo("multiplicacion.out", res)

if len(sys.argv) != 3:
    print("Uso del programa:\npython3 bytes.py archivo1 archivo2")
else:
    by = Bytes(sys.argv[1], sys.argv[2])
    by.xor()
    by.mult()
