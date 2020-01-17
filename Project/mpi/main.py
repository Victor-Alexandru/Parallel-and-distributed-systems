import os

import math
from mpi4py import MPI
from model.image import ImagePrint

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
p = comm.Get_size()

print("HELLLO WORLD , my rank is : ", rank, "   -------")


def transform_to_gray_matrix(rgb_tuple_matrix):
    rez = []
    for tuple in rgb_tuple_matrix:
        R, G, B = tuple
        avg = int(R) * 0.299 + int(G) * 0.587 + int(B) * 0.144
        if avg > 255:
            avg = 255
        avg = math.ceil(avg)
        rez.extend([avg, avg, avg])
    return rez


def transform_to_white_matrix(rgb_tuple_matrix):
    rez = []
    for tuple in rgb_tuple_matrix:
        R, G, B = tuple
        avg = int(R) + int(G) + int(B) // 3
        avg = math.ceil(avg)
        if avg > 255:
            avg = 255
        avg = math.ceil(avg)
        rez.extend([avg, avg, avg])
    return rez


if rank == 0:
    scriptDir = os.path.dirname(__file__)
    impath = os.path.join(scriptDir, '../images/test.jpg')

    imgPr = ImagePrint(impath)
    raw_colors = imgPr.getRGB()
    split_factor = len(raw_colors) // 4
    for i in range(4):
        if i == 0:
            send_array = raw_colors[:split_factor]
        elif i == 3:
            send_array = raw_colors[i * split_factor:(i + 1) * split_factor]
        else:
            send_array = raw_colors[i * split_factor:(i + 1) * split_factor]
        comm.send(send_array, dest=i + 1)

    gray_first_part = comm.recv(source=1)
    gray_second_part = comm.recv(source=2)
    gray_third_part = comm.recv(source=3)
    gray_fourth_part = comm.recv(source=4)

    result_matrix = gray_first_part + gray_second_part + gray_third_part + gray_fourth_part

    imgPr.transform_save_gray(result_matrix)


elif rank == 1:
    first_part = comm.recv(source=0)
    comm.send(transform_to_gray_matrix(first_part), dest=0)

elif rank == 2:
    second_part = comm.recv(source=0)
    comm.send(transform_to_gray_matrix(second_part), dest=0)

elif rank == 3:
    third_part = comm.recv(source=0)
    comm.send(transform_to_gray_matrix(third_part), dest=0)

elif rank == 4:
    fourth_part = comm.recv(source=0)
    comm.send(transform_to_gray_matrix(fourth_part), dest=0)
# exec with mpiexec -n 5 python main.py
