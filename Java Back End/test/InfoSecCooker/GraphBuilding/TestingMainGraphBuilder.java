package InfoSecCooker.GraphBuilding;

import InfoSecCooker.GraphLogic.GraphBuilding.MainGraphBuilder;
import InfoSecCooker.GraphLogic.GraphBuilding.TaskFactory;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;

import java.util.HashMap;

public class TestingMainGraphBuilder extends MainGraphBuilder
{
    public TestingMainGraphBuilder(TaskFactory taskFactory)
    {
        super(taskFactory, new RunTimeConfigurations());
    }

    public TaskFactory getTaskFactory()
    {
        return taskFactory;
    }

    public HashMap<Long, TaskGraphNode> getFullyConnectedGraphNodes()
    {
        return fullyConnectedGraphNodes;
    }

    public HashMap<Long, TaskGraphNode> getUnconnectedGraphNodes()
    {
        return unconnectedGraphNodes;
    }

    public HashMap<Long, PipeGraphEdge> getPipeGraphEdgeHashMap()
    {
        return pipeGraphEdgeHashMap;
    }

    public HashMap<Long, TaskGraphNode> getPipeGraphEdgeIdToUpStreamNode()
    {
        return pipeGraphEdgeIdToUpStreamNode;
    }

    public HashMap<Long, Integer> getPipeGraphEdgeIdToUpStreamOutputNumber()
    {
        return pipeGraphEdgeIdToUpStreamOutputNumber;
    }

    public HashMap<Long, TaskGraphNode> getPipeGraphEdgeIdToDownStreamNode()
    {
        return pipeGraphEdgeIdToDownStreamNode;
    }

    public HashMap<Long, Integer> getPipeGraphEdgeIdToDownStreamInputNumber()
    {
        return pipeGraphEdgeIdToDownStreamInputNumber;
    }


}
