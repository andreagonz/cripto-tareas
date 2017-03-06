import random
import math

def mcd(a, b):
        if b == 0:
                return a
        else:
            return mcd(b, a % b)

sum = 0
for x in range(1000):
        a = random.randrange(1000)
        b = random.randrange(1000)
        if 1 == mcd(a, b):
                sum += 1

res = sum / 1000
print("6/pi^2: " + str(6 / (math.pi * math.pi)))
print("Porcentaje de primos relativos: " + str(res))
print("Aproximacion de pi: " + str((res / 6) * math.pi))
