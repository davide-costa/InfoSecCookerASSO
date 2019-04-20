package InfoSecCooker.GraphNodes.SpecificNodeImplementations.Compression;

import InfoSecCooker.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphNodes.GraphNodeInformation;
import InfoSecCooker.Outputs.InfoSecData;

import java.util.ArrayList;
import java.util.HashMap;

public class Zip extends CompressionNodeTask
{
    public Zip(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations, ArrayList<InfoSecData> inputs, InfoSecData output)
    {
        super(graphNodeInformation, sources, destinations, inputs, output);
    }

    @Override
    public ArrayList<InfoSecData> computeOutput(ArrayList<InfoSecData> infoSecDataArrayList) throws CollectionsException
    {
        //TODO
        return null;
    }
}
