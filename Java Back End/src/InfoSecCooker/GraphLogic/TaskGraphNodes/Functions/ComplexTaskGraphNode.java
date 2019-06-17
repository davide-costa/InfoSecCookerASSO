package InfoSecCooker.GraphLogic.TaskGraphNodes.Functions;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.NullDataReceivedFromGraphNodeAsInput;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements Function flow control.
 * This class represents a complex graph node, i.e., a node that is a composition of nodes. This can be also called a function, as the original project specification states.
 * This is can also be viewed as a graph itself because it is a set of nodes and edges.
 * To create this node, the source edges should be connected to a SwitchNodeTask which should be connected to other nodes.
 * The outputs of this node should also be SwitchNodeTask's.
 */
public class ComplexTaskGraphNode extends TaskGraphNode
{

    /**
     * The number of source nodes should be the same as the number of source edges. These are the nodes that are connected to the source edges (indirectly and through SwitchNodeTask's).
     */
    protected ArrayList<TaskGraphNode> sourceNodes;

    /**
     * The number of sink nodes should be the same as the number of destination edges. These are the nodes that are connected to the destination edges (indirectly and through SwitchNodeTask's).
     */
    protected ArrayList<TaskGraphNode> sinkNodes;

    /**
     * All the graph nodes that this complex node contains.
     */
    protected ArrayList<TaskGraphNode> taskGraphNodes;

    /**
     * Entrance nodes are SwitchNodeTask class instances that represent make the connection between the inputs edges and the edges that connect to the sourceNodes.
     * When they tick, they will close the circuit for one clock cycle. They let one single burst of data through on each cycle.
     */
    protected ArrayList<SwitchNodeTask> entranceNodes;

    /**
     * Same as the entrance nodes but they make the connection between the edges of the sink nodes and the destination edges
     */
    protected ArrayList<SwitchNodeTask> exitNodes;

    public ComplexTaskGraphNode(GraphNodeInformation graphNodeInformation, HashMap<Integer, PipeGraphEdge> sources,
                                HashMap<Integer, PipeGraphEdge> destinations, ArrayList<TaskGraphNode> sourceNodes,
                                ArrayList<TaskGraphNode> sinkNodes, ArrayList<TaskGraphNode> taskGraphNodes,
                                ArrayList<SwitchNodeTask> entranceNodes, ArrayList<SwitchNodeTask> exitNodes,
                                RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
        this.sourceNodes = sourceNodes;
        this.sinkNodes = sinkNodes;
        this.taskGraphNodes = taskGraphNodes;
        this.entranceNodes = entranceNodes;
        this.exitNodes = exitNodes;
    }

    @Override
    public void tick() throws CollectionsException, NullDataReceivedFromGraphNodeAsInput, InterruptedException
    {
        for (SwitchNodeTask node : entranceNodes)
            node.tick();

        //Tick method for the "middle" nodes is not called here because it is assumed that they are running on they own threads
        //It is not this class' responsibility to run them on threads

        for (SwitchNodeTask node : exitNodes)
            node.tick();
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

    public ArrayList<TaskGraphNode> getSourceNodes()
    {
        return sourceNodes;
    }

    public ArrayList<TaskGraphNode> getSinkNodes()
    {
        return sinkNodes;
    }

    public ArrayList<TaskGraphNode> getTaskGraphNodes()
    {
        return taskGraphNodes;
    }

    public ArrayList<SwitchNodeTask> getEntranceNodes()
    {
        return entranceNodes;
    }

    public ArrayList<SwitchNodeTask> getExitNodes()
    {
        return exitNodes;
    }
}
