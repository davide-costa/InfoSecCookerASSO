package InfoSecCooker.GraphNodes.SpecificNodeImplementations.Compression;

import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphNodes.BasicTaskGraphNode;
import InfoSecCooker.GraphNodes.GraphNodeInformation;
import InfoSecCooker.Outputs.InfoSecData;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class CompressionNodeTask extends BasicTaskGraphNode
{
    public CompressionNodeTask(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations, ArrayList<InfoSecData> inputs, InfoSecData output)
    {
        super(graphNodeInformation, sources, destinations);
    }
}
