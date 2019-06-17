package InfoSecCooker.GraphNodes.SpecificNodeImplementations.Compression;

import InfoSecCooker.Data.InfoSecData;
import InfoSecCooker.Data.InfoSecPacket;
import InfoSecCooker.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphNodes.GraphNodeInformation;

import java.util.ArrayList;
import java.util.HashMap;

public class Zip extends CompressionNodeTask
{
    public Zip(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations, ArrayList<InfoSecPacket> inputs, InfoSecPacket output)
    {
        super(graphNodeInformation, sources, destinations, inputs, output);
    }

    @Override
    public ArrayList<InfoSecData> computeOutput(ArrayList<InfoSecData> infoSecPacketArrayList) throws CollectionsException
    {
        //TODO
        return null;
    }
}
