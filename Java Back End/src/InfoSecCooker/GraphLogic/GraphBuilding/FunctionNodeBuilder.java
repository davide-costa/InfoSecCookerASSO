package InfoSecCooker.GraphLogic.GraphBuilding;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.Functions.SwitchNodeTask;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;

import java.util.ArrayList;
import java.util.HashMap;

public class FunctionNodeBuilder extends GraphBuilder
{
    ArrayList<TaskGraphNode> sources;
    ArrayList<TaskGraphNode> sinks;

    HashMap<Integer, PipeGraphEdge> sourceEdges;
    HashMap<Integer, PipeGraphEdge> destinationEdges;
    HashMap<Integer, PipeGraphEdge> entranceEdges;
    HashMap<Integer, PipeGraphEdge> outgoingEdges;

    ArrayList<SwitchNodeTask> entranceShortCircuitNodes;
    ArrayList<SwitchNodeTask> exitanceShortCircuitNodes;


    public FunctionNodeBuilder(TaskFactory taskFactory, RunTimeConfigurations runTimeConfigurations)
    {
        super(taskFactory, runTimeConfigurations);
    }


}
