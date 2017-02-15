import sys

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

    def beetlejuice(self, a, dif):
        arch = bytearray()
        for b in a:
            arch.append(b)
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
            res.append(bA[i] ^ bB[i])
        return res
        
    def xor(self):
        res = None
        if len(self.bytesA) > len(self.bytesB):
            beetB = self.beetlejuice(self.bytesB, len(self.bytesA) - len(self.bytesB))
            res = self.xor_aux(self.bytesA, beetB)
        else:
            beetA = self.beetlejuice(self.bytesA, len(self.bytesB) - len(self.bytesA))
            res = self.xor_aux(self.bytesB, beetA)
        self.escribe_archivo("xor.out", res)
    
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
        i  = 0
        for b in self.bytesA:
            print(str(i) + " Byte origen: " + bin(self.bytesA[i]) + " Byte a muliplicar: " + bin(bmult))
            res.append(self.redu(self.mult_aux(bmult, b)))
            i += 1
        self.escribe_archivo("multiplicacion.out", res)

if len(sys.argv) != 3:
    print("Uso del programa:\npython3 bytes.py archivo1 archivo2")
else:
    by = Bytes(sys.argv[1], sys.argv[2])
    by.xor()
    by.mult()

try:
    A = open("xor.out", "rb")
    bytesA = bytearray(A.read())
    A.close()
    B = open("ejemplo1/xor.out", "rb")
    bytesB = bytearray(B.read())
    B.close()

    '''
    if len(bytesA) != len(bytesB):
        print("Longitud xors distinta: " + len(bytesA) + " " + len(bytesB))
        sys.exit(0)
    
    for i in range(len(bytesA)):
        if bytesA[i] != bytesB[i]:
            print("XOR error " + str(bytesA[i]) + " " + str(bytesB[i]))
    print("XOR correcto")
    '''
    
    C = open("ejemplo1/multiplicacion.out", "rb")
    bytesC = bytearray(C.read())
    C.close()
    D = open("multiplicacion.out", "rb")
    bytesD = bytearray(D.read())
    D.close()

    if len(bytesC) != len(bytesD):
        print("Longitud mult distinta: " + str(len(bytesC)) + " " + str(len(bytesD)))
        sys.exit(0)
    for i in range(len(bytesC)):
        if i == 658399:
            if bytesC[i] != bytesD[i]:
                print(str(i) + " Multiplicacion error: Miau: " + str(bin(bytesC[i])) + " Kihui: " + str(bin(bytesD[i])))
            else:
                print(str(i) + " Multiplicacion correcta Miau: " + str(bin(bytesC[i])) + " Kihui: " + str(bin(bytesD[i])))

except:
    print("Archivo(s) de prueba inválido(s)")
