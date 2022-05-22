# Job Board

A job board is a class whos main function
is a map from pawns (IWorkers?) to jobs (IWorkables)

the intention is that a pawn or job does not know, itself
what its doing, but contacts the job board each frame
to get what its current job is.

when a IWorkable begins to have work, it may post itself
to the board, and when it is finished it will remove itself
from the board, by sending messages to the board.

IWorkers on the other hand, are able to request a job
from the board, and once they have it, will wimply ask
the board for its current job each from, to attempt to do
work. at some point, the board will simply start returning
null instead of the IWorker's current job. to is when the
worker knows it can move on to another action.

A Worker may also tell a job board, that it no longer
wishes to do work, and the job will be released.