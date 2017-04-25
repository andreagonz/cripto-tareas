'''
Lee dos archivos de entrada y nos dice si su hash MD5 coincide.
'''
import hashlib
import sys

if len(sys.argv) < 3:
    print "Uso: python pruebamd5.py <archivo1> <archivo2>"
else :
    s1 = ""
    s2 = ""
    try:
        f1 = open(sys.argv[1], 'rb')
        f2 = open(sys.argv[2], 'rb')
        s1 = f1.read()
        s2 = f2.read()
        f1.close()
        f2.close()
    except:
        print "Error al leer archivos"
        sys.exit(0)

    m = hashlib.md5()
    m.update(s1)
    d1 = m.digest()
    print sys.argv[1] + ":"
    print d1
    m = hashlib.md5()
    m.update(s2)
    d2 = m.digest()
    print sys.argv[2] + ":"
    print d2
    print "Son iguales? " + str(d1 == d2)
