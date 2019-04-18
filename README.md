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
