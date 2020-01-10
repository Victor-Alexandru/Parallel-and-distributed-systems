class SubscribeMessage:
    def __init__(self, var, rank):
        self._var = var
        self._rank = rank


class UpdateMessage:
    def __init__(self, var, value):
        self._var = var
        self._rank = value


class ChangeMessage:
    def __init__(self, var, new_value, old_value):
        self._var = var
        self._new_value = new_value
        self._old_value = old_value


class Message:
    def __init__(self, exit):
        self._exit = False
        self._update_message = None
        self._change_message = None
        self._subscribe_message = None

    def set_exit(self, nV):
        self._exit = nV

    def set_subscribe_message(self, nV):
        self._subscribe_message = nV

    def set_change_message(self, nV):
        self._change_message = nV

    def set_update_message(self, nV):
        self._update_message = nV


class DSM:
    def __init__(self):
        self._subscribers = {}
        self._a = 9
        self._b = 10
        self._c = 11
