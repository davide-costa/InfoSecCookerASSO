package InfoSecCooker.GraphNodes.SpecificNodeImplementations.Collections;

import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphNodes.BasicTaskGraphNode;
import InfoSecCooker.GraphNodes.GraphNodeInformation;
import InfoSecCooker.Data.InfoSecData;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class CollectionsTaskGraphNode extends BasicTaskGraphNode
{
    public CollectionsTaskGraphNode(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations, ArrayList<InfoSecData> inputs, InfoSecData output)
    {
        super(graphNodeInformation, sources, destinations);
    }
}
