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

## High-level Architecture
We used a front-end and back-end architecture. We used Java for the back-end, implementing the entire logic and JavaScript for implementing the GUI. Both applications communicate using a network protocol: HTTP.

## Architecture, Problems Found and Design Patterns used to solve them
We started developing this at the class and initially had support for multiple tasks but we assumed they started waiting for inputs ("pulling" data from sources) and computing immediately and performed only one cycle.
The architecture was just modeling the Graph nodes, and the way they communicated data was using the pull architecture, were each downstream (jusante) node called the function to compute and get the output of the upstream (montante) node it was connected to. The graph edges (pipes) were not represented and consisted of method calls. The mechanism was started by calling the most downstream (jusante) node and each node would call the method to get the output of each upstream node it was connected to, and so on.

The teacher said that the requirements were that each node should not only perform one cycle and we could have nodes continously executing, for example "A node that is generating random numbers 4 times per second" and that we should be modeling also the pipes (the graph edges) that transfered data between the nodes, i.e., using the architecture pipes and filters.
To solve this problem, we added a class for representing the pipes, responsible for handling the data transferring between the nodes. It was a wrapper class for the Java ArrayBlockingQueue. We used this class since it implements the required features: the pipe should have a method to write data to it that should block if the buffer is full and a method to read data that blocks until data is available on the pipe, allowing nodes to be blocked waiting for inputs, as long as they run on different threads.
To represent the data that a node receives we had a class called InfoSecGraphData that is derived to represent each particular type of data.
To enable the nodes to run on different threads, we created a new class responsible for running the node. This class runs on a Thread and so we are able to implement nodes with cycles based on time. For the above example, for the node that performs work 4 times per second, this class will call the tick method of the graph node 4 times per second.
The class that represents the Graph node keeps an internal state so that it is possible to know if it is IDLING, WAITING_FOR_INPUTS, COMPUTING, or OUTPUTING.
The class that runns the node also keeps an internal state IDLING, SLEEPING, WORKING, PAUSED, or STOPPED. This state is used to inform the front end and also to be able to pause, stop and resume the Task. This was implemented expecting to add support for the user to pause the Graph and resume it (keeping track of the time and waiting only the remaining time) or stop and reset the whole graph, being able to start it again later.
All the Threads running the nodes are observed by another class, using the Observer pattern. This allows us to notify the front end app of the errors that occurr on the Task nodes and their state.

All the above respects to the superclasses that implement the main logic.
Then we started thinking about implementing Function Nodes, i.e., a node that is composed by other nodes. To implement this we created a derived class of the task graph node. We implemented this in the form of extracting a function from the graph, given the list of nodes composing it. Since the logic is complicated because only the ids of the nodes that would compose the function would be known and we would need to perform graph analysis to indentify which nodes are the sources (the ones that connect to the outside of the function upstream direction) and which nodes are the sinks of the function (the ones that connect to the outside of the function downstream direction), we decided to put this logic into a separate class called FunctionExtractor. This uses the Builder pattern.

Then, we started thinking about implementing the graph building logic, consisting of methods that would be called to perform the actions to build the graph, such as:
 - adding nodes
 - adding edges: connecting node outputs to node inputs
 - removing nodes
 - removing edges
 - setting tick interval of a node

For this, we created a class called GraphBuilder, using the Builder Design Pattern. This class already created the Task nodes and the pipe edges, all connected to each other and ready to work, process and transfer information through the pipes.
This GraphBuilder contains a method to verify if all the nodes are in correct connected state and without errors and a method return the graph built.

When the user wants to start the graph, an instance of another class GraphRunner is created to implement this feature. It creates the Task running threads, with the appropriate tick intervals (tick interval default is 0, for a node that immediately starts waiting for inputs (pulling information), then computes and outputs the result).
This class contains methods to start the graph (starts all the Task nodes). After starting the Graph all the Task runners are started and the information starts flowing from in upstream to downstream direction. The GraphRunner has support to pause and later resume the graph execution, and keep the remaining time in terms of tick interval, i.e., for example if the tick interval is 500 ms and it has been a sleep for only 300 ms when this method is called, the next time it is resumed, it will sleep for 200 ms before beginning computation. It also has a method to stop and reset the execution of the graph which clears the remaining time of the tick intervals and the next time it is resumed, the count for the tick interval starts from 0. (It also has a method to resume the graph execution (which can only be called if it it paused or stopped).).

Then we shifted our focus to improve the logic of the classes that represent the graph nodes.
We thought it would be usefull to keep track of the data traveling through the graph and rapidly determine the context it was used for. For that, we encapsulated the data class (class InfoSecGraphData) into a class called InfoSecGraphPacket. The class that travells through the pipes is the InfoSecGraphPacket.
Remembering that each pipe (graph edge) connects one output of a node to another input of a node:
This was done by analogy with computer networks. The InfoSecGraphPacket contains the InfoSecGraphData inside, the information of the source node (equivalent to source ip address in computer networks), the information of the destination node (equivalent to destination ip address in computer networks), the output number (of the source graph node) it left from (equivalent to source port in computer networks), the output number (of the destination graph node) it arrived at (equivalent to destination port in computer networks). The InfoSecGraphPacket can be best seen as a UDP packet from computer networks that has inside the payload (data) that we want to transfer. That data in this case is represented by an instance of the class InfoSecGraphData.

The class that represents the task graph node, operates this InfoSecGraphPacket is the base class that represents the task graph node. This class has a tick method that reads one instance of the class InfoSecGraphPacket from each input, registers the packet in the packet registry (just like WireShark does in computer networks), gets the InfoSecGraphData that is encapsulated in that packet calls a method "computeOutput" that receives the data read from each input (instances of the class InfoSecGraphData, this method doesn't know the class InfoSecGraphPacket) and returns the result of the computation (instances of class InfoSecGraphData). Then, it encapsulates this result of the computation into InfoSecGraphPacket classes, setting the source node, destination node, source port and destination port.
The method "computeOutput" is abstract in the base class and will be implemented by each derived class, to implemente the specific features (namely, but not limited to, ReadFile, DNSLookup, WriteToSocket). This is using the Strategy Design Pattern.

To build this derived classes and since we need to highly isolate the logic from the GUI, we decided to create a class called TaskFactory that has a method called buildTaskGraphNodeWithName that receives a string representing the name of the Task. This is perfect for isolating the logic from the front-end, because the front end referrences the tasks by their names and the back-end builds them according to that. This method returns the task graph node corresponding to the name received as parameter. We quickly identified this as a factory method Design Pattern.


The back end 