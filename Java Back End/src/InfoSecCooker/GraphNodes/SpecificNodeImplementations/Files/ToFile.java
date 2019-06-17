package InfoSecCooker.GraphNodes.SpecificNodeImplementations.Files;

import InfoSecCooker.Data.InfoSecData;
import InfoSecCooker.Data.InfoSecPacket;
import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphNodes.BasicTaskGraphNode;
import InfoSecCooker.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.GraphNodes.GraphNodeInformation;

import java.util.ArrayList;
import java.util.HashMap;

public class ToFile extends BasicTaskGraphNode
{
    String filename;

    public ToFile(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations, ArrayList<InfoSecPacket> inputs, InfoSecPacket output, String filename)
    {
        super(graphNodeInformation, sources, destinations);
        this.filename = filename;
    }

    @Override
    public ArrayList<InfoSecData> computeOutput(ArrayList<InfoSecData> infoSecPacketArrayList) throws CollectionsException
    {

        return null;
    }
}
