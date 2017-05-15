F = Zmod(0xffffffff00000001000000000000000000000000ffffffffffffffffffffffff)
E = EllipticCurve(F, [-3, 0x5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b])
G = E(0x6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296, 0x4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5)
Q = E(0xcfc746589e4a140785b3bf94c7269ad1b17ad259fbe717c276ae0b0e749833af, 0x9ee25d020b5be979be4f9367e271322ce8a1006aef0e41f611e7bb1930978ef8)

d = 1
res = G

while res != Q:
    res += G
    d += 1

print(str(E) + "\n")
print("d = " + str(d))
print("G = " + str(G))
print("Q = " + str(Q))
print("d*G = " + str(d*G))
