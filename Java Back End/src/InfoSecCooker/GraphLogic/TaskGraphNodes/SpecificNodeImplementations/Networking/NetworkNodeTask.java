package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Networking;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.BasicTaskGraphNode;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;

import java.util.HashMap;

public abstract class NetworkNodeTask extends BasicTaskGraphNode
{
    public NetworkNodeTask(GraphNodeInformation graphNodeInformation,
                           RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
    }
}
