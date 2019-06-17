# InfoSecCookerASSO

We will be developing InfoSecCooker as describied in 2nd ASSO case study using Java.

## Functionalities

### Sources
 - Open File
 - Fetch URL
 - HTTP Inbound

### Sinks
 - Send to File
 - Post via HTTP
 - WebSockets

### Handlers
 We will be implementing all the handlers that can challenge the software architecture of the project. There are multiple kinds of handlers (Conversion, Arithmetic/Logic, ...) and we will develop one of each kind because multiple of the same kind usually don't challenge the architecture. Although there are some exceptions that we will take into account, for example Sort and Head are both of the same kind (Collections) but one outputs just one element while the order outputs a collection of elements.

### Flow Control
 - Cycles - allows to buffer n inputs until an output is produced
 - Functions - allows to create more complex handlers based on more simple ones
 - Errors - allows to display on the GUI the error that ocurred and the respective task

## Architecture
We used a front-end and back-end architecture. We used Java for the back-end, implementing the entire logic and JavaScript for implementing the GUI. Both applications communicate using a network protocol: HTTP.

## Problems Found and Design Patterns used to solve them
We started developing this at the class and initially had support for multiple tasks but we assumed they started waiting for inputs ("pulling" data from sources) and computing immediately and performed only one cycle.
The architecture was just modeling the Graph nodes, and the way they communicated data was using the pull architecture, were each downstream (jusante) node called the function to compute and get the output of the upstream (montante) node it was connected to. The graph edges (pipes) were not represented and consisted of method calls. The mechanism was started by calling the most downstream (jusante) node and each node would call the method to get the output of each upstream node it was connected to, and so on.

The teacher said that the requirements were that each node should not only perform one cycle and we could have nodes continously executing, for example "A node that is generating random numbers 4 times per second" and that we should be modeling also the pipes (the graph edges) that transfered data between the nodes, i.e., using the architecture pipes and filters.
To solve this problem, we added a class for representing the pipes, responsible for handling the data transferring between the nodes. It was a wrapper class for the Java ArrayBlockingQueue. We used this class since it implements the required features: the pipe should have a method to write data to it that should block if the buffer is full and a method to read data that blocks until data is available on the pipe, allowing nodes to be blocked waiting for inputs, as long as they run on different threads.

responsible for running the node. This class runs on a Thread  placed each node on a thread 