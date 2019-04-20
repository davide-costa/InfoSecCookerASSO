package InfoSecCooker.GraphNodes.FlowControl;

import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphNodes.BasicTaskGraphNode;
import InfoSecCooker.GraphNodes.GraphNodeInformation;
import InfoSecCooker.Data.InfoSecData;
import InfoSecCooker.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.RuntimeExceptions.ExpectedEdgeOnNodeInputButNotFound;
import InfoSecCooker.RuntimeExceptions.InfoSecCookerRuntimeException;
import InfoSecCooker.RuntimeExceptions.NullDataReceivedFromGraphNodeAsInput;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements Cycle flow control.
 */
public class BufferingTaskGraphNode extends BasicTaskGraphNode
{
    int bufferSize;

    public BufferingTaskGraphNode(GraphNodeInformation graphNodeInformation,
                                  HashMap<Integer, PipeGraphEdge> sources,
                                  HashMap<Integer, PipeGraphEdge> destinations,
                                  int bufferSize)
    {
        super(graphNodeInformation, sources, destinations);
        this.bufferSize = bufferSize;
    }

    @Override
    public void tick() throws CollectionsException, NullDataReceivedFromGraphNodeAsInput, InterruptedException, ExpectedEdgeOnNodeInputButNotFound, InfoSecCookerRuntimeException
    {
        ArrayList<ArrayList<InfoSecData>> bufferOfOutputs = new ArrayList<>();
        for (int i = 0; i < bufferSize; i++)
        {
            ArrayList<InfoSecData> inputs = readDataFromSources();
            bufferOfOutputs.add(inputs);
            inputs.clear();
        }

        for (int i = 0; i < bufferSize; i++)
        {
            outputDataToDestinations(bufferOfOutputs.get(i));
        }
    }

    @Override
    public ArrayList<InfoSecData> computeOutput(ArrayList<InfoSecData> infoSecDataArrayList) throws CollectionsException
    {
        return null;
    }

}
