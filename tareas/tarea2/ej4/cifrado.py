'''
Andrea Itzel González Vargas
Carlos Gerardo Acosta Hernández
'''
import sys
import os
from math import floor

'''
Clase que cifra y descifra mensajes con los
esquemas de cifrado cesar, afin, mezclado y vigenere
'''
class Cifrado:

    '''
    Constructor de la clase
    '''
    def __init__(self, clave, entrada):
        self.clave = clave
        self.entrada = entrada

    '''
    Función que cifra el mensaje de acuerdo con
    el esquema indicado
    '''
    def cifra(self, esquema):
        res = ""
        if esquema == "cesar":
            res = self.cifra_cesar()
        elif esquema == "afin":
            res = self.cifra_afin()
        elif esquema == "mezclado":
            res = self.cifra_mezclado()
        elif esquema == "vigenere":
            res = self.cifra_vigenere()
        else:
            print("Esquema de cifrado no reconocido")
            sys.exit(0)
        return res

    '''
    Función que descifra el mensaje de acuerdo con
    el esquema indicado
    '''
    def descifra(self, esquema):
        res = ""
        if esquema == "cesar":
            res = self.descifra_cesar()
        elif esquema == "afin":
            res = self.descifra_afin()
        elif esquema == "mezclado":
            res = self.descifra_mezclado()
        elif esquema == "vigenere":
            res = self.descifra_vigenere()
        else:
            print("Esquema de descifrado no reconocido")
            sys.exit(0)
        return res

    '''
    Funcion que cifra la entrada utilizando el esquema de cifrado
    de Cesar
    '''
    def cifra_cesar(self):
        out = ""
        for x in self.entrada:
            ni = ord(x) + int(self.clave)
            if ni > 255:
                out = out + chr(ni % 256)
            else:
                out = out + chr(ni)
        return out

    '''
    Funcion que descifra la entrada utilizando el esquema de cifrado
    de Cesar
    '''
    def descifra_cesar(self):
        out = ""
        for x in self.entrada:
            ni = ord(x) - int(self.clave)
            # Evitamos sacar el modulo innecesariamente
            if ni < 0:
                out = out + chr(ni % 256)
            else:
                out = out + chr(ni)
        return out

    '''
    Funcion que regresa el numero maximo comun divisor de dos numeros
    '''
    def mcd(self, a, b):
        if b == 0:
            return a
        else:
            return self.mcd(b, a % b)

    '''
    Funcion que regresa el inverso de un numero en el campo Z256
    '''
    def inverso(self, a):
        # Fuerza bruta
        for i in range(256):
            if (a * i) % 256 == 1:
                return i
        return 0

    '''
    Funcion que regresa la clave para el esquema de cifrado afin
    '''
    def clave_afin(self):
        # Se quitan caracteres innecesarios de la clave de entrada
        # y se divide en una lista con dos elementos
        lst = self.clave.replace(" ", "").replace("\n", "").split(',')
        # Si la longitud de la lista es menor de dos o os elementos
        # no son digitos es invalida
        if len(lst) < 2 or not lst[0].isdigit() or not lst[1].isdigit():
            print("Clave inválida")
            sys.exit(0)
        r = int(lst[0])
        k = int(lst[1])
        # Si el primer numero de la clave no es primo relativo con 256
        # es invalida
        if self.mcd(r, 256) != 1:
            print("El primer número de la clave debe ser primo relativo con 256")
            sys.exit(0)
        return (r, k)

    '''
    Funcion que cifra la entrada utilizando el esquema de cifrado
    Afin
    '''
    def cifra_afin(self):
        lst = self.clave_afin()
        r = lst[0]
        k = lst[1]
        res = ""
        for c in self.entrada:
            res += chr((r * ord(c) + k) % 256)
        return res

    '''
    Funcion que descifra la entrada utilizando el esquema de cifrado
    Afin
    '''
    def descifra_afin(self):
        lst = self.clave_afin()
        r = lst[0]
        k = lst[1]
        res = ""
        for c in self.entrada:
            res += chr(((ord(c) - k) * self.inverso(r)) % 256)
        return res

    '''
    Funcion que cifra la entrada utilizando el esquema de cifrado
    Mezclado
    '''
    def cifra_mezclado(self):
        dicc = {}
        res = ""
        lst = self.clave.split('\n')
        if len(lst) < 2 or len(lst[0]) != len(lst[1]):
            print("Clave inválida")
            sys.exit(0)
        # Se llena un diccionario con las correspondencias de cada letra
        for i in range(len(lst[0])):
            k = {lst[0][i] : lst[1][i]}
            if dicc.get(lst[0][i], None) != None:
                print("No repetir carácteres en la clave")
                sys.exit(0)
            dicc.update(k)
        for c in self.entrada:
            if dicc.get(c, None) != None:
                res += dicc.get(c)
            else:
                res += c
        return res

    '''
    Funcion que descifra la entrada utilizando el esquema de cifrado
    Mezclado
    '''
    def descifra_mezclado(self):
        dicc = {}
        res = ""
        lst = self.clave.split('\n')
        if len(lst) < 2 or len(lst[0]) != len(lst[1]):
            print("Clave inválida")
            sys.exit(0)
        for i in range(len(lst[0])):
            k = {lst[1][i] : lst[0][i]}
            if dicc.get(lst[1][i], None) != None:
                print("No repetir carácteres en la clave")
                sys.exit(0)
            dicc.update(k)
        for c in self.entrada:
            if dicc.get(c, None) != None:
                res += dicc.get(c)
            else:
                res += c
        return res

    '''
    Funcion que cifra la entrada utilizando el esquema de cifrado
    Vigenere
    '''
    def cifra_vigenere(self):
        out = ""
        m = len(self.clave) 
        pos = 0
        for x in self.entrada:
            if pos > m - 1:
                pos = 0
            ni = ord(x) + ord(self.clave[pos])
            # Por eficiencia evitamos el modulo lo mas que podamos
            if ni > 255:
                out = out + chr(ni % 256)
            else:
                out = out + chr(ni)
            pos += 1
        return out        

    '''
    Funcion que descifra la entrada utilizando el esquema de cifrado
    Afin
    '''
    def descifra_vigenere(self):
        out = ""
        m = len(self.clave) 
        pos = 0
        for x in self.entrada:
            if pos > m - 1:
                pos = 0
            ni = ord(x) - ord(self.clave[pos])
            if ni < 0:
                out = out + chr(ni % 256)
            else:
                out = out + chr(ni)
            pos += 1
        return out    

'''
Funcion que escribe la cadena recibida en un archivo con el nombre
recibido
'''
def escribe_archivo(nom, arch):
    try:
        na = open(nom, "w")
        na.write(arch)
        na.close()
    except:
        print("Error al crear archivo " + nom)

if len(sys.argv) < 5:
    print("Uso del programa:\npython3 cifrado.py [c|d] [cesar|afin|mezclado|vigenere] <archivoClave> <archivoEntrada>")
    
else:
    cd = sys.argv[1]
    esquema = sys.argv[2]
    clave = sys.argv[3]
    entrada = sys.argv[4]
    try:
        archk = open(clave, "r")
        clave = archk.read()
        archk.close()
        arche = open(entrada, "r")
        entrada = arche.read()
        arche.close()
    except:
        print("Archivo(s) inválido(s)")
    cifrado = Cifrado(clave, entrada)
    if cd == "c":
        res = cifrado.cifra(esquema)
        escribe_archivo(os.path.splitext(sys.argv[4])[0] + ".cifrado", res)
    elif cd == "d":
        res = cifrado.descifra(esquema)
        escribe_archivo(os.path.splitext(sys.argv[4])[0] + ".descifrado", res)
    else:
        print("Argumento '" + cd + "' no reconocido")
