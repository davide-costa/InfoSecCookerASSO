package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sinks;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.BasicTaskGraphNode;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;

import java.util.HashMap;

public abstract class SinkNodeTask extends BasicTaskGraphNode
{
    public SinkNodeTask(GraphNodeInformation graphNodeInformation,
                        RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
    }
}
