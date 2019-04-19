package InfoSecCooker.GraphNodes.SpecificNodeImplementations.Files;

import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphNodes.BasicTaskGraphNode;
import InfoSecCooker.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.GraphNodes.GraphNodeInformation;
import InfoSecCooker.Outputs.InfoSecData;

import java.util.ArrayList;
import java.util.HashMap;

public class ToFile extends BasicTaskGraphNode
{
    String filename;

    public ToFile(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations, ArrayList<InfoSecData> inputs, InfoSecData output, String filename)
    {
        super(graphNodeInformation, sources, destinations);
        this.filename = filename;
    }

    @Override
    public InfoSecData computeOutput(ArrayList<InfoSecData> infoSecDataArrayList) throws CollectionsException
    {

        return null;
    }
}
