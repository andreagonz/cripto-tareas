'''
Andrea Itzel González Vargas
Carlos Gerardo Acosta Hernández
'''
import sys

class Bytes:

    '''
    Constructor que abre los archivos A y B y los guarda en
    arreglos de bytes
    '''
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
        # Polinomio irreducible
        self.pol = int("100011011", 2)
        self.pirr_poli = bytearray()
        self.pirr_poli.append(1)
        self.pirr_poli.append(0)
        self.pirr_poli.append(0)
        self.pirr_poli.append(0)
        self.pirr_poli.append(1)

    '''
    Función que rellena el arreglo recibido con
    la cadena 'beetlejuice'
    '''
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

    '''
    Función auxiliar para la función xor que efectua
    la operación xor entre los bytes del archivo A y
    el archivo B y regresa el resultado
    '''
    def xor_aux(self, bA, bB):
        res = bytearray()
        for i in range(len(bA)):
            res.append(bA[i] ^ bB[i])
        return res

    '''
    Función que realiza un xor entre los bytes de A y B y
    escribe el resultado en un archivo nuevo
    '''
    def xor(self):
        res = None
        # Se escoge el archivo más pequeño para rellenarlo
        if len(self.bytesA) > len(self.bytesB):
            beetB = self.beetlejuice(self.bytesB, len(self.bytesA) - len(self.bytesB))
            res = self.xor_aux(self.bytesA, beetB)
        else:
            beetA = self.beetlejuice(self.bytesA, len(self.bytesB) - len(self.bytesA))
            res = self.xor_aux(self.bytesB, beetA)
        self.escribe_archivo("xor.out", res)

    '''
    Función que escribe el arreglo de bytes recibido en
    un nuevo archivo con el nombre recibido
    '''
    def escribe_archivo(self, nom, ba):
        try:
            na = open(nom, "wb")
            na.write(ba)
            na.close()
        except:
            print("Error al crear archivo " + nom)

    '''
    Función que regresa el grado de el polinomio recibido
    '''
    def get_grado(self, i):
        g = 0
        while i > 1:
            i >>= 1
            g += 1
        return g

    '''
    Función que reduce el polinomio recibido con respecto
    al polinomio irreducible
    '''
    def redu(self, a):
        k = 0
        g = self.get_grado(a)
        # Mientras el grado del residuo es mayor a 7
        while g >= 8:
            aux = 0
            # Se encuentra el polinomio  por el cual se multiplicará al divisor
            if a & (1 << g):
                aux = 1 << g - 8
            # Se multiplica al divisor (el polinomio irreducible) por el polinomio encontrado
            aux = self.mult_aux(self.pol, aux)
            # Se le resta el resultado de la multiplicación a el residuo (que es lo mismo que sumarlos)
            a ^= aux
            g = self.get_grado(a)
        return a

    '''
    Función que multiplica dos polinomios. Regresa un polinomio resultado
    sin reducir
    '''
    def mult_aux(self, bmult, b):
        res = 0
        i = 1
        for x in range(self.get_grado(bmult) + 1):
            if bmult & i:
                res ^= b << x
            i <<= 1
        return res

    '''
    Función que multiplica los bytes del archivo A con los del B y los reduce con
    respecto a el polinomio irreducible
    '''
    def mult(self):
        bmult = int("AA", 16)
        res = bytearray()
        # Se recorre el archivo A y se multiplica cada uno de sus bytes
        for b in self.bytesA:
            res.append(self.redu(self.mult_aux(bmult, b)))
        self.escribe_archivo("multiplicacion.out", res)

    '''
    Regresa el grado de un polinomio con coeficientes de polinomio
    '''
    def get_grado_poli(self, a):
        i = len(a) - 1
        while i > 0:
            if a[i] != 0:
                return i
            i -= 1
        return i

    '''
    Regresa la suma de dos polinomios con coeficientes de polinomio
    '''
    def suma_poli(self, a, b):
        res = bytearray()
        if len(a) > len(b):
            for i in range(len(a)):
                if i > len(b) - 1:
                    res.append(0 ^ a[i])
                else:
                    res.append(b[i] ^ a[i])
        else:
            for i in range(len(b)):
                if i > len(a) - 1:
                    res.append(0 ^ b[i])
                else:
                    res.append(b[i] ^ a[i])
        return res

    '''
    Regresa la reduccion de un polinomio con coeficientes de polinomio
    de acuerdo al polinomio irreducible
    '''
    def redu_poli(self, a):
        g = self.get_grado_poli(a)
        while g >= 4:
            b = bytearray(g - 4 + 1)
            b[len(b) - 1] = a[g]
            b = self.mult_poli_aux(self.pirr_poli, b)
            a = self.suma_poli(a,b)
            g = self.get_grado_poli(a)
        return a

    '''
    Regresa la multiplacion de dos polinomios con coeficientes de polinomio
    '''
    def mult_poli_aux(self, bmult, b):
        res = bytearray(len(bmult) + len(b) - 1)
        for i in range(len(bmult)):
            for j in range(len(b)):
                res[i + j] = res[i + j]  ^ (self.redu(self.mult_aux(bmult[i], b[j])))
        return res

    '''
    Regresa la reversa de un polinomio con coeficientes de polinomio,
    ya que para efectuar las otras operaciones se voltearon los polinomios
    '''
    def reversa(self, b):
        a = bytearray(4)
        a[0] = b[3]
        a[1] = b[2]
        a[2] = b[1]
        a[3] = b[0]
        return a

    '''
    Escribe en un archivo la multiplicacion de los polinomios de bloques de
    4 bytes con otro bloque de 4 bytes de todos los bytes del archivo A
    '''
    def mult_poli(self):
        for x in range(len(self.bytesA) % 4):
            self.bytesA.append(0)
        pbmult = bytearray()
        pbmult.append(2)
        pbmult.append(1)
        pbmult.append(1)
        pbmult.append(3)
        res = bytearray()
        i = 0
        while i < len(self.bytesA):
            b = bytearray()
            b.append(self.bytesA[1 + 3])
            b.append(self.bytesA[i + 2])
            b.append(self.bytesA[i + 1])
            b.append(self.bytesA[i])
            res += self.reversa(self.redu_poli(self.mult_poli_aux(pbmult, b)))
            i += 4
        self.escribe_archivo("multiplicacion_poli.out", res)        
        
if len(sys.argv) != 3:
    print("Uso del programa:\npython3 bytes.py archivo1 archivo2")
else:
    by = Bytes(sys.argv[1], sys.argv[2])
    
    '''
    Ejercicio 1 a)
    '''
    by.xor()
    
    '''
    Ejercicio 1 b)
    '''
    by.mult()
    
    '''
    Ejercicio 1 c)
    '''
    by.mult_poli()
