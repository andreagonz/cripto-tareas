import math

n = 172205490419
phi = 172204660344
p_mas_q = n - phi + 1

r1 = (p_mas_q + (math.sqrt((p_mas_q ** 2) - 4 * n))) / 2
r2 = (p_mas_q - (math.sqrt((p_mas_q ** 2) - 4 * n))) / 2

print(r1)
print(r2)

print(r1 * r2)
