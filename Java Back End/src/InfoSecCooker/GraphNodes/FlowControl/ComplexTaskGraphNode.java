package InfoSecCooker.GraphNodes.FlowControl;

import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphNodes.TaskGraphNode;
import InfoSecCooker.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.RuntimeExceptions.NullDataReceivedFromGraphNodeAsInput;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements Function flow control.
 * This class represents a complex graph node, i.e., a node that is a composition of nodes. This can be also called a function, as the original project specification states.
 * This is can also be viewed as a graph itself because it is a set of nodes and edges.
 * To create this node, the source edges should be connected to a ShortCircuitNodeTask which should be connected to other nodes.
 * The outputs of this node should also be ShortCircuitNodeTask's.
 */
public class ComplexTaskGraphNode extends TaskGraphNode
{

    /**
     * The number of source nodes should be the same as the number of source edges. These are the nodes that are connected to the source edges (indirectly and through ShortCircuitNodeTask's).
     */
    ArrayList<TaskGraphNode> sourceNodes;

    /**
     * The number of sink nodes should be the same as the number of destination edges. These are the nodes that are connected to the destination edges (indirectly and through ShortCircuitNodeTask's).
     */
    ArrayList<TaskGraphNode> sinkNodes;

    /**
     * All the graph nodes that this complex node contains.
     */
    ArrayList<TaskGraphNode> taskGraphNodes;

    /**
     * Entrance nodes are ShortCircuitNodeTask class instances that represent make the connection between the sources edges and the edges that connect to the sourceNodes.
     * When they tick, they will close the circuit for one clock cycle. They let one single burst of data through on each cycle.
     */
    ArrayList<ShortCircuitNodeTask> entranceNodes;

    /**
     * Same as the entrance nodes but they make the connection between the edges of the sink nodes and the destination edges
     */
    ArrayList<ShortCircuitNodeTask> exitNodes;

    public ComplexTaskGraphNode(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources,
                                HashMap<Integer, PipeGraphEdge> destinations, ArrayList<TaskGraphNode> sourceNodes,
                                ArrayList<TaskGraphNode> sinkNodes, ArrayList<TaskGraphNode> taskGraphNodes,
                                ArrayList<ShortCircuitNodeTask> entranceNodes, ArrayList<ShortCircuitNodeTask> exitNodes)
    {
        super(graphNodeInformation, sources, destinations);
        this.sourceNodes = sourceNodes;
        this.sinkNodes = sinkNodes;
        this.taskGraphNodes = taskGraphNodes;
        this.entranceNodes = entranceNodes;
        this.exitNodes = exitNodes;
    }

    @Override
    public void tick() throws CollectionsException, NullDataReceivedFromGraphNodeAsInput, InterruptedException
    {
        for (ShortCircuitNodeTask node : entranceNodes)
            node.tick();

        //Tick method for the "middle" nodes is not called here because it is assumed that they are running on they own threads
        //It is not this class' responsibility to run them on threads

        for (ShortCircuitNodeTask node : exitNodes)
            node.tick();
    }
}
