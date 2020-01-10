from mpi4py import MPI

comm = MPI.COMM_WORLD
rank = comm.Get_rank()


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
        self._subscribers['a'] = []
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
        for i in range(3):
            if rank == i:
                continue
            comm.send(msg, dest=i)

    def subscriebe_to_two(self, var):
        self._subscribers[var].append(rank)
        msg = Message()
        msg.set_subscribe_message(SubscribeMessage(var, rank))
        self.sendAll(msg)

    def is_subscribed(self, variable, rank):
        return rank in self._subscribers[variable]

    def subscribe_to(self, variable, rank):
        self._subscribers[variable].append(rank)
