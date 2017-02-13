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
        if len(self.bytesA) > len(self.bytesB):
            beetB = self.beetlejuice(self.bytesB, len(self.bytesA) - len(self.bytesB))
            res = self.xor_aux(self.bytesA, beetB)
            self.escribe_archivo("xor.out", res)
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
        
    def mult(self):
        return None

if len(sys.argv) != 3:
    print("Uso del programa:\npython3 bytes.py archivo1 archivo2")
else:
    by = Bytes(sys.argv[1], sys.argv[2])
    by.xor()
    by.mult()
