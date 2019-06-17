package InfoSecCooker.GraphBuilding;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.Functions.ComplexTaskGraphNode;
import InfoSecCooker.GraphLogic.TaskGraphNodes.Functions.SwitchNodeTask;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;

import java.util.ArrayList;
import java.util.HashMap;

public class TestingComplexTaskGraphNode extends ComplexTaskGraphNode
{
    public TestingComplexTaskGraphNode(GraphNodeInformation graphNodeInformation,
                                       HashMap<Integer, PipeGraphEdge> sources,
                                       HashMap<Integer, PipeGraphEdge> destinations,
                                       ArrayList<TaskGraphNode> sourceNodes,
                                       ArrayList<TaskGraphNode> sinkNodes,
                                       ArrayList<TaskGraphNode> taskGraphNodes,
                                       ArrayList<SwitchNodeTask> entranceNodes,
                                       ArrayList<SwitchNodeTask> exitNodes,
                                       RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, sources, destinations, sourceNodes, sinkNodes, taskGraphNodes, entranceNodes, exitNodes, runTimeConfigurations);
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
