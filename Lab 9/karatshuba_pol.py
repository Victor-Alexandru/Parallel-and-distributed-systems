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

A = [1, 2, 3, 4]
B = [1, 1, 1, 2]


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
    r2 = complete_pol(z1, len(z2) * 2)

    for i in range((len(B) + len(A) - 1)):
        result[i] = r1[i] + r2[i] if i < len(z1) else r2[i]

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

        print(z1)
        print(z2)
        print(z3)
        return last_karat_part(z1, z2, z3);


print(multiply(A, B, len(A), len(B)))
print(karatsuba_pol(A, B))
