import hashlib
import base64

m = hashlib.sha1()
m.update("hola")
print(base64.b64encode(m.digest()))
