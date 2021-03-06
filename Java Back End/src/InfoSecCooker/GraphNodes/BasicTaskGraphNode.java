package InfoSecCooker.GraphNodes;

import InfoSecCooker.Data.InfoSecData;
import InfoSecCooker.Data.InfoSecPacket;
import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.RuntimeExceptions.ExpectedEdgeOnNodeInputButNotFound;
import InfoSecCooker.RuntimeExceptions.ExpectedEdgeOnNodeOutputButNotFound;
import InfoSecCooker.RuntimeExceptions.InfoSecCookerRuntimeException;
import InfoSecCooker.RuntimeExceptions.NullDataReceivedFromGraphNodeAsInput;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a basic graph node, i.e., a node that is one single node and not a composition of nodes.
 */
public abstract class BasicTaskGraphNode extends TaskGraphNode
{
    public BasicTaskGraphNode(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations)
    {
        super(graphNodeInformation, sources, destinations);
    }

    /**
     * This function performs all the magic regarding graph nodes (with the help of the aux functions it calls).
     * It waits for the data from the sources. The method receiveData of class PipeGraphEdge blocks if data is not ready yet. When all the receiveData's have returned, we are guaranteed to have all he data necessary from the sources.
     * Then, it computes the task. (Method is implemented by derived class because special behaviour is needed for each type of Task).
     * Then it outputs the data to all the destinations it should. The method sendData from class PipeGraphEdge is guaranteed to not let the destination be flooded if this node is a fast sender and the destination is a slow receiver because it blocks when the buffer is full.
     * This method is called for each "tick" or cycle of the node.
     * @throws CollectionsException
     * @throws NullDataReceivedFromGraphNodeAsInput
     * @throws InterruptedException
     */
    public void tick() throws InfoSecCookerRuntimeException, InterruptedException
    {
        state = TaskGraphNodeState.WAITING_FOR_INPUTS;
        ArrayList<InfoSecData> inputs = readDataFromSources();

        state = TaskGraphNodeState.COMPUTING;
        ArrayList<InfoSecData> outputs = computeOutput(inputs);


        state = TaskGraphNodeState.OUTPUTING;
        outputDataToDestinations(outputs);

        state = TaskGraphNodeState.IDLING;
        inputs.clear();
    }

    public abstract ArrayList<InfoSecData> computeOutput(ArrayList<InfoSecData> infoSecPacketArrayList) throws CollectionsException;
}
