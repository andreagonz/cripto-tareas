'''
Andrea Itzel González Vargas
Carlos Gerardo Acosta Hernández
'''

import sys
import os
from math import floor

class Cifrado:

    def __init__(self, clave, entrada):
        self.clave = clave
        self.entrada = entrada
        
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
                
    def cifra_cesar(self):
        out = ""
        for x in self.entrada:
            ni = ord(x) + int(self.clave)
            if ni > 255:
                out = out+chr(self.modulo(ni))
                #out = out+chr(ni%256)
            else:
                out = out+chr(ni)
        print("res: "+out)
        return out

    def descifra_cesar(self):
        out = ""
        for x in self.entrada:
            ni = ord(x) - int(self.clave)
            if ni < 0:
                out = out+chr(self.modulo(ni))
                #out = out+chr(ni%256)
            else:
                out = out+chr(ni)
        print("res: "+out)
        return out

    def mcd(self, a, b):
        if b == 0:
            return a
        else:
            return self.mcd(b, a % b)

    def inverso(self, a):
        for i in range(256):
            if (a * i) % 256 == 1:
                return i
        return 0
        
    def cifra_afin(self):
        lst = self.clave.replace(" ", "").replace("\n", "").split(',')
        if len(lst) < 2 or not lst[0].isdigit() or not lst[1].isdigit():
            print("Clave inválida")
            sys.exit(0)
        r = int(lst[0])
        k = int(lst[1])
        if self.mcd(r, 256) != 1:
            print("El primer número de la clave debe ser primo relativo con 256")
            sys.exit(0)
        res = ""
        for c in self.entrada:
            res += chr((r * ord(c) + k) % 256)
        return res

    def descifra_afin(self):
        lst = self.clave.replace(" ", "").replace("\n", "").split(',')
        if len(lst) < 2 or not lst[0].isdigit() or not lst[1].isdigit():
            print("Clave inválida")
            sys.exit(0)
        r = int(lst[0])
        k = int(lst[1])
        if self.mcd(r, 256) != 1:
            print("El primer número de la clave debe ser primo relativo con 256")
            sys.exit(0)
        res = ""
        for c in self.entrada:
            res += chr(((ord(c) - k) * self.inverso(r)) % 256)
        return res

    def cifra_mezclado(self):
        dicc = {}
        res = ""
        lst = self.clave.split('\n')
        if len(lst) < 2 or len(lst[0]) != len(lst[1]):
            print("Clave inválida")
            sys.exit(0)
        for i in range(len(lst[0])):
            k = {lst[0][i] : lst[1][i]}
            if dicc.get(lst[0][i], None) != None:
                print("No repetir carácteres en la clave")
                sys.exit(0)
            dicc.update(k)
        print(dicc)
        for c in self.entrada:
            if dicc.get(c, None) != None:
                res += dicc.get(c)
            else:
                res += c
        return res

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
        print(dicc)
        for c in self.entrada:
            if dicc.get(c, None) != None:
                res += dicc.get(c)
            else:
                res += c
        return res

    def cifra_vigenere(self):
        out = ""
        m = len(self.clave) 
        pos = 0
        for x in self.entrada:
            if pos > m-1:
                pos = 0
            ni = ord(x) + ord(self.clave[pos])
            if ni > 255:
                out = out+chr(self.modulo(ni))
                #out = out+chr(ni%256)
            else:
                out = out+chr(ni)
            pos += 1
        print("res: "+out)
        return out        

    def descifra_vigenere(self):
        out = ""
        m = len(self.clave) 
        pos = 0
        for x in self.entrada:
            if pos > m-1:
                pos = 0
            ni = ord(x) - ord(self.clave[pos])
            if ni < 0:
                out = out+chr(self.modulo(ni))
                #out = out+chr(ni%256)
            else:
                out = out+chr(ni)
            pos += 1
        print("res: "+out)
        return out      

    def modulo(self,a):
        a = (a >> 16) + (a & 0xFFFF); #sum base 2**16 digitos
        a = (a >>  8) + (a & 0xFF);   #sum base 2**8 digitos 
        if (a < 255):
            return a;
        if (a < (2 * 255)):
            return a - 255;
        return a - (2 * 255);
    

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
