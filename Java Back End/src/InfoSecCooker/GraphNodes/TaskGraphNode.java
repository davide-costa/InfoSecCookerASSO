package InfoSecCooker.GraphNodes;

import InfoSecCooker.Data.InfoSecData;
import InfoSecCooker.Data.InfoSecPacket;
import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.RuntimeExceptions.ExpectedEdgeOnNodeInputButNotFound;
import InfoSecCooker.RuntimeExceptions.ExpectedEdgeOnNodeOutputButNotFound;
import InfoSecCooker.RuntimeExceptions.InfoSecCookerRuntimeException;
import InfoSecCooker.RuntimeExceptions.NullDataReceivedFromGraphNodeAsInput;
import InfoSecCooker.WireShark.NetworkCardDescription;
import InfoSecCooker.WireShark.PacketRegistry;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TaskGraphNode
{
    /**
     * The state is IDLING the default state, when the node has just been created.
     * The task remains in state IDLING when the tick method is not executing.
     * When it is waiting for input (waiting for the read function to return, aka "preso no read"), the state is WAITING_FOR_INPUTS.
     * When it has received all the necessary inputs and is performing the computation implemented by the specific task, the state is COMPUTING.
     * When it is writing the data to the outputs (it may block because of the size of the buffer and if the other end (downstream (jusante) end) is a slow receiver), the state is OUTPUTING.
     */
    enum TaskGraphNodeState
    {IDLING, WAITING_FOR_INPUTS, COMPUTING, OUTPUTING}

    GraphNodeInformation graphNodeInformation;

    protected HashMap<Integer, PipeGraphEdge> sources;
    protected HashMap<Integer, PipeGraphEdge> destinations; //i dunno if there can be multiple destinations, but this works for 1 or many
    TaskGraphNodeState state;

    boolean enablePacketCapture;
    PacketRegistry incomingPacketRegistry;
    PacketRegistry outgoingPacketRegistry;

    public TaskGraphNode(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations, boolean enablePacketCapture)
    {
        state = TaskGraphNodeState.IDLING;
        this.graphNodeInformation = graphNodeInformation;
        this.sources = sources;
        this.destinations = destinations;
        this.enablePacketCapture = enablePacketCapture;
        incomingPacketRegistry = new PacketRegistry();
        outgoingPacketRegistry = new PacketRegistry();
    }

    public TaskGraphNode(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources, HashMap<Integer, PipeGraphEdge> destinations)
    {
        this(graphNodeInformation, sources, destinations, true);
    }

    public HashMap<Integer, PipeGraphEdge> getSources()
    {
        return sources;
    }

    public HashMap<Integer, PipeGraphEdge> getDestinations()
    {
        return destinations;
    }

    public GraphNodeInformation getGraphNodeInformation()
    {
        return graphNodeInformation;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskGraphNode that = (TaskGraphNode) o;
        return this.getGraphNodeInformation().equals(that.getGraphNodeInformation().getId());
    }

    public abstract void tick() throws CollectionsException, NullDataReceivedFromGraphNodeAsInput, InterruptedException, ExpectedEdgeOnNodeInputButNotFound, InfoSecCookerRuntimeException;

    protected ArrayList<InfoSecData> readDataFromSources() throws ExpectedEdgeOnNodeInputButNotFound, NullDataReceivedFromGraphNodeAsInput
    {
        ArrayList<InfoSecData> inputs = new ArrayList<>();
        int sourcesSize = sources.size();
        for (int i = 0; i < sourcesSize; i++)
        {
            PipeGraphEdge source = sources.get(i);
            if (source == null)
                throw new ExpectedEdgeOnNodeInputButNotFound("", getGraphNodeInformation().id, i);

            InfoSecPacket packet = source.receiveData();
            if (packet == null)
                throw new NullDataReceivedFromGraphNodeAsInput("Null packet received from graph node", source.getSource().getGraphNodeInformation(), graphNodeInformation);

            if (enablePacketCapture)
            {
                NetworkCardDescription networkCardWhereWasCaptured = new NetworkCardDescription(NetworkCardDescription.Type.INPUT, i, graphNodeInformation);
                incomingPacketRegistry.registerPacketCaptured(packet, networkCardWhereWasCaptured);
            }

            inputs.add(packet.getInfoSecData());
        }

        return inputs;
    }

    protected void outputDataToDestinations(ArrayList<InfoSecData> outputs) throws ExpectedEdgeOnNodeOutputButNotFound, InterruptedException
    {
        int destinationsSize = destinations.size();
        for (int i = 0; i < destinationsSize; i++)
        {
            PipeGraphEdge destination = destinations.get(i);
            if (destination == null)
                throw new ExpectedEdgeOnNodeOutputButNotFound("", getGraphNodeInformation().id, i);

            InfoSecPacket infoSecPacket = new InfoSecPacket(outputs.get(i), graphNodeInformation, destination.getDestination().graphNodeInformation,
                    i, destination.getDestinationPort());

            if (enablePacketCapture)
            {
                NetworkCardDescription networkCardWhereWasCaptured = new NetworkCardDescription(NetworkCardDescription.Type.OUTPUT, i, graphNodeInformation);
                outgoingPacketRegistry.registerPacketCaptured(infoSecPacket, networkCardWhereWasCaptured);
            }

            destination.sendData(infoSecPacket);
        }
    }
}
