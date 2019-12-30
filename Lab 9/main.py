import math
from mpi4py import MPI

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
print("HELLO WORLD this is my rank", rank)
p = comm.Get_size()


def karatsuba(x, y):
    if len(str(x)) == 1 or len(str(y)) == 1:
        return x * y
    else:
        m = max(len(str(x)), len(str(y)))
        m2 = m // 2

        a = x // 10 ** (m2)
        b = x % 10 ** (m2)
        c = y // 10 ** (m2)
        d = y % 10 ** (m2)

        z0 = karatsuba(b, d)
        z1 = karatsuba((a + b), (c + d))
        z2 = karatsuba(a, c)

        return (z2 * 10 ** (2 * m2)) + ((z1 - z2 - z0) * 10 ** (m2)) + (z0)


def karatsuba_mpi(x, y):
    if x < 10 and y < 10:
        return x * y

    n = max(len(str(x)), len(str(y)))
    m = int(math.ceil(float(n) / 2))

    # divide x into two half
    xh = int(math.floor(x / 10 ** m))
    xl = int(x % (10 ** m))

    # divide y into two half
    yh = int(math.floor(y / 10 ** m))
    yl = int(y % (10 ** m))

    # Karatsuba's algorithm.
    if rank == 1:
        print("1")
        f1 = karatsuba(xh, yh)
        comm.send(f1, dest=0)
    elif rank == 2:
        print("2")
        f2 = karatsuba(xl, yl)
        comm.send(f2, dest=0)
    elif rank == 3:
        print("3")
        f3 = karatsuba(xh + xl, yh + yl)
        comm.send(f3, dest=0)
    elif rank == 0:
        print("0")
        s1 = comm.recv(source=1)
        print('received: ', s1)
        s2 = comm.recv(source=2)
        print('received: ', s2)
        s3 = comm.recv(source=3)
        print('received: ', s3)
        s4 = s3 - s2 - s1
        print('computed s4 as:', s4)
        return int(s1 * (10 ** (m * 2)) + s4 * (10 ** m) + s2)




print('result: ', karatsuba_mpi(12, 12))