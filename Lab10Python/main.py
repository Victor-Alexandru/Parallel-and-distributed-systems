import math
from dsm import *
from mpi4py import MPI
from threading import Thread

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
print("HELLO WORLD this is my rank", rank)
p = comm.Get_size()


def listen_thread(dsm):
    while True:
        print("Rank " + str(rank) + " waiting ")
        msg = comm.recv(source=MPI.ANY_SOURCE, tag=MPI.ANY_TAG, )
        if msg.must_exit():
            break
        if msg.get_update_message() is not None:
            print(
                "Rank " + str(
                    rank) + " received : " + msg.get_update_message().var + " -> " + msg.get_update_message().value)
            dsm.set_variable(msg.get_update_message().var, msg.get_update_message().value)
        if msg.get_subscribe_message() is not None:
            print(
                "Rank " + str(
                    rank) + " received : " + msg.get_subscribe_message().rank + "  sub to  " + msg.get_subscribe_message().var)
            dsm.subscribe_to(msg.get_subscribe_message().var, msg.get_subscribe_message().rank)


def write_variables(dsm):
    print("Rank " + str(rank) + " a= " + dsm.a + " b= " + dsm.b + " c= " + dsm.c + " subs: ")
    for key, value in dsm._subscribers.items():
        rez_string = key + " : [ "
        for curr_rank in value:
            rez_string += curr_rank + " "
        rez_string += " ] "
        print(rez_string)


# GicaGica12323

def main_program():
    dsm = DSM()
    if rank == 0:
        thread = Thread(target=listen_thread, args=(dsm,))
        thread.start()
        thread.join()
        exit = False
        dsm.subscriebe_to_two("a")
        dsm.subscriebe_to_two("b")
        dsm.subscriebe_to_two("c")
    elif rank == 1:
        thread = Thread(target=listen_thread, args=(dsm,))
        thread.start()
        dsm.subscribe_to("a")
    elif rank == 2:
        thread = Thread(target=listen_thread, args=(dsm,))
        thread.start()
        dsm.subscribe_to("b")


main_program()

# rulare : mpiexec -n 3 python main.py
