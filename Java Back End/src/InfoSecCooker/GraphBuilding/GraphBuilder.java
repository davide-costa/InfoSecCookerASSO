package InfoSecCooker.GraphBuilding;

import InfoSecCooker.GraphBuilding.Exceptions.*;
import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphEdge.PipeGraphEdgeWithJavaArrayBlockingQueueImplementation;
import InfoSecCooker.GraphNodes.TaskGraphNode;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphBuilder
{
    HashMap<Long, TaskGraphNode> graph = new HashMap<>();
    //The recently added graph nodes, that don't have all their inputs and outputs all filled yet
    HashMap<Long, TaskGraphNode> unconnectedGraphNodes = new HashMap<>();

    public boolean areAllNodesFullyConnected()
    {
        return unconnectedGraphNodes.size() == 0;
    }

    public void addNewNode(String newNodeId) throws GraphBuildingException
    {
        TaskGraphNode taskGraphNode = TaskFactory.buildTaskGraphNodeWithName(newNodeId);
        if (taskGraphNode == null)
            throw new AttemptToCreateUnknownNode();

        unconnectedGraphNodes.put(taskGraphNode.getGraphNodeInformation().getId(), taskGraphNode);
    }

    public void AddEdgeFromNode1ToNode2OutputToInput(long node1Id, long node2Id,
                                                   int outputNumber, int inputNumber) throws GraphBuildingException
    {
        //TODO fill ports
        PipeGraphEdge graphEdge = new PipeGraphEdgeWithJavaArrayBlockingQueueImplementation();
        TaskGraphNode graphNode1 = graph.get(node1Id);
        TaskGraphNode graphNode2 = graph.get(node2Id);
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

        PipeGraphEdge destinationEdgeAlreadyExistant = graphNode1.getDestinations().get(outputNumber);
        if (destinationEdgeAlreadyExistant != null)
            throw new AttemptingToConnectOutputAlreadyConnected(node1Id, outputNumber);

        PipeGraphEdge sourceEdgeAlreadyExistant = graphNode2.getSources().get(inputNumber);
        if (sourceEdgeAlreadyExistant != null)
            throw new AttemptingToConnectOutputAlreadyConnected(node2Id, inputNumber);

        //TODO check compatibility between the nodes connected

        //if all error checks have passed, connect the nodes to the edge and the edge to the nodes
        graphNode1.getDestinations().put(outputNumber, graphEdge);
        graphNode2.getSources().put(inputNumber, graphEdge);
        graphEdge.setSource(graphNode1);
        graphEdge.setSource(graphNode2);

        //TODO check if node is fully connected, if yes add to appropriate map
    }
}
