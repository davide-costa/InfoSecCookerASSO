package InfoSecCooker.GraphLogic.GraphBuilding;

import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.*;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdgeWithJavaArrayBlockingQueueImplementation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GraphBuilder
{
    public TaskFactory taskFactory;

    public HashMap<Long, TaskGraphNode> fullyConnectedGraphNodes = new HashMap<>();
    //The recently added graph nodes, that don't have all their inputs and outputs all filled yet
    public HashMap<Long, TaskGraphNode> unconnectedGraphNodes = new HashMap<>();
    public HashMap<Long, Integer> numberOfCyclesOfNode = new HashMap<>();
    public HashMap<Long, Integer> nodeIdToTickIntervalMillis = new HashMap<>();

    public HashMap<Long, PipeGraphEdge> pipeGraphEdgeHashMap = new HashMap<>();

    //This hash map associates an edge id to the node connected to its upstream end
    public HashMap<Long, TaskGraphNode> pipeGraphEdgeIdToUpStreamNode = new HashMap<>();
    //This hash map associates an edge id to the input number of the upstream node it is connected to
    public HashMap<Long, Integer> pipeGraphEdgeIdToUpStreamOutputNumber = new HashMap<>();
    //This hash map associated an edge id to the node connected to its downstream end
    public HashMap<Long, TaskGraphNode> pipeGraphEdgeIdToDownStreamNode = new HashMap<>();
    //This hash map associates an edge id to the output number of the downstream node it is connected to
    public HashMap<Long, Integer> pipeGraphEdgeIdToDownStreamInputNumber = new HashMap<>();


    //The graph builders for the function nodes
    public HashMap<Long, FunctionNodeBuilder> functionNodeIdToFunctionNodeBuilder = new HashMap<>();

    RunTimeConfigurations runTimeConfigurations;

    public GraphBuilder(TaskFactory taskFactory, RunTimeConfigurations runTimeConfigurations)
    {
        this.taskFactory = taskFactory;
        this.runTimeConfigurations = runTimeConfigurations;
    }

    public boolean areAllNodesFullyConnected()
    {
        return unconnectedGraphNodes.size() == 0;
    }

    /**
     * Adds a new Task to the graph.
     *
     * @param taskName The name of the Task to be added. The name must be registered in the factory of Tasks.
     * @return The id to reference (on future calls) the Task that was just created.
     * @throws GraphBuildingException
     */
    public long addNewTaskNode(String taskName) throws GraphBuildingException
    {
        TaskGraphNode taskGraphNode = taskFactory.buildTaskGraphNodeWithName(taskName);
        if (taskGraphNode == null)
            throw new AttemptToCreateUnknownNode();

        return saveInformationOfCreatedTaskNode(taskGraphNode);
    }

    private long saveInformationOfCreatedTaskNode(TaskGraphNode taskGraphNode)
    {
        long id = taskGraphNode.getGraphNodeInformation().getId();
        unconnectedGraphNodes.put(id, taskGraphNode);
        numberOfCyclesOfNode.put(id, 1); //default 1

        return id;
    }

    /**
     * Adds a new Task source to the graph.
     *
     * @param taskName The name of the Task to be added. The name must be registered in the factory of Tasks.
     * @param additionalInfo Additional info for sources, like a filename, and url, ...
     * @return The id to reference (on future calls) the Task that was just created.
     * @throws GraphBuildingException
     */
    public long addNewSourceTaskNode(String taskName, String additionalInfo) throws AttemptToCreateUnknownNode
    {
        TaskGraphNode taskGraphNode = taskFactory.buildTaskSourceGraphNodeWithName(taskName, additionalInfo);
        if (taskGraphNode == null)
            throw new AttemptToCreateUnknownNode();

        return saveInformationOfCreatedTaskNode(taskGraphNode);
    }

    /**
     * Adds a new Task sink to the graph.
     *
     * @param taskName The name of the Task to be added. The name must be registered in the factory of Tasks.
     * @param additionalInfo Additional info for sink nodes, like a filename, and url, ...
     * @return The id to reference (on future calls) the Task that was just created.
     * @throws GraphBuildingException
     */
    public long addNewSinkTaskNode(String taskName, String additionalInfo) throws AttemptToCreateUnknownNode
    {
        TaskGraphNode taskGraphNode = taskFactory.buildTaskSinkGraphNodeWithName(taskName, additionalInfo);
        if (taskGraphNode == null)
            throw new AttemptToCreateUnknownNode();

        return saveInformationOfCreatedTaskNode(taskGraphNode);
    }


    /**
     * Adds a new edge between two tasks to the graph.
     *
     * @param node1Id      The id of the first task node (upstream end of the edge)
     * @param node2Id      The id of the second task node (downstream end of the edge)
     * @param outputNumber The number of the output of the first node (connected to the upstream end of the edge)
     * @param inputNumber  The number of the input of the second node (connected to the upstream end of the edge)
     * @return The id to reference (on future calls) the edge that was just created.
     * @throws GraphBuildingException
     */
    public long addEdgeFromNode1ToNode2OutputToInput(long node1Id, long node2Id,
                                                     int outputNumber, int inputNumber) throws GraphBuildingException
    {
        TaskGraphNode graphNode1 = fullyConnectedGraphNodes.get(node1Id);
        TaskGraphNode graphNode2 = fullyConnectedGraphNodes.get(node2Id);
        if (graphNode1 == null)
        {
            graphNode1 = unconnectedGraphNodes.get(node1Id);
            if (graphNode1 == null)
                throw new ReferenceToUnExistantNodeInTheGraph(node1Id);
        }

        if (graphNode2 == null)
        {
            graphNode2 = unconnectedGraphNodes.get(node2Id);
            if (graphNode2 == null)
                throw new ReferenceToUnExistantNodeInTheGraph(node2Id);
        }

        PipeGraphEdge destinationEdgeAlreadyExistant = graphNode1.getOutputWithNumber(outputNumber);
        if (destinationEdgeAlreadyExistant != null)
            throw new AttemptingToConnectOutputAlreadyConnected(node1Id, outputNumber);

        PipeGraphEdge sourceEdgeAlreadyExistant = graphNode2.getInputWithNumber(inputNumber);
        if (sourceEdgeAlreadyExistant != null)
            throw new AttemptingToConnectInputAlreadyConnected(node2Id, inputNumber);


        PipeGraphEdge graphEdge = new PipeGraphEdgeWithJavaArrayBlockingQueueImplementation(runTimeConfigurations);

        //if all error checks have passed, connect the nodes to the edge and the edge to the nodes
        graphNode1.addOutput(outputNumber, graphEdge);
        graphNode2.addInput(inputNumber, graphEdge);
        graphEdge.setSource(graphNode1);
        graphEdge.setDestination(graphNode2);
        graphEdge.setSourcePort(outputNumber);
        graphEdge.setDestinationPort(inputNumber);

        addNodeToFullyConnectedNodesGraphIfSo(node1Id, graphNode1);
        addNodeToFullyConnectedNodesGraphIfSo(node2Id, graphNode2);

        long edgeId = graphEdge.getId();
        pipeGraphEdgeHashMap.put(edgeId, graphEdge);
        pipeGraphEdgeIdToUpStreamNode.put(edgeId, graphNode1);
        pipeGraphEdgeIdToUpStreamOutputNumber.put(edgeId, outputNumber);
        pipeGraphEdgeIdToDownStreamNode.put(edgeId, graphNode2);
        pipeGraphEdgeIdToDownStreamInputNumber.put(edgeId, inputNumber);

        return edgeId;
    }

    private void addNodeToFullyConnectedNodesGraphIfSo(long node1Id, TaskGraphNode graphNode)
    {
        if (graphNode.areNumberOfInputsAndNumberOfOutputsInACorrectState())
        {
            unconnectedGraphNodes.remove(node1Id);
            fullyConnectedGraphNodes.put(node1Id, graphNode);
        }
    }

    public void removeTaskNode(long taskNodeId) throws ReferenceToUnExistantNodeInTheGraph
    {
        TaskGraphNode taskGraphNode = fullyConnectedGraphNodes.get(taskNodeId);

        if (taskGraphNode == null)
        {
            taskGraphNode = unconnectedGraphNodes.get(taskNodeId);
            if (taskGraphNode == null)
                throw new ReferenceToUnExistantNodeInTheGraph(taskNodeId);
            else
                unconnectedGraphNodes.remove(taskNodeId);
        } else
            fullyConnectedGraphNodes.remove(taskNodeId);

        HashMap<Integer, PipeGraphEdge> inputs = taskGraphNode.getInputs();
        HashMap<Integer, PipeGraphEdge> outputs = taskGraphNode.getOutputs();

        //Remove edges that are connected to the node being removed from the graph
        removeInputEdgesThatAreConnectedToTheNodeBeingRemoved(inputs);
        removeOutputEdgesThatAreConnectedToTheNodeBeingRemoved(outputs);

        taskGraphNode.destructor();
    }

    private void removeInputEdgesThatAreConnectedToTheNodeBeingRemoved(HashMap<Integer, PipeGraphEdge> inputs)
    {
        Iterator currInput = inputs.entrySet().iterator();
        while (currInput.hasNext())
        {
            Map.Entry pair = (Map.Entry) currInput.next();
            PipeGraphEdge currEdge = (PipeGraphEdge) pair.getValue();
            Long currEdgeId = currEdge.getId();
            pipeGraphEdgeHashMap.remove(currEdgeId);
            pipeGraphEdgeIdToUpStreamNode.remove(currEdgeId);
            pipeGraphEdgeIdToUpStreamOutputNumber.remove(currEdgeId);
            pipeGraphEdgeIdToDownStreamNode.remove(currEdgeId);
            pipeGraphEdgeIdToDownStreamInputNumber.remove(currEdgeId);
        }
    }

    private void removeOutputEdgesThatAreConnectedToTheNodeBeingRemoved(HashMap<Integer, PipeGraphEdge> outputs)
    {
        Iterator currInput;
        currInput = outputs.entrySet().iterator();
        while (currInput.hasNext())
        {
            Map.Entry pair = (Map.Entry) currInput.next();
            PipeGraphEdge currEdge = (PipeGraphEdge) pair.getValue();
            Long currEdgeId = currEdge.getId();
            pipeGraphEdgeHashMap.remove(currEdgeId);
            pipeGraphEdgeIdToUpStreamNode.remove(currEdgeId);
            pipeGraphEdgeIdToUpStreamOutputNumber.remove(currEdgeId);
            pipeGraphEdgeIdToDownStreamNode.remove(currEdgeId);
            pipeGraphEdgeIdToDownStreamInputNumber.remove(currEdgeId);
        }
    }

    public void removeEdge(long edgeId) throws GraphBuildingException
    {
        PipeGraphEdge pipeGraphEdge = pipeGraphEdgeHashMap.get(edgeId);
        if (pipeGraphEdge == null)
            throw new ReferenceToUnExistantEdgeInTheGraph(edgeId);

        TaskGraphNode upstreamNode = pipeGraphEdgeIdToUpStreamNode.get(edgeId);
        Integer upstreamOutputNumber = pipeGraphEdgeIdToUpStreamOutputNumber.get(edgeId);

        TaskGraphNode downstreamNode = pipeGraphEdgeIdToDownStreamNode.get(edgeId);
        Integer downstreamInputNumber = pipeGraphEdgeIdToDownStreamInputNumber.get(edgeId);

        if (upstreamNode == null || upstreamOutputNumber == null ||
                downstreamNode == null || downstreamInputNumber == null)
            throw new InconsistentInternalInformationOnGraphBuilder();

        upstreamNode.removeOutput(upstreamOutputNumber);
        downstreamNode.removeInput(downstreamInputNumber);
        moveNodeFromFullyConnectedNodesToUnConnectedNodesIfSo(upstreamNode);
        moveNodeFromFullyConnectedNodesToUnConnectedNodesIfSo(downstreamNode);

        pipeGraphEdgeHashMap.remove(edgeId);
        pipeGraphEdgeIdToUpStreamNode.remove(edgeId);
        pipeGraphEdgeIdToUpStreamOutputNumber.remove(edgeId);
        pipeGraphEdgeIdToDownStreamNode.remove(edgeId);
        pipeGraphEdgeIdToDownStreamInputNumber.remove(edgeId);
    }

    private void moveNodeFromFullyConnectedNodesToUnConnectedNodesIfSo(TaskGraphNode upstreamNode)
    {
        if (!upstreamNode.areNumberOfInputsAndNumberOfOutputsInACorrectState())
        {
            fullyConnectedGraphNodes.remove(upstreamNode.getGraphNodeInformation().getId());
            unconnectedGraphNodes.put(upstreamNode.getGraphNodeInformation().getId(), upstreamNode);
        }
    }

    public void setTickIntervalOfNode(long nodeId, int tickInterval) throws ReferenceToUnExistantNodeInTheGraph
    {
        TaskGraphNode taskGraphNode = fullyConnectedGraphNodes.get(nodeId);
        if (taskGraphNode == null)
        {
            taskGraphNode = unconnectedGraphNodes.get(nodeId);
            if (taskGraphNode == null)
                throw new ReferenceToUnExistantNodeInTheGraph(nodeId);
        }

        nodeIdToTickIntervalMillis.put(nodeId, tickInterval);
    }

    public void setNumberOfCyclesOfNode(long nodeId, int numberOfCycles) throws ReferenceToUnExistantNodeInTheGraph
    {
        TaskGraphNode taskGraphNode = fullyConnectedGraphNodes.get(nodeId);
        if (taskGraphNode == null)
        {
            taskGraphNode = unconnectedGraphNodes.get(nodeId);
            if (taskGraphNode == null)
                throw new ReferenceToUnExistantNodeInTheGraph(nodeId);
        }

        numberOfCyclesOfNode.put(nodeId, numberOfCycles);
    }

    public void setBufferSizeOfNode(long taskNodeId, int bufferSize) throws ReferenceToUnExistantNodeInTheGraph
    {
        TaskGraphNode taskGraphNode = fullyConnectedGraphNodes.get(taskNodeId);

        if (taskGraphNode == null)
        {
            taskGraphNode = unconnectedGraphNodes.get(taskNodeId);
            if (taskGraphNode == null)
                throw new ReferenceToUnExistantNodeInTheGraph(taskNodeId);
        }

        taskGraphNode.setBufferSize(bufferSize);
    }

    public boolean doesGraphHaveAtLeastOneNode()
    {
        return fullyConnectedGraphNodes.size() > 0 || unconnectedGraphNodes.size() > 0;
    }

    public boolean isGraphFullyBuiltAndWithoutErrors()
    {
        return fullyConnectedGraphNodes.size() > 0 && unconnectedGraphNodes.size() == 0;
    }

    public ArrayList<TaskGraphNode> getNodes()
    {
        ArrayList<TaskGraphNode> nodes = new ArrayList<>();
        Iterator currPair = fullyConnectedGraphNodes.entrySet().iterator();
        while (currPair.hasNext())
        {
            Map.Entry pair = (Map.Entry) currPair.next();
            nodes.add((TaskGraphNode) pair.getValue());
        }
        currPair = unconnectedGraphNodes.entrySet().iterator();
        while (currPair.hasNext())
        {
            Map.Entry pair = (Map.Entry) currPair.next();
            nodes.add((TaskGraphNode) pair.getValue());
        }

        return nodes;
    }

    public HashMap<Long, Integer> getNodeTickIntervals()
    {
        return nodeIdToTickIntervalMillis;
    }

    public HashMap<Long, Integer> getNodeNumberOfCycles()
    {
        return numberOfCyclesOfNode;
    }
}
