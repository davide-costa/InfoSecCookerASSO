package InfoSecCooker.GraphNodes.SpecificNodeImplementations.Compression;

import InfoSecCooker.Data.InfoSecPacket;
import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphNodes.BasicTaskGraphNode;
import InfoSecCooker.GraphNodes.GraphNodeInformation;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class CompressionNodeTask extends BasicTaskGraphNode
{
    public CompressionNodeTask(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations, ArrayList<InfoSecPacket> inputs, InfoSecPacket output)
    {
        super(graphNodeInformation, sources, destinations);
    }
}
