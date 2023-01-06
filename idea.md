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

# Convert ITileThing to class

Think about these words and what they mean

Tile
TileThing
Entity
Item

# Mouse up / down bubbling

the events should still go through all layers,
and be allowed to be handled by any layer, if
that layer syas its handled.

problem: unknown operating order of right click
opening / closing the build panel. and its state
is controlled from 2 places.

the build layer can cancel and cause the panel
to close, but also the tab should be able to hook
into global mouse events for re-opening the panel.

opening has to be done from the build tab, because
it is the thing that activates the build layer.

consequence is that without proper care a single
event could immediately open & close the panel in
the same frame. so, the event should be able to
be captured and cease propogation.


# ready()
call order goes from top to bottom \/\/\/
ready is called before scene linkage, and
serves to initialize values that may be
needed before incoming requests.

# connect()
connect is solely for ensuring links to
other objects. get() and getAll()

# create()
create is guaranteed to only run once for
an object, even after save/load

# start()
start is called any time the object is added
to a scene