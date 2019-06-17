package InfoSecCooker.GraphLogic.TaskGraphNodes.FlowControl;

import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphData;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ExpectedEdgeOnNodeInputButNotFound;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.InfoSecCookerRuntimeException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.NullDataReceivedFromGraphNodeAsInput;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;

import java.util.ArrayList;

/**
 * This class implements Cycle flow control.
 */
public class BufferingTaskGraphNode extends TaskGraphNode
{
    int bufferSize;

    public BufferingTaskGraphNode(GraphNodeInformation graphNodeInformation,
                                  int bufferSize,
                                  RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
        this.bufferSize = bufferSize;
    }

    @Override
    public void tick() throws CollectionsException, NullDataReceivedFromGraphNodeAsInput, InterruptedException, ExpectedEdgeOnNodeInputButNotFound, InfoSecCookerRuntimeException
    {
        ArrayList<ArrayList<InfoSecGraphData>> bufferOfOutputs = new ArrayList<>();
        for (int i = 0; i < bufferSize; i++)
        {
            ArrayList<InfoSecGraphData> inputs = readDataFromSources();
            bufferOfOutputs.add(inputs);
        }

        for (int i = 0; i < bufferSize; i++)
        {
            outputDataToDestinations(bufferOfOutputs.get(i));
        }
    }

    @Override
    public boolean areNumberOfInputsInACorrectState()
    {
        return true;
    }

    @Override
    public boolean areNumberOfOutputsInACorrectState()
    {
        return true;
    }

}
