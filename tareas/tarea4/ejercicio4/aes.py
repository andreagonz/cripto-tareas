import os
from Crypto.Cipher import AES

iv = os.urandom(16)

encryptor = AES.new(key, AES.MODE_CBC, IV=iv)
