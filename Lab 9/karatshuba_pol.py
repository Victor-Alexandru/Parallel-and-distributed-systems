from mpi4py import MPI

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
p = comm.Get_size()

# Input: A : polynomial of degree at most n − 1 with n = 2k for k ∈ N
# Input: B : polynomial of degree at most n − 1
# Output: C : polynomial
# if n = 1 then return C ← AB
# C1 ← A(0)B(0) by a recursive call
# C2 ← A(1)B(1) by a recursive call
# C3 ← A(0) + A(1)
# C4 ← B(0) + B(1)
# C5 ← C3C4 by a recursive call
# C6 ← C5 − C1 − C2
# C ← C1 + C6Xn/2 + C2Xn
# return C

# public static Polinom KaratsubaSequentialForm(Polinom p1, Polinom p2) {
#         if (p1.getDegree() < 2 || p2.getDegree() < 2) {
#             return baseMultiplication(p1, p2);
#         }
#
#         int len = Math.max(p1.getDegree(), p2.getDegree()) / 2;
#         Polinom lowP1 = new Polinom(p1.getTerms().subList(0, len));
#         Polinom highP1 = new Polinom(p1.getTerms().subList(len, p1.getLength()));
#         Polinom lowP2 = new Polinom(p2.getTerms().subList(0, len));
#         Polinom highP2 = new Polinom(p2.getTerms().subList(len, p2.getLength()));
#
#         Polinom z1 = KaratsubaSequentialForm(lowP1, lowP2);
#         Polinom z2 = KaratsubaSequentialForm(add(lowP1, highP1), add(lowP2, highP2));
#         Polinom z3 = KaratsubaSequentialForm(highP1, highP2);
#
#         //calculate the final result with the algorithm
#         Polinom r1 = completePolynom(z3, 2 * len);
#         Polinom r2 = completePolynom(subtract(subtract(z2, z3), z1), len);
#         return add(add(r1, r2), z1);
#     }

A = [1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4]
B = [1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4]

low_first_A = A[:len(A) // 2]
high_first_A = A[len(A) // 2:]
low_first_B = B[:len(B) // 2]
high_first_B = B[len(B) // 2:]


def printPoly(poly, n,fr):
    for i in range(n):
        print(poly[i], end="")
        if (i != 0):
            print("x^", i, end="")
        if (i != n - 1):
            print(" + ", end="")

        # Driver Code


def multiply(A, B, m, n):
    prod = [0] * (m + n - 1)
    for i in range(m):
        for j in range(n):
            prod[i + j] += A[i] * B[j]
    return prod


def complete_pol(z1, offset):
    rez = []
    for i in range(offset):
        rez.append(0)
    for i in range(0, len(z1)):
        rez.append(z1[i])
    return rez


def last_karat_part(z1, z2, z3):
    result = [0] * (len(B) + len(A) - 1)
    r1 = complete_pol(z1, len(z1) * 2)
    r2 = complete_pol(z1 + z3, len(z2) * 2)

    for i in range((len(B) + len(A) - 1)):
        result[i] = r2[i] + r1[i]

    return result


def karatsuba_pol(A, B):
    if (len(A) < 3 and len(B) < 3):
        return multiply(A, B, len(A), len(B))
    else:
        low_first_A = A[:len(A) // 2]
        high_first_A = A[len(A) // 2:]
        low_first_B = B[:len(B) // 2]
        high_first_B = B[len(B) // 2:]
        z1 = karatsuba_pol(low_first_A, low_first_B)
        z2 = karatsuba_pol([low_first_A[i] + high_first_A[i] for i in range(len(low_first_A))],
                           [low_first_B[i] + high_first_B[i] for i in range(len(high_first_B))])
        z3 = karatsuba_pol(high_first_A, high_first_B)

        return [z2[i] - z2[i] + z3[i] for i in range(len(z1))]


if rank == 0:
    result_rank_one = comm.recv(source=1)
    result_rank_two = comm.recv(source=2)
    result_rank_three = comm.recv(source=3)

    final_rez = complete_pol(
        [result_rank_two[i] - result_rank_one[i] + result_rank_three[i] for i in range(len(result_rank_one))], len(A))
    # print(final_rez)
    printPoly(multiply(A, B, len(A), len(B)), len(A) + len(B) - 1, final_rez)

elif rank == 1:
    result = karatsuba_pol(low_first_A, low_first_B)
    comm.send(result, dest=0, )
elif rank == 2:
    result = karatsuba_pol([low_first_A[i] + high_first_A[i] for i in range(len(low_first_A))],
                           [low_first_B[i] + high_first_B[i] for i in range(len(high_first_B))])
    comm.send(result, dest=0, )
elif rank == 3:
    result = karatsuba_pol(high_first_A, high_first_B)
    comm.send(result, dest=0, )

# rulare : mpiexec -n 4 python karatshuba_pol.py
