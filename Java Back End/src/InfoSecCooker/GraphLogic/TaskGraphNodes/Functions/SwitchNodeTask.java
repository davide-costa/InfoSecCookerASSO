package InfoSecCooker.GraphLogic.TaskGraphNodes.Functions;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;
import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphPacket;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.NullDataReceivedFromGraphNodeAsInput;

import java.util.HashMap;

/**
 * This node represents a switch.
 * This means that it connects to edges but everything works as if this node wasn't there when the switch is closed, i.e., whe the tick method is called.
 * When the tick is called, it closes the circuit, making the short for one clock cycle. This means that when the tick method is called, it will let one single burst of data (one instance of the object that represents data (class InfoSecGraphPacket)) flow through its source edge to the destination edge.
 * This can be viewed as a short circuit because just like some electric components, it acts as if it wasn't there, i.e., as if everything was wire. An electrical example of this is an uncharged capacitor, this node can be viewed as one.
 * In the InfoSecCooker this is used for building complex nodes, because a node is needed on the input and output of the complex to make the connection work between the complex node and the outside.
 * This node will not be run on a thread, but it will be a class instance... yes I know I'm creating a new class instance for a simple short circuit, but lets not sacrifice good abstraction over performance.
 */
public class SwitchNodeTask extends TaskGraphNode
{

    public SwitchNodeTask(GraphNodeInformation graphNodeInformation,
                          HashMap<Integer, PipeGraphEdge> sources,
                          HashMap<Integer, PipeGraphEdge> destinations,
                          RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, sources, destinations,runTimeConfigurations);
    }

    @Override
    public void tick() throws CollectionsException, NullDataReceivedFromGraphNodeAsInput, InterruptedException
    {
        InfoSecGraphPacket data = inputs.get(0).receiveData();
        outputs.get(0).sendData(data);
    }

    @Override
    public boolean areNumberOfInputsInACorrectState()
    {
        return inputs.size() == 1;
    }

    @Override
    public boolean areNumberOfOutputsInACorrectState()
    {
        return outputs.size() == 1;
    }
}
