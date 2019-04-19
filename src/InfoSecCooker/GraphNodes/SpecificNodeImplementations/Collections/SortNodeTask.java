package InfoSecCooker.GraphNodes.SpecificNodeImplementations.Collections;

import InfoSecCooker.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphNodes.GraphNodeInformation;
import InfoSecCooker.Outputs.CollectionData;
import InfoSecCooker.Outputs.InfoSecData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SortNodeTask extends CollectionsTaskGraphNode
{
    public SortNodeTask(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations, ArrayList<InfoSecData> inputs, InfoSecData output)
    {
        super(graphNodeInformation, sources, destinations, inputs, output);
    }

    @Override
    public InfoSecData computeOutput(ArrayList<InfoSecData> infoSecDataArrayList) throws CollectionsException
    {
        InfoSecData infoSecData = infoSecDataArrayList.get(0);
        if (!(infoSecData instanceof CollectionData))
            throw new CollectionsException("infoSecData is not of type CollectionData");

        CollectionData collectionData = (CollectionData) infoSecData;
        List collection = collectionData.getCollection();
        Collections.sort(collection);

        return new CollectionData(collection);
    }
}
