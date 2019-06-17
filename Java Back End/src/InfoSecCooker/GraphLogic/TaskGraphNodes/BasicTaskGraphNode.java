package InfoSecCooker.GraphLogic.TaskGraphNodes;

import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphData;
import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphDataBufferedCollection;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.*;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a basic graph node, i.e., a node that is one single node and not a composition of nodes.
 */
public abstract class BasicTaskGraphNode extends TaskGraphNode
{
    ArrayList<ArrayList<InfoSecGraphData>> bufferOutputCollection;

    public BasicTaskGraphNode(GraphNodeInformation graphNodeInformation,
                              HashMap<Integer, PipeGraphEdge> sources,
                              HashMap<Integer, PipeGraphEdge> destinations,
                              RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, sources, destinations, runTimeConfigurations);
    }

    public BasicTaskGraphNode(GraphNodeInformation graphNodeInformation, RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
        bufferOutputCollection = new ArrayList<>(bufferSize);
    }

    @Override
    public void setBufferSize(int bufferSize)
    {
        this.bufferSize = bufferSize;
        bufferOutputCollection = new ArrayList<>(bufferSize);
    }

    /**
     * This function performs all the magic regarding graph nodes (with the help of the aux functions it calls).
     * It waits for the data from the inputs. The method receiveData of class PipeGraphEdge blocks if data is not ready yet. When all the receiveData's have returned, we are guaranteed to have all he data necessary from the inputs.
     * Then, it computes the task. (Method is implemented by derived class because special behaviour is needed for each type of Task).
     * Then it outputs the data to all the outputs it should. The method sendData from class PipeGraphEdge is guaranteed to not let the destination be flooded if this node is a fast sender and the destination is a slow receiver because it blocks when the buffer is full.
     * This method is called for each "tick" or cycle of the node.
     * @throws CollectionsException
     * @throws NullDataReceivedFromGraphNodeAsInput
     * @throws InterruptedException
     */
    public void tick() throws InfoSecCookerRuntimeException, InterruptedException
    {
        state = TaskGraphNodeState.WAITING_FOR_INPUTS;
        ArrayList<InfoSecGraphData> inputs = readDataFromSources();

        state = TaskGraphNodeState.COMPUTING;
        ArrayList<InfoSecGraphData> parsedInputs = parseInputs(inputs);
        ArrayList<InfoSecGraphData> outputs = computeOutput(parsedInputs);

        if (bufferSize == 1)
        {
            state = TaskGraphNodeState.OUTPUTING;
            outputDataToDestinations(outputs);
        }
        else
        {
            for (int i = 0; i < outputs.size(); i++)
            {
                bufferOutputCollection.get(i).add(outputs.get(i));
            }
            if (bufferOutputCollection.size() == bufferSize)
            {
                ArrayList<InfoSecGraphData> output = new ArrayList<>();
                for (ArrayList<InfoSecGraphData> data : bufferOutputCollection)
                {
                    InfoSecGraphDataBufferedCollection dataBufferedCollection = new InfoSecGraphDataBufferedCollection(data);
                    output.add(dataBufferedCollection);
                }
                state = TaskGraphNodeState.OUTPUTING;
                outputDataToDestinations(output);
                bufferOutputCollection.clear();
            }
        }


        state = TaskGraphNodeState.IDLING;
    }

    protected abstract ArrayList<InfoSecGraphData> computeOutput(ArrayList<InfoSecGraphData> infoSecPacketArrayList) throws
        ComputationException, InputParametersException;

    private ArrayList<InfoSecGraphData> parseInputs(ArrayList<InfoSecGraphData> infoSecPacketArrayList) throws
            InputParametersException, ParsingInputsException
    {
        ArrayList<InfoSecGraphData> normalInputsArray = new ArrayList<>();
        for(int i = 0; i < infoSecPacketArrayList.size(); i++)
        {
            InfoSecGraphData infoSecGraphData = infoSecPacketArrayList.get(i);
            InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection =
                    checkForPotentialBufferedCollectionParameterIfExistent(infoSecGraphData);
            if(infoSecGraphDataBufferedCollection == null)
            {
                normalInputsArray.add(infoSecGraphData);
                continue;
            }

            InfoSecGraphData normalInput = transformBufferedInputIntoNormalInput(infoSecGraphDataBufferedCollection, i + 1);
            normalInputsArray.add(normalInput);
        }

        return normalInputsArray;
    }

    protected abstract InfoSecGraphData transformBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection, int index) throws
            InputParametersException, ParsingInputsException;

    protected InfoSecGraphData getNthParameterIfExistent(ArrayList<InfoSecGraphData> infoSecPacketArrayList,
                                                           Class<?> classInstance, int nthValue) throws InputParametersException
    {
        InfoSecGraphData infoSecGraphData = infoSecPacketArrayList.get(nthValue);
        if (!(infoSecGraphData.getClass() == classInstance))
            throw new InputParametersException("InfoSecGraphData is not of type \"" + classInstance.getName() + "\"");
        return infoSecGraphData;
    }

    protected InfoSecGraphDataBufferedCollection checkForPotentialBufferedCollectionParameterIfExistent(
            InfoSecGraphData infoSecGraphData)
    {
        if (infoSecGraphData.getClass() == InfoSecGraphDataBufferedCollection.class)
            return (InfoSecGraphDataBufferedCollection) infoSecGraphData;

        return null;
    }
}
