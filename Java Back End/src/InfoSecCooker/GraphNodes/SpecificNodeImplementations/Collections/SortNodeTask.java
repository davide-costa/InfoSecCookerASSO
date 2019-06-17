package InfoSecCooker.GraphNodes.SpecificNodeImplementations.Collections;

import InfoSecCooker.Data.InfoSecData;
import InfoSecCooker.Data.InfoSecPacket;
import InfoSecCooker.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphNodes.GraphNodeInformation;
import InfoSecCooker.Data.CollectionData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SortNodeTask extends CollectionsTaskGraphNode
{
    public SortNodeTask(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations, ArrayList<InfoSecPacket> inputs, InfoSecPacket output)
    {
        super(graphNodeInformation, sources, destinations, inputs, output);
    }

    @Override
    public ArrayList<InfoSecData> computeOutput(ArrayList<InfoSecData> infoSecDataArrayList) throws CollectionsException
    {
        InfoSecData infoSecPacket = infoSecDataArrayList.get(0);
        if (!(infoSecPacket instanceof CollectionData))
            throw new CollectionsException("infoSecPacket is not of type CollectionData");

        CollectionData collectionData = (CollectionData) infoSecPacket;
        List collection = collectionData.getCollection();
        Collections.sort(collection);
        ArrayList<InfoSecData> data = new ArrayList<>();
        data.add(new CollectionData(collection));

        return data;
    }
}
