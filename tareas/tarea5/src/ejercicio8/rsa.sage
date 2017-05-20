from Crypto.PublicKey import RSA

k = '''-----BEGIN PUBLIC KEY-----
MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzl5VggSXb/Jm2oqkPeRQwtpmGlLnJT
Nre4LKx3VUljtLzYWj4xoG+aHBouwJT7DyeibpasCH8Yderr4zIGTNUCAwEAAQ==
-----END PUBLIC KEY-----'''

k2 = '''-----BEGIN PUBLIC KEY-----
MF0wDQYJKoZIhvcNAQEBBQADTAAwSQJCAPsrpwx56OTlKtGAWn24bo5HUg3xYtnz
nTj1X/8Hq7pLYNIVE57Yxoyr3zTOOBJufgTNzdKS0Rc5Ti4zZUkCkQvpAgMBAAE=
-----END PUBLIC KEY-----'''

c = 0x41b4e1609390ff8fb5f225b010d1cc79253dcab1744d5f865daabad0e28d259141722382114d9a73106b4d429676dae60a1528a0eb3b73eab0e9d2165c72492f
pk = RSA.importKey(k)
pk2 = RSA.importKey(k2)
p = gcd(pk.n, pk2.n)
q = pk.n / p

print("n = p * q")
print("p = " + str(p))
print("q = " + str(q))

phi = (p - 1) * (q - 1)
aee = xgcd(pk.e, phi)
d = Integer(mod(aee[1], phi))
m = power_mod(c, d, pk.n)

print("c = " + str(c))
print("m = " + str(m))
print("hex(m) = " + str(hex(m)))
