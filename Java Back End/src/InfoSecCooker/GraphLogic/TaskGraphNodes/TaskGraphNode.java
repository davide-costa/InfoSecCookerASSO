package InfoSecCooker.GraphLogic.TaskGraphNodes;

import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphData;
import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphPacket;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ExpectedEdgeOnNodeInputButNotFound;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ExpectedEdgeOnNodeOutputButNotFound;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.InfoSecCookerRuntimeException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.NullDataReceivedFromGraphNodeAsInput;
import InfoSecCooker.GraphLogic.GraphWireShark.NetworkCardDescription;
import InfoSecCooker.GraphLogic.GraphWireShark.PacketRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class TaskGraphNode implements Comparable
{
    /**
     * The state is IDLING the default state, when the node has just been created.
     * The task remains in state IDLING when the tick method is not executing.
     * When it is waiting for input (waiting for the read function to return, aka "preso no read"), the state is WAITING_FOR_INPUTS.
     * When it has received all the necessary inputs and is performing the computation implemented by the specific task, the state is COMPUTING.
     * When it is writing the data to the outputs (it may block because of the size of the buffer and if the other end (downstream (jusante) end) is a slow receiver), the state is OUTPUTING.
     */
    public enum TaskGraphNodeState
    {IDLING, WAITING_FOR_INPUTS, COMPUTING, OUTPUTING}

    GraphNodeInformation graphNodeInformation;

    protected HashMap<Integer, PipeGraphEdge> inputs;
    protected HashMap<Integer, PipeGraphEdge> outputs;
    TaskGraphNodeState state;
    protected int bufferSize;

    boolean enablePacketCapture;
    PacketRegistry incomingPacketRegistry;
    PacketRegistry outgoingPacketRegistry;

    public TaskGraphNode(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> inputs, HashMap<Integer, PipeGraphEdge> outputs, boolean enablePacketCapture)
    {
        state = TaskGraphNodeState.IDLING;
        this.graphNodeInformation = graphNodeInformation;
        this.inputs = inputs;
        this.outputs = outputs;
        this.enablePacketCapture = enablePacketCapture;
        incomingPacketRegistry = new PacketRegistry();
        outgoingPacketRegistry = new PacketRegistry();
        bufferSize = 1;
    }

    public TaskGraphNode(GraphNodeInformation graphNodeInformation,
                         HashMap<Integer, PipeGraphEdge> inputs,
                         HashMap<Integer, PipeGraphEdge> outputs,
                         RunTimeConfigurations runTimeConfigurations)
    {
        this(graphNodeInformation, inputs, outputs, runTimeConfigurations.enablePacketCaptureOnTaskGraphNodes.get());
    }

    public TaskGraphNode(GraphNodeInformation graphNodeInformation, RunTimeConfigurations runTimeConfigurations)
    {
        this(graphNodeInformation, new HashMap<>(), new HashMap<>(), runTimeConfigurations);
    }

    public TaskGraphNodeState getState()
    {
        return state;
    }

    public HashMap<Integer, PipeGraphEdge> getInputs()
    {
        return inputs;
    }

    public HashMap<Integer, PipeGraphEdge> getOutputs()
    {
        return outputs;
    }

    public PacketRegistry getIncomingPacketRegistry()
    {
        return incomingPacketRegistry;
    }

    public PacketRegistry getOutgoingPacketRegistry()
    {
        return outgoingPacketRegistry;
    }

    public void addInput(int sourceNumber, PipeGraphEdge source)
    {
        inputs.put(sourceNumber, source);
    }

    public void addOutput(int destinationNumber, PipeGraphEdge destination)
    {
        outputs.put(destinationNumber, destination);
    }

    public void removeInput(int sourceNumber)
    {
        inputs.remove(sourceNumber);
    }

    public void removeOutput(int destinationNumber)
    {
        outputs.remove(destinationNumber);
    }


    public PipeGraphEdge getInputWithNumber(int number)
    {
        return inputs.get(number);
    }

    public PipeGraphEdge getOutputWithNumber(int number)
    {
        return outputs.get(number);
    }

    public GraphNodeInformation getGraphNodeInformation()
    {
        return graphNodeInformation;
    }

    public void setBufferSize(int bufferSize)
    {
        this.bufferSize = bufferSize;
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

    protected ArrayList<InfoSecGraphData> readDataFromSources() throws ExpectedEdgeOnNodeInputButNotFound, NullDataReceivedFromGraphNodeAsInput
    {
        System.out.println("readDataFromSources");
        ArrayList<InfoSecGraphData> inputs = new ArrayList<>();
        int sourcesSize = this.inputs.size();
        int i = 0;
        Iterator it = this.inputs.entrySet().iterator();
        while (it.hasNext() && i < sourcesSize)
        {
            System.out.println("while");
            Map.Entry pair = (Map.Entry) it.next();

            PipeGraphEdge source = (PipeGraphEdge) pair.getValue();
            if (source == null)
                throw new ExpectedEdgeOnNodeInputButNotFound("Expected Edge On Node Input But Not Found", getGraphNodeInformation().id, i);

            InfoSecGraphPacket packet = source.receiveData();
             if (packet == null)
                throw new NullDataReceivedFromGraphNodeAsInput("Null packet received from graph node", source.getSource().getGraphNodeInformation(), graphNodeInformation);

            if (enablePacketCapture)
            {
                NetworkCardDescription networkCardWhereWasCaptured = new NetworkCardDescription(NetworkCardDescription.Type.INPUT, i, graphNodeInformation);
                incomingPacketRegistry.registerPacketCaptured(packet, networkCardWhereWasCaptured);
            }

            inputs.add(packet.getInfoSecGraphData());
            i++;
        }

        return inputs;
    }

    protected void outputDataToDestinations(ArrayList<InfoSecGraphData> outputs) throws ExpectedEdgeOnNodeOutputButNotFound, InterruptedException
    {
        if (outputs == null)
            return;

        int i = 0;
        Iterator it = this.outputs.entrySet().iterator();
        while (it.hasNext() && i < outputs.size())
        {
            Map.Entry pair = (Map.Entry) it.next();

            PipeGraphEdge destination = (PipeGraphEdge) pair.getValue();
            if (destination == null)
                throw new ExpectedEdgeOnNodeOutputButNotFound("Expected Edge On Node Output But Not Found", getGraphNodeInformation().id, i);

            InfoSecGraphPacket infoSecGraphPacket = new InfoSecGraphPacket(outputs.get(i), graphNodeInformation, destination.getDestination().graphNodeInformation,
                    i, destination.getDestinationPort());

            if (enablePacketCapture)
            {
                NetworkCardDescription networkCardWhereWasCaptured = new NetworkCardDescription(NetworkCardDescription.Type.OUTPUT, i, graphNodeInformation);
                outgoingPacketRegistry.registerPacketCaptured(infoSecGraphPacket, networkCardWhereWasCaptured);
            }

            destination.sendData(infoSecGraphPacket);
            i++;
        }
    }

    /**
     * This method checks if the number of inputs are in a correct state.
     * The special implementation of each Task node should know how the number of inputs should be in a correct state.
     * There may also be multiple correct states.
     * @return A boolean value indicating weather the inputs are in a correct state.
     */
    public abstract boolean areNumberOfInputsInACorrectState();


    /**
     * This method checks if the number of outputs are in a correct state.
     * The special implementation of each Task node should know how the number of outputs should be in a correct state.
     * There may also be multiple correct states.
     * @return A boolean value indicating weather the outputs are in a correct state.
     */
    public abstract boolean areNumberOfOutputsInACorrectState();

    public boolean areNumberOfInputsAndNumberOfOutputsInACorrectState()
    {
        return areNumberOfInputsInACorrectState() && areNumberOfOutputsInACorrectState();
    }

    /**
     * This method checks if the kind of inputs (the data kind that the other end outputs) are in a correct state, i.e., if the data kind is compatible.
     * The special implementation of each Task node should know how the kind of inputs should be in a correct state.
     * There may also be multiple correct states.
     * @return A boolean value indicating weather the inputs are in a correct state.
     */
    //public abstract boolean areInputKindsInACorrectState();

    /**
     * This method checks if the kind of outputs (the data kind that the other end outputs) are in a correct state, i.e., if the data kind is compatible.
     * The special implementation of each Task node should know how the kind of outputs should be in a correct state.
     * There may also be multiple correct states.
     * @return A boolean value indicating weather the outputs are in a correct state.
     */
    //public abstract boolean areOutputKindsInACorrectState();

    /**
     * Removes references to this node on other nodes
     */
    public void destructor()
    {
        int sourceSize = inputs.size();
        for (int i = 0; i < sourceSize; i++)
        {
            PipeGraphEdge pipeGraphEdge = inputs.get(i);
            TaskGraphNode upstreamNode = pipeGraphEdge.getSource();
            int upstreamNodeOutputNumber = pipeGraphEdge.getSourcePort();
            upstreamNode.removeOutput(upstreamNodeOutputNumber);
        }

        int destinationsSize = outputs.size();
        for (int i = 0; i < destinationsSize; i++)
        {
            PipeGraphEdge pipeGraphEdge = outputs.get(i);
            TaskGraphNode downstreamNode = pipeGraphEdge.getDestination();
            int downstreamNodeOutputNumber = pipeGraphEdge.getDestinationPort();
            downstreamNode.removeInput(downstreamNodeOutputNumber);
        }
    }

    @Override
    public int compareTo(Object o)
    {
        Long thisId = this.graphNodeInformation.getId();
        Long oId = ((TaskGraphNode) o).graphNodeInformation.getId();

        return thisId.compareTo(oId);
    }

}
