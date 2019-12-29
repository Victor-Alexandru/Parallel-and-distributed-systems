from mpi4py import MPI
from utils import *
from random import randrange

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
p = comm.Get_size()


# A[] represents coefficients of first polynomial
# B[] represents coefficients of second polynomial
# m and n are sizes of A[] and B[] respectively
def multiply(A, B, m, n, result, start, end):
    # Multiply two polynomials term by term
    # Take ever term of first polynomial
    for i in range(start, end):
        # Multiply the current term of first
        # polynomial with every term of
        # second polynomial.
        for j in range(n):
            result[i + j] += A[i] * B[j]
    return result


# A utility function to print a polynomial
def printPoly(poly, n):
    for i in range(n):
        print(poly[i], end="")
        if (i != 0):
            print("x^", i, end="")
        if (i != n - 1):
            print(" + ", end="")

        # Driver Code


# first polynomial with the degreee from 0 to 16
A = [124, 4, 32, 56, 546, 6, 3, 46, 34, 6, 43, 64, 34, 234, 32, 43, 2, 432, 4231, 124, 134, 352, 54, 46, 6, 5436, 43,
     64, 36, 43, 643, 5634, 6, 5, 4, 3, 2, 3, 5, 2, 2, 23]
# second polynomial 2x+1
B = [1, 2]

m = len(A)
n = len(B)

split_phase = m // 4

result = [0] * (n + m - 1)

print("Rank:", rank, "  in action -- ")


def expand(string):
    rez = []
    for x in string:
        rez.append(int(x))
    return rez


if rank == 0:
    result_rank_one = comm.recv(source=1)
    result_rank_two = comm.recv(source=2)
    result_rank_three = comm.recv(source=3)
    result_rank_four = comm.recv(source=4)

    final_result = [result_rank_one[i] + result_rank_two[i] + result_rank_three[i] + result_rank_four[i] for i in
                    range(len(result_rank_four))]
    printPoly(A, len(A))
    print("\n********")
    printPoly(B, len(B))
    print("\n********")
    printPoly(final_result, n + m - 1)
elif rank == 1:
    multiply(A, B, m, n, result, 0, split_phase)
    comm.send(result, dest=0, )
elif rank == 2:
    multiply(A, B, m, n, result, split_phase, 2 * split_phase)
    comm.send(result, dest=0, )
elif rank == 3:
    multiply(A, B, m, n, result, 2 * split_phase, 3 * split_phase)
    comm.send(result, dest=0, )
elif rank == 4:
    multiply(A, B, m, n, result, 3 * split_phase, len(A))
    comm.send(result, dest=0, )
