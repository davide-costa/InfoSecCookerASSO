package InfoSecCooker.GraphNodes;

import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.RuntimeExceptions.ExpectedEdgeOnNodeInputButNotFound;
import InfoSecCooker.RuntimeExceptions.InfoSecCookerRuntimeException;
import InfoSecCooker.RuntimeExceptions.NullDataReceivedFromGraphNodeAsInput;
import InfoSecCooker.Outputs.InfoSecData;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TaskGraphNode
{
    GraphNodeInformation graphNodeInformation;

    HashMap<Integer, PipeGraphEdge> sources;
    HashMap<Integer, PipeGraphEdge> destinations; //i dunno if there can be multiple destinations, but this works for 1 or many

    ArrayList<InfoSecData> inputs;
    InfoSecData output;

    public TaskGraphNode(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations)
    {
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
