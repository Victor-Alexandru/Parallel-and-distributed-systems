import math
from dsm import *
from mpi4py import MPI
from threading import Thread
from time import sleep

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
print("HELLO WORLD this is my rank", rank)
p = comm.Get_size()


class SubscribeMessage:
    def __init__(self, var, rank):
        self.var = var
        self.rank = rank


class UpdateMessage:
    def __init__(self, var, value):
        self.var = var
        self.rank = value


class ChangeMessage:
    def __init__(self, var, new_value, old_value):
        self._var = var
        self._new_value = new_value
        self._old_value = old_value


class Message:
    def __init__(self, ):
        self._exit = False
        self._update_message = None
        self._change_message = None
        self._subscribe_message = None

    def get_update_message(self):
        return self._update_message

    def get_subscribe_message(self):
        return self._subscribe_message

    def set_exit(self, nV):
        self._exit = nV

    def set_subscribe_message(self, nV):
        self._subscribe_message = nV

    def set_change_message(self, nV):
        self._change_message = nV

    def set_update_message(self, nV):
        self._update_message = nV

    def must_exit(self):
        return self._exit


class DSM:
    def __init__(self):
        self._subscribers = {}
        self.a = 9
        self.b = 10
        self.c = 11
        self._subscribers['a'] = [1]
        self._subscribers['b'] = []
        self._subscribers['c'] = []

    def set_variable(self, variable_name, value):
        if variable_name == "a":
            self.a = value
        elif variable_name == "b":
            self.b = value
        elif variable_name == "c":
            self.c = value

    def sendAll(self, msg):
        for i in range(2):
            if i != rank:
                print("Rankul :", rank, "a trimis un mesaj spre ", i)
                if msg.get_subscribe_message():
                    comm.send(["subscribe", msg.get_subscribe_message(
                    ).rank, msg.get_subscribe_message().var], dest=i)
                print("Mesaj trimis")

    def subscriebe_to_two(self, var):
        self._subscribers[var].append(rank)
        msg = Message()
        msg.set_subscribe_message(SubscribeMessage(var, rank))
        self.sendAll(msg,)

    def is_subscribed(self, variable, rank):
        return rank in self._subscribers[variable]

    def subscribe_to(self, variable, rank):
        self._subscribers[variable].append(rank)


def listen_thread(dsm):
    while True:
        print("Rank " + str(rank) + " waiting ")

        msg = comm.recv(source=0)

        print("am primit mesaj")


        if msg[0] == "subscribe":
            subsc_mesasge = SubscribeMessage(msg[2], msg[1])
            msg = Message()
            msg.set_subscribe_message(subsc_mesasge)
            

        if msg.must_exit():
            break

        if msg.get_update_message() is not None:
            print(
                "Rank " + str(
                    rank) + " received : " + msg.get_update_message().var + " -> " + msg.get_update_message().value)
            dsm.set_variable(msg.get_update_message().var,
                             msg.get_update_message().value)

        if msg.get_subscribe_message() is not None:
            print(
                "Rank " + str(
                    rank) + " received : " + str(msg.get_subscribe_message().rank) + "  sub to  " + str(msg.get_subscribe_message().var))
            # dsm.subscribe_to(msg.get_subscribe_message().var, msg.get_subscribe_message().rank)


def write_variables(dsm):
    print("Rank " + str(rank) + " a= " + dsm.a +
          " b= " + dsm.b + " c= " + dsm.c + " subs: ")
    for key, value in dsm._subscribers.items():
        rez_string = key + " : [ "
        for curr_rank in value:
            rez_string += curr_rank + " "
        rez_string += " ] "
        print(rez_string)


# GicaGica12323

def main_program(flag=True):
    dsm = DSM()
    if rank == 0:
        exit = False
        sleep(1)
        dsm.subscriebe_to_two("a")
        dsm.subscriebe_to_two("a")
        dsm.subscriebe_to_two("a")
        dsm.subscriebe_to_two("a")
        dsm.subscriebe_to_two("a")
        dsm.subscriebe_to_two("a")
        # dsm.subscriebe_to_two("b")
        # dsm.subscriebe_to_two("c")
    elif rank == 1:
        thread = Thread(target=listen_thread, args=(dsm,))
        thread.start()
    elif rank == 2:
        thread = Thread(target=listen_thread, args=(dsm,))
        thread.start()
        dsm.subscribe_to("b")


main_program()
print(" Programul cu rank ", rank, "  a terminat treaba")

# rulare : mpiexec -n 3 python main.py
