from mpi4py import MPI

comm = MPI.COMM_WORLD
rank = comm.Get_rank()

if rank == 0:
    data = {'a': 7, 'b': 3.14}
    print("Process with rank ", rank, "will send", data)
    comm.send(data, dest=1, tag=11)
    comm.send(data, dest=2, tag=11)
    comm.send(data, dest=3, tag=11)
elif rank == 1:
    data = comm.recv(source=0, tag=11)
    print("Process with rank ", rank, "will recv", data)
elif rank == 2:
    data = comm.recv(source=0, tag=11)
    print("Process with rank ", rank, "will recv", data)
elif rank == 3:
    data = comm.recv(source=0, tag=11)
    print("Process with rank ", rank, "will recv", data)
