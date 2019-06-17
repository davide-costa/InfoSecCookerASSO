package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Hashing;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.BasicTaskGraphNode;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;

import java.util.HashMap;

public abstract class HashingNodeTask extends BasicTaskGraphNode
{
    public HashingNodeTask(GraphNodeInformation graphNodeInformation,
                           RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
    }

}
