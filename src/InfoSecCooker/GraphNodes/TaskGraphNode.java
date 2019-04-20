package InfoSecCooker.GraphNodes;

import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.RuntimeExceptions.ExpectedEdgeOnNodeInputButNotFound;
import InfoSecCooker.RuntimeExceptions.InfoSecCookerRuntimeException;
import InfoSecCooker.RuntimeExceptions.NullDataReceivedFromGraphNodeAsInput;

import java.util.HashMap;

public abstract class TaskGraphNode
{
    /**
     * The state is IDLING the default state, when the node has just been created.
     * The task remains in state IDLING when the tick method is not executing.
     * When it is waiting for input (waiting for the read function to return, aka "preso no read"), the state is WAITING_FOR_INPUTS.
     * When it has received all the necessary inputs and is performing the computation implemented by the specific task, the state is COMPUTING.
     * When it is writing the data to the outputs (it may block because of the size of the buffer and if the other end (downstream (jusante) end) is a slow receiver), the state is OUTPUTING.
     */
    enum TaskGraphNodeState {IDLING, WAITING_FOR_INPUTS, COMPUTING, OUTPUTING}

    GraphNodeInformation graphNodeInformation;

    protected HashMap<Integer, PipeGraphEdge> sources;
    protected HashMap<Integer, PipeGraphEdge> destinations; //i dunno if there can be multiple destinations, but this works for 1 or many
    TaskGraphNodeState state;

    public TaskGraphNode(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations)
    {
        state = TaskGraphNodeState.IDLING;
        this.graphNodeInformation = graphNodeInformation;
        this.sources = sources;
        this.destinations = destinations;
    }

    public HashMap<Integer, PipeGraphEdge> getSources()
    {
        return sources;
    }

    public HashMap<Integer, PipeGraphEdge> getDestinations()
    {
        return destinations;
    }

    public GraphNodeInformation getGraphNodeInformation()
    {
        return graphNodeInformation;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskGraphNode that = (TaskGraphNode) o;
        return this.getGraphNodeInformation().equals(that.getGraphNodeInformation().getId());
    }

    public abstract void tick() throws CollectionsException, NullDataReceivedFromGraphNodeAsInput, InterruptedException, ExpectedEdgeOnNodeInputButNotFound, InfoSecCookerRuntimeException;
}
